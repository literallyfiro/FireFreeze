package me.onlyfire.firefreeze.methods;

import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.utils.InventoryBuilder;
import me.onlyfire.firefreeze.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FreezeGUI {

    public void add(Player player) {
        Firefreeze plugin = Firefreeze.getInstance();

        InventoryBuilder builder = new InventoryBuilder()
                .setTitle(plugin.getConfigFile().getString("freeze_methods.gui.name"))
                .setRows(plugin.getConfigFile().getInt("freeze_methods.gui.rows"));

        for (String sec : plugin.getConfigFile().getSection("freeze_methods.gui.items").getKeys(false)) {
            String path = "freeze_methods.gui.items." + sec;

            ItemStack item = new ItemBuilder()
                    .setPath(path)
                    .setMaterial(Material.getMaterial(plugin.getConfigFile().getString(path + ".material")))
                    .setAmount(1)
                    .setName(plugin.getConfigFile().getString(path + ".name"))
                    .setLore(plugin.getConfigFile().getStringList(path + ".lore"))
                    .build();

            builder.addItem(item, plugin.getConfigFile().getInt(path + ".slot"));

        }

        builder.build().send(player);
    }

}
