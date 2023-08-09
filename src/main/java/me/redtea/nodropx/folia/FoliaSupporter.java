package me.redtea.nodropx.folia;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.SneakyThrows;
import me.redtea.nodropx.service.respawn.RespawnService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.io.FileReader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

//Experimental. Must be refactored in the future.
public class FoliaSupporter {
    private RespawnService respawnService;

    @Getter
    private static boolean runningOnFolia = false;

    @SneakyThrows
    public void run(RespawnService respawnService, Plugin plugin) {
        String version = ((JsonObject) new JsonParser().parse(new FileReader("version_history.json"))).get("currentVersion").getAsString();
        if(!version.toLowerCase().contains("folia") && !plugin.getConfig().getBoolean("forceUseFoliaPatches")) {
            return;
        }
        runningOnFolia = true;
        plugin.getLogger().info("Found Folia! Applying patches for this...");
        this.respawnService = respawnService;
        Bukkit.getPluginManager().registerEvents(new FoliaDeathListener(), plugin);
        plugin.getLogger().info("Patches for Folia successfully applied!");
    }


    /*
    temp fix when folia not called respawn event
     */
    class FoliaDeathListener implements Listener {
        private final Set<Player> toRespawn = new CopyOnWriteArraySet<>();

        public FoliaDeathListener() {
            new Thread(() -> {
                while (true) {
                    for(Player player : toRespawn) {
                        if(!player.isDead()) {
                            onRespawn(player);
                            toRespawn.remove(player);
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        public void onRespawn(Player player) {
            respawnService.giveSavedItems(player);
        }

        @EventHandler
        public void onDeath(PlayerDeathEvent event) {
            toRespawn.add(event.getEntity());
        }
    }
}
