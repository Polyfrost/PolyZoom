package org.polyfrost.polyzoom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import org.polyfrost.polyzoom.PolyZoom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;getEventDWheel()I"))
	private int pz$scrollZoom(int dWheel) {
		return PolyZoom.instance().consumeScroll(dWheel);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private void pz$tick(CallbackInfo ci) {
		PolyZoom.instance().tick();
	}
}
