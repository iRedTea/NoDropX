package me.redtea.nodropx.service.capasity;

import me.redtea.nodropx.libs.carcadex.repo.Repo;
import me.redtea.nodropx.libs.carcadex.repo.impl.yaml.YamlRepo;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public interface CapacityService {


    List<Integer> get(ItemStack item);
    void reload();

    default List<Integer> get(Material material) {
        return get(new ItemStack(material));
    }
}
