package io.github.fabricators_of_create.porting_lib.transfer.cache;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("NonExtendableApiUsage")
public enum EmptyItemLookupCache implements BlockApiCache<Storage<ItemVariant>, Direction> {
	INSTANCE;

	@Override
	@Nullable
	public Storage<ItemVariant> find(@Nullable BlockState state, Direction context) {
		return null;
	}

	@Override
	@Nullable
	public BlockEntity getBlockEntity() {
		return null;
	}

	@Override
	public BlockApiLookup<Storage<ItemVariant>, Direction> getLookup() {
		return ItemStorage.SIDED;
	}

	@Override
	public ServerLevel getWorld() {
		throw new UnsupportedOperationException("Cannot call getWorld on an empty cache as no world is associated with it");
	}

	@Override
	public BlockPos getPos() {
		return BlockPos.ZERO;
	}
}
