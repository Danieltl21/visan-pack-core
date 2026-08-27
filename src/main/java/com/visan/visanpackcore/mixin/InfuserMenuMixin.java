package com.visan.visanpackcore.mixin;

import com.visan.visanpackcore.EnchantmentCostCalculator;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(targets = "fuzs.enchantinginfuser.world.inventory.InfuserMenu")
public abstract class InfuserMenuMixin {

    @Unique
    private ItemEnchantments visanPackCore$originalEnchantments =
            ItemEnchantments.EMPTY;

    @Shadow
    public abstract ItemEnchantments getItemEnchantments();

    @Inject(
            method = "setInitialEnchantments",
            at = @At("HEAD")
    )
    private void visanPackCore$captureOriginalEnchantments(
            net.minecraft.world.level.Level level,
            Optional<ItemEnchantments> itemEnchantments,
            CallbackInfo ci
    ) {
        this.visanPackCore$originalEnchantments =
                itemEnchantments.orElse(ItemEnchantments.EMPTY);
    }

    @Inject(
            method = "calculateEnchantingCost",
            at = @At("HEAD"),
            cancellable = true
    )
    private void visanPackCore$calculateEnchantingCost(
            CallbackInfoReturnable<Integer> cir
    ) {
        int cost = EnchantmentCostCalculator.calculateVanillaEnchantingCost(
                this.visanPackCore$originalEnchantments,
                this.getItemEnchantments()
        );

        cir.setReturnValue(cost);
    }
}