package io.github.fabricators_of_create.porting_lib.entity.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import io.github.fabricators_of_create.porting_lib.entity.extensions.AbstractMinecartExtensions;
import io.github.fabricators_of_create.porting_lib.extensions.extensions.BaseRailBlockExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends Entity implements AbstractMinecartExtensions {
	@Shadow
	protected abstract double getMaxSpeed();

	@Unique
	private boolean canUseRail = true;

	@Unique
	private float currentSpeedCapOnRail = getMaxSpeedOnRail();

	@Unique
	@Nullable
	private Float maxSpeedAirLateral = null;

	@Unique
	private float maxSpeedAirVertical = DEFAULT_MAX_SPEED_AIR_VERTICAL;

	@Unique
	private double dragAir = DEFAULT_AIR_DRAG;

	public AbstractMinecartMixin(EntityType<?> variant, Level world) {
		super(variant, world);
	}

	@Override
	public BlockPos getCurrentRailPos() {
		BlockPos pos = new BlockPos(Mth.floor(getX()), Mth.floor(getY()), Mth.floor(getZ()));
		BlockPos below = pos.below();
		if (level().getBlockState(below).is(BlockTags.RAILS)) {
			pos = below;
		}

		return pos;
	}

	@Override
	public double getMaxSpeedWithRail() {
		if (!canUseRail()) return getMaxSpeed();
		BlockPos pos = getCurrentRailPos();
		BlockState state = level().getBlockState(pos);
		if (!state.is(BlockTags.RAILS)) return getMaxSpeed();

		float railMaxSpeed = ((BaseRailBlockExtensions) state.getBlock()).getRailMaxSpeed(state, level(), pos, (AbstractMinecart) (Object) this);
		return Math.min(railMaxSpeed, getCurrentCartSpeedCapOnRail());
	}

	@Override
	public void moveMinecartOnRail(BlockPos pos) {
		double d24 = isVehicle() ? 0.75D : 1.0D;
		double d25 = getMaxSpeedWithRail();
		Vec3 vec3d1 = getDeltaMovement();
		move(MoverType.SELF, new Vec3(Mth.clamp(d24 * vec3d1.x, -d25, d25), 0.0D, Mth.clamp(d24 * vec3d1.z, -d25, d25)));
	}

	@Override
	public boolean canUseRail() {
		return canUseRail;
	}

	@Override
	public void setCanUseRail(boolean use) {
		canUseRail = use;
	}

	@Override
	public float getCurrentCartSpeedCapOnRail() {
		return currentSpeedCapOnRail;
	}

	@Override
	public void setCurrentCartSpeedCapOnRail(float value) {
		currentSpeedCapOnRail = Math.min(value, getMaxSpeedOnRail());
	}

	@Override
	public float getMaxSpeedAirLateral() {
		return maxSpeedAirLateral == null ? (float) getMaxSpeed() : maxSpeedAirLateral;
	}

	@Override
	public void setMaxSpeedAirLateral(float value) {
		maxSpeedAirLateral = value;
	}

	@Override
	public float getMaxSpeedAirVertical() {
		return maxSpeedAirVertical;
	}

	@Override
	public void setMaxSpeedAirVertical(float value) {
		maxSpeedAirVertical = value;
	}

	@Override
	public double getDragAir() {
		return dragAir;
	}

	@Override
	public void setDragAir(double value) {
		dragAir = value;
	}

	@WrapOperation(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;onRails:Z", opcode = Opcodes.GETFIELD))
	public boolean tick$onRails(AbstractMinecart instance, Operation<Boolean> original) {
		return instance.canUseRail() && original.call(instance);
	}

	@WrapOperation(method = "comeOffTrack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;getMaxSpeed()D"))
	private double comeOffTrack$getMaxSpeed(AbstractMinecart instance, Operation<Double> original) {
		return instance.onGround() ? original.call(instance) : (double) getMaxSpeedAirLateral();
	}

	@Inject(method = "comeOffTrack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"))
	private void comeOffTrack$move(CallbackInfo ci) {
		if (getMaxSpeedAirVertical() > 0 && getDeltaMovement().y > getMaxSpeedAirVertical()) {
			if (Math.abs(getDeltaMovement().x) < 0.3f && Math.abs(getDeltaMovement().z) < 0.3f) {
				setDeltaMovement(new Vec3(getDeltaMovement().x, 0.15f, getDeltaMovement().z));
			} else {
				setDeltaMovement(new Vec3(getDeltaMovement().x, getMaxSpeedAirVertical(), getDeltaMovement().z));
			}
		}
	}

	@ModifyConstant(method = "comeOffTrack", constant = @Constant(doubleValue = 0.95D))
	private double comeOffTrack$dragAir(double value) {
		return getDragAir();
	}

	@ModifyConstant(method = "moveAlongTrack", constant = @Constant(doubleValue = 0.0078125))
	private double moveAlongTrack$slopeAdjustment(double value) {
		return getSlopeAdjustment();
	}
}
