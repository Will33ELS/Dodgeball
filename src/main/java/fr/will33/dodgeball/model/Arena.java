package fr.will33.dodgeball.model;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    private final Cuboid cuboid, blueZone, redZone;
    private final List<Location> blueSpawn = new ArrayList<>(),
            redSpawn = new ArrayList<>(),
            ballSpawn = new ArrayList<>();
    private final List<Player> bluePlayers = new ArrayList<>(),
            redPlayers = new ArrayList<>();
    private Statut statut = Statut.LOBBY;

    public Arena(@NonNull Cuboid cuboid, @NonNull Cuboid blueZone, @NonNull Cuboid redZone) {
        Preconditions.checkNotNull(cuboid);
        Preconditions.checkNotNull(blueZone);
        Preconditions.checkNotNull(redZone);
        this.cuboid = cuboid;
        this.blueZone = blueZone;
        this.redZone = redZone;
    }

    /**
     * Recover blue spawn location
     * @return
     */
    public List<Location> getBlueSpawn() {
        return blueSpawn;
    }

    /**
     * Recover red spawn location
     * @return
     */
    public List<Location> getRedSpawn() {
        return redSpawn;
    }

    /**
     * Recover ball spawn location
     * @return
     */
    public List<Location> getBallSpawn() {
        return ballSpawn;
    }

    /**
     * Recover blue players
     * @return
     */
    public List<Player> getBluePlayers() {
        return bluePlayers;
    }

    /**
     * Recover red players
     * @return
     */
    public List<Player> getRedPlayers() {
        return redPlayers;
    }

    /**
     * Recover play zone
     * @return
     */
    public Cuboid getPlayZone() {
        return cuboid;
    }

    /**
     * Recover blue zone
     * @return
     */
    public Cuboid getBlueZone() {
        return blueZone;
    }

    /**
     * Recover red zone
     * @return
     */
    public Cuboid getRedZone() {
        return redZone;
    }

    /**
     * Get arena statut
     * @return
     */
    public Statut getStatut() {
        return statut;
    }

    /**
     * Define arena statut
     * @param statut
     */
    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public static enum Statut{
        LOBBY, INGAME
    }

}
