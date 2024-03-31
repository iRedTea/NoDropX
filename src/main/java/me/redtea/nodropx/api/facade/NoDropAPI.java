package me.redtea.nodropx.api.facade;

import lombok.val;
import me.redtea.nodropx.api.facade.manipulator.StorageManipulator;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

/**
 * Facade for all plugin functions
 */
public interface NoDropAPI {
    /**
     * Set no drop to item
     * @param item the item who's no drop property are changing
     * @param isNoDrop will the item can drop from inventory on death?
     * @author redtea
     * @since 1.0.0
     */
    ItemStack setNoDrop(@NotNull ItemStack item, boolean isNoDrop);

    /**
     * Check if item is no drop item
     * @param item the item being checked
     * @return true if item can not drop from inventory on death
     * @author redtea
     * @since 1.0.0
     */
    boolean isNoDrop(@NotNull ItemStack item);

    /**
     * Generate new no drop item by material and amount
     * @param material material of item
     * @param amount count of item
     * @return no drop item
     * @author redtea
     * @since 1.0.0
     */
    default ItemStack getNoDrop(@NotNull Material material, int amount) {
        val item = new ItemStack(material, amount);
        setNoDrop(item, true);
        return item;
    }

    /**
     * Gives the entity no drop item
     * @param entity who will get no drop item
     * @param item no drop item
     * @author redtea
     * @since 1.0.0
     */
    void giveNoDrop(@NotNull HumanEntity entity, @NotNull ItemStack item);

    /**
     * Gives the entity no drop item
     * @param entity who will get no drop item
     * @param material type of item
     * @param amount amount of item
     * @author redtea
     * @since 1.0.0
     */
    default void giveNoDrop(@NotNull HumanEntity entity, @NotNull Material material, int amount) {
        giveNoDrop(entity, getNoDrop(material, amount));
    }

    /**
     * returns an object for accessing the storage of a player with UUID playerUniqueId
     * @param playerUniqueId UUID of player
     * @return object for accessing the storage
     * @author redtea
     * @since 1.0.0
     */
    @NotNull StorageManipulator getStorageManipulator(@NotNull UUID playerUniqueId);

    /**
     * returns the slot numbers that no drop item with this itemStack saves at death
     * @param itemStack itemStack to check
     * @return numbers of saved slots
     * @author redtea
     * @since 1.0.3
     */
    @NotNull Collection<Integer> getCapacitySlots(ItemStack itemStack);

    /**
     * returns the slot numbers that no drop item with this material saves at death
     * @param material material to check
     * @return numbers of saved slots
     * @author redtea
     * @since 1.0.0
     */
    @NotNull Collection<Integer> getCapacitySlots(Material material);

    /**
     * @see NoDropAPI#getCapacitySlots(Material)
     */
    @NotNull Collection<Integer> getCapacitySlots(String material);

    /**
     * @see NoDropAPI#getStorageManipulator(UUID) 
     */
    default @NotNull StorageManipulator getStorageManipulator(OfflinePlayer offlinePlayer) {
        return getStorageManipulator(offlinePlayer.getUniqueId());
    }

    /**
     * @see NoDropAPI#giveNoDrop(HumanEntity, Material, int)
     */
    void giveNoDrop(@NotNull HumanEntity entity, @NotNull String material, int amount);

    /**
     * @see NoDropAPI#giveNoDrop(HumanEntity, Material, int)
     */
    default void giveNoDrop(@NotNull HumanEntity entity, @NotNull Material material) {
        giveNoDrop(entity, material, 1);
    }

    /**
     * @see NoDropAPI#giveNoDrop(HumanEntity, Material, int)
     */
    default void giveNoDrop(@NotNull HumanEntity entity, @NotNull String material) {
        giveNoDrop(entity, material, 1);
    }

    /**
     * @see NoDropAPI#getNoDrop(Material, int)
     */
    @NotNull ItemStack getNoDrop(@NotNull String material, int amount);

    /**
     * @see NoDropAPI#getNoDrop(Material, int)
     */
    default @NotNull ItemStack getNoDrop(@NotNull Material material) {
        return getNoDrop(material, 1);
    }

    /**
     * @see NoDropAPI#getNoDrop(Material, int)
     */
    default @NotNull ItemStack getNoDrop(@NotNull String material) {
        return getNoDrop(material, 1);
    }

    /**
     * Set AllNoDrop
     * @param item the item who's AllNoDrop property are changing
     * @param isAllNoDrop will the item save all items in player's inventory from death?
     * @author redtea
     * @since 1.0.4
     */
    ItemStack setAllNoDrop(@NotNull ItemStack item, boolean isAllNoDrop);

    /**
     * Check if item is AllNoDrop
     * @param item the item being checked
     * @return true if the item save all items in player's inventory from death
     * @author redtea
     * @since 1.0.4
     */
    boolean isAllNoDrop(@NotNull ItemStack item);

    /**
     * Generate new nodropall item by material and amount
     * @param material material of item
     * @param amount count of item
     * @return nodropall item
     * @author redtea
     * @since 1.0.4
     */
    default ItemStack getAllNoDrop(@NotNull Material material, int amount) {
        val item = new ItemStack(material, amount);
        setAllNoDrop(item, true);
        return item;
    }

    /**
     * Generate new nodropall item by material and amount
     * @param material material of item
     * @param amount count of item
     * @return nodropall item
     * @author redtea
     * @since 1.0.4
     */

    /**
     * @see NoDropAPI#getAllNoDrop(Material, int)
     */
     ItemStack getAllNoDrop(@NotNull String material, int amount);

    /**
     * @see NoDropAPI#getAllNoDrop(Material, int)
     */
    void giveAllNoDrop(HumanEntity entity, @NotNull String material, int amount);
}
