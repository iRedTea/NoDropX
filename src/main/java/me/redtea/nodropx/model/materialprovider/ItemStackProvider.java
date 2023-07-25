package me.redtea.nodropx.model.materialprovider;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ItemStackProvider {
        ItemStack get(String s);
        List<String> allMaterials();
}
