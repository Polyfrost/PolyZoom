package org.polyfrost.polyzoom.util

import cc.polyfrost.oneconfig.gui.animations.EaseInOutQuad
import kotlin.reflect.KProperty

class AnimationDelegate(initial: Float, private val duration: () -> Int) {
    private var animation = EaseInOutQuad(duration(), initial, initial, false)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Float =
        animation.get()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
        if (animation.end != value) {
            animation = EaseInOutQuad(duration(), animation.get(), value, false)
        }
    }
}