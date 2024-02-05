package org.polyfrost.polyzoom.mixin;

import net.minecraft.entity.player.InventoryPlayer;
import org.polyfrost.polyzoom.config.ModConfig;
import org.polyfrost.polyzoom.core.Zoom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryPlayer.class)
public class InventoryPlayerMixin {
    @Inject(method = "changeCurrentItem", at = @At("HEAD"), cancellable = true)
    private void scrollZoom(int direction, CallbackInfo ci) {
        if (ModConfig.INSTANCE.getScrollToScale() && Zoom.INSTANCE.isZooming()) ci.cancel();
    }
}