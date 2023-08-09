package me.redtea.nodropx.menu.storage.singlepage;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.libs.carcadex.repo.MutableRepo;
import me.redtea.nodropx.service.nodrop.NoDropService;
import me.redtea.nodropx.util.FoliaSupportedUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class SinglePageController {
    private final NoDropService noDropService;
    private final SinglePageGui singlePageGui;
    private final MutableRepo<UUID, List<ItemStack>> personalStorageRepo;
    private final FoliaSupportedUtils foliaSupportedUtils;

    private final List<InventoryAction> clickActions = Lists.newArrayList(InventoryAction.MOVE_TO_OTHER_INVENTORY, InventoryAction.PLACE_ALL, InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME,
            InventoryAction.SWAP_WITH_CURSOR, InventoryAction.NOTHING, InventoryAction.COLLECT_TO_CURSOR, InventoryAction.HOTBAR_MOVE_AND_READD, InventoryAction.UNKNOWN);


    public void onClick(InventoryClickEvent event) {
        if(clickActions.contains(event.getAction()) && !(noDropService.isNoDrop(event.getCurrentItem()))) {
            event.setCancelled(true);
        }
        foliaSupportedUtils.runTask(() -> {
            for(ItemStack itemStack : event.getInventory()) {
                if (itemStack != null && !noDropService.isNoDrop(itemStack)) {
                    event.getWhoClicked().getInventory().addItem(itemStack);
                    event.getInventory().remove(itemStack);
                    ((Player) event.getWhoClicked()).updateInventory();
                }
            }
        });

    }

    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        personalStorageRepo.update(event.getPlayer().getUniqueId(),
                Arrays.asList(singlePageGui.getCache().get(player.getUniqueId()).getContents()));
    }
}
