package me.redtea.nodropx.model.materialprovider.impl;

import me.redtea.nodropx.model.materialprovider.ItemStackProvider;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemStackProviderDefault implements ItemStackProvider {
    @Override
    public ItemStack get(String s) {
        if(s == null || s.equals("")) return new ItemStack(Material.AIR);
        Material material = Material.matchMaterial(s);
        if(material != null) return new ItemStack(material);

        String[] spl = s.split(":");

        if (spl[0].equals("minecraft")) {
            material = Material.matchMaterial(spl[1]);
        } else {
            if(spl.length < 2) {
                material = Material.matchMaterial(spl[0]);
            } else {
                material = Material.matchMaterial(spl[0]+"_"+spl[1]);;
            }
        }
        return new ItemStack(material == null ? Material.AIR : material);
    }

    @Override
    public List<String> allMaterials() {
        return Arrays.stream(Material.values()).map(Enum::name).collect(Collectors.toList());
    }
}
