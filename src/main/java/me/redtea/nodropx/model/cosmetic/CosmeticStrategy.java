package me.redtea.nodropx.model.cosmetic;

import org.bukkit.inventory.ItemStack;

public interface CosmeticStrategy {
    void apply(ItemStack itemStack);
    void loadYaml(Object fromYaml);
    String tag();
}
