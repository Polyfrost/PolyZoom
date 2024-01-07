package org.polyfrost.polyzoom.core

import net.minecraftforge.client.event.EntityViewRenderEvent
import net.minecraftforge.client.event.FOVUpdateEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.polyfrost.polyzoom.config.ModConfig
import org.polyfrost.polyzoom.util.AnimationDelegate

object Zoom {
    private var smoothedScale by AnimationDelegate(1f) { ModConfig.animationDuration }

    @SubscribeEvent
    fun onHandFov(event: FOVUpdateEvent) {
        if (!ModConfig.enabled) return
        when (ModConfig.showHand) {
            1 -> event.newfov /= smoothedScale
            2 -> if (smoothedScale != 1f) event.newfov = 10f
        }
    }

    @SubscribeEvent
    fun onFov(event: EntityViewRenderEvent.FOVModifier) {
        if (!ModConfig.enabled) return
        smoothedScale = if (shouldZoom()) ModConfig.scale else 1f
        event.fov /= smoothedScale
    }

    private var lastKeybindPressed = false
    private var toggled = false

    private fun shouldZoom(): Boolean {
        if (!ModConfig.enabled) return false
        val nowPressed = ModConfig.keybind.isActive
        if (!ModConfig.keybindToggle) return nowPressed
        if (lastKeybindPressed != nowPressed) {
            if (nowPressed) {
                toggled = !toggled
            }
            lastKeybindPressed = nowPressed
        }
        return toggled
    }
}