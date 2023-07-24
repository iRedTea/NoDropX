package me.redtea.nodropx.service.nbt.impl;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.redtea.nodropx.service.nbt.NBTService;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Tr7zwNBTServiceImpl implements NBTService {
    @Override
    public void setNoDropTag(ItemStack item, boolean isNoDrop) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean("isNoDrop", true);
        nbtItem.applyNBT(item);
    }

    @Override
    public boolean getNoDropTag(ItemStack item) {
        if(item == null || item.getType() == Material.AIR) return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasTag("isNoDrop") && nbtItem.getBoolean("isNoDrop");
    }
}
