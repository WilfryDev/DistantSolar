package jn.willfrydev.xalmas.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import jn.willfrydev.xalmas.XAlmas;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemManager {

    private final XAlmas plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ItemManager(XAlmas plugin) {
        this.plugin = plugin;
    }

    // Convierte &#HEX y &a a formato compatible con MiniMessage y Bukkit 1.16.5
    public String colorize(String text) {
        if (text == null) return "";

        // Convertir &#HEX a <#HEX>
        text = text.replaceAll("&#([a-fA-F0-9]{6})", "<#$1>");

        // Convertir códigos legacy básicos a MiniMessage por si los usuarios los usan
        text = text.replace("&a", "<green>").replace("&b", "<aqua>").replace("&c", "<red>")
                .replace("&d", "<light_purple>").replace("&e", "<yellow>").replace("&f", "<white>")
                .replace("&0", "<black>").replace("&1", "<dark_blue>").replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>").replace("&4", "<dark_red>").replace("&5", "<dark_purple>")
                .replace("&6", "<gold>").replace("&7", "<gray>").replace("&8", "<dark_gray>").replace("&9", "<blue>")
                .replace("&l", "<bold>").replace("&o", "<italic>").replace("&n", "<underlined>")
                .replace("&m", "<strikethrough>").replace("&k", "<obfuscated>").replace("&r", "<reset>");

        Component component = mm.deserialize(text);
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public ItemStack createAlmaItem(String key, int amount) {
        ConfigurationSection section = plugin.getAlmasConfig().getConfigurationSection("items." + key);
        if (section == null) return null;

        String matStr = section.getString("material", "RED_DYE");
        Material material = Material.matchMaterial(matStr.toUpperCase());
        if (material == null) material = Material.RED_DYE;

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        // Nombre
        if (section.contains("name")) {
            meta.setDisplayName(colorize(section.getString("name")));
        }

        // Lore
        if (section.contains("lore")) {
            List<String> lore = new ArrayList<>();
            for (String line : section.getStringList("lore")) {
                lore.add(colorize(line));
            }
            meta.setLore(lore);
        }

        // Encantamientos
        if (section.contains("enchantments")) {
            for (String enchStr : section.getStringList("enchantments")) {
                String[] split = enchStr.split(":");
                Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(split[0].toLowerCase()));
                if (enchant != null) {
                    meta.addEnchant(enchant, Integer.parseInt(split[1]), true);
                }
            }
        }

        if (section.contains("flags")) {
            for (String flagStr : section.getStringList("flags")) {
                try {
                    meta.addItemFlags(ItemFlag.valueOf(flagStr.toUpperCase()));
                } catch (IllegalArgumentException ignored) {}
            }
        }

        if (material == Material.PLAYER_HEAD && section.contains("base64")) {

            SkullMeta skullMeta = (SkullMeta) meta;
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", section.getString("base64")));
            skullMeta.setPlayerProfile(profile);
        }

        item.setItemMeta(meta);
        return item;
    }
}