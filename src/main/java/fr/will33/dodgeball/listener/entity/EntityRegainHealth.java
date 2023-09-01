package fr.will33.dodgeball.listener.entity;

import fr.will33.dodgeball.Dodgeball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityRegainHealth implements Listener {

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event){
        if(event.getEntity() instanceof Player player){
            if(Dodgeball.getInstance().getArena(player) != null){
                event.setCancelled(true);
            }
        }
    }

}
