package com.visan.visanpackcore.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "dev.excavate.ExcavationHandler")
public abstract class ExcavationHandlerMixin {

    @Inject(method = "onBlockBreak", at = @At("HEAD"), cancellable = true, remap = false)
    private static void visanPackCore$cancelNonHoeCropBreak(BlockEvent.BreakEvent event, CallbackInfo ci) {
        if (!(event.getPlayer().getMainHandItem().getItem() instanceof HoeItem)) {
            BlockState originState = event.getLevel().getBlockState(event.getPos());
            if (originState.getBlock() instanceof CropBlock crop && crop.isMaxAge(originState)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "onUseItemOnBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private static void visanPackCore$cancelOffhandAoE(UseItemOnBlockEvent event, CallbackInfo ci) {
        if (event.getHand() == InteractionHand.OFF_HAND) {
            ci.cancel();
        }
    }
}
