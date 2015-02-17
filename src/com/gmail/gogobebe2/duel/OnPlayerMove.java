package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMove implements Listener{
    private final Duel plugin;

    public OnPlayerMove(Duel plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getPlayersInGame().isEmpty()) {
            Player accepter, requester;
            for (Player[] players : plugin.getPlayersInGame()) {
                if (players[0].equals(player) || players[1].equals(player)) {
                    accepter = players[0];
                    requester = players[1];

                    double accepterX = plugin.getOriginalLocation().get(accepter).getX();
                    double accepterY = plugin.getOriginalLocation().get(accepter).getY();
                    double accepterZ = plugin.getOriginalLocation().get(accepter).getZ();
                    double requesterX = plugin.getOriginalLocation().get(requester).getX();
                    double requesterY = plugin.getOriginalLocation().get(requester).getY();
                    double requesterZ = plugin.getOriginalLocation().get(requester).getZ();

                    double centerX = (requesterX + accepterX) / 2;
                    double centerY = (accepterY + requesterY) / 2;
                    double centerZ = (accepterZ + requesterZ) / 2;

                    Location playerLoc = event.getTo();

                    boolean _30BlocksAway = (
                            (playerLoc.getX() >= (30 + centerX) || playerLoc.getX() <= (centerX - 30))
                                    ||  (playerLoc.getY() >= (30 + centerZ) || playerLoc.getY() <= (centerY - 30))
                                    ||  (playerLoc.getZ() >= (30 + centerZ) || playerLoc.getZ() <= (centerZ - 30))
                    );
                    if (_30BlocksAway) {
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD
                                + "You have reached the border of the duel! Turn back and face your enemy like a man!");
                        event.setCancelled(true);
                    }
                    return;
                }
            }
        }

        if (!plugin.getPlayersGameStarting().isEmpty()) {
            for (Player p : plugin.getPlayersGameStarting()) {
                if (p.equals(player)) {
                    event.setCancelled(true);
                }
            }
        }


    }
}