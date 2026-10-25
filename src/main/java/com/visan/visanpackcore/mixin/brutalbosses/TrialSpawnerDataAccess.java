package com.visan.visanpackcore.mixin;

import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;
import java.util.UUID;

@Mixin(TrialSpawnerData.class)
public interface TrialSpawnerDataAccess {
    @Accessor("currentMobs")
    Set<UUID> getCurrentMobs();

    @Accessor("detectedPlayers")
    Set<UUID> getDetectedPlayers();

    @Accessor("totalMobsSpawned")
    int getTotalMobsSpawned();
}
