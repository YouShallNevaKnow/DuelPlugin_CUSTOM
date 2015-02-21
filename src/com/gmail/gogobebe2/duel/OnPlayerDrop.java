package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Created by william on 2/21/15.
 */
public class OnPlayerDrop implements Listener{
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if (!Duel.getPlayersInGame().isEmpty()) {
            for (Player[] players : Duel.getPlayersInGame()) {
                if (players[0].equals(event.getPlayer()) || players[1].equals(event.getPlayer())) {
                    event.getPlayer().sendMessage(ChatColor.BLUE + "You cannot drop items whilst in a duel!");
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
