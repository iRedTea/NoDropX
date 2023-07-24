package me.redtea.nodropx.model.cosmetic.impl;

import me.redtea.nodropx.model.cosmetic.CosmeticStrategy;
import me.redtea.nodropx.util.MD5ColorUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

public class LoreMD5Cosmetics implements CosmeticStrategy {


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
        lore = list.stream().map(MD5ColorUtils::translateHexColorCodes).collect(Collectors.toList());
    }

    @Override
    public String tag() {
        return "lore";
    }


}
