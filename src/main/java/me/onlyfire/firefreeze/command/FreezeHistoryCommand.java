package me.onlyfire.firefreeze.command;

import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.objects.FreezeHistory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeHistoryCommand implements CommandExecutor {
    private final Firefreeze plugin;

    public FreezeHistoryCommand(Firefreeze plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getPrefix() + plugin.getMessagesFile().getString("errors.only_players")));
            return true;
        }

        if (!sender.hasPermission("firefreeze.freezehistory")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getPrefix() + plugin.getMessagesFile().getString("errors.not_permission")));
            return true;
        }

        if (args.length != 1){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getPrefix() + plugin.getMessagesFile().getString("errors.incorrect_usage")
                            .replace("{COMMAND}", "/freezehistory <player>")));
            return true;
        }

        FreezeHistory history = plugin.getConnection().searchFreezeHistoryFor(args[0]);
        history.sendTo(sender);
        return true;
    }
}
