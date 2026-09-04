package com.visan.visanpackcore;

import net.neoforged.fml.common.Mod;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@Mod(VisanPackCore.MOD_ID)
public final class VisanPackCore {

    public static final String MOD_ID = "visanpackcore";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VisanPackCore(net.neoforged.bus.api.IEventBus modEventBus) {
        com.visan.visanpackcore.loot.ModLootConditions.LOOT_CONDITIONS.register(modEventBus);
    }
}