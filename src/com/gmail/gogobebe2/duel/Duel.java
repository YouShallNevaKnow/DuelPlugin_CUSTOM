package com.gmail.gogobebe2.duel;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Duel extends JavaPlugin {
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

            if (args.length > 1) {
                player.sendMessage(ChatColor.GOLD + "Too many arguments! Type " + ChatColor.GREEN + ChatColor.ITALIC
                        + "/duel <player>" + ChatColor.GOLD + " or " + ChatColor.GREEN + ChatColor.ITALIC + "/duel accept");
                return true;
            } else if (args[0].equalsIgnoreCase("accept")) {
                //Accepting a duel with /duel accept:



                return true;
            } else {
                //Requesting a duel with /duel <player>:



                return true;
            }
        }
        return true;
    }
}