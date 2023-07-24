package me.redtea.nodropx.service.nodrop.impl;

import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.service.cosmetic.CosmeticService;
import me.redtea.nodropx.service.nbt.NBTService;
import me.redtea.nodropx.service.nodrop.NoDropService;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class NoDropServiceImpl implements NoDropService {
    private final NBTService nbtService;
    private final CosmeticService cosmeticService;

    @Override
    public void setNoDrop(ItemStack item, boolean isNoDrop) {
        nbtService.setNoDropTag(item, isNoDrop);
        cosmeticService.applyAll(item);
    }

    @Override
    public boolean isNoDrop(ItemStack item) {
        if(item == null) return false;
        return nbtService.getNoDropTag(item);
    }
}
