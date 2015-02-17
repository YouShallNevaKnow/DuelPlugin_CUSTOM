package com.gmail.gogobebe2.duel;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnPlayerDamaged implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerDamageReceive(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (!Duel.getPlayersInGame().isEmpty()) {
                for (Player[] players : Duel.getPlayersInGame()) {
                    if (players[0].equals(player) || players[1].equals(player)) {
                        Player killer;
                        if (players[0].equals(player)) {
                            killer = players[1];
                        } else {
                            killer = players[0];
                        }
                        if (e.getFinalDamage() >= player.getHealth()) {
                            //Been killed...

                            DuelUtils.leaveDuel(players, player, killer);

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
