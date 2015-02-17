package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

public class DuelUtils {
    public static void leaveDuel(Player[] players, Player player, Player killer) {
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
        player.sendMessage(ChatColor.DARK_AQUA + killer.getDisplayName() + " won the duel against you! ");
    }
}
