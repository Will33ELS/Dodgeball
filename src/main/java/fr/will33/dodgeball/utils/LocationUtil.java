package fr.will33.dodgeball.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {

    /**
     * Recover location from string
     * @param value
     * @return
     */
    public static Location fromString(String value){
        String[] informations = value.split(",");
        return new Location(Bukkit.getWorld(informations[0]), Double.parseDouble(informations[1]), Double.parseDouble(informations[2]), Double.parseDouble(informations[3]), Float.parseFloat(informations[4]), Float.parseFloat(informations[5]));
    }

    /**
     * Recover String from location
     * @param location Instance of the location
     * @return
     */
    public static String toString(Location location){
        return String.format("%s,%s,%s,%s,%s,%s", location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

}
