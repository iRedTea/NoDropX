package me.redtea.nodropx.model.materialprovider.impl;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import me.redtea.nodropx.model.materialprovider.ItemStackProvider;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsAdderProvider implements ItemStackProvider {
    @Override
    public ItemStack get(String s) {
        CustomStack stack = CustomStack.getInstance(s);
        return (stack != null) ? stack.getItemStack() : null;
    }

    @Override
    public List<String> allMaterials() {
        if((ItemsAdder.getAllItems() == null) || ItemsAdder.getAllItems().isEmpty()) return Collections.emptyList();
        return ItemsAdder.getAllItems().stream().map(CustomStack::getNamespacedID).filter(s -> !s.startsWith("_iainternal")).collect(Collectors.toList());
    }
}
