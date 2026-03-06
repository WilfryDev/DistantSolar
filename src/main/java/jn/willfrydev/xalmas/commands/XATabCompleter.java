package jn.willfrydev.xalmas.commands;

import jn.willfrydev.xalmas.XAlmas;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class XATabCompleter implements TabCompleter {

    private final XAlmas plugin;

    public XATabCompleter(XAlmas plugin) {

        this.plugin = plugin;
    }

    @Override

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {

            completions.add("give");
            completions.add("reload");
            completions.add("help");

        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {

            if (plugin.getAlmasConfig().contains("items")) {

                completions.addAll(plugin.getAlmasConfig().getConfigurationSection("items").getKeys(false));
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            completions.add("1");
            completions.add("10");
            completions.add("64");
        }
        return completions;
    }
}