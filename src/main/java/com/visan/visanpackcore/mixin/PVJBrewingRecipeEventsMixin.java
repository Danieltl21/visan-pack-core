package com.visan.visanpackcore.mixin;

import dev.orderedchaos.projectvibrantjourneys.common.events.PVJBrewingRecipeEvents;
import dev.orderedchaos.projectvibrantjourneys.core.registry.PVJPotions;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PVJBrewingRecipeEvents.class)
public class PVJBrewingRecipeEventsMixin {

    @Inject(method = "registerBrewingRecipes", at = @At("HEAD"), cancellable = true, remap = false)
    private static void visanpackcore$replaceBrewingRecipes(RegisterBrewingRecipesEvent event, CallbackInfo ci) {
        PotionBrewing.Builder builder = event.getBuilder();
        
        // Use Glow Berries instead of Glowcap
        builder.addMix(Potions.AWKWARD, Items.GLOW_BERRIES, PVJPotions.GLOWING);
        builder.addMix(PVJPotions.GLOWING, Items.REDSTONE, PVJPotions.LONG_GLOWING);

        // Use Sculk instead of Glowing Blue Fungus
        builder.addMix(Potions.AWKWARD, Items.SCULK, PVJPotions.SPORADIC_SILENCE);
        builder.addMix(PVJPotions.SPORADIC_SILENCE, Items.REDSTONE, PVJPotions.LONG_SPORADIC_SILENCE);
        builder.addMix(PVJPotions.SPORADIC_SILENCE, Items.GLOWSTONE_DUST, PVJPotions.STRONG_SPORADIC_SILENCE);

        ci.cancel();
    }
}
