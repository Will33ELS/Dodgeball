package fr.will33.dodgeball.listener.entity;

import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.model.Arena;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class EntityPickupItem implements Listener {

    @EventHandler
    public void onPickup(EntityPickupItemEvent event){
        Item item = event.getItem();
        if(event.getEntity() instanceof Player player){
            Arena arena = Dodgeball.getInstance().getArena(player);
            if(Dodgeball.getInstance().getConfigurationManager().getBallItemStack().isSimilar(event.getItem().getItemStack())){
                if(arena == null){
                    event.setCancelled(true);
                } else {
                    if(player.getInventory().contains(Dodgeball.getInstance().getConfigurationManager().getBallItemStack())){
                        event.setCancelled(true);
                    }
                    if(arena.getBlueZone().isIn(item.getLocation()) && !arena.getBluePlayers().contains(player)){
                        event.setCancelled(true);
                    }
                    if(arena.getRedZone().isIn(item.getLocation()) && !arena.getRedPlayers().contains(player)){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
