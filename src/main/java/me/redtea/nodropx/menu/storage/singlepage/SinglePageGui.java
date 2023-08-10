package me.redtea.nodropx.menu.storage.singlepage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.libs.carcadex.repo.MutableRepo;
import me.redtea.nodropx.libs.message.container.Messages;
import me.redtea.nodropx.menu.CachedMenu;
import me.redtea.nodropx.util.MD5ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RequiredArgsConstructor
public class SinglePageGui implements CachedMenu {
    private final Messages messages;
    private final MutableRepo<UUID, List<ItemStack>> personalStorageRepo;
    @Getter
    private final Map<UUID, Inventory> cache = new HashMap<>();

    @Override
    public void open(Player player) {
        if(cache.containsKey(player.getUniqueId())) {
            player.openInventory(cache.get(player.getUniqueId()));
            return;
        }
        Inventory gui = Bukkit.createInventory(null, 54, MD5ColorUtils.translateHexColorCodes(messages.get("gui.personalStorage.title").asUnparsedString()));
        for(ItemStack item : personalStorageRepo.get(player.getUniqueId()).orElse(new ArrayList<>())) if(item != null) gui.addItem(item);

        cache.put(player.getUniqueId(), gui);
        player.openInventory(gui);
    }

    public void clearCache(Player player) {
        cache.remove(player.getUniqueId());
    }

    public void clearAllCache() {
        cache.clear();
    }

    public boolean isStorageGui(Inventory inventory) {
        return cache.containsValue(inventory);
    }
}
