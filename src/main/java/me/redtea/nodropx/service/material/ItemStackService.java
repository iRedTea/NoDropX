package me.redtea.nodropx.service.material;

import me.redtea.nodropx.model.materialprovider.ItemStackProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ItemStackService {
    @NotNull
    ItemStack get(String s, int amount);

    void registerProvider(ItemStackProvider provider);

    List<String> allMaterials();
}
