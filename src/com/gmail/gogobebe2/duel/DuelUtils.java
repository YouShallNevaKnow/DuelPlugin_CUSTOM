package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

/**
 * Created by william on 2/17/15.
 */
public class DuelUtils {
    public static void leaveDuel(Duel plugin, Player[] players, Player player, Player killer) {
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
            if (!p.getActivePotionEffects().isEmpty()) {
                for (PotionEffect potion : p.getActivePotionEffects()) {
                    p.removePotionEffect(potion.getType());
                }
            }
            for (PotionEffect potion : plugin.getOriginalPotionEffects().get(p)) {
                p.addPotionEffect(potion);
            }

            plugin.getPlayersInGame().remove(players);

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
    }
}
