package me.redtea.nodropx.service.respawn.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.api.event.NoDropItemDropOnDeathEvent;
import me.redtea.nodropx.libs.carcadex.repo.Repo;
import me.redtea.nodropx.service.nodrop.NoDropService;
import me.redtea.nodropx.service.respawn.RespawnService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RequiredArgsConstructor
public class RespawnServiceImpl implements RespawnService {
    private final NoDropService noDropService;
    private final Map<String, Pair[]> toRespawn = new HashMap<>();
    private final Repo<String, List<Integer>> capacity;


    @Override
    public void giveSavedItems(Player player) {
        if(toRespawn.containsKey(player.getName())) {
            Pair[] pairs = toRespawn.get(player.getName());
            for(Pair pair : pairs) player.getInventory().setItem(pair.slot, pair.item);
            toRespawn.remove(player.getName());
        }
    }

    @Override
    public void saveItemsBeforeDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Set<Integer> noDropSlots = new HashSet<>();
        for(int i = 0; i < player.getInventory().getSize(); ++i) {
            ItemStack item = player.getInventory().getItem(i);
            if(!noDropService.isNoDrop(item)) continue;
            NoDropItemDropOnDeathEvent callingEvent = new NoDropItemDropOnDeathEvent(
                    player,
                    item
            );
            Bukkit.getPluginManager().callEvent(callingEvent);

            if(!callingEvent.isWillDropped()) {
                noDropSlots.add(i);
                noDropSlots.addAll(getProtectedSlots(item));
            }
        }

        List<Pair> toSave = new ArrayList<>();
        for(int slot : noDropSlots) {
            ItemStack item = player.getInventory().getItem(slot);
            toSave.add(new Pair(slot, item));
            event.getDrops().remove(item);
        }

        if(toSave.isEmpty()) return;
        toRespawn.put(player.getName(), toSave.toArray(new Pair[0]));
    }

    @AllArgsConstructor
    static class Pair {
        int slot;
        ItemStack item;
    }

    private List<Integer> getProtectedSlots(ItemStack i) {
        return capacity.get(i.getType().name()).orElse(new ArrayList<>());
    }
}
