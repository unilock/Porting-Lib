package io.github.fabricators_of_create.porting_lib.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import io.github.fabricators_of_create.porting_lib.block.FlexibilityCheckingRailBlock;
import io.github.fabricators_of_create.porting_lib.block.SlopeCreationCheckingRailBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.RailState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@Mixin(RailState.class)
public abstract class RailStateMixin {

	@Shadow
	@Final
	private Level level;

	@Shadow
	@Final
	private BlockPos pos;

	@Shadow
	@Final
	private BaseRailBlock block;

	@Shadow
	private BlockState state;

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"))
	private Comparable<?> port_lib$redirectRailDirectionCheck(BlockState instance, Property<?> property) {
		return block.getRailDirection(state, level, pos, (AbstractMinecart) null);
	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BaseRailBlock;isStraight()Z"))
	private boolean port_lib$redirectIsStraightCheckToCheckFlexibility(BaseRailBlock instance, Operation<Boolean> original) {
		if (instance instanceof FlexibilityCheckingRailBlock checking) {
			return !checking.isFlexibleRail(state, level, pos);
		}
		return original.call(instance);
	}

	@WrapOperation(method = { "connectTo", "place" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"))
	private boolean port_lib$redirectRailChecksToCheckSlopes(Level level, BlockPos pos, Operation<Boolean> original) {
		boolean canMakeSlopes = true;
		if (block instanceof SlopeCreationCheckingRailBlock checking) {
			canMakeSlopes = checking.canMakeSlopes(state, level, pos);
		}
		return original.call(level, pos) && canMakeSlopes;
	}

}
