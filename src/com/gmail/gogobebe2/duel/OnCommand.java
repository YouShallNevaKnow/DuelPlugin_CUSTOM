package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
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
        if (Duel.getDisableCommand().contains(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot use commands while in a duel! To leave type /leave");
            Duel.getDisableCommand().remove(event.getPlayer());
            event.setCancelled(true);
        }

    }


}
