package jn.willfrydev.xalmas.api;

import jn.willfrydev.xalmas.XAlmas;
import java.util.UUID;

public class XAlmasAPI {

    private static XAlmas plugin;

    // Esto lo llamaremos en el onEnable() de la clase principal
    public static void init(XAlmas instance) {
        plugin = instance;
    }

    /**
     * Obtiene la cantidad de almas de un jugador.
     */
    public static int getSouls(UUID playerUUID) {
        if (plugin == null) return 0;
        return plugin.getDataConfig().getInt("jugadores." + playerUUID + ".almas", 0);
    }

    /**
     * Añade almas a un jugador.
     */
    public static void addSouls(UUID playerUUID, int amount) {
        if (plugin == null) return;
        int current = getSouls(playerUUID);
        plugin.getDataConfig().set("jugadores." + playerUUID + ".almas", current + amount);
        plugin.saveData();
    }

    /**
     * Quita almas a un jugador (evitando que baje de 0).
     */
    public static void removeSouls(UUID playerUUID, int amount) {
        if (plugin == null) return;
        int current = getSouls(playerUUID);
        int finalAmount = Math.max(0, current - amount);
        plugin.getDataConfig().set("jugadores." + playerUUID + ".almas", finalAmount);
        plugin.saveData();
    }
}