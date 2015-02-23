package com.gmail.gogobebe2.duel;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by william on 2/23/15.
 */
public class OnPlayerJoin implements Listener{
    private Duel duel;

    public OnPlayerJoin(Duel duel) {
        this.duel = duel;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        duel.getConfig().set("Players." + event.getPlayer().getUniqueId(), event.getPlayer().getUniqueId());
        duel.getConfig().set("Players." + event.getPlayer().getUniqueId() + ".name" + event.getPlayer().getName(), event.getPlayer().getName());

        if (!duel.getConfig().contains("Players." + event.getPlayer().getUniqueId() + ".wins")) {
            duel.getConfig().set("Players." + event.getPlayer().getUniqueId() + ".wins", 0);
        }

        if (!duel.getConfig().contains("Players." + event.getPlayer().getUniqueId() + ".losses")) {
            duel.getConfig().set("Players." + event.getPlayer().getUniqueId() + ".losses", 0);
        }

    }

}
