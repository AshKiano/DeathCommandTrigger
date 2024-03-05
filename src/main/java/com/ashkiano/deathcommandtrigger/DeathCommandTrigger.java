package com.ashkiano.deathcommandtrigger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathCommandTrigger extends JavaPlugin {
    private FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        // Save default config if not exists
        saveDefaultConfig();

        Metrics metrics = new Metrics(this, 20946);

        // Register event listener
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);

        // Print a message to the console to indicate the plugin has been enabled
        getLogger().info("DeathCommandTrigger has been enabled.");
        getLogger().info("Thank you for using the DeathCommandTrigger plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");
    }

    @Override
    public void onDisable() {
        // Print a message to the console to indicate the plugin has been disabled
        getLogger().info("DeathCommandTrigger has been disabled.");
    }

    public FileConfiguration getPluginConfig() {
        return config;
    }
}
