package me.onlyfire.firefreeze.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryBuilder {

    private Inventory inventory;
    private int rows;
    private String title;
    private Map<ItemStack, Integer> items;

    public InventoryBuilder(){
        this.rows = 0;
        this.title = "";
        this.items = new HashMap<>();
    }

    public InventoryBuilder setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public InventoryBuilder setTitle(String title) {
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        return this;
    }

    public InventoryBuilder addItem(ItemStack item, int pos){
        this.items.put(item, pos);
        return this;
    }

    public InventoryBuilder build(){
        this.inventory = Bukkit.createInventory(null, rows * 9, title);

        for (Map.Entry<ItemStack, Integer> items : this.items.entrySet()){
            this.inventory.setItem(items.getValue(), items.getKey());
        }

        return this;
    }

    public void send(Player player){
        player.openInventory(this.inventory);
    }

}
