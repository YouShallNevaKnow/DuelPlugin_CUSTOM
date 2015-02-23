package com.gmail.gogobebe2.duel;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {
    private Duel duel;

    public OnPlayerQuit(Duel duel) {
        this.duel = duel;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!Duel.getPlayersInGame().isEmpty()) {
            for (Player[] players : Duel.getPlayersInGame()) {
                for (Player player : players) {
                    if (player.equals(event.getPlayer())) {
                        Player killer;
                        if (players[0].equals(event.getPlayer())) {
                            killer = players[1];
                        }
                        else {
                            killer = players[0];
                        }
                        DuelUtils.leaveDuel(players, player, killer, duel.getConfig());
                    }
                }
            }
        }
    }

}
