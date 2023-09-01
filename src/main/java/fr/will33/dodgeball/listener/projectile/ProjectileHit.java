package fr.will33.dodgeball.listener.projectile;

import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.model.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Random;

public class ProjectileHit implements Listener {

    @EventHandler
    public void onHit(ProjectileHitEvent event){
        Arena arena = Dodgeball.getInstance().getGameManager().getArena();
        if(event.getHitBlock() == null) return;
        if(event.getEntity() instanceof Snowball snowball){
            if("Dodgeball".equalsIgnoreCase(snowball.getCustomName())){
                if(arena.getPlayZone().isIn(event.getHitBlock().getLocation())){
                    Dodgeball.getInstance().getGameManager().dropBall(event.getHitBlock().getLocation());
                } else {
                    Location loc = arena.getBallSpawn().get(new Random().nextInt(arena.getBallSpawn().size()));
                    Dodgeball.getInstance().getGameManager().dropBall(loc);
                }
            }
        }
    }

}
