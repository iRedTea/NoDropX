package me.redtea.nodropx.model.cosmetic.impl;

import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.NoDropX;
import me.redtea.nodropx.model.cosmetic.CosmeticStrategy;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LoreLegacyCosmetic implements CosmeticStrategy {
    private List<String> lore;
    @Override
    public void apply(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        if(lore == null) lore = new ArrayList<>();
        lore.addAll(this.lore);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void loadYaml(Object fromYaml) {
        List<String> list = (List<String>) fromYaml;
        lore = list.stream().map(it -> ChatColor.translateAlternateColorCodes('&', it)).collect(Collectors.toList());
    }

    @Override
    public String tag() {
        return "lore";
    }
}
