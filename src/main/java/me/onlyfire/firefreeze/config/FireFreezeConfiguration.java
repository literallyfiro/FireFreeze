package me.onlyfire.firefreeze.config;

import com.google.common.io.ByteStreams;
import me.onlyfire.firefreeze.Firefreeze;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;

public class FireFreezeConfiguration {

    private File file;
    private FileConfiguration config;
    private String name;

    private Firefreeze plugin = Firefreeze.getInstance();

    public FireFreezeConfiguration(String name) {
        this.name = name;
        this.file = new File(plugin.getDataFolder(), File.separator + name);
        saveDefaultConfig();
    }

    public void saveDefaultConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();

                InputStream is = plugin.getResource(name);
                OutputStream os = new FileOutputStream(file);

                ByteStreams.copy(is, os);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }

}
