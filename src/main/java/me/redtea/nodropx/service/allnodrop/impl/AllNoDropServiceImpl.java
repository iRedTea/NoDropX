package me.redtea.nodropx.service.allnodrop.impl;

import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.service.allnodrop.AllNoDropService;
import me.redtea.nodropx.service.nbt.NBTService;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class AllNoDropServiceImpl implements AllNoDropService {
    private final NBTService nbtService;

    @Override
    public void setAllNoDrop(ItemStack item, boolean isAllNoDrop) {
        nbtService.setBoolTag(item, "isAllNoDrop", isAllNoDrop);
        nbtService.setBoolTag(item, "isNoDrop", isAllNoDrop);
    }

    @Override
    public boolean isAllNoDrop(ItemStack item) {
        return nbtService.getBoolTag(item, "isAllNoDrop");
    }
}
