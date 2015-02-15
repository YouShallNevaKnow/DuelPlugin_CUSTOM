package com.gmail.gogobebe2.duel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;

public class Duel extends JavaPlugin {

    //<Accepter, Requester>
    private static List<Player[]> pendingDuelRequests = new ArrayList<>();
    private static List<Player> playersGameStarting = new ArrayList<>();
    private static List<Player[]> playersInGame = new ArrayList<>();

    public static void setPlayersInGame(List<Player[]> playersInGame) {
        Duel.playersInGame = playersInGame;
    }

    public static HashMap<Player, GameMode> getOriginalGamemode() {
        return originalGamemode;
    }

    public static void setOriginalGamemode(HashMap<Player, GameMode> originalGamemode) {
        Duel.originalGamemode = originalGamemode;
    }

    public static HashMap<Player, Location> getOriginalLocation() {
        return originalLocation;
    }

    public static void setOriginalLocation(HashMap<Player, Location> originalLocation) {
        Duel.originalLocation = originalLocation;
    }

    public static HashMap<Player, Integer> getOriginalFoodLevel() {
        return originalFoodLevel;
    }

    public static void setOriginalFoodLevel(HashMap<Player, Integer> originalFoodLevel) {
        Duel.originalFoodLevel = originalFoodLevel;
    }

    public static HashMap<Player, Integer> getOriginalFireTicks() {
        return originalFireTicks;
    }

    public static void setOriginalFireTicks(HashMap<Player, Integer> originalFireTicks) {
        Duel.originalFireTicks = originalFireTicks;
    }

    public static HashMap<Player, Float> getOriginalEXP() {
        return originalEXP;
    }

    public static void setOriginalEXP(HashMap<Player, Float> originalEXP) {
        Duel.originalEXP = originalEXP;
    }

    public static HashMap<Player, Double> getOriginalHealth() {
        return originalHealth;
    }

    public static void setOriginalHealth(HashMap<Player, Double> originalHealth) {
        Duel.originalHealth = originalHealth;
    }

    public static HashMap<Player, Collection<PotionEffect>> getOriginalPotionEffects() {
        return originalPotionEffects;
    }

    public static void setOriginalPotionEffects(HashMap<Player, Collection<PotionEffect>> originalPotionEffects) {
        Duel.originalPotionEffects = originalPotionEffects;
    }

    public static List<Player> getPlayersGameStarting() {
        return playersGameStarting;
    }

    public static void setPlayersGamingStarting(List<Player> playersGamingStarting) {
        Duel.playersGameStarting = playersGamingStarting;
    }

    public static List<Player[]> getPlayersInGame() {
        return playersInGame;
    }

    private static HashMap<Player, Location> originalLocation = new HashMap<>();
    private static HashMap<Player, GameMode> originalGamemode = new HashMap<>();
    private static HashMap<Player, Integer> originalFoodLevel = new HashMap<>();
    private static HashMap<Player, Integer> originalFireTicks = new HashMap<>();
    private static HashMap<Player, Float> originalEXP = new HashMap<>();
    private static HashMap<Player, Double> originalHealth = new HashMap<>();
    private static HashMap<Player, Collection<PotionEffect>> originalPotionEffects = new HashMap<>();


    @Override
    public void onEnable() {
        loadConf();
        getServer().getPluginManager().registerEvents(new OnPlayerMove(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDamaged(this), this);
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
                @SuppressWarnings("UnnecessaryLocalVariable") Player accepter = player;
                Player requester = null;

                //Accepting a duel with /duel accept:
                if (pendingDuelRequests.isEmpty()) {
                    accepter.sendMessage(ChatColor.RED + "No duel requests pending!");
                    return true;
                }

                for (Player[] players : pendingDuelRequests) {
                    if (players[0].equals(accepter)) {
                        if (Arrays.asList(players).indexOf(accepter) == 1) {
                            requester = players[0];
                        } else {
                            requester = players[1];
                        }
                        break;
                    }
                }
                if (requester == null) {
                    accepter.sendMessage(ChatColor.RED + "No duel requests pending!");
                    return true;
                }

                Player[] players = {accepter, requester};
                for (Player[] pp : pendingDuelRequests) {
                    if (Arrays.equals(pp, players)) {
                        pendingDuelRequests.remove(pp);
                        break;
                    }
                }

                accepter.sendMessage(ChatColor.DARK_PURPLE + " You accepted " + requester.getDisplayName() + ChatColor.DARK_PURPLE + "'s duel request");
                requester.sendMessage(ChatColor.DARK_PURPLE + accepter.getDisplayName() + ChatColor.DARK_PURPLE + " has accepted your duel request");

                /**************** Accepter original data ***************/
                //Accepter original data
                originalLocation.put(accepter, accepter.getLocation());
                originalGamemode.put(accepter, accepter.getGameMode());
                originalFoodLevel.put(accepter, accepter.getFoodLevel());
                originalFireTicks.put(accepter, accepter.getFireTicks());
                originalEXP.put(accepter, accepter.getExp());
                originalHealth.put(accepter, accepter.getHealth());
                originalPotionEffects.put(accepter, accepter.getActivePotionEffects());

                //Requester original data
                originalLocation.put(requester, requester.getLocation());
                originalGamemode.put(requester, requester.getGameMode());
                originalFoodLevel.put(requester, requester.getFoodLevel());
                originalFireTicks.put(requester, requester.getFireTicks());
                originalEXP.put(requester, requester.getExp());
                originalHealth.put(requester, requester.getHealth());
                originalPotionEffects.put(accepter, accepter.getActivePotionEffects());

                /*******************************************************/

                playersGameStarting.add(accepter);
                playersGameStarting.add(requester);
                for (Player p : players) {
                    @SuppressWarnings("SpellCheckingInspection") final Player[] plrs = players;
                    final Player plr = p;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            plr.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Duel starting in" + ChatColor.GREEN + " 3");
                        }
                    }, 20);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            plr.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Duel starting in" + ChatColor.GREEN + " 2");
                        }
                    }, 20 * 2);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            plr.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Duel starting in" + ChatColor.GREEN + " 1");
                        }
                    }, 20 * 3);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            playersGameStarting.remove(plr);
                            playersInGame.add(plrs);
                            plr.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.ITALIC + "FIGHT!");
                            plr.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                            // TODO:
                            // Do:
                            // playersInGame.remove(plrs);
                            // on player death.
                        }
                    }, 20 * 4);
                }



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
                        || (player.getLocation().getZ() - target.getLocation().getZ() > 10)
                        ||
                           (player.getLocation().getX() - target.getLocation().getX() < -10)
                        || (player.getLocation().getY() - target.getLocation().getY() < -10)
                        || (player.getLocation().getZ() - target.getLocation().getZ() < -10)) {

                    player.sendMessage(ChatColor.GOLD + "You need to be within 10 blocks of that player to duel!");
                    return true;
                }

                if (!pendingDuelRequests.isEmpty()) {
                    boolean stopLoop = false;
                    for (Player[] players : pendingDuelRequests) {
                        for (Player p : players) {
                            if (p.equals(player) || p.equals(target)) {
                                p.sendMessage(ChatColor.RED + "Previous duel request canceled");
                                stopLoop = true;
                            }
                        }
                        if (stopLoop) {
                            pendingDuelRequests.remove(players);
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