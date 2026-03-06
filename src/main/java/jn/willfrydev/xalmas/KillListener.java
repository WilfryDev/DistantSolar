package jn.willfrydev.xalmas;

import jn.willfrydev.xalmas.api.events.PlayerReceiveSoulEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class KillListener implements Listener {

    private final XAlmas plugin;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final Random random = new Random();

    public KillListener(XAlmas plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null) return;

        if (plugin.getConfig().getStringList("options.disabled-worlds").contains(killer.getWorld().getName())) {
            return;
        }

        if (plugin.getConfig().getBoolean("options.anti-farming.block-same-ip")) {
            if (victim.getAddress() != null && killer.getAddress() != null &&
                    victim.getAddress().getAddress().equals(killer.getAddress().getAddress())) {
                plugin.sendMessage(killer, "messages.anti-farming-ip");
                return;
            }
        }


        long cooldownTime = plugin.getConfig().getInt("options.anti-farming.player-cooldown") * 1000L;

        if (cooldowns.containsKey(victim.getUniqueId())) {

            long timeLeft = (cooldowns.get(victim.getUniqueId()) + cooldownTime) - System.currentTimeMillis();
            if (timeLeft > 0) {

                String msg = plugin.getConfig().getString("messages.anti-farming-cooldown", "")
                        .replace("<tiempo>", String.valueOf(timeLeft / 1000));

                String prefix = plugin.getConfig().isString("messages.prefix") ? plugin.getConfig().getString("messages.prefix") : "";
                if (prefix.equalsIgnoreCase("false")) prefix = "";

                killer.sendMessage(plugin.getItemManager().colorize(prefix + msg));
                return;
            }
        }

        cooldowns.put(victim.getUniqueId(), System.currentTimeMillis());


        String itemKey = plugin.getConfig().getString("options.item-to-drop", "muertes");

        if (itemKey.equalsIgnoreCase("aleatorio")) {

            if (plugin.getAlmasConfig().contains("items")) {

                List<String> keys = new ArrayList<>(plugin.getAlmasConfig().getConfigurationSection("items").getKeys(false));
                itemKey = keys.isEmpty() ? "muertes" : keys.get(random.nextInt(keys.size()));
            }
        }

        PlayerReceiveSoulEvent apiEvent = new PlayerReceiveSoulEvent(killer, victim, itemKey);
        plugin.getServer().getPluginManager().callEvent(apiEvent);

        if (apiEvent.isCancelled()) return;

        itemKey = apiEvent.getSoulType();

        String path = "jugadores." + killer.getUniqueId() + ".almas";
        int almasActuales = plugin.getDataConfig().getInt(path, 0);
        plugin.getDataConfig().set(path, almasActuales + 1);
        plugin.saveData();

        ItemStack almaItem = plugin.getItemManager().createAlmaItem(itemKey, 1);
        if (almaItem != null) {
            String dropMode = plugin.getConfig().getString("options.drop-mode", "INVENTORY").toUpperCase();

            if (dropMode.equals("DROP")) {
                victim.getWorld().dropItemNaturally(victim.getLocation(), almaItem);
            } else {
                killer.getInventory().addItem(almaItem);
            }
        }

        if (plugin.getConfig().getBoolean("sounds.receive-soul.enabled")) {
            try {
                Sound sound = Sound.valueOf(plugin.getConfig().getString("sounds.receive-soul.sound"));
                float volume = (float) plugin.getConfig().getDouble("sounds.receive-soul.volume");
                float pitch = (float) plugin.getConfig().getDouble("sounds.receive-soul.pitch");
                killer.playSound(killer.getLocation(), sound, volume, pitch);
            } catch (Exception ignored) {}
        }

        String msg = plugin.getConfig().getString("messages.soul-received", "")
                .replace("<victima>", victim.getName());

        String prefix = plugin.getConfig().isString("messages.prefix") ? plugin.getConfig().getString("messages.prefix") : "";
        if (prefix.equalsIgnoreCase("false")) prefix = "";

        killer.sendMessage(plugin.getItemManager().colorize(prefix + msg));
    }
}