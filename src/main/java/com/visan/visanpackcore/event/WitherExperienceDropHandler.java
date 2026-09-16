package com.visan.visanpackcore.event;

import com.visan.visanpackcore.VisanPackCore;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

@EventBusSubscriber(modid = VisanPackCore.MOD_ID)
public class WitherExperienceDropHandler {

    @SubscribeEvent
    public static void onWitherExperienceDrop(LivingExperienceDropEvent event) {
        if (event.getEntity() instanceof WitherBoss) {
            event.setDroppedExperience(1000);
        }
    }
}
