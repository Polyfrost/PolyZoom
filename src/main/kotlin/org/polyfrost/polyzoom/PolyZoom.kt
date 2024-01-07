package org.polyfrost.polyzoom

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import org.polyfrost.polyzoom.config.ModConfig
import org.polyfrost.polyzoom.core.Zoom

@Mod(
    modid = PolyZoom.MODID,
    name = PolyZoom.NAME,
    version = PolyZoom.VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter"
)
object PolyZoom {
    const val MODID = "@ID@"
    const val NAME = "@NAME@"
    const val VERSION = "@VER@"

    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent) {
        ModConfig
        MinecraftForge.EVENT_BUS.register(Zoom)
    }
}