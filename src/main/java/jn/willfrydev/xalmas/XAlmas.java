package jn.willfrydev.xalmas;

import jn.willfrydev.xalmas.api.XAlmasAPI;
import jn.willfrydev.xalmas.commands.XACommand;
import jn.willfrydev.xalmas.commands.XATabCompleter;
import jn.willfrydev.xalmas.papi.AlmasExpansion;
import jn.willfrydev.xalmas.utils.ItemManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class XAlmas extends JavaPlugin {

    private File dataFile;
    private FileConfiguration dataConfig;

    private File almasFile;
    private FileConfiguration almasConfig;

    private ItemManager itemManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        loadAlmasConfig();
        createDataFile();

        this.itemManager = new ItemManager(this);

        XAlmasAPI.init(this);

        printLogo(true);

        getCommand("xa").setExecutor(new XACommand(this));
        getCommand("xa").setTabCompleter(new XATabCompleter(this));
        getServer().getPluginManager().registerEvents(new KillListener(this), this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AlmasExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {

        saveData();
        printLogo(false);

    }

    private void printLogo(boolean isEnable) {
        CommandSender console = getServer().getConsoleSender();
        String status = isEnable ? "&a✔ HABILITADO" : "&c✖ DESHABILITADO";
        String version = getDescription().getVersion();

        String[] logo = {
                "",
                "&#FACBCB        _   _                     ",
                "&#FACBCB__  __ /_\\ | |_ __ ___   __ _ ___ ",
                "&#FACBCB\\ \\/ ///_\\\\| | '_ ` _ \\ / _` / __|",
                "&#FACBCB >  </  _  \\ | | | | | | (_| \\__ \\",
                "&#FACBCB/_/\\_\\_/ \\_/_|_| |_| |_|\\__,_|___/",
                "",
                "&8» &7Version: &#FACBCB" + version + " &8| &7Autor: &#FACBCBxPlugins",
                "&8» &7Status: " + status,
                ""
        };

        for (String line : logo) {
            console.sendMessage(itemManager.colorize(line));
        }
    }

    public void loadAlmasConfig() {

        almasFile = new File(getDataFolder(), "almas.yml");
        if (!almasFile.exists()) {
            saveResource("almas.yml", false);
        }
        almasConfig = YamlConfiguration.loadConfiguration(almasFile);
    }

    private void createDataFile() {
        File dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) dataFolder.mkdirs();

        dataFile = new File(dataFolder, "data.dsb");
        if (!dataFile.exists()) {
            try { dataFile.createNewFile(); }
            catch (IOException e) { getLogger().severe("No se pudo crear data.dsb!"); }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void sendMessage(CommandSender sender, String path) {

        String message = getConfig().getString(path, "");
        if (message.isEmpty()) return;

        String prefix = "";
        if (getConfig().isString("messages.prefix")) {
            String p = getConfig().getString("messages.prefix");
            if (p != null && !p.equalsIgnoreCase("false")) {
                prefix = p;
            }
        }

        String finalMsg = itemManager.colorize(prefix + message);
        sender.sendMessage(finalMsg);
    }

    public FileConfiguration getDataConfig() { return dataConfig; }
    public FileConfiguration getAlmasConfig() { return almasConfig; }
    public ItemManager getItemManager() { return itemManager; }

    public void saveData() {
        try { dataConfig.save(dataFile); }
        catch (IOException e) { getLogger().severe("No se pudo guardar data.dsb!"); }
    }
}