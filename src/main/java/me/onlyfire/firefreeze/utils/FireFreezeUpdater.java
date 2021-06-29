package me.onlyfire.firefreeze.utils;

import me.onlyfire.firefreeze.Firefreeze;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FireFreezeUpdater {
    private Firefreeze plugin;
    private int rId;
    private String pluginVersion;
    private boolean updateFound;

    public FireFreezeUpdater(Firefreeze plugin, int resourceId) {
        this.plugin = plugin;
        this.rId = resourceId;
        this.pluginVersion = plugin.getDescription().getVersion();
    }

    public void update() {
        plugin.getLogger().info("Checking for updates...");

        Tasks.runAsync(() -> {
            try {
                HttpURLConnection connection = (HttpsURLConnection) new URL(
                        "https://api.spigotmc.org/legacy/update.php?resource=" + rId
                ).openConnection();

                connection.setRequestProperty("User-Agent", "FireFreezeUpdater");

                String newVer = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
                updateFound = !newVer.equalsIgnoreCase(pluginVersion);

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        if (updateFound) {
            plugin.getLogger().info("Found a new Update! Please download it at https://www.spigotmc.org/resources/77105/");
        } else {
            plugin.getLogger().info("No updates found.");
        }
    }


    public boolean updateAvailable() {
        return updateFound;
    }
}
