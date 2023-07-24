package me.redtea.nodropx.service.cosmetic;

import me.redtea.nodropx.model.cosmetic.CosmeticStrategy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public interface CosmeticService {
    void applyAll(ItemStack item);
    void add(CosmeticStrategy strategy);
    void load(ConfigurationSection section);
}
