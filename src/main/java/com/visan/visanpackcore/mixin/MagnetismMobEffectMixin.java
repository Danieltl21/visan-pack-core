package com.visan.visanpackcore.mixin;

import artifacts.effect.MagnetismMobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MagnetismMobEffect.class)
public class MagnetismMobEffectMixin {

    @ModifyConstant(method = "applyEffectTick", constant = @Constant(intValue = 1, ordinal = 1))
    private int buffMagnetismBaseRange(int original) {
        // Replaces the '1' in 'Math.min(1 + amplifier, 10)' with '3'
        // Resulting in Math.min(3 + amplifier, 10)
        return 2;
    }
}
