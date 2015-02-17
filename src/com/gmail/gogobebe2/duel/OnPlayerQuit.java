package com.gmail.gogobebe2.duel;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by william on 2/17/15.
 */
public class OnPlayerQuit implements Listener {
    private final Duel plugin;

    public OnPlayerQuit(Duel plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!plugin.getPlayersInGame().isEmpty()) {
            for (Player[] players : plugin.getPlayersInGame()) {
                for (Player player : players) {
                    if (player.equals(event.getPlayer())) {
                        Player killer;
                        if (players[0].equals(event.getPlayer())) {
                            killer = players[1];
                        }
                        else {
                            killer = players[0];
                        }
                        DuelUtils.leaveDuel(plugin, players, player, killer);
                    }
                }
            }
        }
    }

}
