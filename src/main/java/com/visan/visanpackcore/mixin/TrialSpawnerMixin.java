package com.visan.visanpackcore.mixin;

import com.brutalbosses.BrutalBosses;
import com.brutalbosses.entity.BossSpawnHandler;
import com.brutalbosses.entity.BossType;
import com.brutalbosses.entity.BossTypeManager;
import com.visan.visanpackcore.VisanPackCore;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Mixin(value = TrialSpawner.class, priority = 1000)
public abstract class TrialSpawnerMixin {

    @Shadow
    public abstract TrialSpawnerConfig getConfig();

    @Shadow
    public abstract TrialSpawnerData getData();

    @Shadow
    public abstract boolean isOminous();

    @Inject(method = "spawnMob", at = @At("RETURN"), cancellable = false)
    private void visanpackcore$spawnBossOnLastWave(ServerLevel level, BlockPos pos,
            CallbackInfoReturnable<Optional<UUID>> cir) {
        if (!isOminous()) {
            return;
        }

        // We only proceed if a mob actually spawned
        if (cir.getReturnValue().isEmpty()) {
            return;
        }

        TrialSpawner spawner = (TrialSpawner) (Object) this;
        TrialSpawnerConfig config = spawner.getConfig();
        TrialSpawnerData data = spawner.getData();

        int numberOfPlayers = ((TrialSpawnerDataAccess) data).getDetectedPlayers().size();

        // Calculate target based on exact spawner logic:
        // totalMobsSpawned + simultaneousMobs >= totalTarget
        int targetTotal = config.calculateTargetTotalMobs(numberOfPlayers) - (int) config.totalMobsAddedPerPlayer();
        int totalMobsSpawned = ((TrialSpawnerDataAccess) data).getTotalMobsSpawned();

        // Check if this spawn pushes us into the final wave
        if (totalMobsSpawned + 1 >= targetTotal) {
            net.minecraft.util.random.SimpleWeightedRandomList<net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> lootTables = config.lootTablesToEject();
            java.util.Optional<net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> randomLootTable = lootTables.getRandomValue(level.getRandom());

            if (randomLootTable.isPresent()) {
                net.minecraft.resources.ResourceLocation lootTableId = randomLootTable.get().location();
                
                List<BossType> allPossibleBosses = BossTypeManager.instance.lootTableSpawnEntries.get(lootTableId);

                if (allPossibleBosses != null && !allPossibleBosses.isEmpty()) {
                    BossType bossType = allPossibleBosses.get(level.getRandom().nextInt(allPossibleBosses.size()));
                    if (bossType != null && !bossType.getID().getPath().equals("dummyboss")) {
                        // Find mobs before spawning so we can isolate the newly created boss
                        List<Mob> beforeMobs = level.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(20));

                        BossSpawnHandler.spawnBoss(level, pos, bossType, null);

                        // Find mobs after spawning and extract the new ones
                        List<Mob> afterMobs = level.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(20));
                        afterMobs.removeAll(beforeMobs);

                        // Add new boss (and its passengers, if any) to currentMobs so the spawner
                        // tracks them
                        Set<UUID> currentMobs = ((TrialSpawnerDataAccess) data).getCurrentMobs();
                        for (Mob newMob : afterMobs) {
                            currentMobs.add(newMob.getUUID());
                        }
                    }
                }
            }
        }
    }
}
