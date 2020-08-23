package me.onlyfire.firefreeze.backend;

import me.onlyfire.firefreeze.Firefreeze;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class SQLiteDownloader {

    public static void download(){
        File file = new File(Firefreeze.getInstance().getDataFolder() + "/database/sqlite-jdbc-3.30.1.jar");

        if (!file.exists()) {
            Bukkit.getConsoleSender().sendMessage("§e[FireFreeze] SQLite Driver not found! Downloading...");
            Bukkit.getScheduler().runTaskAsynchronously(Firefreeze.getInstance(), () -> {
                try {
                    ReadableByteChannel readableByteChannel = Channels.newChannel(new URL("https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.32.3.2/sqlite-jdbc-3.32.3.2.jar").openStream());
                    FileOutputStream outputStream = new FileOutputStream(Firefreeze.getInstance().getDataFolder() + "/database/sqlite-jdbc-3.30.1.jar");

                    outputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                } catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage("§c[FireFreeze] Error occurred while downloading the sqlite driver");
                    e.printStackTrace();
                }
            });
            Bukkit.getConsoleSender().sendMessage("§e[FireFreeze] Finished downloading the SQLite driver!");
        }
    }

}
