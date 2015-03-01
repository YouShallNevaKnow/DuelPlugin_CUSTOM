package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

enum events {
    DROP, PICKUP
}

public class DuelUtils {

    public static <E extends PlayerEvent> void cancelDropOrPickup(E e) throws IllegalArgumentException{
        events eventState;
        if (e instanceof PlayerDropItemEvent) {
            eventState = events.DROP;
        }
        else if (e instanceof PlayerPickupItemEvent) {
            eventState = events.PICKUP;
        }
        else {
            throw new IllegalArgumentException("cancelDropOrPickup<E>(E e) takes the parameter of type PlayerDropItemEvent or PlayerPickupItemEvent");
        }

        if (!Duel.getPlayersInGame().isEmpty()) {
            for (Player[] players : Duel.getPlayersInGame()) {
                if (players[0].equals(e.getPlayer()) || players[1].equals(e.getPlayer())) {
                    if (eventState.equals(events.DROP)) {
                        @SuppressWarnings("ConstantConditions") PlayerDropItemEvent event = (PlayerDropItemEvent) e;
                        if (event.getItemDrop().getItemStack().getType().equals(Material.ARROW)) {
                            return;
                        }
                        event.getPlayer().sendMessage(ChatColor.BLUE + "You cannot drop items whilst in a duel!");
                        event.setCancelled(true);
                        return;
                    }
                    else if (eventState.equals(events.PICKUP)) {
                        PlayerPickupItemEvent event = (PlayerPickupItemEvent) e;
                        if (event.getItem().getItemStack().getType().equals(Material.ARROW)) {
                            return;
                        }
                        event.setCancelled(true);
                        return;
                    }

                }
            }
        }
    }
    public static void leaveDuel(Player[] players, Player player, Player killer, Duel duel) {
        Firework f = killer.getWorld().spawn(killer.getLocation(), Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(true).trail(true)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.AQUA).build());
        f.setFireworkMeta(fm);

        for (Player p : players) {
            p.teleport(Duel.getOriginalLocation().get(p));
            p.setGameMode(Duel.getOriginalGamemode().get(p));
            p.setFoodLevel(Duel.getOriginalFoodLevel().get(p));
            p.setFireTicks(Duel.getOriginalFireTicks().get(p));
            p.setExp(Duel.getOriginalEXP().get(p));
            p.setHealth(Duel.getOriginalHealth().get(p));
            if (!p.getActivePotionEffects().isEmpty()) {
                for (PotionEffect potion : p.getActivePotionEffects()) {
                    p.removePotionEffect(potion.getType());
                }
            }
            for (PotionEffect potion : Duel.getOriginalPotionEffects().get(p)) {
                p.addPotionEffect(potion);
            }

            Duel.getPlayersInGame().remove(players);

            Duel.getOriginalLocation().remove(p);
            Duel.getOriginalGamemode().remove(p);
            Duel.getOriginalFoodLevel().remove(p);
            Duel.getOriginalFireTicks().remove(p);
            Duel.getOriginalEXP().remove(p);
            Duel.getOriginalHealth().remove(p);
            Duel.getOriginalPotionEffects().remove(p);
        }

        killer.sendMessage(ChatColor.DARK_AQUA + "You won a duel against " + player.getDisplayName() + ". Congratulations!");
        if (!duel.getConfig().contains("Players." + killer.getUniqueId() + ".losses")) {
            duel.getConfig().set("Players." + killer.getUniqueId() + ".losses", 0);
        }
        if (!duel.getConfig().contains("Players." + killer.getUniqueId() + ".wins")) {
            duel.getConfig().set("Players." + killer.getUniqueId() + ".wins", 0);
        }
        else {
            duel.getConfig().set("Players." + killer.getUniqueId() + ".wins", duel.getConfig().getInt("Players." + killer.getUniqueId() + ".wins") + 1);
        }


        player.sendMessage(ChatColor.DARK_AQUA + killer.getDisplayName() + " won the duel against you! ");
        if (!duel.getConfig().contains("Players." + player.getUniqueId() + ".wins")) {
            duel.getConfig().set("Players." + player.getUniqueId() + ".wins", 0);
        }
        if (!duel.getConfig().contains("Players." + player.getUniqueId() + ".losses")) {
            duel.getConfig().set("Players." + player.getUniqueId() + ".losses", 0);
        }
        else {
            duel.getConfig().set("Players." + player.getUniqueId() + ".losses", duel.getConfig().getInt("Players." + player.getUniqueId() + ".losses") + 1);
        }
        duel.saveConfig();
    }
}
