package fr.will33.dodgeball.model;

import fr.will33.dodgeball.Dodgeball;
import fr.will33.dodgeball.exception.ArenaConfigurationException;
import fr.will33.dodgeball.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ArenaConfiguration {

    private Cuboid arena, blueZone, redZone;
    private final List<Location> blueSpawn = new ArrayList<>(),
            redSpawn = new ArrayList<>(),
            ballSpawn = new ArrayList<>();
    private Step step = Step.GLOBAL_ZONE;

    /**
     * Recover arena cuboid
     * @return
     */
    public Cuboid getArena() {
        return arena;
    }

    /**
     * Define arena cuboid
     * @param arena
     */
    public void setArena(Cuboid arena) {
        this.arena = arena;
    }

    /**
     * Recover blue zone
     * @return
     */
    public Cuboid getBlueZone() {
        return blueZone;
    }

    /**
     * Define blue zone
     * @param blueZone
     */
    public void setBlueZone(Cuboid blueZone) {
        this.blueZone = blueZone;
    }

    /**
     * Recover red zone
     * @return
     */
    public Cuboid getRedZone() {
        return redZone;
    }

    /**
     * Define red zone
     * @param redZone
     */
    public void setRedZone(Cuboid redZone) {
        this.redZone = redZone;
    }

    /**
     * Recover all blue spawn points
     * @return
     */
    public List<Location> getBlueSpawn() {
        return blueSpawn;
    }

    /**
     * Recover all red spawn points
     * @return
     */
    public List<Location> getRedSpawn() {
        return redSpawn;
    }

    /**
     * Recover all ball spawn points
     * @return
     */
    public List<Location> getBallSpawn() {
        return ballSpawn;
    }

    /**
     * Recover step of the configuration
     * @return
     */
    public Step getStep() {
        return step;
    }

    /**
     * Define step of the configuration
     * @param step
     */
    public void setStep(Step step) {
        this.step = step;
    }

    /**
     * Finish arena setup
     * @throws ArenaConfigurationException
     */
    public void finish() throws ArenaConfigurationException {
        if(this.arena == null || this.blueZone == null || this.redZone == null || this.blueSpawn.isEmpty() || this.redSpawn.isEmpty() || this.ballSpawn.isEmpty()){
            throw new ArenaConfigurationException(Dodgeball.getInstance().getConfig().getString("messages.config.incompleteConfig"));
        }
        FileConfiguration config = Dodgeball.getInstance().getConfig();
        config.set("arena.world", this.getArena().getPos1().getWorld().getName());
        config.set("arena.arenaZone", this.getArena().toString());
        config.set("arena.blueZone", this.getBlueZone().toString());
        config.set("arena.redZone", this.getRedZone().toString());
        config.set("arena.blueSpawnLocation", this.getBlueSpawn().stream().map(LocationUtil::toString).toList());
        config.set("arena.redSpawnLocation", this.getRedSpawn().stream().map(LocationUtil::toString).toList());
        config.set("arena.ballSpawnLocation", this.getBallSpawn().stream().map(LocationUtil::toString).toList());
        Dodgeball.getInstance().saveConfig();
        Arena arena = new Arena(this.getArena(), this.getBlueZone(), this.getRedZone());
        arena.getBlueSpawn().addAll(this.getBlueSpawn());
        arena.getRedSpawn().addAll(this.getRedSpawn());
        arena.getBallSpawn().addAll(this.getBallSpawn());
        Dodgeball.getInstance().getGameManager().setArena(arena);
    }

    public static enum Step{
        GLOBAL_ZONE, BLUE_ZONE, RED_ZONE, BLUE_SPAWN_POINTS, RED_SPAWN_POINTS, BALL_SPAWN_POINTS;
    }
}
