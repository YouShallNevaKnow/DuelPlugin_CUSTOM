package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnPlayerDamaged implements Listener {
    private Duel duel;

    public OnPlayerDamaged(Duel duel) {
        this.duel = duel;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerDamageReceive(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!Duel.getPlayersInGame().isEmpty()) {
                for (Player[] players : Duel.getPlayersInGame()) {
                    Player killer = null;

                    if (event instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent eEvent = (EntityDamageByEntityEvent) event;
                        if (players[0].equals(player) && players[1].equals(eEvent.getDamager())) {
                            killer = players[1];
                        } else if (players[1].equals(player) && players[0].equals(eEvent.getDamager())) {
                            killer = players[0];
                        } else if (players[0].equals(eEvent.getDamager()) || players[1].equals(eEvent.getDamager())) {
                            if (eEvent.getDamager() instanceof Player) {
                                killer = (Player) eEvent.getDamager();
                                killer.sendMessage(ChatColor.BLUE + "You cannot damage people who aren't in the duel.");
                            }
                            event.setCancelled(true);
                            return;
                        } else if (players[0].equals(player) || players[1].equals(player)) {
                            if (eEvent.getDamager() instanceof Player) {
                                killer = (Player) eEvent.getDamager();
                                killer.sendMessage(ChatColor.BLUE + "You cannot damage people who are in a duel!");
                                event.setCancelled(true);
                                return;
                            }
                            else if (eEvent.getDamager() instanceof Arrow) {
                                if (players[0].equals(player)) {
                                    killer = players[1];
                                } else if (players[1].equals(player)) {
                                    killer = players[0];
                                }
                                Arrow arrow = (Arrow) eEvent.getDamager();
                                if (!arrow.getShooter().equals(killer)) {
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            else {
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }
                    else {
                        if (players[0].equals(player)) {
                            killer = players[1];
                        } else if (players[1].equals(player)) {
                            killer = players[0];
                        }
                    }

                    if (killer != null) {
                        if (event.getFinalDamage() >= player.getHealth()) {
                            DuelUtils.leaveDuel(players, player, killer, duel);
                            event.setCancelled(true);
                            return;
                        } else {
                            event.setCancelled(false);
                        }
                    }
                }

            }

        }
        else {
            if (event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent eEvent = (EntityDamageByEntityEvent) event;
                for (Player[] players : Duel.getPlayersInGame()) {
                    if (players[0].equals(eEvent.getDamager()) || players[1].equals(eEvent.getDamager())) {
                        eEvent.getDamager().sendMessage(ChatColor.BLUE + "You cannot damage mobs while in a duel!");
                        event.setCancelled(true);
                        break;
                    }
                    else if (eEvent.getDamager() instanceof Arrow) {
                        Arrow arrow = (Arrow) eEvent.getDamager();

                        if (players[0].equals(arrow.getShooter()) || players[1].equals(arrow.getShooter())) {
                            eEvent.getDamager().sendMessage(ChatColor.BLUE + "You cannot damage mobs while in a duel!");
                            event.setCancelled(true);
                            break;
                        }
                    }

                }
            }
        }

    }
}
