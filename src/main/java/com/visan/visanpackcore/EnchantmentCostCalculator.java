package com.visan.visanpackcore;

import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class EnchantmentCostCalculator {

    private EnchantmentCostCalculator() {
    }

    /**
     * Calculates the total enchantment cost using the vanilla anvil
     * cost of each enchantment multiplied by its level.
     *
     * Does not include anvil prior-work penalties.
     */
    public static int getVanillaEnchantmentCosts(ItemEnchantments itemEnchantments) {
        int enchantmentCosts = 0;

        for (var entry : itemEnchantments.entrySet()) {
            enchantmentCosts += entry.getKey().value().getAnvilCost() * entry.getIntValue();
        }

        return enchantmentCosts;
    }

    /**
     * Calculates the difference between the current and original
     * enchantment costs.
     */
    public static int calculateVanillaEnchantingCost(
            ItemEnchantments original,
            ItemEnchantments current
    ) {
        return getVanillaEnchantmentCosts(current) - getVanillaEnchantmentCosts(original);
    }
}