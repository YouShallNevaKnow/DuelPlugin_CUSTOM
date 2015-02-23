package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by william on 2/23/15.
 */
public class OnCommand implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event){
        if (!event.getMessage().equalsIgnoreCase("/leaveduel")) {
            Player player = event.getPlayer();
            for (Player players[] : Duel.getPlayersInGame()) {
                if (players[0].equals(player) || players[1].equals(player)) {
                    player.sendMessage(ChatColor.RED + "You cannot use commands while in a duel! To leave type /leaveduel");
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }


}
