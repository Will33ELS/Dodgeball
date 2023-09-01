package fr.will33.dodgeball.listener.player;

import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.manager.ConfigurationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        ConfigurationManager configurationManager = Dodgeball.getInstance().getConfigurationManager();
        if(player.equals(configurationManager.getOwnerConfig())){
            configurationManager.setOwnerConfig(null);
            configurationManager.setArenaConfiguration(null);
            configurationManager.setZoneConfiguration(null);
        }
    }

}
