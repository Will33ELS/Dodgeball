package fr.will33.dodgeball;

import fr.will33.dodgeball.commands.ConfigurationCommand;
import fr.will33.dodgeball.commands.JoinCommand;
import fr.will33.dodgeball.commands.DodgeballCommand;
import fr.will33.dodgeball.exception.DodgeballConfigurationException;
import fr.will33.dodgeball.manager.ConfigurationManager;
import fr.will33.dodgeball.manager.GameManager;
import fr.will33.dodgeball.manager.ListenerManager;
import fr.will33.dodgeball.model.Arena;
import fr.will33.guimodule.GuiModule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Dodgeball extends JavaPlugin {

    private ConfigurationManager configurationManager;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.configurationManager = new ConfigurationManager();
        this.gameManager = new GameManager(this.configurationManager);
        try {
            this.configurationManager.loadConfiguration(this.gameManager, this.getConfig());
        } catch (DodgeballConfigurationException e) {
            throw new RuntimeException(e);
        }

        new ListenerManager().registerListeners(this);
        new GuiModule(this);

        this.getCommand("config").setExecutor(new ConfigurationCommand(this.configurationManager));
        this.getCommand("join").setExecutor(new JoinCommand(this.gameManager));
        this.getCommand("dodgeball").setExecutor(new DodgeballCommand());
    }

    @Override
    public void onDisable() {
        this.gameManager.endGame();
    }

    /**
     * Recover the arena where the player is located
     * @param player Instance of the player
     * @return Instance of the arena
     */
    public @Nullable Arena getArena(Player player){
        return this.gameManager.getArena().getBluePlayers().contains(player) || this.gameManager.getArena().getRedPlayers().contains(player) ? this.gameManager.getArena() : null;
    }

    /**
     * Recover game manager instance
     * @return
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Recover configuration manager instance
     * @return
     */
    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    /**
     * Recover instance of the plugin
     * @return
     */
    public static Dodgeball getInstance(){
        return Dodgeball.getPlugin(Dodgeball.class);
    }
}
