package me.redtea.nodropx.menu.storage.pages;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.ScrollingGui;
import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.menu.CachedMenu;
import me.redtea.nodropx.libs.carcadex.repo.MutableRepo;
import me.redtea.nodropx.libs.message.container.Messages;
import me.redtea.nodropx.service.material.ItemStackService;
import me.redtea.nodropx.service.nodrop.NoDropService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MFPageGui implements CachedMenu {
    private final Messages messages;
    private final MutableRepo<UUID, List<ItemStack>> personalStorageRepo;
    private final NoDropService noDropService;
    private final ItemStackService itemStackService;
    private final Plugin plugin;

    private final Map<UUID, ScrollingGui> cache = new HashMap<>();

    public void open(Player player) {
        if(cache.containsKey(player.getUniqueId())) {
            cache.get(player.getUniqueId()).open(player);
            return;
        }

        ScrollingGui gui = Gui.scrolling()
                .title(messages.get("gui.personalStorage.title").asComponent())
                .rows(6)
                .pageSize(45)
                .disableAllInteractions()
                .create();
        setFrame(gui);
        setButtons(gui);
        setAction(gui);
        setOnClose(gui);
        for(ItemStack item : personalStorageRepo.get(player.getUniqueId()).orElse(new ArrayList<>())) {
            gui.addItem(ItemBuilder.from(item).asGuiItem());
        }

        cache.put(player.getUniqueId(), gui);
        gui.open(player);
    }

    private void setOnClose(ScrollingGui gui) {
        gui.setCloseGuiAction(event -> personalStorageRepo.update(event.getPlayer().getUniqueId(), gui.getPageItems().stream()
                .map(GuiItem::getItemStack)
                .collect(Collectors.toList())));
    }

    private void setAction(ScrollingGui gui) {
        gui.setDefaultClickAction(event -> {
            if((event.getSlot() >= 45 && event.getSlot() <= 53) || event.getClickedInventory() instanceof PlayerInventory) {
                return;
            }
            if(event.getAction() == InventoryAction.PLACE_ALL && !event.getClick().isShiftClick()) {
                if(!noDropService.isNoDrop(event.getCursor())) {
                   return;
                }
                ItemStack is = event.getCursor();
                event.setCursor(new ItemStack(Material.AIR));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        gui.addItem(ItemBuilder.from(is).asGuiItem());
                        gui.update();
                    }
                }.runTask(plugin);
            } else if(event.getAction() == InventoryAction.SWAP_WITH_CURSOR && !event.getClick().isShiftClick()) {
                if(!noDropService.isNoDrop(event.getCursor())) {
                    return;
                }
                ItemStack is = event.getCursor();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        gui.addItem(ItemBuilder.from(is).asGuiItem());
                        gui.update();
                    }
                }.runTask(plugin);
                event.setCursor(new ItemStack(Material.AIR));
            } else if(event.getAction() == InventoryAction.PICKUP_ALL) {
                event.setCursor(event.getCurrentItem());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        gui.removePageItem(event.getCurrentItem());
                        gui.update();
                    }
                }.runTask(plugin);

            }

        });
    }


    private void setFrame(ScrollingGui gui) {
        for(int i = 1; i < 10; ++i) {
            gui.setItem(6, i, ItemBuilder.from(
                    itemStackService.get(messages.get("gui.personalStorage.frame.material").asUnparsedString(), 1)
            ).name(messages.get("gui.personalStorage.frame.name").asComponent()).asGuiItem(event -> event.setCancelled(true)));
        }
    }

    private void setButtons(ScrollingGui gui) {
        gui.setItem(6, 3, ItemBuilder.from(
                itemStackService.get(messages.get("gui.personalStorage.prev.material").asUnparsedString(), 1)
        ).name(messages.get("gui.personalStorage.prev.name").asComponent()).asGuiItem(event -> {
            gui.previous();
            event.setCancelled(true);
        }));
        gui.setItem(6, 7, ItemBuilder.from(
                itemStackService.get(messages.get("gui.personalStorage.next.material").asUnparsedString(), 1)
        ).name(messages.get("gui.personalStorage.next.name").asComponent()).asGuiItem(event -> {
            gui.next();
            event.setCancelled(true);
        }));
    }

    public void clearCache(Player player) {
        cache.remove(player.getUniqueId());
    }

    public void clearAllCache() {
        cache.forEach((k, v) -> {
            Player player = Bukkit.getPlayer(k);
            if(player != null) v.close(player);
        });
        cache.clear();
    }

}
