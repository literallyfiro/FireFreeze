package me.onlyfire.firefreeze.utils;

import me.onlyfire.firefreeze.Firefreeze;
import org.bukkit.Bukkit;

public class Tasks {

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Firefreeze.getInstance(), runnable);
    }

}
