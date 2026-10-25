package com.visan.visanpackcore.mixin.minecraft;

import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(VaultBlock.class)
public class VaultBlockMixin {

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static BlockBehaviour.Properties makeUnbreakable(BlockBehaviour.Properties properties) {
        return properties.strength(-1.0F, 3600000.0F);
    }
}
