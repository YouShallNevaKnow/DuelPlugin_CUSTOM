package com.gmail.gogobebe2.duel;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class OnPlayerDrop implements Listener{
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        DuelUtils.cancelDropOrPickup(event);
    }
}
