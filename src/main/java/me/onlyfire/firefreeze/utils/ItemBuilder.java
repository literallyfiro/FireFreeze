package me.onlyfire.firefreeze.utils;

import me.onlyfire.firefreeze.Firefreeze;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;
    private String name;
    private List<String> lore;
    private String path;

    public ItemBuilder() {
        this.name = "";
        this.path = "";
        this.lore = new ArrayList<>();
    }

    public ItemBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        this.meta.setDisplayName(ColorUtil.colorize(name));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (Firefreeze.getInstance().getConfigFile().getBoolean(path + ".enable_lore")) {
            List<String> coloredLore = new ArrayList<>();

            for (String s : lore) {
                coloredLore.add(ColorUtil.colorize(s));
            }

            this.lore = coloredLore;
            this.meta.setLore(coloredLore);
        }
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(meta);
        return item;
    }
}
