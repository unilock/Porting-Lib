package io.github.fabricators_of_create.porting_lib.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;

import io.github.fabricators_of_create.porting_lib.extensions.extensions.BaseRailBlockExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@Mixin(PoweredRailBlock.class)
public class PoweredRailBlockMixin {
	@Redirect(method = "findPoweredRailSignal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"))
	private Comparable<?> findPoweredRailSignal$getValue(BlockState instance, Property<?> property, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos blockPos) {
		return ((BaseRailBlockExtensions) instance.getBlock()).getRailDirection(instance, level, blockPos, (AbstractMinecart) null);
	}
}
