package me.redtea.nodropx.factory.text.impl;

import lombok.SneakyThrows;
import me.redtea.nodropx.NoDropX;
import me.redtea.nodropx.factory.text.TextContext;
import me.redtea.nodropx.factory.text.TextServicesFactory;
import me.redtea.nodropx.model.cosmetic.CosmeticStrategy;
import me.redtea.nodropx.model.cosmetic.impl.LoreKyoriCosmetic;
import me.redtea.nodropx.model.cosmetic.impl.LoreLegacyCosmetic;
import me.redtea.nodropx.model.cosmetic.impl.LoreMD5Cosmetics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class TextServicesFactoryImpl implements TextServicesFactory {
    private CosmeticStrategy cosmeticStrategy;
    private boolean isSupportKyoriLore;
    private boolean isSupportHEX = Integer.parseInt(Bukkit.getVersion().split("\\.")[1]) >= 16;


    public TextContext create(NoDropX plugin) {

        try {
            Class<?> clazz = Class.forName("com.destroystokyo.paper.utils.PaperPluginLogger");
            isSupportKyoriLore = isSupportHEX;
        } catch (ClassNotFoundException e) {
            isSupportKyoriLore = false;
        }

        if(plugin.getConfig().getBoolean("forceUseLegacy")) {
            isSupportHEX = false;
            isSupportKyoriLore = false;
        } else if(plugin.getConfig().getBoolean("forceUseMD5Colors")) {
            isSupportKyoriLore = false;
        }


        String folder = "legacy";
        if(isSupportKyoriLore) folder = "mm";
        else if(isSupportHEX) folder = "md5";

        File messages = loadResource(plugin, folder, "messages.yml");
        File cosmetics = loadResource(plugin, folder, "cosmetics.yml");

        if(!isSupportHEX) {
            plugin.getLogger().warning("This minecraft version did not support HEX");
            plugin.getLogger().warning("Using legacy message serializer");
            cosmeticStrategy = new LoreLegacyCosmetic();
        } else if(!isSupportKyoriLore) {
            plugin.getLogger().warning("This minecraft version did not support Kyori");
            plugin.getLogger().warning("Using legacy MD5 message serializer");
            cosmeticStrategy = new LoreMD5Cosmetics();
        } else cosmeticStrategy = new LoreKyoriCosmetic();

        return new TextContext(cosmeticStrategy, messages, cosmetics, isSupportHEX, isSupportKyoriLore);
    }





    @SneakyThrows
    private File loadResource(Plugin plugin, String folder, String resourceName) {
        File file = new File(plugin.getDataFolder(), resourceName);
        if(!file.exists()) {
            InputStream in = plugin.getResource("text/" + folder + "/" + resourceName);
            Files.copy(in, file.toPath());
        }
        return file;
    }


}
