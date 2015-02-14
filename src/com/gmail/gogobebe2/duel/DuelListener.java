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
                Location accepterOriginalLocation = Duel.getOriginalLocation(accepter);
                Location requesterOriginalLocation = Duel.getOriginalLocation(requester);
                
                double accepterX = accepterOriginalLocation.getX();
                double accepterY = accepterOriginalLocation.getY();
                double accepterZ = accepterOriginalLocation.getZ();
                double requesterX = requesterOriginalLocation.getX();
                double requesterY = requesterOriginalLocation.getY();
                double requesterZ = requesterOriginalLocation.getZ();

                double centerX;
                double centerY;
                double centerZ;

                centerX = (requesterX + accepterX) / 2;

                centerY = (accepterY + requesterY) / 2;

                centerZ = (accepterZ + requesterZ) / 2;

                Location playerLoc = event.getTo();

                boolean _30BlocksAway = (
                            (playerLoc.getX() >= (10 + centerX) || playerLoc.getX() <= (centerX - 10))
                        ||  (playerLoc.getY() >= (10 + centerZ) || playerLoc.getY() <= (centerY - 10))
                        ||  (playerLoc.getZ() >= (10 + centerZ) || playerLoc.getZ() <= (centerZ - 10))
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
