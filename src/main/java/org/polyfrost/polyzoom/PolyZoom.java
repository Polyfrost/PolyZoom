package org.polyfrost.polyzoom;

import java.util.Objects;
import java.util.function.Consumer;

import org.polyfrost.oneconfig.api.ui.v1.keybind.KeybindHelper;
import org.polyfrost.oneconfig.internal.legacy.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.world.WorldRenderer;

public class PolyZoom implements ClientModInitializer {
	private static final int UNBOUND = -1;

	private static PolyZoom instance;

	public static PolyZoom instance() {
		return Objects.requireNonNull(instance);
	}

	private ZoomConfig config;
	private ZoomHelper zoom, secondaryZoom;

	private boolean zooming, secondaryZooming;
	private int scrollSteps;
	private double previousZoomDivisor = 1.0, divisor = 1.0, lastWorldDivisor = 1.0;

	@Override
	public void onInitializeClient() {
		if (instance != null) throw new IllegalStateException("PolyZoom has already been initialized!");
		instance = this;

		config = new ZoomConfig();
		zoom = new ZoomHelper(config, false);
		secondaryZoom = new ZoomHelper(config, true);

		bind("Zoom", InputConstants.KEY_C, this::onZoomKey);
		bind("Secondary Zoom", InputConstants.KEY_F6, this::onSecondaryZoomKey);
		bind("Zoom In", UNBOUND, down -> { if (down) addScrollStep(1); });
		bind("Zoom Out", UNBOUND, down -> { if (down) addScrollStep(-1); });
	}

	private void bind(String name, int key, Consumer<Boolean> action) {
		KeybindHelper builder = KeybindHelper.builder().name(name).category("PolyZoom").action(action);
		if (key != UNBOUND) builder.key(key);
		builder.register();
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

	public float zoomDivisor(float tickDelta) {
		if (!zooming) {
			if (!config.retainZoomSteps) scrollSteps = 0;
			zoom.reset();
		}
		previousZoomDivisor = zoom.getZoomDivisor(tickDelta);
		divisor = previousZoomDivisor * secondaryZoom.getZoomDivisor(tickDelta);
		return (float) divisor;
	}

	public void onWorldFov(WorldRenderer worldRenderer) {
		if (divisor == lastWorldDivisor) return;
		lastWorldDivisor = divisor;
		worldRenderer.onViewChanged();
	}

	public boolean affectHandFov() {
		return config.affectHandFov;
	}

	public boolean useCinematicCamera() {
		return secondaryZooming || (zooming && config.cinematicCamera > 0);
	}

	public float cinematicSmoothness(float smoother) {
		if (!zooming || config.cinematicCamera <= 0) return smoother;
		return (float) (smoother / (config.cinematicCamera / 100.0));
	}

	public float relativeSensitivity(float sensitivity) {
		return (float) (sensitivity / ZoomHelper.lerp(config.relativeSensitivity / 100.0, 1.0, previousZoomDivisor));
	}

	public float relativeViewBobbing(float bob) {
		if (!config.relativeViewBobbing) return bob;
		return (float) (bob / ZoomHelper.lerp(0.2, 1.0, previousZoomDivisor));
	}

	public boolean hideHud() {
		return secondaryZooming && config.secondaryHideHUDOnZoom;
	}
}
