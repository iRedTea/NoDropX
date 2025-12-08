package me.redtea.nodropx.service.capasity.impl;

import me.redtea.nodropx.libs.carcadex.repo.Repo;
import me.redtea.nodropx.libs.carcadex.repo.impl.yaml.YamlRepo;
import me.redtea.nodropx.service.capasity.CapacityService;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class CapacityServiceImpl implements CapacityService {
    private final Repo<String, List<Integer>> capacityRepo;
    public CapacityServiceImpl(Plugin plugin) {
        capacityRepo = new YamlRepo<>(
                new File(plugin.getDataFolder(), "capacity.yml").toPath(),
                plugin,
                conf -> conf.getKeys(false).stream().collect(Collectors.toMap(
                        value -> value,
                        conf::getIntegerList))
        );
    }

    @Override
    public List<Integer> get(ItemStack item) {
        String key = item.getType().name();
        if(item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
            key += "->" + item.getItemMeta().getCustomModelData();
        }
        Optional<List<Integer>> result = capacityRepo.get(key);
        return result.orElse(Collections.emptyList());
    }

    @Override
    public void reload() {
        capacityRepo.reload();
    }
}
