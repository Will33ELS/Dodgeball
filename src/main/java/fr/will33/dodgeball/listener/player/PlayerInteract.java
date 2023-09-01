package fr.will33.dodgeball.listener.player;

import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.manager.ConfigurationManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ConfigurationManager configurationManager = Dodgeball.getInstance().getConfigurationManager();
        if(player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_HOE && player.equals(configurationManager.getOwnerConfig()) && event.getHand() == EquipmentSlot.HAND){
            if(configurationManager.getZoneConfiguration() != null){
                if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    event.setCancelled(true);
                    configurationManager.getZoneConfiguration().setPos1(event.getClickedBlock().getLocation());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("messages.zone.point1")));
                } else if(event.getAction() == Action.LEFT_CLICK_BLOCK){
                    event.setCancelled(true);
                    configurationManager.getZoneConfiguration().setPos2(event.getClickedBlock().getLocation());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("messages.zone.point2")));
                }
                if(configurationManager.getZoneConfiguration().getPos1() != null && configurationManager.getZoneConfiguration().getPos2() != null){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("messages.zone.success")));
                }
            }
        }
        if(event.getItem() != null && event.getItem().isSimilar(configurationManager.getBallItemStack())){
            event.setCancelled(true);
            event.getItem().setAmount(event.getItem().getAmount() - 1);
            Projectile projectile = player.launchProjectile(Snowball.class);
            projectile.setShooter(player);
            projectile.setCustomName("Dodgeball");
            projectile.setCustomNameVisible(false);
            projectile.setPersistent(false);
        }
    }

}
