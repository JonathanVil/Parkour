package xyz.jonathanvil.parkour.game;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.jonathanvil.parkour.ParkourMain;

import java.util.List;
import java.util.UUID;

/**
 * Created by Jonathan on 12-09-2016.
 */
public class Timer extends BukkitRunnable {

    private Parkour parkour;

    private int playertime;
    private int cooldown;

    private UUID uuid;

    private boolean paused;

    public Timer(UUID uuid) {

        this.parkour = parkour;

        this.playertime = 0;
        this.cooldown = 0;

        this.uuid = uuid;

        this.paused = true;

        this.runTaskTimer(ParkourMain.getPlugin(ParkourMain.class), 0, 20);

    }

    @Override
    public void run() {

        if(cooldown > 0) {
            cooldown--;
        }

        if(!paused) {

            playertime++;

        }

    }

    public int getPlayertime() {
        return playertime;
    }


    public void resetPlayertime() {
        playertime = 0;
    }

    public void resetCooldown() {
        cooldown = 0;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setPlayerTimePaused(boolean paused) {
        paused = paused;
    }

}
