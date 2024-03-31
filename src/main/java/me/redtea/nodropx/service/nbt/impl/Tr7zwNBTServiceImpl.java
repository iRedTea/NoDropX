package me.redtea.nodropx.service.nbt.impl;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.redtea.nodropx.service.nbt.NBTService;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Tr7zwNBTServiceImpl implements NBTService {


    @Override
    public void setBoolTag(ItemStack item, String tag, boolean value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean(tag, value);
        nbtItem.applyNBT(item);
    }

    @Override
    public boolean getBoolTag(ItemStack item, String tag) {
        if(item == null || item.getType() == Material.AIR) return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasTag(tag) && nbtItem.getBoolean(tag);
    }
}
