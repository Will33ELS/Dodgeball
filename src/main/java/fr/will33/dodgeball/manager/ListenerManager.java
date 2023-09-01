package fr.will33.dodgeball.manager;

import fr.will33.dodgeball.listener.entity.EntityDamageByEntity;
import fr.will33.dodgeball.listener.entity.EntityPickupItem;
import fr.will33.dodgeball.listener.entity.EntityRegainHealth;
import fr.will33.dodgeball.listener.player.PlayerInteract;
import fr.will33.dodgeball.listener.player.PlayerMove;
import fr.will33.dodgeball.listener.player.PlayerQuit;
import fr.will33.dodgeball.listener.projectile.ProjectileHit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerManager {

    /**
     * Register all listeners
     * @param javaPlugin Instance of the plugin
     */
    public void registerListeners(JavaPlugin javaPlugin){
        PluginManager pluginManager = Bukkit.getPluginManager();

        //ENTITY
        pluginManager.registerEvents(new EntityDamageByEntity(), javaPlugin);
        pluginManager.registerEvents(new EntityPickupItem(), javaPlugin);
        pluginManager.registerEvents(new EntityRegainHealth(), javaPlugin);

        //PLAYER
        pluginManager.registerEvents(new PlayerInteract(), javaPlugin);
        pluginManager.registerEvents(new PlayerMove(), javaPlugin);
        pluginManager.registerEvents(new PlayerQuit(), javaPlugin);

        //PROJECTILE
        pluginManager.registerEvents(new ProjectileHit(), javaPlugin);
    }

}
