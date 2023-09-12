package fr.will33.dodgeball.listener.entity;

import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.model.Arena;
import fr.will33.dodgeball.task.RespawnTask;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

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
                                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
                                if(!player.isInvulnerable()) {
                                    if (player.getHealth() - 2 > 0) {
                                        player.setHealth(player.getHealth() - 2);
                                        player.setGameMode(GameMode.SPECTATOR);
                                        RespawnTask respawnTask = new RespawnTask(arena, player);
                                        respawnTask.runTaskTimer(Dodgeball.getInstance(), 20, 20);
                                        Dodgeball.getInstance().getGameManager().getRespawnTasks().put(player, respawnTask);
                                        Dodgeball.getInstance().getGameManager().broadcast(Dodgeball.getInstance().getConfig().getString("messages.death").replace("%player%", player.getName()).replace("%killer%", shooter.getName()));
                                    } else {
                                        player.setGameMode(GameMode.SPECTATOR);
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("messages.youreDeath")));
                                        Dodgeball.getInstance().getGameManager().broadcast(Dodgeball.getInstance().getConfig().getString("messages.eliminate").replace("%player%", player.getName()).replace("%killer%", shooter.getName()));
                                        Dodgeball.getInstance().getGameManager().checkWin();
                                    }
                                    this.dropAllBall(player);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void dropAllBall(Player player){
        for(ItemStack itemStack : player.getInventory().getStorageContents()){
            if(itemStack != null && Dodgeball.getInstance().getConfigurationManager().getBallItemStack().isSimilar(itemStack)){
                player.getInventory().removeItem(itemStack);
                player.getWorld().dropItem(player.getLocation(), itemStack);
            }
        }
    }

}
