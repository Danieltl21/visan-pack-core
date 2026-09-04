package com.visan.visanpackcore.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

public class InsideStructureTagCondition implements LootItemCondition {
    public static final MapCodec<InsideStructureTagCondition> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    TagKey.codec(Registries.STRUCTURE).fieldOf("tag").forGetter(InsideStructureTagCondition::tag)
            ).apply(instance, InsideStructureTagCondition::new)
    );

    private final TagKey<Structure> tag;

    public InsideStructureTagCondition(TagKey<Structure> tag) {
        this.tag = tag;
    }

    public TagKey<Structure> tag() {
        return this.tag;
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.INSIDE_STRUCTURE_TAG.get();
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return false;
        ServerLevel level = context.getLevel();
        BlockPos pos = BlockPos.containing(origin);
        return level.structureManager().getStructureWithPieceAt(pos, this.tag).isValid();
    }
}
