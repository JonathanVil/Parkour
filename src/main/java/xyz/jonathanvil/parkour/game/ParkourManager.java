package xyz.jonathanvil.parkour.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.jonathanvil.parkour.ParkourMain;

import java.util.*;

/**
 * Created by Jonathan on 12-09-2016.
 */
public class ParkourManager {

    private Set<Parkour> parkours;

    private Map<String, Integer> timer;

    public ParkourManager() {
        parkours = new HashSet<>();
        timer = new HashMap<>();
    }


    public void addParkour(Parkour parkour) {
        parkours.add(parkour);
    }

    public void removeParkour(Parkour arena) {
        parkours.remove(arena);
    }


    public String msg() {
        return "dette virker!";
    }


    public Parkour getParkourByName(String name) {
        for (Parkour parkour : parkours) {
            if (parkour.getName().equalsIgnoreCase(name)) {
                return parkour;
            }
        }

        return null;
    }

    public Parkour getParkourByPlayer(String name) {
        for (Parkour parkour : parkours) {
            if (parkour.getPlayers().contains(name)) {
                return parkour;
            }
        }

        return null;
    }

    public Parkour getParkourByStartLocation(Location location) {
        for(Parkour parkour : parkours) {
            if(parkour.getStartLocation() == location) {
                return parkour;
            }
        }
        return null;
    }

    public int getTime(String name) {
        return timer.get(name);
    }

    public void resetTimer(String name) {
        if(timer.containsKey(name)) {
            int time = timer.get(name);
            time = 0;
        }
    }

    public void startParkour(Player player, Parkour parkour) {
        player.sendMessage(ParkourMain.getInstance().getPrefix() + "§6You have started the parkour.");
        player.sendMessage(ParkourMain.getInstance().getPrefix() + "§7Your time has been reset to §60:00§6.");
    }

    public void finishParkour(Player player, Parkour parkour) {

        player.sendMessage(ParkourMain.getInstance().getPrefix() + "§7You finished the parkour! Time: §6" + formatTime(player.getName() + "§7."));
        resetTimer(player.getName());
    }



    public int getMinutes(String name) {

        return timer.get(name) / 60;

    }

    public int getSeconds(String name) {
        return timer.get(name) % 60;

    }


    public String formatTime(String name) {

        int iminutes = getMinutes(name), iseconds = getSeconds(name);
        String minutes = String.valueOf(getMinutes(name)), seconds = String.valueOf(getSeconds(name));

        if (iminutes < 10) {
            minutes = "0".concat(minutes);
        }

        if (iseconds < 10) {
            seconds = "0".concat(seconds);
        }

        return minutes + ":" + seconds;

    }

    public void addTime(String name) {
        int time = timer.get(name);
        time++;
    }

    public ArrayList<String> getPlayersInParkour() {
        ArrayList<String> players = new ArrayList<>();
        for(Parkour parkour : parkours) {
            for(Player player : parkour.getPlayers()) {
                players.add(player.getName());
            }
        }
        return players;
    }


}
