package org.polyfrost.polyzoom.util

import cc.polyfrost.oneconfig.gui.animations.Animation
import cc.polyfrost.oneconfig.gui.animations.DummyAnimation
import cc.polyfrost.oneconfig.gui.animations.EaseInOutQuad
import cc.polyfrost.oneconfig.utils.dsl.mc
import org.polyfrost.polyzoom.core.Zoom
import kotlin.reflect.KProperty

class AnimationDelegate(initial: Float, private val duration: () -> Float) {
    var animation: Animation = DummyAnimation(initial)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Float {
        if (!animation.isFinished) mc.renderGlobal.setDisplayListEntitiesDirty()
        Zoom.isZooming = !animation.isFinished || animation.end != 1f || Zoom.shouldZoom
        return animation.get()
    }


    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
        if (animation.end != value) {
            animation = EaseInOutQuad((500 / duration()).toInt(), animation.get(), value, false)
        }
    }
}