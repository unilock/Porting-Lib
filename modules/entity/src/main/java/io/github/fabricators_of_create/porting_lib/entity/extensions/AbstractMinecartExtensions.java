package io.github.fabricators_of_create.porting_lib.entity.extensions;

import net.minecraft.core.BlockPos;

public interface AbstractMinecartExtensions {
	float DEFAULT_MAX_SPEED_AIR_LATERAL = 0.4f;
	float DEFAULT_MAX_SPEED_AIR_VERTICAL = -1.0f;
	double DEFAULT_AIR_DRAG = 0.95f;

	default BlockPos getCurrentRailPos() {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default double getMaxSpeedWithRail() {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default void moveMinecartOnRail(BlockPos pos) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default boolean canUseRail() {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default void setCanUseRail(boolean use) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default float getMaxSpeedOnRail() {
		return 1.2f; // default in Forge
	}

	default float getCurrentCartSpeedCapOnRail() {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default void setCurrentCartSpeedCapOnRail(float value) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default float getMaxSpeedAirLateral() {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default void setMaxSpeedAirLateral(float value) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default float getMaxSpeedAirVertical() {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default void setMaxSpeedAirVertical(float value) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default double getDragAir() {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default void setDragAir(double value) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default double getSlopeAdjustment() {
		return 0.0078125D; // default in Forge
	}
}
