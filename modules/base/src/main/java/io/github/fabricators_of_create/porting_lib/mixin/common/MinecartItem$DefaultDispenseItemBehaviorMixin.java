package io.github.fabricators_of_create.porting_lib.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import com.llamalad7.mixinextras.sugar.Local;

import io.github.fabricators_of_create.porting_lib.extensions.extensions.BaseRailBlockExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@Mixin(targets = "net/minecraft/world/item/MinecartItem$1")
public class MinecartItem$DefaultDispenseItemBehaviorMixin {
	@Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockSource;getLevel()Lnet/minecraft/server/level/ServerLevel;")))
	private Comparable<?> execute$getValue(BlockState instance, Property<?> property, @Local Level level, @Local BlockPos blockPos) {
		return ((BaseRailBlockExtensions) instance.getBlock()).getRailDirection(instance, level, blockPos, (AbstractMinecart) null);
	}
}
