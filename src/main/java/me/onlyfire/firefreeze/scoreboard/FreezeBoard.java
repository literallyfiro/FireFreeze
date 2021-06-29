package me.onlyfire.firefreeze.scoreboard;

import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.objects.FreezeProfile;
import me.onlyfire.firefreeze.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class FreezeBoard {

    private Firefreeze plugin;
    private static FreezeBoard instance;

    public FreezeBoard(Firefreeze plugin) {
        this.plugin = plugin;
    }

    public void createScoreboard(Player player, Player staff) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        if (plugin.getScoreboardFile().getBoolean("enable_freeze_scoreboard")) {
            player.setScoreboard(scoreboard);
            plugin.getScoreboardPlayers().put(player.getUniqueId(), new ScoreBoardUpdater(player, staff));
        }

    }

    public void deleteScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        plugin.getScoreboardPlayers().remove(player.getUniqueId());
    }

    public static FreezeBoard getInstance() {
        if (instance == null) {
            instance = new FreezeBoard(Firefreeze.getInstance());
        }
        return instance;
    }

    public static class ScoreBoardUpdater extends BukkitRunnable {
        private Player player;
        private Player staff;
        private Firefreeze plugin = Firefreeze.getInstance();

        public ScoreBoardUpdater(Player player, Player staff) {
            this.player = player;
            this.staff = staff;
            runTaskTimerAsynchronously(plugin, 2L, 20L);
        }

        @Override
        public void run() {
            if (player == null) {
                cancel();
            }

            FreezeProfile profile = new FreezeProfile(player);
            if (profile.isFrozen()) {
                Scoreboard scoreboard = player.getScoreboard();
                if (scoreboard == null) {
                    scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                }

                if (plugin.getScoreboardFile().getBoolean("enable_freeze_scoreboard")) {
                    Objective objective = scoreboard.getObjective("firefreezescore");

                    if (objective == null) {
                        objective = scoreboard.registerNewObjective("firefreezescore", "dummy");
                        objective.setDisplayName(ColorUtil.colorize(plugin.getScoreboardFile().getString("title")));
                        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    }

                    int size = this.plugin.getScoreboardFile().getStringList("lines").size();
                    StringBuilder noLine = new StringBuilder("§r");

                    for (String s : this.plugin.getScoreboardFile().getStringList("lines")) {
                        if (s.isEmpty() || s.equals("")) {
                            noLine.append("§r");
                            objective.getScore(ColorUtil.colorizePAPI(player, noLine.toString())
                                    .replace("{staff}", staff.getName()))
                                    .setScore(size);
                        } else {
                            objective.getScore(ColorUtil.colorizePAPI(player, s)
                                    .replace("{staff}", staff.getName()))
                                    .setScore(size);
                        }
                        size--;
                    }
                }

                player.setScoreboard(scoreboard);
            } else {
                cancel();
            }
        }
    }

}


