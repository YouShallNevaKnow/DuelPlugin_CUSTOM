package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnPlayerDamaged implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerDamageReceive(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!Duel.getPlayersInGame().isEmpty()) {
                for (Player[] players : Duel.getPlayersInGame()) {
                    Player killer = null;
                    if (players[0].equals(player) && players[1].equals(killer)) {
                        killer = players[1];
                    } else if (players[1].equals(player) && players[0].equals(killer)) {
                        killer = players[0];
                    } else {
                        if (event instanceof EntityDamageByEntityEvent) {
                            EntityDamageByEntityEvent eEvent = (EntityDamageByEntityEvent) event;

                            if (players[0].equals(eEvent.getDamager()) || players[1].equals(eEvent.getDamager())) {
                                if (!(eEvent.getDamager() instanceof Player)) {
                                    killer = (Player) eEvent.getDamager();
                                    killer.sendMessage(ChatColor.BLUE + "You cannot damage people who aren't in the duel.");
                                }
                                event.setCancelled(true);
                                return;
                            } else if (players[0].equals(player) || players[1].equals(player)) {
                                if (!(eEvent.getDamager() instanceof Player)) {
                                    killer = (Player) eEvent.getDamager();
                                    killer.sendMessage(ChatColor.BLUE + "You cannot damage people who are in a duel!");
                                }
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }
                    if (killer != null) {
                        if (event.getFinalDamage() >= player.getHealth()) {
                            DuelUtils.leaveDuel(players, player, killer);
                            event.setCancelled(true);
                        } else {
                            event.setCancelled(false);
                        }
                    }
                }

            }

        }

    }
}
