package com.visan.visanpackcore.mixin.mendingrework;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "dusk.mendingrework.compat.JEIMendingReworkPlugin", remap = false)
public class JEIPluginMixin {
    @Inject(method = "onRuntimeAvailable", at = @At("HEAD"), cancellable = true, remap = false)
    private void visan_cancelOnRuntimeAvailable(CallbackInfo ci) {
        // Prevent Mending Rework from removing vanilla anvil recipes from JEI
        ci.cancel();
    }

    @Inject(method = "registerRecipes", at = @At("HEAD"), cancellable = true, remap = false)
    private void visan_cancelRegisterRecipes(CallbackInfo ci) {
        // Prevent Mending Rework from adding custom anvil recipes to JEI
        ci.cancel();
    }
}
