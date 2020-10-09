package me.onlyfire.firefreeze.tags;

import com.nametagedit.plugin.NametagEdit;
import me.onlyfire.firefreeze.Firefreeze;
import me.onlyfire.firefreeze.objects.FreezeTag;
import me.onlyfire.firefreeze.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FreezeTags {

    private String provider;

    public FreezeTags(String provider) {
        this.provider = provider;
    }

    public void setPrefix(Player player) {
        String prefix = ColorUtil.colorize(Firefreeze.getInstance().getConfigFile().getString("freeze_methods.tab_prefix_suffix.prefix"));
        String suffix = ColorUtil.colorize(Firefreeze.getInstance().getConfigFile().getString("freeze_methods.tab_prefix_suffix.suffix"));

        switch (provider.toLowerCase()) {
            case "nametagedit":
                if (Bukkit.getPluginManager().isPluginEnabled("NametagEdit")) {
                    String firstPrefix = NametagEdit.getApi().getNametag(player).getPrefix();
                    String firstSuffix = NametagEdit.getApi().getNametag(player).getSuffix();
                    Firefreeze.getInstance().getPrefixSuffix().put(player.getUniqueId(), new FreezeTag(prefix, suffix, firstPrefix, firstSuffix));

                    NametagEdit.getApi().setPrefix(player, prefix);
                    NametagEdit.getApi().setSuffix(player, suffix);
                }
                break;
            default:
                Firefreeze.getInstance().getLogger().warning("Freeze prefix/suffix Provider is null or not found. Please change it on the config");
                break;
        }
    }

    public void removePrefix(Player player) {
        switch (provider.toLowerCase()) {
            case "nametagedit":
                if (Bukkit.getPluginManager().isPluginEnabled("NametagEdit")) {
                    NametagEdit.getApi().setPrefix(player, Firefreeze.getInstance().getPrefixSuffix().get(player.getUniqueId()).getFirstPrefix());
                    NametagEdit.getApi().setSuffix(player, Firefreeze.getInstance().getPrefixSuffix().get(player.getUniqueId()).getFirstSuffix());
                    Firefreeze.getInstance().getPrefixSuffix().remove(player.getUniqueId());
                }
                break;
            default:
                Firefreeze.getInstance().getLogger().warning("Freeze prefix/suffix Provider is null or not found. Please change it on the config");
                break;
        }
    }
}
