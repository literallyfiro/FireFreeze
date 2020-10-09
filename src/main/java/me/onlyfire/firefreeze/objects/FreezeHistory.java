package me.onlyfire.firefreeze.objects;

import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.utils.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FreezeHistory {

    private List<String> staff;
    private List<String> time;
    private String player;

    public FreezeHistory(List<String> staff, List<String> time, String player) {
        this.staff = staff;
        this.time = time;
        this.player = player;
    }

    public void sendTo(Player sender) {
        if (!(staff.isEmpty() || time.isEmpty())) {
            int i = 0;

            sender.sendMessage(ColorUtil.colorizePAPI(sender, Firefreeze.getInstance().getMessagesFile().getString("freeze_history.has_history")
                    .replace("{PLAYER}", player)
                    .replace("{ENTRIES}", String.valueOf(staff.size()))));

            for (String header : Firefreeze.getInstance().getMessagesFile().getStringList("freeze_history.header")) {
                sender.sendMessage(ColorUtil.colorizePAPI(sender, header));
            }

            while (i < staff.size() && i < time.size()) {
                sender.sendMessage(ColorUtil.colorizePAPI(sender, Firefreeze.getInstance().getMessagesFile().getString("freeze_history.history")
                        .replace("{STAFF}", String.valueOf(staff.get(i)))
                        .replace("{TIME}", String.valueOf(time.get(i)))));
                i++;
            }

            for (String footer : Firefreeze.getInstance().getMessagesFile().getStringList("freeze_history.footer")) {
                sender.sendMessage(ColorUtil.colorizePAPI(sender, footer));
            }

        } else {
            sender.sendMessage(ColorUtil.colorizePAPI(sender, Firefreeze.getInstance().getMessagesFile().getString("freeze_history.no_history")
                    .replace("{PLAYER}", player)));
        }
    }
}
