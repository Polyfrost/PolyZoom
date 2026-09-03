package org.polyfrost.polyzoom;

import net.fabricmc.api.ClientModInitializer;

import java.util.Objects;

public class PolyZoom implements ClientModInitializer {
	private static PolyZoom instance;

	public static PolyZoom instance() {
		return Objects.requireNonNull(instance);
	}

	@Override
	public void onInitializeClient() {
		if (instance != null) {
			throw new IllegalStateException("PolyZoom has already been initialized!");
		}
		instance = this;
	}
}
