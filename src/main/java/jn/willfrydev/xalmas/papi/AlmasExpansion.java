package jn.willfrydev.xalmas.papi;

import jn.willfrydev.xalmas.XAlmas;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AlmasExpansion extends PlaceholderExpansion {

    private final XAlmas plugin;

    public AlmasExpansion(XAlmas plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() { return "xa"; }

    @Override
    public @NotNull String getAuthor() { return "WillfryDev"; }

    @Override
    public @NotNull String getVersion() { return plugin.getDescription().getVersion(); }

    @Override
    public boolean persist() { return true; }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        // %xa_almas%
        if (params.equalsIgnoreCase("almas")) {
            int almas = plugin.getDataConfig().getInt("jugadores." + player.getUniqueId() + ".almas", 0);
            return String.valueOf(almas);
        }

        return null;
    }
}