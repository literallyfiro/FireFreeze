package me.onlyfire.firefreeze;

import lombok.Getter;
import me.onlyfire.firefreeze.backend.SQLConnection;
import me.onlyfire.firefreeze.command.FreezeCommand;
import me.onlyfire.firefreeze.command.FreezeHistoryCommand;
import me.onlyfire.firefreeze.command.MainCommand;
import me.onlyfire.firefreeze.command.UnfreezeCommand;
import me.onlyfire.firefreeze.config.FireFreezeConfiguration;
import me.onlyfire.firefreeze.listener.FreezeListener;
import me.onlyfire.firefreeze.objects.FreezeProfile;
import me.onlyfire.firefreeze.objects.FreezeTag;
import me.onlyfire.firefreeze.scoreboard.FreezeBoard;
import me.onlyfire.firefreeze.tasks.AnydeskTask;
import me.onlyfire.firefreeze.tasks.FreezeMainTask;
import me.onlyfire.firefreeze.utils.ColorUtil;
import me.onlyfire.firefreeze.utils.FireFreezeUpdater;
import me.onlyfire.firefreeze.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;

@Getter
public class Firefreeze extends JavaPlugin {

    private static Firefreeze instance;

    private FireFreezeConfiguration configFile;
    private FireFreezeConfiguration messagesFile;
    private FireFreezeConfiguration locationFile;
    private FireFreezeConfiguration scoreboardFile;
    private FireFreezeUpdater updater;
    private SQLConnection connection;

    // FROZEN PLAYER - STAFF
    private Map<UUID, UUID> frozenPlayers = new HashMap<>();
    private Map<UUID, FreezeBoard.ScoreBoardUpdater> scoreboardPlayers = new HashMap<>();
    private Map<UUID, AnydeskTask> anydeskTask = new HashMap<>();
//    private Map<UUID, UUID> freezeChat = new HashMap<>();
    private Map<UUID, FreezeTag> prefixSuffix = new HashMap<>();

    private FreezeMainTask mainTask;
    private String prefix;

    public static Firefreeze getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        setupStorage();
        loadCommandsAndListeners();

        this.prefix = ColorUtil.colorize(getMessagesFile().getString("prefix"));
        this.mainTask = new FreezeMainTask(this);
        this.updater = new FireFreezeUpdater(this, 77105);

        if (getConfigFile().getBoolean("other_settings.enable_autoupdater")) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> updater.update(), 80L, 7200 * 20);
        }

        this.mainTask.runTaskTimerAsynchronously(this, 5L, 60L);

        new Metrics(this, 7133);
    }

    @Override
    public void onDisable() {
        for (FreezeProfile profiles : getAllPlayers()) {
            if (profiles.isFrozen() && profiles.getWhoFroze() != null)
                profiles.forceUnfreeze(profiles.getWhoFroze());
        }
        closeAll();
    }

    private void setupStorage() {
        this.configFile = new FireFreezeConfiguration("config.yml");
        this.messagesFile = new FireFreezeConfiguration("messages.yml");
        this.locationFile = new FireFreezeConfiguration("locations.yml");
        this.scoreboardFile = new FireFreezeConfiguration("scoreboard.yml");

        switch (configFile.getString("storage_type").toLowerCase()) {
            case "sqlite": {
                try {
                    this.connection = new SQLConnection();
                } catch (SQLException | ClassNotFoundException ex) {
                    getServer().getConsoleSender().sendMessage("§c[FireFreeze] Could not connect to the sqlite database!");
                    ex.printStackTrace();
                }
                break;
            }

            case "mysql": {
                try {
                    this.connection = new SQLConnection(
                            configFile.getString("mysql_configuration.host"),
                            configFile.getString("mysql_configuration.port"),
                            configFile.getString("mysql_configuration.user"),
                            configFile.getString("mysql_configuration.password"),
                            configFile.getString("mysql_configuration.database"));
                } catch (ClassNotFoundException | SQLException e) {
                    getServer().getConsoleSender().sendMessage("§c[FireFreeze] Could not connect to the mysql database!");
                    e.printStackTrace();
                }
                break;
            }
            default: {
                getServer().getConsoleSender().sendMessage("§c[FireFreeze] Could not find correct database! Be sure to use sqlite or mysql in the config!");
                onDisable();
                break;
            }
        }
        getServer().getConsoleSender().sendMessage("§a[FireFreeze] Connection estabilished!");
    }

    private void loadCommandsAndListeners() {
        getCommand("freeze").setExecutor(new FreezeCommand(this));
        getCommand("freezehistory").setExecutor(new FreezeHistoryCommand(this));
        getCommand("unfreeze").setExecutor(new UnfreezeCommand(this));
        getCommand("firefreeze").setExecutor(new MainCommand(this));

        getServer().getPluginManager().registerEvents(new FreezeListener(this), this);
    }

    private void closeAll() {
        if (this.mainTask != null) this.mainTask.cancel();

        try {
            if (this.connection != null) this.connection.close();
        } catch (SQLException ex) {
            getServer().getConsoleSender().sendMessage("§c[FireFreeze] Could not stop SQL process!");
            ex.printStackTrace();
        }
    }

    public List<FreezeProfile> getAllPlayers() {
        List<FreezeProfile> list = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            FreezeProfile profile = new FreezeProfile(player);
            list.add(profile);
        }

        return list;
    }

    public boolean newVersionCheck() {
        return (!Bukkit.getVersion().equals("1.7") || !Bukkit.getVersion().equals("1.8"));
    }


}
