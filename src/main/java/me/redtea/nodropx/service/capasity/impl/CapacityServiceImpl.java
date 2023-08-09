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
    private final Map<String, Integer> customModelDataRequired = new HashMap<>();
    public CapacityServiceImpl(Plugin plugin) {
        capacityRepo = new YamlRepo<>(
                new File(plugin.getDataFolder(), "capacity.yml").toPath(),
                plugin,
                conf -> conf.getKeys(false).stream().collect(Collectors.toMap(
                        value -> {
                            if(value.contains("->")) {
                                String[] args = value.split("->");
                                customModelDataRequired.put(args[0], Integer.parseInt(args[1]));
                                return args[0];
                            } else return value;
                        },
                        conf::getIntegerList))
        );
    }

    @Override
    public List<Integer> get(ItemStack item) {
        String key = item.getType().name();
        Optional<List<Integer>> result = capacityRepo.get(key);
        if(!result.isPresent()) {
            return Collections.emptyList();
        }
        if(customModelDataRequired.containsKey(key)) {
            int cmi = customModelDataRequired.get(key);
            if(!item.getItemMeta().hasCustomModelData() || item.getItemMeta().getCustomModelData() != cmi) {
                return Collections.emptyList();
            }
        }
        return result.get();
    }

    @Override
    public void reload() {
        capacityRepo.reload();
    }
}
