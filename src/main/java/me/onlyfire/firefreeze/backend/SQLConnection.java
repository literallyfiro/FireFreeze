package me.onlyfire.firefreeze.backend;

import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.enums.EntryType;
import me.onlyfire.firefreeze.objects.FreezeHistory;
import me.onlyfire.firefreeze.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SQLConnection {

    private Connection connection;

    public SQLConnection(String host, String port, String user, String psw, String database)
            throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=yes", user, psw);
        Bukkit.getConsoleSender().sendMessage("§a[FireFreeze] MySQL Connection selected!");
        this.createTable();
    }

    public SQLConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        File file = new File(Firefreeze.getInstance().getDataFolder() + "/database/database.db");

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        Bukkit.getConsoleSender().sendMessage("§a[FireFreeze] SQLite Connection selected!");
        new SQLiteDownloader().download();
        this.createTable();
    }

    private void createTable() {
        Tasks.runAsync(() -> {
            PreparedStatement st = null;

            try {
                st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS entries (" +
                        "uuid VARCHAR(50) NOT NULL, " +
                        "player VARCHAR(25) NOT NULL, " +
                        "staff VARCHAR(25) NOT NULL, " +
                        "type VARCHAR(10) NOT NULL, " +
                        "date DATE NOT NULL," +
                        "timestamp INTEGER NOT NULL DEFAULT 0)");

                st.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeStatement(st);
            }

            PreparedStatement st1 = null;

            try {
                st1 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS frozenPlayers " +
                        "(" +
                        "frozen VARCHAR(50) NOT NULL, " +
                        "staff VARCHAR(25) NOT NULL" +
                        ")");

                st1.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeStatement(st1);
            }
        });
    }

    public void addEntry(Player player, String staff, EntryType type) {
        Tasks.runAsync(() -> {
            PreparedStatement st = null;

            try {
                st = connection.prepareStatement("INSERT INTO entries (uuid, player, staff, type, date, timestamp) VALUES (?,?,?,?,?,?)");

                st.setString(1, player.getUniqueId().toString());
                st.setString(2, player.getName());
                st.setString(3, staff);
                st.setString(4, type.toName());
                st.setString(5, new SimpleDateFormat(Firefreeze.getInstance().getConfigFile().getString("other_settings.time_set")).format(new Date()));
                st.setLong(6, System.currentTimeMillis());

                st.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeStatement(st);
            }
        });
    }

    public Player getWhoFroze(Player player) {
        AtomicReference<Player> whoFroze = new AtomicReference<>();

        Tasks.runAsync(() -> {

            PreparedStatement st = null;
            ResultSet result = null;

            try {
                st = connection.prepareStatement("SELECT staff FROM frozenPlayers WHERE frozen = ?");

                st.setString(1, player.getUniqueId().toString());

                result = st.executeQuery();

                if (result.next()) {
                    whoFroze.set(Bukkit.getPlayer(UUID.fromString(result.getString(1))));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeStatement(st);
                if (result != null) {
                    try {
                        result.close();
                    } catch (SQLException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }

        });

        return whoFroze.get();
    }

    public Player getFroze(Player player) {
        AtomicReference<Player> whoFroze = new AtomicReference<>();

        Tasks.runAsync(() -> {
            PreparedStatement st = null;
            ResultSet result = null;

            try {
                st = connection.prepareStatement("SELECT frozen FROM frozenPlayers WHERE staff = ?");

                st.setString(1, player.getUniqueId().toString());

                result = st.executeQuery();

                if (result.next()) {
                    whoFroze.set(Bukkit.getPlayer(UUID.fromString(result.getString(1))));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeStatement(st);
                if (result != null) {
                    try {
                        result.close();
                    } catch (SQLException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
        });

        return whoFroze.get();
    }

    public void addFreeze(Player player, Player staff) {
        Tasks.runAsync(() -> {
            PreparedStatement st = null;

            if (!isFrozen(player)) {
                try {
                    st = connection.prepareStatement("INSERT INTO frozenPlayers (frozen, staff) VALUES (?,?)");

                    st.setString(1, player.getUniqueId().toString());
                    st.setString(2, staff.getUniqueId().toString());

                    st.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    closeStatement(st);
                }
            }
        });
    }

    private void closeStatement(PreparedStatement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex2) {
                ex2.printStackTrace();
            }
        }
    }

    public void removeFreeze(Player player) {
        Tasks.runAsync(() -> {
            PreparedStatement st = null;

            try {
                st = connection.prepareStatement("DELETE FROM frozenPlayers WHERE frozen = ?");

                st.setString(1, player.getUniqueId().toString());

                st.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeStatement(st);
            }
        });

    }

    public void removeFreezeForced(Player player) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement("DELETE FROM frozenPlayers WHERE frozen = ?");

            st.setString(1, player.getUniqueId().toString());

            st.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeStatement(st);
        }

    }

    public boolean isFrozen(Player player) {
        AtomicBoolean bool = new AtomicBoolean(false);

        Tasks.runAsync(() -> {
            PreparedStatement st = null;
            ResultSet result = null;

            try {
                st = connection.prepareStatement("SELECT frozen FROM frozenPlayers WHERE frozen = ?");

                st.setString(1, player.getUniqueId().toString());

                result = st.executeQuery();

                bool.set(result.next());
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeStatement(st);
                if (result != null) {
                    try {
                        result.close();
                    } catch (SQLException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
        });

        return bool.get();
    }

    public FreezeHistory searchFreezeHistoryFor(String player) {
        AtomicReference<FreezeHistory> history = new AtomicReference<>();

        Tasks.runAsync(() -> {
            PreparedStatement st = null;
            ResultSet result = null;

            try {
                st = connection.prepareStatement("SELECT * FROM entries WHERE player = ? AND type = ?");

                st.setString(1, player);
                st.setString(2, EntryType.FREEZE.toName());

                List<String> staffList = new ArrayList<>();
                List<String> timeList = new ArrayList<>();

                result = st.executeQuery();

                while (result.next()) {
                    staffList.add(result.getString("staff"));
                    timeList.add(result.getString("date"));
                }

                history.set(new FreezeHistory(staffList, timeList, player));
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeStatement(st);
                if (result != null) {
                    try {
                        result.close();
                    } catch (SQLException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
        });


        return history.get();
    }


    public void restoreEntries() {
        Tasks.runAsync(() -> {
            Statement st = null;

            try {
                st = this.connection.createStatement();
                st.executeUpdate("DELETE FROM entries");

            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                if (st != null) {
                    try {
                        st.close();
                    } catch (SQLException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
        });
    }

    public void removeEntriesFor(final String player) {
        Tasks.runAsync(() -> {
            PreparedStatement st = null;
            try {
                st = this.connection.prepareStatement("DELETE FROM entries WHERE player = ?");
                st.setString(1, player);
                st.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeStatement(st);
            }
        });
    }

    public void close() throws SQLException {
        this.connection.close();
    }
}
