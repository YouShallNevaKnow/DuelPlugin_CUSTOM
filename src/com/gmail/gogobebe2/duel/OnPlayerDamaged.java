package com.gmail.gogobebe2.duel;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by william on 2/15/15.
 */
public class OnPlayerDamaged implements Listener {
    private final Duel plugin;

    public OnPlayerDamaged(Duel plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerDamageReceive(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (!plugin.getPlayersInGame().isEmpty()) {
                for (Player[] players : plugin.getPlayersInGame()) {
                    if (players[0].equals(player) || players[1].equals(player)) {
                        Player killer;
                        if (players[0].equals(player)) {
                            killer = players[1];
                        } else {
                            killer = players[0];
                        }
                        if (e.getFinalDamage() >= player.getHealth()) {
                            //Been killed...

                            DuelUtils.leaveDuel(plugin, players, player, killer);

                            e.setCancelled(true);

                            //Needs to return so the loop doesn't carry on.
                            return;
                        } else {
                            //Just hit and not killed, I want it to also override factions or other plugins similar.

                            e.setCancelled(false);
                        }

                    }

                }

            }

        }
    }
}
