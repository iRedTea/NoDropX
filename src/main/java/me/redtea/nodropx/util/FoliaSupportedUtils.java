package me.redtea.nodropx.util;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class FoliaSupportedUtils {
    private final Plugin plugin;

    public void runTask(Runnable runnable) {
        try {
            Bukkit.getScheduler().runTask(plugin, runnable);
        } catch (UnsupportedOperationException e) {
            new Thread(runnable).start();
        }
    }
}
