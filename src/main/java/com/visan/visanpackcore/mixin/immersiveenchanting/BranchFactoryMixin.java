package com.visan.visanpackcore.mixin.immersiveenchanting;

import me.alfie.immersiveenchanting.api.node.BuildBranchesEvent;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "me.alfie.immersiveenchanting.gui.tab.enchanting.node.BranchFactory")
public class BranchFactoryMixin {

    @Inject(method = "buildEnchantingBranches", at = @At("HEAD"), cancellable = true)
    private static void visanpackcore$hideBookEnchantments(BuildBranchesEvent event, CallbackInfo ci) {
        if (event.getStack().is(Items.BOOK)) {
            ci.cancel();
        }
    }
}
