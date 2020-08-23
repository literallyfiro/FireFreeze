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
    private int rId = 0;
    private String pluginVersion;
    private boolean updateFound;

    public FireFreezeUpdater(Firefreeze plugin, int resourceId) {
        this.plugin = plugin;
        this.rId = resourceId;
        this.pluginVersion = plugin.getDescription().getVersion();
    }

    public void update() {
        plugin.getLogger().info("Checking for updates...");

        try {
            HttpURLConnection connection = (HttpsURLConnection) new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=" + rId
            ).openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            String newVer = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
            updateFound = !newVer.equalsIgnoreCase(pluginVersion);

        } catch (IOException exception) {
            exception.printStackTrace();
        }


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
