package com.gmail.gogobebe2.duel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Duel extends JavaPlugin {

    //<Accepter, Requester>
    private List<Player[]> pendingDuelRequests = new ArrayList<>();

    @Override
    public void onEnable() {
        loadConf();
        getServer().getPluginManager().registerEvents(new DuelListener(this), this);
    }

    private void loadConf() {
        this.getLogger().info("Loading config");
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
        }
        reloadConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("duel")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You have to be a player to use this command!");
                return true;
            }

            Player player = (Player) sender;

            if (args.length > 1 || args.length < 1) {
                player.sendMessage(ChatColor.GOLD + "Too many arguments! Type " + ChatColor.GREEN + ChatColor.ITALIC
                        + "/duel <player>" + ChatColor.GOLD + " or " + ChatColor.GREEN + ChatColor.ITALIC + "/duel accept");
                return true;
            } else if (args[0].equalsIgnoreCase("deny")) {
                //Denying a duel with /duel deny:


                return true;
            } else if (args[0].equalsIgnoreCase("accept")) {
                //Accepting a duel with /duel accept:


                return true;
            } else {
                //Requesting a duel with /duel <player>:

                Player target = Bukkit.getPlayer(args[0]);
                if (!target.isOnline()) {
                    player.sendMessage(ChatColor.RED + "Can not find player!");
                    return true;
                } else if (player.equals(target)) {
                    player.sendMessage(ChatColor.DARK_PURPLE + "You can't duel yourself silly billy!");
                    return true;
                } else if ((player.getLocation().getX() - target.getLocation().getX() > 10)
                        || (player.getLocation().getY() - target.getLocation().getY() > 10)
                        || (player.getLocation().getZ() - target.getLocation().getZ() > 10)) {

                    player.sendMessage(ChatColor.GOLD + "You need to be within 10 blocks of that player to duel!");
                    return true;
                }

                if (!pendingDuelRequests.isEmpty()) {
                    for (Player[] players : pendingDuelRequests) {
                        for (Player p : players) {
                            if (p.equals(player) || p.equals(target)) {
                                for (Player pp : players) {
                                    pp.sendMessage(ChatColor.RED + "Previous duel request canceled");
                                }
                                pendingDuelRequests.remove(players);
                            }
                        }
                        if (pendingDuelRequests.isEmpty()) {
                            break;
                        }
                    }
                }

                pendingDuelRequests.add(new Player[]{target, player});
                player.sendMessage(ChatColor.RED + "Duel request sent to " + ChatColor.DARK_RED + target.getDisplayName() + ChatColor.RED + ". To cancel, type " + ChatColor.GREEN + "/duel cancel");
                target.sendMessage(ChatColor.DARK_RED + player.getDisplayName() + ChatColor.RED + " sent you a duel request. To accept type "
                        + ChatColor.GREEN + "/duel accept" + ChatColor.RED + " or " + ChatColor.GREEN + "/duel deny" + ChatColor.RED + " to deny.");
                return true;
            }
        }
        return true;
    }

}