package fr.will33.dodgeball.listener.entity;

import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.model.Arena;
import fr.will33.dodgeball.task.RespawnTask;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;
import java.util.Random;

public class EntityDamageByEntity implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        Arena arena = Dodgeball.getInstance().getGameManager().getArena();
        if(event.getEntity() instanceof Player player){
            if(Dodgeball.getInstance().getArena(player) != null) {
                if (event.getDamager() instanceof Player) {
                    event.setCancelled(true);
                }
                if(event.getDamager() instanceof Snowball snowball && snowball.getShooter() instanceof Player shooter){
                    if(Dodgeball.getInstance().getArena(shooter) != null) {
                        if((arena.getBluePlayers().contains(player) && arena.getRedPlayers().contains(shooter)) || (arena.getBluePlayers().contains(shooter) && arena.getRedPlayers().contains(player))) {
                            if ("Dodgeball".equalsIgnoreCase(snowball.getCustomName())) {
                                event.setCancelled(true);
                                if(!player.isInvulnerable()) {
                                    if (player.getHealth() - 2 > 0) {
                                        player.setHealth(player.getHealth() - 2);
                                        player.setGameMode(GameMode.SPECTATOR);
                                        RespawnTask respawnTask = new RespawnTask(arena, player);
                                        respawnTask.runTaskLater(Dodgeball.getInstance(), 20 * 5);
                                        Dodgeball.getInstance().getGameManager().getRespawnTasks().put(player, respawnTask);
                                    } else {
                                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
                                        player.setGameMode(GameMode.SPECTATOR);
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("messages.youreDeath")));
                                        Dodgeball.getInstance().getGameManager().checkWin();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(event.getDamager() instanceof Snowball snowball){
            if ("Dodgeball".equalsIgnoreCase(snowball.getCustomName())) {
                if(arena.getPlayZone().isIn(event.getEntity().getLocation())) {
                    Dodgeball.getInstance().getGameManager().dropBall(event.getEntity().getLocation());
                } else {
                    Location loc = arena.getBallSpawn().get(new Random().nextInt(arena.getBallSpawn().size()));
                    Dodgeball.getInstance().getGameManager().dropBall(loc);
                }
            }
        }
    }

}