package me.redtea.nodropx.service.material.impl;

import lombok.val;
import me.redtea.nodropx.model.materialprovider.ItemStackProvider;
import me.redtea.nodropx.service.material.ItemStackService;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemStackServiceImpl implements ItemStackService {
    private final List<ItemStackProvider> providers = new ArrayList<>();

    @Override
    public @NotNull ItemStack get(String s, int amount) {
        ItemStack result = new ItemStack(Material.AIR);
        for(ItemStackProvider provider : providers) {
            val i = provider.get(s);
            if(i != null && i.getType() != Material.AIR) {
                result = i;
                break;
            }
        }
        result.setAmount(amount);
        return result;
    }

    @Override
    public void registerProvider(ItemStackProvider provider) {
        providers.add(provider);
    }

    private List<String> materials = null;
    @Override
    public List<String> allMaterials() {
        if(materials == null) materials = providers.stream().map(ItemStackProvider::allMaterials).flatMap(List::stream).collect(Collectors.toList());
        return materials;
    }
}
