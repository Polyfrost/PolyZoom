package org.polyfrost.polyzoom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import org.objectweb.asm.Opcodes;
import org.polyfrost.polyzoom.PolyZoom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow
	private Minecraft minecraft;

	@ModifyReturnValue(method = "getFov(FZ)F", at = @At("RETURN"))
	private float pz$zoom(float fov, float tickDelta, boolean fovChanged) {
		return PolyZoom.instance().zoomFov(fov, tickDelta, fovChanged, this.minecraft.worldRenderer);
	}

	@ModifyExpressionValue(method = {"render(FJ)V", "tick"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;smoothCamera:Z", opcode = Opcodes.GETFIELD))
	private boolean pz$useSmoothCam(boolean og) {
		return og || PolyZoom.instance().useCinematicCamera();
	}

	@ModifyExpressionValue(method = {"render(FJ)V", "tick"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;mouseSensitivity:F", opcode = Opcodes.GETFIELD))
	private float pz$relativeSensitivity(float og) {
		return PolyZoom.instance().relativeSensitivity(og);
	}

	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SmoothUtil;smooth(FF)F"), index = 1)
	private float pz$cinematicSmoothness(float smoother) {
		return PolyZoom.instance().cinematicSmoothness(smoother);
	}

	@ModifyVariable(method = "applyViewBobbing", at = @At("STORE"), ordinal = 2)
	private float pz$relativeViewBobbing(float bob) {
		return PolyZoom.instance().relativeViewBobbing(bob);
	}

	@ModifyExpressionValue(method = "render(FJ)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;hideGui:Z", opcode = Opcodes.GETFIELD))
	private boolean pz$hideHud(boolean og) {
		return og || PolyZoom.instance().hideHud();
	}
}
