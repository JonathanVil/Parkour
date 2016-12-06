package xyz.jonathanvil.parkour.game;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonathan on 12-09-2016.
 */
public class Parkour {

    private String name;

    private List<Player> players;

    private Location startLocation;

    private Map<Location, Material> blocks;
    //private List<Block> blocksn;

    public Parkour(String name, Map<Location, Material> blocks, Location startLocation) {

        this.name = name;

        this.blocks = blocks;

        this.startLocation = startLocation;

        this.players = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public void setBlocks(Map<Location, Material> blocks) {
        this.blocks = blocks;
    }

    public void addBlock(Block block) {
        blocks.put(block.getLocation(), block.getType());
    }

    public Map<Location, Material> getBlocks() {
        return blocks;
    }

}
