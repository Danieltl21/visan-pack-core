package com.visan.visanpackcore.mixin;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

    @Inject(method = "repairPlayerItems", at = @At("HEAD"), cancellable = true)
    private void visanPackCore$cancelMending(ServerPlayer player, int amount, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(amount);
    }
}
