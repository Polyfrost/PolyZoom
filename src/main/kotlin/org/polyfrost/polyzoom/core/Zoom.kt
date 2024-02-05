package org.polyfrost.polyzoom.core

import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.event.EntityViewRenderEvent
import net.minecraftforge.client.event.FOVUpdateEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse
import org.polyfrost.polyzoom.config.ModConfig
import org.polyfrost.polyzoom.util.AnimationDelegate
import kotlin.math.*

object Zoom {
    private var smoothedScale by AnimationDelegate(1f) { ModConfig.animationDuration }

    private var scrolling = 0f
    private var smoothCam = false
    private var scale = 0f
    private var sens = 1f

    @SubscribeEvent
    fun onHandFov(event: FOVUpdateEvent) {
        if (!ModConfig.enabled) return
//        when (ModConfig.showHand) {
//            1 -> event.newfov /= smoothedScale
//            2 -> if (smoothedScale != 1f) event.newfov = 10f
//        }
    }

    @SubscribeEvent
    fun onFov(event: EntityViewRenderEvent.FOVModifier) {
        if (!ModConfig.enabled) return
        checkKey(keyBind)

        if (lastActivate != isZooming) {
            lastActivate = isZooming
            if (isZooming) {
                sens = mc.gameSettings.mouseSensitivity
                smoothCam = mc.gameSettings.smoothCamera
                mc.gameSettings.smoothCamera = ModConfig.smoothCamera
                scrolling = 0f
            } else {
                mc.gameSettings.mouseSensitivity = sens
                mc.gameSettings.smoothCamera = smoothCam
            }
        }

        if (isZooming && ModConfig.dynamicSens) mc.gameSettings.mouseSensitivity = sens / sqrt(scale)

        scrolling += Mouse.getDWheel().coerceIn(-1..1) * ModConfig.scrollAmount
        scrolling = if (ModConfig.scrollToScale) max(-ModConfig.scale + 1f, scrolling) else 0f
        smoothedScale = if (shouldZoom) ModConfig.scale + scrolling else 1f
        scale = smoothedScale
        event.fov /= scale
    }

    private var lastKeybindPressed = false
    var shouldZoom = false
    var isZooming = false
    private var lastActivate = false
    var keyBind: KeyBinding? = null

    private fun checkKey(key: KeyBinding?): Boolean {
        if (!ModConfig.enabled || mc.currentScreen != null) {
            shouldZoom = false
            return false
        }
        val down = key?.isKeyDown ?: ModConfig.keybind.isActive
        if (ModConfig.keybindToggle) {
            if (lastKeybindPressed != down) {
                if (down) {
                    shouldZoom = !shouldZoom
                }
                lastKeybindPressed = down
            }
        } else {
            shouldZoom = down
        }

        return shouldZoom
    }
}