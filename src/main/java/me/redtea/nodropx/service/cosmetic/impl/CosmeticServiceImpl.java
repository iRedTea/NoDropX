package me.redtea.nodropx.service.cosmetic.impl;

import lombok.val;
import me.redtea.nodropx.model.cosmetic.CosmeticStrategy;
import me.redtea.nodropx.service.cosmetic.CosmeticService;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CosmeticServiceImpl implements CosmeticService {
    private final List<CosmeticStrategy> strategies = new ArrayList<>();

    @Override
    public void applyAll(ItemStack item) {
        strategies.forEach(it -> {
            try {
                it.apply(item);
            } catch (Throwable e) {
                Bukkit.getLogger().severe("[NoDropX Cosmetic] Failed to apply cosmetic with tag " + it.tag());
                e.printStackTrace();
            }
        });
    }

    @Override
    public void add(CosmeticStrategy strategy) {
        strategies.add(strategy);
    }

    @Override
    public void load(ConfigurationSection section) {
        List<CosmeticStrategy> disabled = new ArrayList<>();
        for(CosmeticStrategy strategy : strategies) {
            val obj = section.get(strategy.tag());
            if(obj == null) disabled.add(strategy);
            else strategy.loadYaml(obj);
        }
        strategies.removeAll(disabled);
    }
}
