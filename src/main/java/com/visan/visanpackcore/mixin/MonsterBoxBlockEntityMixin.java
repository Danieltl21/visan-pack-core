package com.visan.visanpackcore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.quark.content.world.block.be.MonsterBoxBlockEntity;

@Mixin(value = MonsterBoxBlockEntity.class, remap = false)
public class MonsterBoxBlockEntityMixin {

    @Inject(method = "lambda$spawnMobs$0", at = @At("HEAD"), cancellable = true, remap = false)
    private void tweakSpawnLogic(ServerLevel serverLevel, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (stack.getItem() instanceof SpawnEggItem egg) {
            EntityType<?> entityType = egg.getType(stack);
            
            boolean spawned = false;
            // Try to find a valid spawn position in a radius of 2 blocks
            for (int i = 0; i < 8; i++) {
                int dx = serverLevel.random.nextInt(5) - 2; // -2 to 2
                int dy = serverLevel.random.nextInt(3) - 1; // -1 to 1
                int dz = serverLevel.random.nextInt(5) - 2; // -2 to 2
                
                if (dx == 0 && dy == 0 && dz == 0) {
                    dy += 1;
                }

                BlockPos spawnPos = pos.offset(dx, dy, dz);
                
                // Create a test entity to check its full bounding box
                Entity dummy = entityType.create(serverLevel);
                if (dummy != null) {
                    dummy.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                    // Check if the entity fits perfectly without suffocating in the walls or ceiling
                    if (serverLevel.noCollision(dummy)) {
                        
                        Entity e = entityType.spawn(serverLevel, stack, null, spawnPos, MobSpawnType.MOB_SUMMONED, true, true);
                        if (e != null) {
                            e.getPersistentData().putBoolean("quark:monster_box_spawned", true);
                            spawned = true;
                            break; // Successfully spawned, exit loop
                        }
                    }
                    dummy.discard(); // Clean up test entity
                }
            }
            
            // If failed, secondary search between radius 3 and 4
            if (!spawned) {
                for (int i = 0; i < 16; i++) {
                    int r = serverLevel.random.nextInt(56);
                    int dx, dz;
                    
                    if (r < 36) { // Top and bottom bands (9 wide, 2 tall each = 36 blocks)
                        dx = (r % 9) - 4; // -4 to 4
                        int row = r / 9; // 0, 1, 2, 3
                        dz = (row < 2) ? (row - 4) : (row + 1); // 0->-4, 1->-3, 2->3, 3->4
                    } else { // Left and right bands (2 wide, 5 tall each = 20 blocks)
                        r -= 36;
                        dz = (r % 5) - 2; // -2 to 2
                        int col = r / 5; // 0, 1, 2, 3
                        dx = (col < 2) ? (col - 4) : (col + 1); // 0->-4, 1->-3, 2->3, 3->4
                    }
                    
                    int dy = serverLevel.random.nextInt(3) - 1; // -1 to 1
                    
                    BlockPos spawnPos = pos.offset(dx, dy, dz);
                    
                    Entity dummy = entityType.create(serverLevel);
                    if (dummy != null) {
                        dummy.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                        if (serverLevel.noCollision(dummy)) {
                            
                            Entity e = entityType.spawn(serverLevel, stack, null, spawnPos, MobSpawnType.MOB_SUMMONED, true, true);
                            if (e != null) {
                                e.getPersistentData().putBoolean("quark:monster_box_spawned", true);
                                break; // Successfully spawned, exit loop
                            }
                        }
                        dummy.discard();
                    }
                }
            }
        }
        ci.cancel();
    }
}
