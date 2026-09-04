package com.visan.visanpackcore.loot;

import com.visan.visanpackcore.VisanPackCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModLootConditions {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, VisanPackCore.MOD_ID);

    public static final Supplier<LootItemConditionType> INSIDE_STRUCTURE_TAG = LOOT_CONDITIONS.register("inside_structure_tag",
            () -> new LootItemConditionType(InsideStructureTagCondition.CODEC));
}
