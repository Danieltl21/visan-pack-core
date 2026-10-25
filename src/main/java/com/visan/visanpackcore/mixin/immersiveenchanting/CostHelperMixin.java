package com.visan.visanpackcore.mixin.immersiveenchanting;

import me.alfie.immersiveenchanting.gui.EnchantingTableMenu;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "me.alfie.immersiveenchanting.util.CostHelper")
public class CostHelperMixin {

    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private static void visanpackcore$preventBookEnchantment(EnchantingTableMenu menu, Holder<Enchantment> enchantmentHolder, int level, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (menu.getToolSlot().getItem().is(Items.BOOK)) {
            cir.setReturnValue(false);
        }
    }
}
