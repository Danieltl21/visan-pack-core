package com.visan.visanpackcore.mixin.mendingrework;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "dusk.mendingrework.MendingRework", remap = false)
public class MendingReworkMixin {
    @Inject(method = "alterRepairMaterials", at = @At("HEAD"), cancellable = true, remap = false)
    private void visan_cancelAlterRepairMaterials(CallbackInfo ci) {
        // Prevent Mending Rework from adding custom repair materials (e.g. String for Bows)
        ci.cancel();
    }
}
