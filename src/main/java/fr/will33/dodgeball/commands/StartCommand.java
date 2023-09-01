package fr.will33.dodgeball.commands;

import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.manager.GameManager;
import fr.will33.dodgeball.model.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
        return false;
    }
}
