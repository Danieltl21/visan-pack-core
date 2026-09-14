package com.visan.visanpackcore.event;

import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

@EventBusSubscriber
public class AnvilRepairPreventionHandler {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        // Prevent Experience Bottle from being used as a repair material in the anvil.
        // Mending Rework adds this behavior via an ItemStack mixin, so we cancel the anvil result here.
        if (event.getRight().is(Items.EXPERIENCE_BOTTLE)) {
            event.setCanceled(true);
        }

    }

}
