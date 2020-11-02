package me.onlyfire.firefreeze.command;

import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.objects.FreezeProfile;
import me.onlyfire.firefreeze.utils.ColorUtil;
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

        Player player = (Player) sender;

        if (!player.hasPermission("firefreeze.freeze")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getPrefix() + plugin.getMessagesFile().getString("errors.not_permission")));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getPrefix() + plugin.getMessagesFile().getString("errors.incorrect_usage")
                            .replace("{COMMAND}", "/freeze <player>")));
            return true;
        }

        Player target = plugin.getServer().getPlayerExact(args[0]);

        if (target.equals(player)) {
            player.sendMessage(ColorUtil.colorize(Firefreeze.getInstance().getPrefix() + plugin.getMessagesFile().getString("errors.cannot_freeze_yourself")));
            return true;
        }

        if (target == null) {
            player.sendMessage(ColorUtil.colorize(Firefreeze.getInstance().getPrefix() + plugin.getMessagesFile().getString("errors.player_not_found")));
            return true;
        }

        if (target.hasPermission("firefreeze.exempt")) {
            player.sendMessage(ColorUtil.colorize(plugin.getPrefix() + plugin.getMessagesFile().getString("errors.cannot_freeze_player")));
            return true;
        }

        FreezeProfile profile = new FreezeProfile(target);

        if (profile.isFrozen()) {
            if (plugin.getConfigFile().getBoolean("command_settings.merge_freeze_with_unfreeeze")) {
                profile.unfreeze(player);
            } else {
                player.sendMessage(ColorUtil.colorize(Firefreeze.getInstance().getPrefix() + plugin.getMessagesFile().getString("errors.player_already_frozen")
                        .replace("{PLAYER}", target.getName())));
            }
            return true;
        }

        if (plugin.getConfigFile().getBoolean("freeze_methods.freeze_chat.enable") && plugin.getFreezeChat().containsKey(player.getUniqueId())) {
            player.sendMessage(ColorUtil.colorize(plugin.getPrefix() + plugin.getMessagesFile().getString("errors.cannot_freeze_more")));
            return true;
        }

        profile.freeze(player);
        return true;
    }
}
