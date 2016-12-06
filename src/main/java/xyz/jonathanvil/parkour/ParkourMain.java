package xyz.jonathanvil.parkour;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jonathanvil.parkour.commands.ParkourAdminCommand;
import xyz.jonathanvil.parkour.game.Parkour;
import xyz.jonathanvil.parkour.game.ParkourManager;
import xyz.jonathanvil.parkour.listeners.PlayerInteract;
import xyz.jonathanvil.parkour.listeners.SignChange;

import java.io.File;
import java.io.IOException;
//import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jonathan on 12-09-2016.
 */
public class ParkourMain extends JavaPlugin {

    private static ParkourMain instance;

    private ParkourManager parkourManager;

    private File configFile;
    private FileConfiguration config;

    /*
    private Connection connection;
    private String host, database, username, password;
    private int port;
    */

    @Override
    public void onEnable() {

        getLogger().info("Parkour by JonathanVil has been enabled!");
        getLogger().info("This plugin is the property of CloudyMC/Jonathan Villeret");
        getLogger().info("Developer Website: www.jonathanvil.xyz");

        instance = this;

        parkourManager = new ParkourManager();

        registerListeners(new PlayerInteract(), new SignChange(), new ParkourAdminCommand());
        registerCommands();

        loadConfig();

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run(){
                for(String name : getParkourManager().getPlayersInParkour()) {

                    getParkourManager().addTime(name);
                }
            }
        }, 0, 20);

    }

    public void onDisable() {

        getLogger().info("Parkour by JonathanVil has been disabled!");
        getLogger().info("This plugin is the property of CloudyMC/Jonathan Villeret");
        getLogger().info("Developer Website: www.jonathanvil.xyz");

    }


    private void registerCommands() {
        getCommand("parkouradmin").setExecutor(new ParkourAdminCommand());
    }

    private void registerListeners(Listener... listeners) {

        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

    }

    public static ParkourMain getInstance() {
        return instance;
    }

    public ParkourManager getParkourManager() {
        return parkourManager;
    }

    private void loadConfig() {

        if (!getDataFolder().exists()) {

            getDataFolder().mkdir();

        }

        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        if (!config.contains("options.prefix")) {
            config.set("options.prefix", "§9Parkour §8§l»§7");
        }

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadParkours();

    }

    private void loadParkours() {

        if(config.contains("parkours")) {
            if (!config.getString("parkours").equals("")) {

                for (String parkour : config.getConfigurationSection("parkours").getKeys(false)) {

                    HashMap<Location, Material> blocks = new HashMap<Location, Material>();
                    //ArrayList<String> blocks = new ArrayList<String>();

                    if (getConfig().getConfigurationSection("parkours." + parkour + ".blocks") != null && !getConfig().getConfigurationSection("parkours." + parkour + ".blocks").equals("")) {
                        ConfigurationSection inventorySection = getConfig().getConfigurationSection("parkours." + parkour + ".blocks");
                        for (String block : inventorySection.getKeys(false)) {

                            if (!config.getString("parkours." + parkour + ".blocks." + block + ".location").equalsIgnoreCase("NONE") && !config.getString("parkours." + parkour + ".blocks." + block + ".material").equalsIgnoreCase("NONE")) {

                                blocks.put(unserializeLocation("parkours." + parkour + ".blocks." + block + ".location"), Material.matchMaterial("parkours." + parkour + ".blocks." + block + ".material"));
                                //blocks.add("parkours." + parkour + ".blocks." + block + ".material");
                            }
                        }
                    }

                    if (!config.getString("parkours." + parkour + ".startLocation").equalsIgnoreCase("NOEXIST")) {

                        getParkourManager().addParkour(new Parkour(parkour,
                                blocks,
                                unserializeLocation(config.getString("parkours." + parkour + ".startLocation"))));
                        blocks.clear();

                    } else {

                        getParkourManager().addParkour(new Parkour(parkour,
                                blocks,
                                (null)));
                        blocks.clear();
                    }
                }

            } else {
                getLogger().warning("There is no parkours defined.");
            }

        }

    }


    public String serializeLocation(Location loc) {
        return loc.getWorld().getName() + "=" + loc.getX() + "=" + loc.getY() + "=" + loc.getZ() + "=" + loc.getPitch() + "=" + loc.getYaw();
    }

    public Location unserializeLocation(String loc) {

        if (loc.equals("NOEXIST")) {
            return null;
        }

        String[] split = loc.split("=");
        return new Location(getServer().getWorld(split[0]),
                Double.parseDouble(split[1]), Double.parseDouble(split[2]),
                Double.parseDouble(split[3]), Float.parseFloat(split[4]),
                Float.parseFloat(split[5]));
    }

    public String serializeMaterial(Material mat) {
        return mat.name();
    }

    public Material unserializeMaterial(String mat) {

        if (mat.equals("NOEXIST")) {
            return null;
        }

        return Material.valueOf(mat);
    }

    public List<Location> listStrToLocs(List<String> locs) {

        List<Location> toReturn = new ArrayList<>();

        locs.stream().forEach(loc -> toReturn.add(unserializeLocation(loc)));

        return toReturn;

    }

    public String chatColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPrefix() {
        return getConfig().getString("options.prefix");
    }

    /*
    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }
    */

    public void setBlocks() {

        for (String parkour : config.getConfigurationSection("parkours").getKeys(false)) {

            ConfigurationSection inventorySection = getConfig().getConfigurationSection("parkours." + parkour + ".blocks");
            for (String block : inventorySection.getKeys(false)) {
                Location loc = unserializeLocation("parkours." + parkour + ".blocks." + block + ".location");
                Material mat = Material.matchMaterial("parkours." + parkour + ".blocks." + block + ".material");

                if(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).getType() != mat) {

                    loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).setType(mat);

                }
            }
        }
    }

}
