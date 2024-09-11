package io.github.fabricators_of_create.porting_lib.mixin.common;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;

import io.github.fabricators_of_create.porting_lib.extensions.extensions.BaseRailBlockExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

@Mixin(BaseRailBlock.class)
public abstract class BaseRailBlockMixin implements BaseRailBlockExtensions {
	@Shadow
	public abstract Property<RailShape> getShapeProperty();

	@Unique
	@Override
	public RailShape getRailDirection(BlockState state, BlockGetter world, BlockPos pos, @Nullable BaseRailBlock cart) {
		return state.getValue(getShapeProperty());
	}

	@Unique
	@Override
	public RailShape getRailDirection(BlockState state, BlockGetter world, BlockPos pos, @Nullable AbstractMinecart cart) {
		return state.getValue(getShapeProperty());
	}

	@Redirect(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"))
	private Comparable<?> neighborChanged$getValue(BlockState instance, Property<?> property, @Local(argsOnly = true) Level level, @Local(argsOnly = true, ordinal = 0) BlockPos blockPos) {
		return getRailDirection(instance, level, blockPos, (AbstractMinecart) null);
	}

	@Redirect(method = "onRemove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"))
	private Comparable<?> onRemove$getValue(BlockState instance, Property<?> property, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos blockPos) {
		return getRailDirection(instance, level, blockPos, (AbstractMinecart) null);
	}
}
