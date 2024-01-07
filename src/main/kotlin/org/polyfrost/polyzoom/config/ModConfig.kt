package org.polyfrost.polyzoom.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import org.polyfrost.polyzoom.PolyZoom

object ModConfig : Config(Mod("Zoom", ModType.UTIL_QOL, "/${PolyZoom.MODID}.svg"), "${PolyZoom.MODID}.json") {
    @KeyBind(name = "Zoom Keybind", size = 2)
    var keybind = OneKeyBind(UKeyboard.KEY_F3, UKeyboard.KEY_B)

    @DualOption(name = "Keybind Activate", left = "Hold", right = "Toggle")
    var keybindToggle = false

    @Slider(name = "Scale", min = 1f, max = 10f)
    var scale = 4f

    @Slider(name = "Animation Duration", min = 0f, max = 1000f, step = 10)
    var animationDuration = 200

    @Dropdown(name = "Show Hand", options = ["Static", "Zoomed", "Never"])
    var showHand = 0

    @Switch(name = "Scroll to Scale", subcategory = "Scroll")
    var scrollToScale = false

    init {
        initialize()
    }

}