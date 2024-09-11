package io.github.fabricators_of_create.porting_lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface FlexibilityCheckingRailBlock {
	boolean isFlexibleRail(BlockState state, BlockGetter level, BlockPos pos);
}
