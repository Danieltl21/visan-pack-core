package com.visan.visanpackcore.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "dev.excavate.ExcavationHighlightRenderer")
public abstract class ExcavationHighlightRendererMixin {

    @Inject(method = "onRenderHighlight", at = @At("HEAD"), cancellable = true, remap = false)
    private static void visanPackCore$cancelNonHoeCropHighlight(RenderHighlightEvent.Block event, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        
        ItemStack tool = mc.player.getMainHandItem();
        BlockState targetState = mc.level.getBlockState(event.getTarget().getBlockPos());
        
        if (targetState.getBlock() instanceof CropBlock crop && crop.isMaxAge(targetState)) {
            if (!(tool.getItem() instanceof HoeItem)) {
                ci.cancel(); 
            }
        }
    }
}
