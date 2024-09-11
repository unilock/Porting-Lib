package io.github.fabricators_of_create.porting_lib.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import io.github.fabricators_of_create.porting_lib.block.MinecartPassHandlerBlock;
import io.github.fabricators_of_create.porting_lib.extensions.extensions.BaseRailBlockExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends Entity {
	private AbstractMinecartMixin(EntityType<?> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "moveAlongTrack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;floor(D)I", ordinal = 4))
	protected void onMoveAlongTrack(BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
		if (blockState.getBlock() instanceof MinecartPassHandlerBlock handler) {
			handler.onMinecartPass(blockState, level(), blockPos, (AbstractMinecart) (Object) this);
		}
	}

	@Redirect(method = "moveAlongTrack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;")))
	private Comparable<?> moveAlongTrack$getValue(BlockState instance, Property<?> property, @Local(argsOnly = true) BlockPos blockPos) {
		return ((BaseRailBlockExtensions) instance.getBlock()).getRailDirection(instance, level(), blockPos, (AbstractMinecart) (Object) this);
	}

	@Redirect(method = "getPosOffs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"))
	private Comparable<?> getPosOffs$getValue(BlockState instance, Property<?> property, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return ((BaseRailBlockExtensions) instance.getBlock()).getRailDirection(instance, level(), new BlockPos(i, j, k), (AbstractMinecart) (Object) this);
	}

	@Redirect(method = "getPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"))
	private Comparable<?> getPos$getValue(BlockState instance, Property<?> property, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return ((BaseRailBlockExtensions) instance.getBlock()).getRailDirection(instance, level(), new BlockPos(i, j, k), (AbstractMinecart) (Object) this);
	}
}
