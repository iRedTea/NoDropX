package me.redtea.nodropx.util;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/*
    Verify for versions:
    - 1.0.2
    - 1.0.3
     */
public class ConfigVerifier {
    public void verifyDefaults(Plugin plugin) {
        Map<String, Object> properties = new HashMap<String, Object>(){{
            put("forceUseLegacy", false);
            put("forceUseMD5Colors", false);

            //1.0.3
            put("checkForUpdates", true);
            put("personalStorageType", "SINGLE_PAGE");
            put("confirmThrowNoDropItem", true);
            put("forceUseFoliaPatches", false);
        }};

        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        for(Map.Entry<String, Object> entry : properties.entrySet()) {
            if(!config.contains(entry.getKey(), true))
                plugin.getConfig().set(entry.getKey(), entry.getValue());
        }
        plugin.saveConfig();
    }
}
