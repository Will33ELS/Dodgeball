package fr.will33.dodgeball.task;

import com.google.common.base.Preconditions;
import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.manager.GameManager;
import fr.will33.dodgeball.utils.TimerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

public class GameTask extends BukkitRunnable {

    private final GameManager gameManager;
    private int seconds;

    public GameTask(@NonNull GameManager gameManager, int seconds) {
        Preconditions.checkNotNull(gameManager);
        this.gameManager = gameManager;
        this.seconds = seconds;
    }

    @Override
    public void run() {
        if(this.seconds == 0){
            this.gameManager.endGame();
            this.cancel();
            return;
        }
        int index = 0;
        this.gameManager.getDodgeScoreboard().setLine(index ++, ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("scoreboard.blueTeam")));
        for(Player pls : this.gameManager.getArena().getBluePlayers()){
            int health = ((int) (pls.getHealth() /2));
            if(pls.getGameMode() == GameMode.SPECTATOR && !this.gameManager.getRespawnTasks().containsKey(pls)){
                health = 0;
            }
            this.gameManager.getDodgeScoreboard().setLine(index ++, "§7" + pls.getName() + ": §e" + health);
        }
        this.gameManager.getDodgeScoreboard().setLine(index ++, "§5");
        this.gameManager.getDodgeScoreboard().setLine(index ++, ChatColor.translateAlternateColorCodes('&', Dodgeball.getInstance().getConfig().getString("scoreboard.redTeam")));
        for(Player pls : this.gameManager.getArena().getRedPlayers()){
            int health = ((int) (pls.getHealth() /2));
            if(pls.getGameMode() == GameMode.SPECTATOR && !this.gameManager.getRespawnTasks().containsKey(pls)){
                health = 0;
            }
            this.gameManager.getDodgeScoreboard().setLine(index ++, "§7" + pls.getName() + ": §e" + health);
        }
        if(this.gameManager.getBossBar() == null){
            BossBar bossBar = Bukkit.createBossBar("§6" + TimerUtil.format(this.seconds), BarColor.YELLOW, BarStyle.SOLID);
            this.gameManager.setBossBar(bossBar);
            this.gameManager.getArena().getBluePlayers().forEach(bossBar::addPlayer);
            this.gameManager.getArena().getRedPlayers().forEach(bossBar::addPlayer);
        } else {
            this.gameManager.getBossBar().setTitle("§6" + TimerUtil.format(this.seconds));
        }
        this.seconds --;
    }
}
