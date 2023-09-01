package fr.will33.dodgeball.manager;

import com.google.common.base.Preconditions;
import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.exception.ArenaConfigurationException;
import fr.will33.dodgeball.exception.DodgeballConfigurationException;
import fr.will33.dodgeball.model.Arena;
import fr.will33.dodgeball.model.ArenaConfiguration;
import fr.will33.dodgeball.model.Cuboid;
import fr.will33.dodgeball.model.ZoneConfiguration;
import fr.will33.dodgeball.utils.ItemBuilder;
import fr.will33.dodgeball.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class ConfigurationManager {

    private Player ownerConfig;
    private int gameDuration;
    private ArenaConfiguration arenaConfiguration;
    private ZoneConfiguration zoneConfiguration;
    private ItemStack ballItemStack;

    /**
     * Load configuration
     * @throws DodgeballConfigurationException
     */
    public void loadConfiguration(@NonNull GameManager gameManager, @NonNull FileConfiguration config) throws DodgeballConfigurationException {
        Preconditions.checkNotNull(gameManager);
        Preconditions.checkNotNull(config);
        World world = Bukkit.getWorld(config.getString("arena.world"));
        if(world == null) throw new ArenaConfigurationException("Le monde n'existe pas ou n'est pas chargé.");
        Arena arena = new Arena(Cuboid.load(world, config.getString("arena.arenaZone")), Cuboid.load(world, config.getString("arena.blueZone")), Cuboid.load(world, config.getString("arena.redZone")));
        arena.getBlueSpawn().addAll(config.getStringList("arena.blueSpawnLocation").stream().map(LocationUtil::fromString).toList());
        arena.getRedSpawn().addAll(config.getStringList("arena.redSpawnLocation").stream().map(LocationUtil::fromString).toList());
        arena.getBallSpawn().addAll(config.getStringList("arena.ballSpawnLocation").stream().map(LocationUtil::fromString).toList());
        gameManager.setArena(arena);
        this.gameDuration = config.getInt("config.gameDuration");
        if(this.gameDuration <= 0) throw new DodgeballConfigurationException("La durée d'une partie doit être supérieur à 0 !");
        this.ballItemStack = this.loadItems(Material.SNOWBALL.name(), ChatColor.translateAlternateColorCodes('&', config.getString("ballItem.displayName")), config.getStringList("ballItem.lore").stream().map(l -> ChatColor.translateAlternateColorCodes('&', l)).toList());
    }

    /**
     * Load item from the configuration
     * @param material Name of the material
     * @param displayName Displayname of the item
     * @param lore Lore of the item
     * @return Instance of the item
     * @throws DodgeballConfigurationException Material error
     */
    private ItemStack loadItems(String material, String displayName, List<String> lore) throws DodgeballConfigurationException {
        Material mat;
        try{
            mat = Material.valueOf(material.toUpperCase());
        } catch (IllegalArgumentException err){
            throw new DodgeballConfigurationException("This material (" + material + ") does not exist. Please check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html");
        }
        return new ItemBuilder(mat, 1, ChatColor.translateAlternateColorCodes('&', displayName), lore.stream().map(l -> ChatColor.translateAlternateColorCodes('&', l)).toList()).toItemStack();
    }

    /**
     * Recover owner of the configuration
     * @return
     */
    public @Nullable Player getOwnerConfig() {
        return ownerConfig;
    }

    /**
     * Define owner of the configuration
     * @param ownerConfig Instance of the player
     */
    public void setOwnerConfig(@Nullable Player ownerConfig) {
        this.ownerConfig = ownerConfig;
    }

    /**
     * Recover instance of the current configuration
     * @return
     */
    public @Nullable ArenaConfiguration getArenaConfiguration() {
        return arenaConfiguration;
    }

    /**
     * Define the current configuration
     * @param arenaConfiguration Instance of the configuration
     */
    public void setArenaConfiguration(@Nullable ArenaConfiguration arenaConfiguration) {
        this.arenaConfiguration = arenaConfiguration;
    }

    /**
     * Recover zone configuration instance
     * @return
     */
    public @Nullable ZoneConfiguration getZoneConfiguration() {
        return zoneConfiguration;
    }

    /**
     * Define zone configuration instance
     * @param zoneConfiguration Instance of the zone configuration
     */
    public void setZoneConfiguration(@Nullable ZoneConfiguration zoneConfiguration) {
        this.zoneConfiguration = zoneConfiguration;
    }

    /**
     * Recover ball itemstack
     * @return
     */
    public @NonNull ItemStack getBallItemStack() {
        return ballItemStack;
    }

    /**
     * Recover game duration in seconds
     * @return
     */
    public int getGameDuration() {
        return gameDuration;
    }
}
