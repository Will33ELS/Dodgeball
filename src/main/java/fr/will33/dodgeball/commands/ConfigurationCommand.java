package fr.will33.dodgeball.commands;

import com.google.common.base.Preconditions;
import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.exception.ArenaConfigurationException;
import fr.will33.dodgeball.manager.ConfigurationManager;
import fr.will33.dodgeball.model.ArenaConfiguration;
import fr.will33.dodgeball.model.Cuboid;
import fr.will33.dodgeball.model.ZoneConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ConfigurationCommand implements CommandExecutor {

    private final ConfigurationManager configurationManager;

    public ConfigurationCommand(@NonNull ConfigurationManager configurationManager) {
        Preconditions.checkNotNull(configurationManager);
        this.configurationManager = configurationManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player && player.isOp()){
            FileConfiguration config = Dodgeball.getInstance().getConfig();
            if(args.length == 0){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.help")));
            } else {
                if(args[0].equalsIgnoreCase("start")){
                    if(this.configurationManager.getArenaConfiguration() != null && !this.configurationManager.getOwnerConfig().equals(player)){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.configurationInProgress")));
                    } else if(this.configurationManager.getArenaConfiguration() == null && this.configurationManager.getOwnerConfig() == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.hoeGlobalCuboid")));
                        player.getInventory().setItem(0, new ItemStack(Material.GOLDEN_HOE));
                        this.configurationManager.setOwnerConfig(player);
                        this.configurationManager.setArenaConfiguration(new ArenaConfiguration());
                        this.configurationManager.setZoneConfiguration(new ZoneConfiguration((confirmGlobal) -> {
                            this.configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.BLUE_ZONE);
                            this.configurationManager.getArenaConfiguration().setArena(confirmGlobal);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.hoeBlueCuboid")));
                            this.configurationManager.setZoneConfiguration(new ZoneConfiguration((confirmBlue) -> {
                                this.configurationManager.getArenaConfiguration().setBlueZone(confirmBlue);
                                this.configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.RED_ZONE);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.hoeRedCuboid")));
                                this.configurationManager.setZoneConfiguration(new ZoneConfiguration((confirmRed) -> {
                                    player.getInventory().remove(Material.GOLDEN_HOE);
                                    this.configurationManager.setZoneConfiguration(null);
                                    this.configurationManager.getArenaConfiguration().setRedZone(confirmRed);
                                    this.configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.BLUE_SPAWN_POINTS);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.addBlueSpawnPoint")));
                                }));
                            }));
                        }));
                    }
                } else {
                    if(player.equals(this.configurationManager.getOwnerConfig())){
                        if(args[0].equalsIgnoreCase("confirm")){
                            if(this.configurationManager.getZoneConfiguration() != null){
                                try{
                                    this.configurationManager.getZoneConfiguration().confirm();
                                } catch (ArenaConfigurationException err){
                                    player.sendMessage("§c" + err.getMessage());
                                }
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.noConfirm")));
                            }
                        } else if(args[0].equalsIgnoreCase("addBlueSpawnPoint") && this.configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.BLUE_SPAWN_POINTS){
                            if(!this.configurationManager.getArenaConfiguration().getBlueZone().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.notInBlueZone")));
                            } else {
                                this.configurationManager.getArenaConfiguration().getBlueSpawn().add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.spawnAdded")));
                            }
                        } else if(args[0].equalsIgnoreCase("stopBlueSpawnPoint") && this.configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.BLUE_SPAWN_POINTS){
                            if(this.configurationManager.getArenaConfiguration().getBlueSpawn().isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.minOneBlueSpawn")));
                            } else {
                                this.configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.RED_SPAWN_POINTS);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.addRedSpawnPoint")));
                            }
                        } else if(args[0].equalsIgnoreCase("addRedSpawnPoint") && this.configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.RED_SPAWN_POINTS){
                            if(!this.configurationManager.getArenaConfiguration().getRedZone().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.notInRedZone")));
                            } else {
                                this.configurationManager.getArenaConfiguration().getRedSpawn().add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.spawnAdded")));
                            }
                        } else if(args[0].equalsIgnoreCase("stopRedSpawnPoint") && this.configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.RED_SPAWN_POINTS){
                            if(this.configurationManager.getArenaConfiguration().getRedSpawn().isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.minOneRedSpawn")));
                            } else {
                                this.configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.BALL_SPAWN_POINTS);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.addBallSpawnPoint")));
                            }
                        } else if(args[0].equalsIgnoreCase("addBallSpawnPoint") && this.configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.BALL_SPAWN_POINTS){
                            if(!this.configurationManager.getArenaConfiguration().getArena().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.notInPlayZone")));
                            } else {
                                this.configurationManager.getArenaConfiguration().getBallSpawn().add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.spawnAdded")));
                            }
                        } else if(args[0].equalsIgnoreCase("stopBallSpawnPoint") && this.configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.BALL_SPAWN_POINTS){
                            if(this.configurationManager.getArenaConfiguration().getBallSpawn().isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.minOneBallSpawn")));
                            } else {
                                try{
                                    this.configurationManager.getArenaConfiguration().finish();
                                    this.configurationManager.setOwnerConfig(null);
                                    this.configurationManager.setArenaConfiguration(null);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.config.success")));
                                } catch (ArenaConfigurationException err){
                                    player.sendMessage("§c" + err.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
