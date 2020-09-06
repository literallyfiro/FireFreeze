package me.onlyfire.firefreeze.listener;

import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.enums.EntryType;
import me.onlyfire.firefreeze.events.PlayerFreezeQuitEvent;
import me.onlyfire.firefreeze.methods.FreezeGUI;
import me.onlyfire.firefreeze.objects.FreezeProfile;
import me.onlyfire.firefreeze.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class FreezeListener implements Listener {

    private Firefreeze plugin;

    public FreezeListener(Firefreeze plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (plugin.getUpdater().updateAvailable()) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (players.hasPermission("firefreeze.admin")) {
                    players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&4[FireFreeze] &cFound an update on SpigotMC! " +
                                    "Please download it at &4https://www.spigotmc.org/resources/77105/"));
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FreezeProfile profile = new FreezeProfile(player);

        if (profile.isFrozen()) {
            plugin.getConnection().addEntry(player, profile.getWhoFroze().getName(), EntryType.QUIT);
            profile.unfreeze(profile.getWhoFroze());

            PlayerFreezeQuitEvent quitEvent = new PlayerFreezeQuitEvent(player);
            Bukkit.getPluginManager().callEvent(quitEvent);

            for (String cmds : plugin.getConfigFile().getStringList("freeze_settings.console_quit_command")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmds.replace("{PLAYER}", player.getName()));
            }


            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("firefreeze.staff")) {
                    pl.sendMessage(ColorUtil.colorize(plugin.getMessagesFile().getString("staff_broadcast.quit")
                            .replace("{PLAYER}", player.getName())));
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        FreezeProfile profile = new FreezeProfile(player);

        if (profile.isFrozen() && plugin.getConfigFile().getBoolean("freeze_settings.onFreeze.disable_movements")) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
                event.setTo(event.getFrom());
            }
        }
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        FreezeProfile profile = new FreezeProfile(player);

        if (profile.isFrozen()) {
            if (!plugin.getConfigFile().getBoolean("freeze_settings.onFreeze.disable_chat")) {
                if (plugin.getConfigFile().getBoolean("freeze_methods.anydesk_task.enable"))
                    plugin.getAnydeskTask().get(player.getUniqueId()).setMessage(event.getMessage());
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCommandExecute(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        FreezeProfile profile = new FreezeProfile(player);

        if (profile.isFrozen() && plugin.getConfigFile().getBoolean("freeze_settings.onFreeze.disable_commands")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        FreezeProfile profile = new FreezeProfile(player);

        if (profile.isFrozen() && plugin.getConfigFile().getBoolean("freeze_settings.onFreeze.disable_drop")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        FreezeProfile profile = new FreezeProfile(player);

        if (event.getItem() == null) return;
        if (event.getClickedBlock() == null) return;

        if (profile.isFrozen()) {
            if (plugin.getConfigFile().getBoolean("freeze_settings.onFreeze.disable_interaction")) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) event.getEntity();

            FreezeProfile profile = new FreezeProfile(player);

            if (profile.isFrozen() && plugin.getConfigFile().getBoolean("freeze_settings.onFreeze.disable_damage")) {
                event.setCancelled(true);
            }
        }

        if (damager instanceof Player) {
            Player player = (Player) event.getDamager();

            FreezeProfile profile = new FreezeProfile(player);

            if (profile.isFrozen() && plugin.getConfigFile().getBoolean("freeze_settings.onFreeze.disable_damage")) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        FreezeProfile profile = new FreezeProfile(player);

        if (profile.isFrozen() && plugin.getConfigFile().getBoolean("freeze_settings.onFreeze.disable_interaction")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamager(EntityDamageByBlockEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) event.getEntity();

            FreezeProfile profile = new FreezeProfile(player);

            if (profile.isFrozen() && plugin.getConfigFile().getBoolean("freeze_settings.onFreeze.disable_damage")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        FreezeProfile profile = new FreezeProfile(player);

        if (event.getCurrentItem() == null) return;
        if (event.getClickedInventory() == null) return;

        if (profile.isFrozen()) {
            if (event.getView().getTitle().equals(ColorUtil.colorize(plugin.getConfigFile().getString("freeze_methods.gui.name")))) {
                event.setCancelled(true);
            }

            if (plugin.getConfigFile().getBoolean("freeze_settings.onFreeze.disable_inventory_click")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        FreezeProfile profile = new FreezeProfile(player);

        if (profile.isFrozen()) {
            if (!plugin.getConfigFile().getBoolean("freeze_methods.gui.enable") &&
                    plugin.getConfigFile().getBoolean("freeze_settings.onFreeze.disable_inventory_open")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        FreezeProfile profile = new FreezeProfile(player);

        if (event.getInventory() == null) return;

        if (profile.isFrozen()) {
            if (event.getView().getTitle().equals(ColorUtil.colorize(plugin.getConfigFile().getString("freeze_methods.gui.name")))) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new FreezeGUI().add(player);
                    }
                }.runTaskLater(plugin, 1L);
            }
        }
    }

}
