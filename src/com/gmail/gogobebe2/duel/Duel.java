package com.gmail.gogobebe2.duel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Duel extends JavaPlugin {
    @Override
    public void onEnable() {
        loadConf();
        getServer().getPluginManager().registerEvents(this, this);
    }

    private void loadConf() {
        this.getLogger().info("Loading config");
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
            reloadConfig();
            if (!Bukkit.getOnlinePlayers().isEmpty()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    storePlayerInfo(player);
                }
            }
        }
        reloadConfig();
    }

    private void storePlayerInfo(Player player) {
        try {
            int wins = this.getConfig().getInt("Players." + player.getUniqueId() + ".wins");
            int loses = this.getConfig().getInt("Players." + player.getUniqueId() + ".loses");
            if ((wins == 0) && (loses == 0)) {
                throw new Exception();
            }
            player.sendMessage(ChatColor.AQUA + "You currently have " + ChatColor.GREEN + wins + ChatColor.AQUA + " wins and "
                    + ChatColor.GREEN + loses + ChatColor.AQUA + " loses. Go duel more people with /duel");
        } catch (Exception except) {
            this.getConfig().set("Players." + player.getUniqueId() + ".wins", 0);
            this.getConfig().set("Players." + player.getUniqueId() + ".loses", 0);
            saveConfig();
            player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "Welcome " + ChatColor.DARK_GREEN + "" + ChatColor.ITALIC
                    + player.getDisplayName() + ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "! Checkout our new plugin! Type /duel to get started.");
            //noinspection SpellCheckingInspection
            player.sendMessage(ChatColor.AQUA + "Regards, William Bryant (A.K.A. gogobebe2)" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " - Plugin developer)");
        }
    }

}