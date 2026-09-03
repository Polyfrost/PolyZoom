package org.polyfrost.polyzoom;

public final class ZoomHelper {
	private final ZoomConfig c;
	private final boolean secondary;

	private double prevInitial, initial, prevTarget;
	private boolean goingIn = true, justSwapped, zoomingLastTick;
	private Transition active = Transition.LINEAR, inactive = Transition.LINEAR;

	private double prevScroll, scroll;
	private int lastTier;

	private boolean resetting;
	private double resetMultiplier;

	public ZoomHelper(ZoomConfig config, boolean secondary) {
		this.c = config;
		this.secondary = secondary;
	}

	private Transition in() { return secondary ? Transition.LINEAR : c.zoomInTransition; }
	private Transition out() { return secondary ? Transition.LINEAR : c.zoomOutTransition; }
	private double timeIn() { return secondary ? c.secondaryZoomInTime : c.zoomInTime; }
	private double timeOut() { return secondary ? c.secondaryZoomOutTime : c.zoomOutTime; }
	private int zoomAmount() { return secondary ? c.secondaryZoomAmount : c.initialZoom; }
	private int maxTiers() { return secondary ? 0 : c.scrollStepCount; }
	private double smoothness() { return secondary ? 1.0 : lerp(c.scrollZoomSmoothness / 100.0, 1.0, 0.1); }

	public void tick(boolean zooming, int tiers, double dt) {
		if (zooming && !zoomingLastTick) resetting = false;
		prevInitial = initial;
		initial = tickTransition(zooming ? 1.0 : 0.0, initial, dt);
		if (justSwapped) {
			justSwapped = false;
			prevInitial = active.inverse(inactive.apply(prevInitial));
		}
		if (!initialIsSmooth()) prevInitial = initial;
		zoomingLastTick = zooming;

		if (tiers > lastTier) resetting = false;
		prevScroll = scroll;
		scroll = tickSmooth(maxTiers() > 0 ? (double) tiers / maxTiers() : 0.0, scroll, dt);
		if (!initialIsSmooth()) prevInitial = initial;
		lastTier = tiers;
	}

	public double getZoomDivisor(float tickDelta) {
		double t = initialIsSmooth() ? active.apply(lerp(tickDelta, prevInitial, initial)) : initial;
		double base = 1 / lerp(t, 1.0, resetting ? resetMultiplier : 1.0 / zoomAmount());
		double scrollT = resetting ? 0.0 : (smoothness() != 1.0 ? lerp(tickDelta, prevScroll, scroll) : scroll);

		double divisor = base * Math.pow(c.zoomPerStep / 100.0, scrollT * maxTiers());
		divisor = Math.clamp(divisor, 0.5, 500.0);

		if (initial == 0.0 && scroll == 0.0) resetting = false;
		if (!resetting) resetMultiplier = 1 / divisor;
		return divisor;
	}

	public void reset() {
		if (!resetting && scroll > 0.0) {
			resetting = true;
			scroll = prevScroll = 0.0;
		}
	}

	private double tickTransition(double target, double current, double dt) {
		if (target != current) {
			boolean up = target > current;
			active = up ? in() : out().opposite();
			inactive = up ? out().opposite() : in();
			if ((up ? prevTarget < target : prevTarget > target) && active.hasInverse()) {
				justSwapped = true;
				current = active.inverse(inactive.apply(current));
			}
		}
		prevTarget = target;
		if (active == Transition.INSTANT) return target;

		if (target > current) {
			goingIn = true;
			return Math.min(current + dt / timeIn(), target);
		}
		if (target < current) {
			goingIn = false;
			return Math.max(current - dt / timeOut(), target);
		}
		goingIn = true;
		return target;
	}

	private boolean initialIsSmooth() {
		if (justSwapped || active == Transition.INSTANT) return false;
		return (goingIn ? timeIn() : timeOut()) > 0.0;
	}

	private double tickSmooth(double target, double current, double dt) {
		double s = smoothness();
		if (s == 1.0) return target;
		double step = Math.abs(target - current) * s / 0.05 * dt;
		return target > current ? Math.min(current + step, target) : Math.max(current - step, target);
	}

	static double lerp(double delta, double start, double end) {
		return start + delta * (end - start);
	}
}
