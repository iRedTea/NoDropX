package me.redtea.nodropx.util;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {
    private final JavaPlugin plugin;
    private final int resourceId;
    private final FoliaSupportedUtils foliaSupportedUtils;


    public UpdateChecker(JavaPlugin plugin, int resourceId, FoliaSupportedUtils foliaSupportedUtils) {
        this.plugin = plugin;
        this.resourceId = resourceId;

        this.foliaSupportedUtils = foliaSupportedUtils;
    }


    public void getVersion(final Consumer<String> consumer) {
        foliaSupportedUtils.runTask(() -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scann = new Scanner(is)) {
                if (scann.hasNext()) {
                    consumer.accept(scann.next());
                }
            } catch (IOException e) {
                plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        });


    }
}
