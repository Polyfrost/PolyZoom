package org.polyfrost.polyzoom.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import org.polyfrost.polyzoom.core.Zoom;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Dynamic("OptiFine")
    @Redirect(method = "getFOVModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/GameSettings;isKeyDown(Lnet/minecraft/client/settings/KeyBinding;)Z"))
    private boolean cancelOptiFine(KeyBinding zoomKey) {
//        Zoom.INSTANCE.setKeyBind(zoomKey);
        return false;
    }
}