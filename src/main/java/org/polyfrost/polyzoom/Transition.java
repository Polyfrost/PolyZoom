package org.polyfrost.polyzoom;

import java.util.function.DoubleUnaryOperator;

public enum Transition {
	INSTANT(t -> t),
	LINEAR(t -> t),
	EASE_IN_SINE(t -> 1 - Math.cos(t * Math.PI / 2), x -> Math.acos(1 - x) * 2 / Math.PI),
	EASE_OUT_SINE(t -> Math.sin(t * Math.PI / 2), x -> Math.asin(x) * 2 / Math.PI),
	EASE_IN_OUT_SINE(t -> -(Math.cos(Math.PI * t) - 1) / 2),
	EASE_IN_QUAD(t -> t * t, Math::sqrt),
	EASE_OUT_QUAD(t -> 1 - (1 - t) * (1 - t), x -> 1 - Math.sqrt(1 - x)),
	EASE_IN_OUT_QUAD(t -> t < 0.5 ? 2 * t * t : 1 - Math.pow(2 - 2 * t, 2) / 2),
	EASE_IN_CUBIC(t -> t * t * t, x -> Math.pow(x, 1 / 3.0)),
	EASE_OUT_CUBIC(t -> 1 - Math.pow(1 - t, 3), x -> 1 - Math.pow(1 - x, 1 / 3.0)),
	EASE_IN_OUT_CUBIC(t -> t < 0.5 ? 4 * t * t * t : 1 - Math.pow(2 - 2 * t, 3) / 2),
	EASE_IN_EXP(t -> exp(10 * t), x -> log(1023 * x + 1)),
	EASE_OUT_EXP(t -> 1 - exp(10 - 10 * t), x -> 1 - log(1023 * (1 - x) + 1)),
	EASE_IN_OUT_EXP(t -> t < 0.5 ? exp(20 * t) / 2 : 1 - exp(20 - 20 * t) / 2);

	private final DoubleUnaryOperator forward, backward;

	Transition(DoubleUnaryOperator forward) {
		this(forward, null);
	}

	Transition(DoubleUnaryOperator forward, DoubleUnaryOperator backward) {
		this.forward = forward;
		this.backward = backward;
	}

	private static double exp(double e) {
		return (Math.pow(2, e) - 1) / 1023;
	}

	private static double log(double v) {
		return Math.log(v) / (10 * Math.log(2));
	}

	public double apply(double t) {
		return forward.applyAsDouble(t);
	}

	public double inverse(double x) {
		return backward.applyAsDouble(x);
	}

	public boolean hasInverse() {
		return backward != null;
	}

	public Transition opposite() {
		if (name().startsWith("EASE_IN_OUT")) return this;
		if (name().startsWith("EASE_IN_")) return values()[ordinal() + 1];
		if (name().startsWith("EASE_OUT_")) return values()[ordinal() - 1];
		return this;
	}
}
