package io.github.fabricators_of_create.porting_lib.extensions.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

import org.jetbrains.annotations.Nullable;

public interface BaseRailBlockExtensions {
	@Deprecated
	default RailShape getRailDirection(BlockState state, BlockGetter world, BlockPos pos, @Nullable BaseRailBlock cart) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default RailShape getRailDirection(BlockState state, BlockGetter world, BlockPos pos, @Nullable AbstractMinecart cart) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default float getRailMaxSpeed(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
		if (cart instanceof MinecartFurnace) return cart.isInWater() ? 0.15f : 0.2f;
		else return cart.isInWater() ? 0.2f : 0.4f;
	}
}
