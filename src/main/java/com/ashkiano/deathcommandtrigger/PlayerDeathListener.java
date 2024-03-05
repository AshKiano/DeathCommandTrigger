package com.ashkiano.deathcommandtrigger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerDeathListener implements Listener {
    private DeathCommandTrigger plugin;
    private HashMap<UUID, List<Long>> playerDeaths = new HashMap<>();

    public PlayerDeathListener(DeathCommandTrigger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Add the current death timestamp to the player's list of deaths
        List<Long> deaths = playerDeaths.getOrDefault(playerId, new ArrayList<>());
        deaths.add(currentTime);
        playerDeaths.put(playerId, deaths);

        // Check if the player has died the required number of times within the time frame
        int deathsRequired = plugin.getPluginConfig().getInt("deaths-required");
        long timeFrameMillis = plugin.getPluginConfig().getLong("time-frame") * 60000; // Convert minutes to milliseconds

        // Remove deaths outside the time frame
        deaths.removeIf(deathTime -> (currentTime - deathTime) > timeFrameMillis);

        // Check the number of deaths within the time frame
        if (deaths.size() >= deathsRequired) {
            String command = plugin.getPluginConfig().getString("command").replace("{player}", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            plugin.getLogger().info("Executing command for " + player.getName() + ": " + command);

            // Reset the player's deaths to prevent the command from being executed repeatedly
            deaths.clear();
        }

        // Optionally, log for debugging purposes
        plugin.getLogger().info(player.getName() + " has died. Total recent deaths: " + deaths.size());
    }
}
