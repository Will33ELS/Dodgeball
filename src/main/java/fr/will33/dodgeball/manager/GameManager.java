package fr.will33.dodgeball.manager;

import com.google.common.base.Preconditions;
import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.model.Arena;
import fr.will33.dodgeball.model.DodgeScoreboard;
import fr.will33.dodgeball.task.GameTask;
import fr.will33.dodgeball.task.RespawnTask;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

public class GameManager {

    private final ConfigurationManager configurationManager;
    private DodgeScoreboard dodgeScoreboard;
    private GameTask gameTask;
    private Arena arena;
    private BossBar bossBar;
    private final List<Item> items = new ArrayList<>();
    private final Map<Player, RespawnTask> respawnTasks = new HashMap<>();

    public GameManager(@NonNull ConfigurationManager configurationManager) {
        Preconditions.checkNotNull(configurationManager);
        this.configurationManager = configurationManager;
    }

    /**
     * Start game
     */
    public void startGame(){
        this.arena.setStatut(Arena.Statut.INGAME);

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.dodgeScoreboard = new DodgeScoreboard(scoreboard);
        this.dodgeScoreboard.create(ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("scoreboard.title")));

        for(Location location : this.arena.getBallSpawn()){
            this.dropBall(location);
        }
        for(Player player : this.arena.getBluePlayers()){
            player.teleport(this.arena.getBlueSpawn().get(this.arena.getBluePlayers().indexOf(player)));
            player.getInventory().clear();
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(6);
            player.setHealth(6);
            player.setScoreboard(scoreboard);
        }
        for(Player player : this.arena.getRedPlayers()){
            player.teleport(this.arena.getRedSpawn().get(this.arena.getRedPlayers().indexOf(player)));
            player.getInventory().clear();
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(6);
            player.setHealth(6);
            player.setScoreboard(scoreboard);
        }

        this.gameTask = new GameTask(this, this.configurationManager.getGameDuration());
        this.gameTask.runTaskTimer(Dodgeball.getInstance(), 20, 20);
    }

    /**
     * End the game
     */
    public void endGame(){
        if(this.arena.getStatut() == Arena.Statut.LOBBY) return;
        this.dodgeScoreboard.remove();
        this.dodgeScoreboard = null;
        this.items.forEach(Entity::remove);
        this.items.clear();
        this.arena.setStatut(Arena.Statut.LOBBY);
        int blueHealth = 0, redHealth = 0;
        for(Player pls : this.arena.getBluePlayers()){
            if(pls.getGameMode() != GameMode.SPECTATOR || !this.respawnTasks.containsKey(pls)) {
                blueHealth += pls.getHealth();
            }
        }
        for(Player pls : this.arena.getRedPlayers()){
            if(pls.getGameMode() != GameMode.SPECTATOR || !this.respawnTasks.containsKey(pls)) {
                redHealth += pls.getHealth();
            }
        }
        if(blueHealth > redHealth){
            this.broadcast(ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("messages.blueWin")));
        } else if(redHealth > blueHealth){
            this.broadcast(ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("messages.redWin")));
        } else {
            this.broadcast(ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("messages.equality")));
        }
        this.arena.getBluePlayers().forEach(this::resetPlayer);
        this.arena.getRedPlayers().forEach(this::resetPlayer);
        this.arena.getBluePlayers().clear();
        this.arena.getRedPlayers().clear();
        Optional.ofNullable(this.bossBar).ifPresent(bossBar -> {
            bossBar.removeAll();
            this.bossBar = null;
        });
        this.gameTask.cancel();
        this.gameTask = null;
    }

    /**
     * Check win
     */
    public void checkWin(){
        int blueHealth = 0, redHealth = 0;
        for(Player pls : this.arena.getBluePlayers()){
            if(pls.getGameMode() == GameMode.SPECTATOR && !this.respawnTasks.containsKey(pls)) {
                blueHealth += pls.getHealth();
            }
        }
        for(Player pls : this.arena.getRedPlayers()){
            if(pls.getGameMode() == GameMode.SPECTATOR && !this.respawnTasks.containsKey(pls)) {
                redHealth += pls.getHealth();
            }
        }
        if(blueHealth == 0 || redHealth == 0){
            this.endGame();
        }
    }

    /**
     * Restaure player
     * @param player Instance of the player
     */
    public void resetPlayer(Player player){
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        player.setHealth(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().remove(this.configurationManager.getBallItemStack());
    }

    /**
     * Drop ball
     * @param location Instance of the location
     * @return Instance of the item
     */
    public @Nullable Item dropBall(Location location){
        if(this.arena.getStatut() == Arena.Statut.LOBBY) return null;
        Item item = location.getWorld().dropItem(location, this.configurationManager.getBallItemStack());
        item.setPersistent(false);
        item.setUnlimitedLifetime(true);
        item.setVelocity(new Vector(0, 0, 0));
        this.items.add(item);
        return item;
    }

    /**
     * Recover arena instance
     * @return
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Define arena instance
     * @param arena Instance of the arena
     */
    public void setArena(Arena arena) {
        this.arena = arena;
    }

    /**
     * Send a message to all players in the game
     * @param message Message to send
     */
    public void broadcast(String message){
        this.arena.getBluePlayers().forEach(pls -> pls.sendMessage(message));
        this.arena.getRedPlayers().forEach(pls -> pls.sendMessage(message));
    }

    /**
     * Recover scoreboard
     * @return
     */
    public @Nullable DodgeScoreboard getDodgeScoreboard() {
        return dodgeScoreboard;
    }

    /**
     * Recover all respawn tasks
     * @return
     */
    public Map<Player, RespawnTask> getRespawnTasks() {
        return respawnTasks;
    }

    /**
     * Recover bossbar
     * @return
     */
    public @Nullable BossBar getBossBar() {
        return bossBar;
    }

    /**
     * Define bossbar
     * @param bossBar
     */
    public void setBossBar(@Nullable BossBar bossBar) {
        this.bossBar = bossBar;
    }
}
