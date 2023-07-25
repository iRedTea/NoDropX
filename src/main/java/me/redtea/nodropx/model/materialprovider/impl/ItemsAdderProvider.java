package me.redtea.nodropx.model.materialprovider.impl;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import me.redtea.nodropx.model.materialprovider.ItemStackProvider;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ItemsAdderProvider implements ItemStackProvider {
    @Override
    public ItemStack get(String s) {
        return CustomStack.getInstance(s).getItemStack();
    }

    @Override
    public List<String> allMaterials() {
        return ItemsAdder.getAllItems().stream().map(CustomStack::getNamespacedID).collect(Collectors.toList());
    }
}
