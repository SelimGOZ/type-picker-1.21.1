package net.fortuneblack05.typerando.mixin;

import net.fortuneblack05.typerando.Client.TypePickerOverlay;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void hideCrosshairDuringSpin(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        // If our spin wheel overlay is active, cancel drawing the crosshair
        if (TypePickerOverlay.isSpinning) {
            ci.cancel();
        }
    }
}