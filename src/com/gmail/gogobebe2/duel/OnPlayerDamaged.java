package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

/**
 * Created by william on 2/15/15.
 */
public class OnPlayerDamaged implements Listener {
    private final Duel plugin;

    public OnPlayerDamaged(Duel plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerDamageReceive(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (!plugin.getPlayersInGame().isEmpty()) {
                for (Player[] players : plugin.getPlayersInGame()) {
                    if (players[0].equals(player) || players[1].equals(player)) {
                        Player killer;
                        if (players[0].equals(player)) {
                            killer = players[1];
                        } else {
                            killer = players[0];
                        }
                        if (e.getFinalDamage() >= player.getHealth()) {

                            Firework f = killer.getWorld().spawn(killer.getLocation(), Firework.class);
                            FireworkMeta fm = f.getFireworkMeta();
                            fm.addEffect(FireworkEffect.builder()
                                    .flicker(true).trail(true)
                                    .with(FireworkEffect.Type.BALL_LARGE)
                                    .withColor(Color.AQUA).build());
                            f.setFireworkMeta(fm);

                            for (Player p : players) {
                                p.teleport(plugin.getOriginalLocation().get(p));
                                p.setGameMode(plugin.getOriginalGamemode().get(p));
                                p.setFoodLevel(plugin.getOriginalFoodLevel().get(p));
                                p.setFireTicks(plugin.getOriginalFireTicks().get(p));
                                p.setExp(plugin.getOriginalEXP().get(p));
                                p.setHealth(plugin.getOriginalHealth().get(p));
                                for (PotionEffect potion : p.getActivePotionEffects()) {
                                    p.removePotionEffect(potion.getType());
                                }
                                for (PotionEffect potion : plugin.getOriginalPotionEffects().get(p)) {
                                    p.addPotionEffect(potion);
                                }

                                plugin.getOriginalLocation().remove(p);
                                plugin.getOriginalGamemode().remove(p);
                                plugin.getOriginalFoodLevel().remove(p);
                                plugin.getOriginalFireTicks().remove(p);
                                plugin.getOriginalEXP().remove(p);
                                plugin.getOriginalHealth().remove(p);
                                plugin.getOriginalPotionEffects().remove(p);
                            }

                            killer.sendMessage(ChatColor.DARK_AQUA + "You won a duel against " + player.getDisplayName() + ". Congratulations!");
                            player.sendMessage(ChatColor.DARK_AQUA + killer.getDisplayName() + " won the duel against you! ");

                            plugin.getPlayersInGame().remove(players);
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
