package com.gmail.gogobebe2.duel;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class OnPlayerPickup implements Listener{
    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent event) {
        DuelUtils.cancelDropOrPickup(event);
    }
}
