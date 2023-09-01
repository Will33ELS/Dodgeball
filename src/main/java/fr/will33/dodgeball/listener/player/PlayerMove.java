package fr.will33.dodgeball.listener.player;

import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.model.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Arena arena = Dodgeball.getInstance().getArena(player);
        if(arena != null && arena.getStatut() == Arena.Statut.INGAME){
            if(arena.getBluePlayers().contains(player) && !arena.getBlueZone().isIn(event.getTo())){
                event.setCancelled(true);
            }
            if(arena.getRedPlayers().contains(player) && !arena.getRedZone().isIn(event.getTo())){
                event.setCancelled(true);
            }
        }
    }

}
