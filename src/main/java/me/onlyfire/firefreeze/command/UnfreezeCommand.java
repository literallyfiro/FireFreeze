package me.onlyfire.firefreeze.command;

import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.objects.FreezeProfile;
import me.onlyfire.firefreeze.utils.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnfreezeCommand implements CommandExecutor {

    private final Firefreeze plugin;

    public UnfreezeCommand(Firefreeze plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtil.colorize(plugin.getPrefix() + plugin.getMessagesFile().getString("errors.only_players")));
            return true;
        }

        if (!sender.hasPermission("firefreeze.unfreeze")) {
            sender.sendMessage(ColorUtil.colorize(plugin.getPrefix() + plugin.getMessagesFile().getString("errors.not_permission")));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ColorUtil.colorize(plugin.getPrefix() + plugin.getMessagesFile().getString("errors.incorrect_usage")
                    .replace("{COMMAND}", "/unfreeze <player>")));
            return true;
        }

        Player target = plugin.getServer().getPlayerExact(args[0]);

        if (target == null) {
            sender.sendMessage(ColorUtil.colorize(Firefreeze.getInstance().getPrefix() + plugin.getMessagesFile().getString("errors.player_not_found")));
            return true;
        }

        if (target.equals(sender)) {
            sender.sendMessage(ColorUtil.colorize(Firefreeze.getInstance().getPrefix() + plugin.getMessagesFile().getString("errors.cannot_freeze_yourself")));
            return true;
        }

        if (target.hasPermission("firefreeze.exempt")) {
            sender.sendMessage(ColorUtil.colorize(plugin.getPrefix() + plugin.getMessagesFile().getString("errors.cannot_freeze_player")));
            return true;
        }

        Player player = (Player) sender;
        FreezeProfile profile = new FreezeProfile(target);

        if (!profile.isFrozen()) {
            player.sendMessage(ColorUtil.colorizePAPI(player, Firefreeze.getInstance().getPrefix() + plugin.getMessagesFile().getString("errors.player_already_unfrozen")
                    .replace("{PLAYER}", target.getName())));
            return true;
        }

        profile.unfreeze(player);
        return true;
    }
}
