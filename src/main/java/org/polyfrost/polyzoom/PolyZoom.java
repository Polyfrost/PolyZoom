package org.polyfrost.polyzoom;

import java.util.Objects;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.world.WorldRenderer;

public class PolyZoom implements ClientModInitializer {
	private static PolyZoom instance;

	public static PolyZoom instance() {
		return Objects.requireNonNull(instance);
	}

	private ZoomConfig config;
	private ZoomHelper zoom, secondaryZoom;

	private boolean zooming, secondaryZooming;
	private int scrollSteps;
	private double divisor = 1.0, lastWorldDivisor = 1.0;
	private boolean handPass;

	@Override
	public void onInitializeClient() {
		if (instance != null) throw new IllegalStateException("PolyZoom has already been initialized!");
		instance = this;

		config = new ZoomConfig(new KeybindConfig());
		zoom = new ZoomHelper(config, false);
		secondaryZoom = new ZoomHelper(config, true);
	}

	public void onZoomKey(boolean down) {
		if (!toggling()) zooming = down;
		else if (down) zooming = !zooming;
	}

	public void onSecondaryZoomKey(boolean down) {
		if (!toggling()) secondaryZooming = down;
		else if (down) secondaryZooming = !secondaryZooming;
	}

	private boolean toggling() {
		return "toggle".equals(config.zoomKeyBehaviour);
	}

	public void addScrollStep(int delta) {
		scrollSteps = Math.clamp(scrollSteps + delta, 0, config.scrollStepCount);
	}

	public int consumeScroll(int dWheel) {
		if (dWheel == 0 || !zooming || !config.scrollZoom || config.keybindScrolling) return dWheel;
		addScrollStep(dWheel > 0 ? 1 : -1);
		return 0;
	}

	public void tick() {
		addScrollStep(0);
		zoom.tick(zooming, scrollSteps, 0.05);
		secondaryZoom.tick(secondaryZooming, scrollSteps, 0.05);
	}

	public float zoomFov(float fov, float tickDelta, boolean worldPass, WorldRenderer worldRenderer) {
		if (!zooming) {
			if (!config.retainZoomSteps) scrollSteps = 0;
			zoom.reset();
		}
		divisor = zoom.getZoomDivisor(tickDelta) * secondaryZoom.getZoomDivisor(tickDelta);
		handPass = !worldPass;
		if (!worldPass) return config.affectHandFov ? (float) (fov / divisor) : fov;
		if (divisor != lastWorldDivisor) {
			lastWorldDivisor = divisor;
			worldRenderer.onViewChanged();
		}
		return (float) (fov / divisor);
	}

	public boolean useCinematicCamera() {
		return secondaryZooming || (zooming && config.cinematicCamera > 0);
	}

	public float cinematicSmoothness(float smoother) {
		if (!zooming || config.cinematicCamera <= 0) return smoother;
		return (float) (smoother / (config.cinematicCamera / 100.0));
	}

	public float relativeSensitivity(float sensitivity) {
		return (float) (sensitivity / ZoomHelper.lerp(config.relativeSensitivity / 100.0, 1.0, divisor));
	}

	public float relativeViewBobbing(float bob) {
		if (!config.relativeViewBobbing) return bob;
		if (handPass) return config.affectHandFov ? (float) (bob / divisor) : bob;
		return (float) (bob / ZoomHelper.lerp(0.2, 1.0, divisor));
	}

	public boolean hideHud() {
		return secondaryZooming && config.secondaryHideHUDOnZoom;
	}
}
