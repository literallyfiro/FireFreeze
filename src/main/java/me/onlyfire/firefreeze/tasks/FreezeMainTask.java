package me.onlyfire.firefreeze.tasks;

import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.methods.FreezeTitle;
import me.onlyfire.firefreeze.objects.FreezeProfile;
import me.onlyfire.firefreeze.utils.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FreezeMainTask extends BukkitRunnable {

    private final Firefreeze plugin;

    public FreezeMainTask(Firefreeze plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (FreezeProfile profile : plugin.getAllPlayers()) {
            if (profile.isFrozen()) {

                Player player = profile.getPlayer();

                if (plugin.getConfigFile().getBoolean("freeze_methods.titles.enable")) {

                    FreezeTitle title = new FreezeTitle();
                    title.send(player,
                            ColorUtil.colorize(plugin.getConfigFile().getString("freeze_methods.titles.freeze_title")),
                            ColorUtil.colorize(plugin.getConfigFile().getString("freeze_methods.titles.freeze_subtitle")),
                            3, 15, 3);

                }
                if (plugin.getConfigFile().getBoolean("freeze_methods.normal_chat.enable")) {

                    for (String s : plugin.getConfigFile().getStringList("freeze_methods.normal_chat.froze_message"))
                        player.sendMessage(ColorUtil.colorizePAPI(player, s).replace("{staff}", profile.getWhoFroze().getName()));

                }
            }
        }
    }
}
