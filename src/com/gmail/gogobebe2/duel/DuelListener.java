package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class DuelListener implements Listener{
    private final Duel plugin;

    public DuelListener(Duel plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!Duel.getPlayersInGame().isEmpty()) {
            Player accepter = null, requester = null;
            for (Player[] players : Duel.getPlayersInGame()) {
                if (players[0].equals(player) || players[1].equals(player)) {
                    accepter = players[0];
                    requester = players[1];
                }
            }

            if (accepter != null && requester != null) {
                double accepterX = accepter.getLocation().getX();
                double accepterY = accepter.getLocation().getY();
                double accepterZ = accepter.getLocation().getZ();
                double requesterX = requester.getLocation().getX();
                double requesterY = requester.getLocation().getY();
                double requesterZ = requester.getLocation().getZ();

                double centerX;
                double centerY;
                double centerZ;

                if (accepterX > requesterX) {
                    centerX = (accepterX - requesterX) / 2 + requesterY;
                }
                else {
                    centerX = (requesterX - accepterX) / 2 + accepterY;
                }

                if (accepterY > requesterY) {
                    centerY = (accepterY - requesterY) / 2 + requesterY;
                }
                else {
                    centerY = (requesterY - accepterY) / 2 + accepterY;
                }

                if (accepterZ > requesterZ) {
                    centerZ = (accepterZ - requesterZ) / 2 + requesterZ;
                }
                else {
                    centerZ = (requesterZ - accepterZ) / 2 + accepterZ;
                }

                Location playerLoc = event.getTo();
                boolean _30BlocksAway = (
                            (playerLoc.getX() > (30 + centerX) || playerLoc.getX() > (centerX - 30))
                        ||  (playerLoc.getY() > (30 + centerZ) || playerLoc.getY() > (centerY - 30))
                        ||  (playerLoc.getZ() > (30 + centerZ) || playerLoc.getZ() > (centerZ - 30))
                );
                if (_30BlocksAway) {
                    event.setTo(event.getFrom());
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD
                            + "You have reached the border of the duel! Turn back and face your enemy like a man!");
                    return;
                }
            }
        }

        if (!Duel.getPlayersGameStarting().isEmpty()) {
            for (Player p : Duel.getPlayersGameStarting()) {
                if (p.equals(player)) {
                    event.setTo(event.getFrom());
                }
            }
        }


    }
}
