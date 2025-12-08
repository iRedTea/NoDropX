package me.redtea.nodropx.service.nbt.impl;

import beer.devs.fastnbt.nms.nbt.NItem;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.redtea.nodropx.service.nbt.NBTService;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FastNBTServiceImpl implements NBTService {


    @Override
    public void setBoolTag(ItemStack item, String tag, boolean value) {
        NItem nbtItem = new NItem(item);
        nbtItem.setBoolean(tag, value);
        nbtItem.merge(item);
    }

    @Override
    public boolean getBoolTag(ItemStack item, String tag) {
        if(item == null || item.getType() == Material.AIR) return false;
        NItem nbtItem = new NItem(item);
        return nbtItem.hasKey(tag) && nbtItem.getBoolean(tag);
    }
}
