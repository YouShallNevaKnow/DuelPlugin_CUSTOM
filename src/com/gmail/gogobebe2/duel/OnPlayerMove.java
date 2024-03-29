package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!Duel.getPlayersInGame().isEmpty()) {
            Player accepter, requester;
            for (Player[] players : Duel.getPlayersInGame()) {
                if (players[0].equals(player) || players[1].equals(player)) {
                    accepter = players[0];
                    requester = players[1];

                    double accepterX = Duel.getOriginalLocation().get(accepter).getX();
                    double accepterZ = Duel.getOriginalLocation().get(accepter).getZ();
                    double requesterX = Duel.getOriginalLocation().get(requester).getX();
                    double requesterZ = Duel.getOriginalLocation().get(requester).getZ();

                    double centerX = (requesterX + accepterX) / 2;
                    double centerZ = (accepterZ + requesterZ) / 2;

                    Location playerLoc = event.getTo();

                    boolean _30BlocksAway = (
                            (playerLoc.getX() >= (40 + centerX) || playerLoc.getX() <= (centerX - 40)) || (playerLoc.getZ() >= (40 + centerZ) || playerLoc.getZ() <= (centerZ - 40))
                    );
                    if (_30BlocksAway) {
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD
                                + "You have reached the border of the duel! Turn back and face your enemy like a man!");
                        Location cords = event.getTo();
                        cords.setX(centerX);
                        cords.setZ(centerZ);
                        cords.setY(Duel.getOriginalLocation().get(accepter).getY() + 2);
                        if (event.getTo().getZ() > event.getFrom().getZ()) {
                            if (event.getTo().getZ() - event.getFrom().getZ() >= 1) {
                                player.teleport(cords);
                            }
                        }
                        if (event.getTo().getZ() < event.getFrom().getZ()) {
                            if (event.getFrom().getZ() - event.getTo().getZ() >= 1) {
                                player.teleport(cords);
                            }
                        }
                        if (event.getTo().getX() > event.getFrom().getX()) {
                            if (event.getTo().getX() - event.getFrom().getX() >= 1) {
                                player.teleport(cords);
                            }
                        }
                        if (event.getTo().getX() < event.getFrom().getX()) {
                            if (event.getFrom().getX() - event.getTo().getX() >= 1) {
                                player.teleport(cords);
                            }
                        }
                        event.setCancelled(true);
                    }
                    return;
                }
            }
        }


        if (!Duel.getPlayersGameStarting().isEmpty()) {
            for (Player p : Duel.getPlayersGameStarting()) {
                if (p.equals(player)) {
                    Location to = event.getTo();
                    Location originalLocation = Duel.getOriginalLocation().get(player);
                    if (  (to.getZ() > originalLocation.getZ() + 1.5 || to.getZ() < originalLocation.getZ() - 1.5)
                       || (to.getX() > originalLocation.getX() + 1.5 || to.getX() < originalLocation.getX() - 1.5)
                       || (to.getY() > originalLocation.getY() + 3.5 || to.getY() < originalLocation.getY() - 3.5)
                       ) {
                        player.sendMessage(ChatColor.AQUA + "The duel hasn't started yet.");
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }


    }
}
