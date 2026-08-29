package com.visan.visanpackcore.event;

import com.visan.visanpackcore.VisanPackCore;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EventBusSubscriber(modid = VisanPackCore.MOD_ID)
public class ExcavateRightClickHandler {

    private static final Set<UUID> excavatingPlayers = new HashSet<>();
    private static final ResourceLocation EXCAVATION_ID = ResourceLocation.fromNamespaceAndPath("excavate", "excavation");

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Player player = event.getEntity();
        if (player.isCrouching()) return;

        ItemStack tool = event.getItemStack();
        if (!(tool.getItem() instanceof net.minecraft.world.item.HoeItem)) return;

        UUID playerId = player.getUUID();
        if (excavatingPlayers.contains(playerId)) return;

        Level level = event.getLevel();
        BlockPos origin = event.getPos();
        BlockState originState = level.getBlockState(origin);

        // Filter using Excavate's crop logic
        if (!(originState.getBlock() instanceof CropBlock crop) || !crop.isMaxAge(originState)) return;

        int enchantLevel = getExcavateLevel(level, tool);
        if (enchantLevel <= 0) return;

        excavatingPlayers.add(playerId);
        try {
            for (int dx = -enchantLevel; dx <= enchantLevel; dx++) {
                for (int dz = -enchantLevel; dz <= enchantLevel; dz++) {
                    if (dx == 0 && dz == 0) continue; // Skip origin as the initial event handles it

                    BlockPos target = origin.offset(dx, 0, dz);
                    BlockState targetState = level.getBlockState(target);
                    
                    // Filter using Excavate's crop logic for the area blocks
                    if (targetState.getBlock() instanceof CropBlock targetCrop && targetCrop.isMaxAge(targetState)) {
                        if (player instanceof ServerPlayer serverPlayer) {
                            Vec3 clickLocation = new Vec3(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5);
                            BlockHitResult hit = new BlockHitResult(clickLocation, Direction.UP, target, false);
                            
                            // Trigger Quark's right-click crop harvest
                            serverPlayer.gameMode.useItemOn(serverPlayer, level, tool, event.getHand(), hit);
                        }
                    }
                }
            }
        } finally {
            excavatingPlayers.remove(playerId);
        }
    }

    private static int getExcavateLevel(Level level, ItemStack stack) {
        if (stack.isEmpty()) return 0;
        return level.registryAccess()
                .registry(Registries.ENCHANTMENT)
                .flatMap(reg -> reg.getHolder(EXCAVATION_ID))
                .map(stack::getEnchantmentLevel)
                .orElse(0);
    }
}
