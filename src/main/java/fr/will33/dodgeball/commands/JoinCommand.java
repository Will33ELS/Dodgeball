package fr.will33.dodgeball.commands;

import com.google.common.base.Preconditions;
import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.gui.JoinGUI;
import fr.will33.dodgeball.manager.GameManager;
import fr.will33.dodgeball.model.Arena;
import fr.will33.guimodule.GuiModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class JoinCommand implements CommandExecutor {

    private final GameManager gameManager;

    public JoinCommand(@NonNull GameManager gameManager) {
        Preconditions.checkNotNull(gameManager);
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player){
            if(this.gameManager.getArena().getStatut() == Arena.Statut.LOBBY) {
                GuiModule.getGuiModule().getGuiManager().openInventory(player, new JoinGUI(Dodgeball.getInstance(), this.gameManager));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("messages.join.gameInProgress")));
            }
        }
        return false;
    }
}
