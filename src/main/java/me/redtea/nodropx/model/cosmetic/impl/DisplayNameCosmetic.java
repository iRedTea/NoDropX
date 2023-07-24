package me.redtea.nodropx.model.cosmetic.impl;

import me.redtea.nodropx.model.cosmetic.CosmeticStrategy;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DisplayNameCosmetic implements CosmeticStrategy {
    private String displayName;

    @Override
    public void apply(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void loadYaml(Object fromYaml) {
        displayName = (String) fromYaml;
    }

    @Override
    public String tag() {
        return "name";
    }
}
