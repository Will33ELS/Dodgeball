package fr.will33.dodgeball.commands;

import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.manager.GameManager;
import fr.will33.dodgeball.model.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class DodgeballCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            sender.sendMessage("§c/dodgeball <start/stop>");
        } else {
            if(args[0].equalsIgnoreCase("start")){
                if(sender.hasPermission("dodgeball.start")){
                    GameManager gameManager = Dodgeball.getInstance().getGameManager();
                    FileConfiguration config = Dodgeball.getInstance().getConfig();
                    if(gameManager.getArena().getStatut() == Arena.Statut.LOBBY) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.start.launchGame")));
                        gameManager.startGame();
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.start.gameInProgress")));
                    }
                }
            } else if(args[0].equalsIgnoreCase("stop")){
                if(sender.hasPermission("dodgeball.stop")){
                    GameManager gameManager = Dodgeball.getInstance().getGameManager();
                    FileConfiguration config = Dodgeball.getInstance().getConfig();
                    if(gameManager.getArena().getStatut() == Arena.Statut.INGAME) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.stop.endGame")));
                        gameManager.endGame();
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.stop.gameNotInProgress")));
                    }
                }
            } else {
                sender.sendMessage("§c/dodgeball <start/stop>");
            }
        }
        return false;
    }
}
