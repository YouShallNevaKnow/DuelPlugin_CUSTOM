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
    private static HashMap<Player, Location> originalLocation = new HashMap<>();
    private static HashMap<Player, GameMode> originalGamemode = new HashMap<>();
    private static HashMap<Player, Integer> originalFoodLevel = new HashMap<>();
    private static HashMap<Player, Integer> originalFireTicks = new HashMap<>();
    private static HashMap<Player, Float> originalEXP = new HashMap<>();
    private static HashMap<Player, Double> originalHealth = new HashMap<>();
    private static HashMap<Player, Collection<PotionEffect>> originalPotionEffects = new HashMap<>();

    public static List<Player[]> getPlayersInGame() {
        return playersInGame;
    }

    public static HashMap<Player, GameMode> getOriginalGamemode() {
        return originalGamemode;
    }

    public static HashMap<Player, Location> getOriginalLocation() {
        return originalLocation;
    }

    public static HashMap<Player, Integer> getOriginalFoodLevel() {
        return originalFoodLevel;
    }

    public static HashMap<Player, Integer> getOriginalFireTicks() {
        return originalFireTicks;
    }

    public static HashMap<Player, Float> getOriginalEXP() {
        return originalEXP;
    }

    public static HashMap<Player, Double> getOriginalHealth() {
        return originalHealth;
    }

    public static HashMap<Player, Collection<PotionEffect>> getOriginalPotionEffects() {
        return originalPotionEffects;
    }

    public static List<Player> getPlayersGameStarting() {
        return playersGameStarting;
    }


    @Override
    public void onEnable() {
        loadConf();
        getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDamaged(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDrop(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerPickup(), this);
    }

    private void loadConf() {
        this.getLogger().info("Loading config");
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
        }
        reloadConfig();
    }

    private boolean JoinArena(Player accepter, Player requester, Player[] players) {

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
        originalPotionEffects.put(requester, requester.getActivePotionEffects());

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
                    plr.setGameMode(GameMode.ADVENTURE);
                    plr.setFireTicks(0);
                    plr.setFoodLevel(20);
                }
            }, 20 * 4);
        }
        return true;
    }

    private boolean StopPendingRequest(Player accepter, boolean isAccepting) {
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

        if (isAccepting) {
            accepter.sendMessage(ChatColor.LIGHT_PURPLE + " You accepted " + requester.getDisplayName() + ChatColor.DARK_PURPLE + "'s duel request");
            requester.sendMessage(ChatColor.LIGHT_PURPLE + accepter.getDisplayName() + ChatColor.DARK_PURPLE + " has accepted your duel request");
            JoinArena(accepter, requester, players);
        } else {
            accepter.sendMessage(ChatColor.LIGHT_PURPLE + "You denied the duel request.");
            requester.sendMessage(ChatColor.LIGHT_PURPLE + accepter.getDisplayName() + " denied your duel request.");
        }
        return true;
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
                player.sendMessage(ChatColor.GOLD + "(Wrong/Wrong number) of arguments! Type " + ChatColor.GREEN + ChatColor.ITALIC
                        + "/duel <player>" + ChatColor.GOLD + " or " + ChatColor.GREEN + ChatColor.ITALIC + "/duel accept");
                return true;
            } else if (args[0].equalsIgnoreCase("deny")) {
                //Denying a duel with /duel deny:
                return StopPendingRequest(player, false);

            } else if (args[0].equalsIgnoreCase("accept")) {
                //Accepting a duel with /duel accept:
                return StopPendingRequest(player, true);

            } else {
                //Requesting a duel with /duel <player>:

                Player target;
                if ((Bukkit.getOnlinePlayers().size() <= 1) || !Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                    player.sendMessage(ChatColor.RED + "Can not find player " + ChatColor.DARK_RED + args[0] + ChatColor.RED + "!");
                    return true;
                }

                target = Bukkit.getPlayer(args[0]);
                if (player.equals(target)) {
                    player.sendMessage(ChatColor.DARK_PURPLE + "You can't duel yourself silly billy!");
                    return true;
                }

                for (Player[] players : playersInGame) {
                    if (players[0].equals(target) || players[1].equals(target)) {
                        player.sendMessage(ChatColor.RED + "That player is already in a duel!");
                        return true;
                    }
                }

                if ((player.getLocation().getX() - target.getLocation().getX() > 10)
                        || (player.getLocation().getY() - target.getLocation().getY() > 10)
                        || (player.getLocation().getZ() - target.getLocation().getZ() > 10)
                        ||
                        (player.getLocation().getX() - target.getLocation().getX() < -10)
                        || (player.getLocation().getY() - target.getLocation().getY() < -10)
                        || (player.getLocation().getZ() - target.getLocation().getZ() < -10)) {

                    player.sendMessage(ChatColor.GOLD + "You need to be within 10 blocks of that player to duel!");
                    return true;
                } else if (!pendingDuelRequests.isEmpty()) {
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