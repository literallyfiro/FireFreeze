package me.onlyfire.firefreeze.command;

import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.objects.FreezeProfile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {
    private final Firefreeze plugin;

    public FreezeCommand(Firefreeze plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getPrefix() + plugin.getMessagesFile().getString("errors.only_players")));
            return true;
        }

        if (!sender.hasPermission("firefreeze.freeze")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getPrefix() + plugin.getMessagesFile().getString("errors.not_permission")));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getPrefix() + plugin.getMessagesFile().getString("errors.incorrect_usage")
                            .replace("{COMMAND}", "/freeze <player>")));
            return true;
        }

        Player target = plugin.getServer().getPlayerExact(args[0]);

        if (target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Firefreeze.getInstance().getPrefix() + plugin.getMessagesFile().getString("errors.player_not_found")));
            return true;
        }

        if (target == sender){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Firefreeze.getInstance().getPrefix() + plugin.getMessagesFile().getString("errors.cannot_freeze_yourself")));
            return true;
        }

        if (target.hasPermission("firefreeze.exempt")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getPrefix() + plugin.getMessagesFile().getString("errors.cannot_freeze_player")));
            return true;
        }

        Player player = (Player) sender;
        FreezeProfile profile = new FreezeProfile(target);

        if (profile.isFrozen()) {
            if (plugin.getConfigFile().getBoolean("command_settings.merge_freeze_with_unfreeeze")) {

                profile.unfreeze(player);

            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Firefreeze.getInstance().getPrefix() + plugin.getMessagesFile().getString("errors.player_already_frozen")
                        .replace("{PLAYER}", target.getName())));
                return true;
            }
            return true;
        }

        profile.freeze(player);

        return true;
    }
}
