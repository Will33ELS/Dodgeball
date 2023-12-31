package fr.will33.dodgeball.task;

import com.google.common.base.Preconditions;
import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.model.Arena;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class RespawnTask extends BukkitRunnable {

    private final Arena arena;
    private final Player player;
    private int seconds = 5;

    public RespawnTask(@NonNull Arena arena, @NonNull Player player) {
        Preconditions.checkNotNull(arena);
        Preconditions.checkNotNull(player);
        this.arena = arena;
        this.player = player;
    }

    @Override
    public void run() {
        if(this.seconds != 0){
            Optional.ofNullable(this.player).ifPresent(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eRespawn dans " + this.seconds + " seconde" + (this.seconds > 1 ? "s" : ""))));
        } else {
            this.cancel();
            if (this.player == null) return;
            this.player.setInvulnerable(true);
            if (this.arena.getBluePlayers().contains(this.player)) {
                this.player.teleport(this.arena.getBlueSpawn().get(this.arena.getBluePlayers().indexOf(this.player)));
            }
            if (this.arena.getRedPlayers().contains(this.player)) {
                this.player.teleport(this.arena.getRedSpawn().get(this.arena.getRedPlayers().indexOf(this.player)));
            }
            this.player.setGameMode(GameMode.SURVIVAL);
            Dodgeball.getInstance().getGameManager().getRespawnTasks().remove(this.player);
            Bukkit.getScheduler().runTaskLater(Dodgeball.getInstance(), () -> this.player.setInvulnerable(false), 20 * 2);
        }
        this.seconds --;
    }
}
