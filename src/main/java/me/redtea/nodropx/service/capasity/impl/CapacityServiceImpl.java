package me.redtea.nodropx.service.capasity.impl;

import beer.devs.fastnbt.nms.nbt.NItem;
import me.redtea.nodropx.libs.carcadex.repo.Repo;
import me.redtea.nodropx.libs.carcadex.repo.impl.yaml.YamlRepo;
import me.redtea.nodropx.service.capasity.CapacityService;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

        if(NItem.hasNBT(item)) {
            NItem nItem = NItem.of(item);
            if(nItem.hasKey("itemsadder")) {
                key = nItem.getCompound("itemsadder").getString("namespace") + ":" + nItem.getCompound("itemsadder").getString("id");
            } else if(item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
                key += "->" + item.getItemMeta().getCustomModelData();
            }
        }

        Optional<List<Integer>> result = capacityRepo.get(key);
        return result.orElse(Collections.emptyList());
    }

    @Override
    public void reload() {
        capacityRepo.reload();
    }
}
