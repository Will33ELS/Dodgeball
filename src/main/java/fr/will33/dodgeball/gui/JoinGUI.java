package fr.will33.dodgeball.gui;

import com.google.common.base.Preconditions;
import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.manager.GameManager;
import fr.will33.dodgeball.model.Arena;
import fr.will33.dodgeball.utils.ItemBuilder;
import fr.will33.guimodule.GuiModule;
import fr.will33.guimodule.gui.AbstractGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JoinGUI extends AbstractGUI {

    private final Dodgeball instance;
    private final GameManager gameManager;

    public JoinGUI(@NonNull Dodgeball instance, @NonNull GameManager gameManager) {
        Preconditions.checkNotNull(instance);
        Preconditions.checkNotNull(gameManager);
        this.instance = instance;
        this.gameManager = gameManager;
    }

    @Override
    public void onDisplay(Player player) {
        this.inventory = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.join.title")));
        this.onUpdate(player);
        player.openInventory(this.inventory);
    }

    @Override
    public void onUpdate(Player player) {
        List<String> blueLore = new ArrayList<>(this.gameManager.getArena().getBluePlayers().stream().map(pls -> "§3- §e" + pls.getName()).toList());
        List<String> redLore = new ArrayList<>(this.gameManager.getArena().getRedPlayers().stream().map(pls -> "§3- §e" + pls.getName()).toList());

        this.setSlotData(new ItemBuilder(Material.BLUE_WOOL, 1, ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.join.blueTeam")), blueLore).toItemStack(), 3, "bleu");
        this.setSlotData(new ItemBuilder(Material.RED_WOOL, 1, ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.join.redTeam")), redLore).toItemStack(), 5, "rouge");
    }

    @Override
    public void onClick(Player player, ItemStack itemStack, String action, ClickType clickType) {
        Arena arena = this.gameManager.getArena();
        arena.getBluePlayers().remove(player);
        arena.getRedPlayers().remove(player);
        if("bleu".equalsIgnoreCase(action)){
            if(arena.getBluePlayers().size() == arena.getBlueSpawn().size()){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.join.teamFull")));
            } else {
                arena.getBluePlayers().add(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.join.joinBlueTeam")));
                player.closeInventory();
            }
        } else if("rouge".equalsIgnoreCase(action)){
            if(arena.getRedPlayers().size() == arena.getRedSpawn().size()){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.join.teamFull")));
            } else {
                arena.getRedPlayers().add(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.join.joinRedTeam")));
                player.closeInventory();
            }
        }
        for(Map.Entry<Player, AbstractGUI> entry : GuiModule.getGuiModule().getGuiManager().getGuis().entrySet()){
            if(entry.getValue() instanceof JoinGUI joinGUI){
                joinGUI.onUpdate(entry.getKey());
            }
        }
    }
}
