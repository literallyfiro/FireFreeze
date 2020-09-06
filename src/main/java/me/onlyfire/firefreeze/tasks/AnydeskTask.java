package me.onlyfire.firefreeze.tasks;

import lombok.Setter;
import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.objects.FreezeProfile;
import me.onlyfire.firefreeze.utils.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class AnydeskTask extends BukkitRunnable {

    private boolean accepted = false;
    private Firefreeze plugin = Firefreeze.getInstance();
    private Player frozen;
    @Setter
    private String message;
    private Instant instThen;

    public AnydeskTask(Player player) {
        this.frozen = player;
        this.message = "";
        this.instThen = Instant.now();
        this.runTaskTimerAsynchronously(plugin, 0L, 3L);
    }

    @Override
    public void run() {
        if (frozen != null) {
            FreezeProfile profile = new FreezeProfile(frozen);

            Instant now = Instant.now();
            Duration duration = Duration.between(instThen, now);
            long seconds = duration.getSeconds();
            long limit = TimeUnit.MINUTES.toSeconds(plugin.getConfigFile().getInt("freeze_methods.anydesk_task.minutes_to_give"));

            if (accepted) {
                for (Player pl : plugin.getServer().getOnlinePlayers()) {
                    if (pl.hasPermission("firefreeze.staff")) {
                        pl.sendMessage(ColorUtil.colorize(plugin.getPrefix() + plugin.getMessagesFile().getString("anydesk_task.accepted")
                                .replace("{PLAYER}", frozen.getName())
                                .replace("{ID}", message)));
                    }
                }
                cancel();
            }

            if (seconds > limit) {
                if (!accepted) {
                    for (Player pl : plugin.getServer().getOnlinePlayers()) {
                        if (pl.hasPermission("firefreeze.staff")) {
                            pl.sendMessage(ColorUtil.colorize(plugin.getPrefix() + plugin.getMessagesFile().getString("anydesk_task.refused")
                                    .replace("{PLAYER}", frozen.getName())));
                        }
                    }
                }
                cancel();
            }

            if (profile.isFrozen()) {

                char[] chars = this.message.toCharArray();
                StringBuilder builder = new StringBuilder();

                for (char c : chars) {
                    if (Character.isDigit(c)) {
                        builder.append(c);
                    }
                }

                String id = builder.toString();
                boolean hasId = id.toCharArray().length > 8;

                if (message.contains("@ad") || hasId) {

                    if (!accepted) {
                        for (Player pl : plugin.getServer().getOnlinePlayers()) {
                            if (pl.hasPermission("firefreeze.staff")) {
                                pl.sendMessage(ColorUtil.colorize(plugin.getPrefix() + plugin.getMessagesFile().getString("anydesk_task.accepted")
                                        .replace("{PLAYER}", frozen.getName())
                                        .replace("{ID}", message)));
                            }
                        }

                    }
                    accepted = true;
                    cancel();
                }
            } else {
                cancel();
            }


        } else {
            cancel();
        }
    }

}
