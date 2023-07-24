package me.redtea.nodropx.util;

import lombok.val;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MaterialUtils {
    @NotNull
    public static Material getMaterialFromString(String s) {
        if(s == null || s.equals("")) return Material.AIR;
        Material material = Material.matchMaterial(s);
        if(material != null) return material;

        String[] spl = s.split(":");

        if (spl[0].equals("minecraft")) {
            material = Material.matchMaterial(spl[1]);
        } else {
            if(spl.length < 2) {
                material = Material.matchMaterial(spl[0]);
            } else {
                material = Material.matchMaterial(spl[0]+"_"+spl[1]);;
            }
        }

        return  material == null ? Material.AIR : material;
    }

    public void remove(Inventory inventory, Material type, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }
}
