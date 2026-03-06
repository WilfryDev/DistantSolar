package jn.willfrydev.xalmas.commands;

import jn.willfrydev.xalmas.XAlmas;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class XACommand implements CommandExecutor {
    private final XAlmas plugin;

    public XACommand(XAlmas plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("xalmas.admin")) {
            plugin.sendMessage(sender, "messages.no-permission");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.loadAlmasConfig();
            plugin.sendMessage(sender, "messages.reload-success");
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {

            if (args.length < 3) {
                plugin.sendMessage(sender, "messages.invalid-amount");
                return true;
            }

            String moneda = args[1];
            int cantidad;
            try {
                cantidad = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                plugin.sendMessage(sender, "messages.invalid-amount");
                return true;
            }

            ItemStack item = plugin.getItemManager().createAlmaItem(moneda, cantidad);
            if (item == null) {
                plugin.sendMessage(sender, "messages.invalid-currency");
                return true;
            }

            if (sender instanceof Player) {

                Player player = (Player) sender;
                player.getInventory().addItem(item);

                String msg = plugin.getConfig().getString("messages.item-given", "&#55FF55✔ Has dado &#FACBCB<cantidad> <moneda>&#55FF55 a <jugador>.");
                msg = msg.replace("<cantidad>", String.valueOf(cantidad))
                        .replace("<moneda>", moneda)
                        .replace("<jugador>", player.getName());

                String prefix = "";
                if (plugin.getConfig().isString("messages.prefix")) {
                    String p = plugin.getConfig().getString("messages.prefix");
                    if (p != null && !p.equalsIgnoreCase("false")) {
                        prefix = p;
                    }
                }

                player.sendMessage(plugin.getItemManager().colorize(prefix + msg));
            }
            return true;
        }

        sendHelpMessage(sender);
        return true;
    }

    private void sendHelpMessage(CommandSender sender) {

        List<String> helpMsg = plugin.getConfig().getStringList("messages.help-message");

        if (helpMsg != null && !helpMsg.isEmpty()) {

            for (String line : helpMsg) {

                sender.sendMessage(plugin.getItemManager().colorize(line));
            }
        } else {

            sender.sendMessage("§cError: No se encontró 'help-message' en config.yml");
        }
    }
}