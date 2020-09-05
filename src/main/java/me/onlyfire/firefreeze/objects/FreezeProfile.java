package me.onlyfire.firefreeze.objects;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.enums.EntryType;
import me.onlyfire.firefreeze.events.PlayerFreezeAddEvent;
import me.onlyfire.firefreeze.events.PlayerFreezeRemoveEvent;
import me.onlyfire.firefreeze.methods.FreezeGUI;
import me.onlyfire.firefreeze.methods.FreezeTitle;
import me.onlyfire.firefreeze.enums.LocationType;
import me.onlyfire.firefreeze.tasks.AnydeskTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class FreezeProfile {

    @Getter private final Player player;

    private Firefreeze plugin = Firefreeze.getInstance();

    public void freeze(Player staff){
        setFrozen(staff, true, false);
    }

    public void unfreeze(Player staff){
        setFrozen(staff, false, false);
    }

    public void forceUnfreeze(Player staff){
        setFrozen(staff, false, true);
    }

    private void setFrozen(@NonNull Player staff, boolean frozen, boolean forced) {
        if (frozen){
            plugin.getFrozenPlayers().add(player.getUniqueId());
            plugin.getConnection().addFreeze(player, staff);

            useMethods(staff, true);

            plugin.getConnection().addEntry(player, staff.getName(), forced ? EntryType.FORCED : EntryType.FREEZE);

            for (String cmds : plugin.getConfigFile().getStringList("freeze_settings.console_freeze_command")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmds
                        .replace("{PLAYER}", player.getName())
                        .replace("{STAFF}", staff.getName()));
            }

            for (Player pl : plugin.getServer().getOnlinePlayers()) {
                if (pl.hasPermission("firefreeze.staff")) {
                    if (pl != staff)
                        pl.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesFile().getString("staff_broadcast.froze")
                                .replace("{STAFF}", staff.getName()).replace("{PLAYER}", player.getName())));
                }
            }

            staff.sendMessage(ChatColor.translateAlternateColorCodes('&', Firefreeze.getInstance().getPrefix() + plugin.getMessagesFile().getString("freeze.player_frozen")
                    .replace("{PLAYER}", player.getName())));

            PlayerFreezeAddEvent addEvent = new PlayerFreezeAddEvent(staff, player);
            plugin.getServer().getPluginManager().callEvent(addEvent);

        }else {
            plugin.getFrozenPlayers().remove(player.getUniqueId());

            useMethods(staff, false);

            plugin.getConnection().removeFreeze(player);
            plugin.getConnection().addEntry(player, staff.getName(), forced ? EntryType.FORCED : EntryType.UNFREEZE);

            for (String cmds : plugin.getConfigFile().getStringList("freeze_settings.console_unfreeze_command")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmds
                        .replace("{PLAYER}", player.getName())
                        .replace("{STAFF}", staff.getName()));
            }

            for (Player pl : plugin.getServer().getOnlinePlayers()) {
                if (pl.hasPermission("firefreeze.staff")) {
                    if (pl != staff)
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesFile().getString("staff_broadcast.unfroze")
                            .replace("{STAFF}", staff.getName()).replace("{PLAYER}", player.getName())));
                }
            }

            staff.sendMessage(ChatColor.translateAlternateColorCodes('&', Firefreeze.getInstance().getPrefix() + plugin.getMessagesFile().getString("unfreeze.player_unfrozen")
                    .replace("{PLAYER}", player.getName())));


            PlayerFreezeRemoveEvent removeEvent = new PlayerFreezeRemoveEvent(staff, player);
            plugin.getServer().getPluginManager().callEvent(removeEvent);

        }
    }

    private void freezeTeleport(Player player, LocationType type){
        Location loc = new Location(type.getWorld(),
                type.getX(),
                type.getY(),
                type.getZ(),
                type.getYaw(),
                type.getPitch());

            if (!type.isEnabled()){
                if (player.hasPermission("firefreeze.admin"))
                    player.sendMessage("§cCan't teleport to the \"" + type.getName() + "\" location because the spawn is not set!");
                return;
            }

            player.teleport(loc);

    }

    private void useMethods(Player staff, boolean freeze){
        if (freeze){
            if (plugin.getConfigFile().getBoolean("freeze_methods.anydesk_task.enable"))
                plugin.getAnydeskTask().put(player.getUniqueId(), new AnydeskTask(player));

            if (plugin.getConfigFile().getBoolean("freeze_methods.enable_freeze_teleport")) {
                //Staff teleport
                freezeTeleport(staff, LocationType.STAFF);

                //Target Teleport
                freezeTeleport(player, LocationType.FROZEN);
            }

            if (plugin.getConfigFile().getBoolean("freeze_methods.gui.enable")) {
                new FreezeGUI().add(player);
            }

            if (plugin.getConfigFile().getBoolean("freeze_methods.titles.enable")) {
                FreezeTitle title = new FreezeTitle();

                title.send(player,
                        ChatColor.translateAlternateColorCodes('&', plugin.getConfigFile().getString("freeze_methods.titles.freeze_title")),
                        ChatColor.translateAlternateColorCodes('&', plugin.getConfigFile().getString("freeze_methods.titles.freeze_subtitle")),
                        5, 20, 5);

            }

            if (plugin.getConfigFile().getBoolean("freeze_methods.normal_chat.enable")){

                for (String s : plugin.getConfigFile().getStringList("freeze_methods.normal_chat.froze_message"))
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));

            }

            if (plugin.getConfigFile().getBoolean("freeze_methods.freeze_effect.enable"))
                player.addPotionEffect(PotionEffectType.getByName(
                        plugin.getConfigFile().getString("freeze_methods.freeze_effect.name"))
                        .createEffect(Integer.MAX_VALUE, plugin.getConfigFile().getInt("freeze_methods.freeze_effect.power")));


            if (plugin.newVersionCheck()){
                if (plugin.getConfigFile().getBoolean("freeze_methods.enable_freeze_glowing")){
                    player.setGlowing(true);
                }
            }



        }else {
            if (plugin.getConfigFile().getBoolean("freeze_methods.anydesk_task.enable"))
                plugin.getAnydeskTask().remove(player.getUniqueId());

            if (plugin.getConfigFile().getBoolean("freeze_methods.enable_freeze_teleport")) {
                //Staff teleport
                freezeTeleport(staff,  LocationType.FINAL);

                //Target Teleport
                freezeTeleport(player, LocationType.FINAL);
            }

            if (plugin.getConfigFile().getBoolean("freeze_methods.freeze_effect.enable"))
                player.removePotionEffect(Objects.requireNonNull(PotionEffectType.getByName(plugin.getConfigFile().getString("freeze_methods.freeze_effect.name"))));

            if (plugin.getConfigFile().getBoolean("freeze_methods.gui.enable")) {
                player.closeInventory();
            }

            if (plugin.getConfigFile().getBoolean("freeze_methods.titles.enable")) {
                FreezeTitle title = new FreezeTitle();

                title.send(player,
                        ChatColor.translateAlternateColorCodes('&', plugin.getConfigFile().getString("freeze_methods.titles.unfreeze_title")),
                        ChatColor.translateAlternateColorCodes('&', plugin.getConfigFile().getString("freeze_methods.titles.unfreeze_subtitle")),
                        5, 20, 5);

            }

            if (plugin.getConfigFile().getBoolean("freeze_methods.normal_chat.enable")){

                for (String s : plugin.getConfigFile().getStringList("freeze_methods.normal_chat.unfroze_message"))
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));

            }

            if (plugin.newVersionCheck()){
                if (plugin.getConfigFile().getBoolean("freeze_methods.enable_freeze_glowing")){
                    player.setGlowing(false);
                }
            }
        }
    }

    public Player getWhoFroze(){
        return plugin.getConnection().getWhoFroze(player);

    }

    public boolean isFrozen(){
        return plugin.getFrozenPlayers().contains(player.getUniqueId());
    }

}
