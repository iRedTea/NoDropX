package me.redtea.nodropx.model.cosmetic.impl;

import me.redtea.nodropx.model.cosmetic.CosmeticStrategy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoreKyoriCosmetic implements CosmeticStrategy {
    private List<Component> lore;
    private final MiniMessage mm = MiniMessage.builder().build();
    @Override
    public void apply(ItemStack itemStack) {
        List<Component> lore = itemStack.lore();
        if(lore == null) lore = new ArrayList<>();
        lore.addAll(this.lore);
        itemStack.lore(lore);
    }

    @Override
    public void loadYaml(Object fromYaml) {
        List<String> list = (List<String>) fromYaml;
        lore = list.stream().map(mm::deserialize).map(it -> Component
                .empty()
                .decoration(TextDecoration.ITALIC, false)
                .append(it)).collect(Collectors.toList());
    }

    @Override
    public String tag() {
        return "lore";
    }
}
