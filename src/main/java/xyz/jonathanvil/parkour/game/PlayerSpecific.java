package xyz.jonathanvil.parkour.game;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Jonathan on 13-09-2016.
 */
public class PlayerSpecific {

    private Map<Parkour, Boolean> done;

    public PlayerSpecific(UUID uuid) {

        this.done = new HashMap<>();

    }



}
