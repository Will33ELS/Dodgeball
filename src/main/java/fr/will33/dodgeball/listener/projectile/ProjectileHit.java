package fr.will33.dodgeball.listener.projectile;

import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.model.Arena;
import org.bukkit.Location;
import org.bukkit.block.Block;
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
        if(event.getEntity() instanceof Snowball snowball){
            if(event.getHitBlock() != null) {
                if ("Dodgeball".equalsIgnoreCase(snowball.getCustomName())) {
                    Block opposate = event.getHitBlock().getRelative(event.getHitBlockFace());
                    if (arena.getPlayZone().isIn(opposate.getLocation())) {
                        Dodgeball.getInstance().getGameManager().dropBall(opposate.getLocation());
                    } else {
                        Location loc = arena.getBallSpawn().get(new Random().nextInt(arena.getBallSpawn().size()));
                        Dodgeball.getInstance().getGameManager().dropBall(loc);
                    }
                }
            }
            if(event.getHitEntity() != null){
                if ("Dodgeball".equalsIgnoreCase(snowball.getCustomName())) {
                    if(arena.getPlayZone().isIn(event.getHitEntity().getLocation())) {
                        Dodgeball.getInstance().getGameManager().dropBall(event.getHitEntity().getLocation());
                    } else {
                        Location loc = arena.getBallSpawn().get(new Random().nextInt(arena.getBallSpawn().size()));
                        Dodgeball.getInstance().getGameManager().dropBall(loc);
                    }
                }
            }
        }
    }

}
