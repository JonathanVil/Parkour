package xyz.jonathanvil.parkour.commands;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.jonathanvil.parkour.ParkourMain;
import xyz.jonathanvil.parkour.game.Parkour;
import xyz.jonathanvil.parkour.listeners.PlayerInteract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jonathan on 13-09-2016.
 */
public class ParkourAdminCommand implements CommandExecutor, Listener {

    HashMap<String, Parkour> addBlock = new HashMap<String, Parkour>();

    HashMap<String, Parkour> setStart = new HashMap<String, Parkour>();

    HashMap<String, Block[]> blocks = new HashMap<String, Block[]>();

    private final ParkourMain plugin = ParkourMain.getInstance();


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("parkouradmin")) {

            if (sender instanceof Player) {

                Player p = (Player) sender;

                if(!p.hasPermission("rank.DEVELOER") || !p.hasPermission("rank.OWNER")) {
                    p.sendMessage(plugin.getPrefix() + " §cThis command requires rank §7[§cDeveloper§7] §cor higher!");
                }

                if (args.length == 0) {

                    sender.sendMessage(getHelpMessage());

                } else if (args.length == 1) {

                    sender.sendMessage(getHelpMessage());

                } else if (args.length == 2) {

                    if (args[0].equalsIgnoreCase("create")) {

                        //sender.sendMessage(args[1]);

                        String parkourName = args[1];

                        if (plugin.getParkourManager().getParkourByName(parkourName) != null) {

                            sender.sendMessage(plugin.getConfig().getString("options.prefix") + " §cThis parkour already exists!");

                        } else {

                            plugin.getConfig().set("parkours." + args[1], "");
                            plugin.getConfig().set("parkours." + args[1] + ".startLocation", "NOEXIST");
                            plugin.getConfig().set("parkours." + args[1] + ".blocks.NONE.material","NONE");
                            plugin.getConfig().set("parkours." + args[1] + ".blocks.NONE.location","NONE");

                            plugin.saveConfig();

                            plugin.getParkourManager().addParkour(new Parkour(args[1], new HashMap<>(), null));

                            sender.sendMessage(plugin.chatColor(plugin.getConfig().getString("options.prefix") + " &aSuccessfully created parkour &b" + args[1]));
                            sender.sendMessage(plugin.chatColor(plugin.getConfig().getString("options.prefix") + " &cSTEP 2: &7Now set the start block with &6/parkouradmin setstart ") + args[1]);

                        }

                    } else if (args[0].equalsIgnoreCase("remove")) {

                        String parkourName = args[1].toString();

                        if (plugin.getParkourManager().getParkourByName(parkourName) != null) {

                            plugin.getConfig().set("parkours." + parkourName, null);
                            plugin.saveConfig();

                            plugin.getParkourManager().removeParkour(plugin.getParkourManager().getParkourByName(parkourName));

                            sender.sendMessage(plugin.chatColor(plugin.getConfig().getString("options.prefix") + " &aSuccessfully removed parkour &b" + parkourName));

                        } else {

                            sender.sendMessage(plugin.chatColor(plugin.getConfig().getString("options.prefix") + " &cThis parkour does not exist!"));

                        }

                    } else if (args[0].equalsIgnoreCase("setstart")) {

                        if (sender instanceof Player) {

                            Parkour parkour = plugin.getParkourManager().getParkourByName(args[1]);
                            Player player = (Player) sender;

                            if (parkour != null) {

                                sender.sendMessage(plugin.getPrefix() + " §7Now left click the block you would like to set.");

                                setStart.put(player.getName(), parkour);

                                if(setStart.containsKey(player.getName())) {
                                    p.sendMessage("hej");
                                }

                            } else {

                                sender.sendMessage(plugin.chatColor(plugin.getConfig().getString("options.prefix") + " &cThis parkour does not exist!"));

                            }

                        }

                    }

                } else if (args.length > 2) {

                    if (args[0].equalsIgnoreCase("blocks")) {

                        if (args[1].equalsIgnoreCase("edit")) {

                            if(sender instanceof Player) {

                                String parkourName = args[2].toString();
                                Player player = (Player) sender;

                                if (plugin.getParkourManager().getParkourByName(parkourName) != null) {

                                    addBlock.clear();
                                    addBlock.put(player.getName(), plugin.getParkourManager().getParkourByName(parkourName));

                                } else {

                                    sender.sendMessage(plugin.chatColor(plugin.getConfig().getString("options.prefix") + " &cThis parkour does not exist!"));

                                }
                            }

                        } else if (args[1].equalsIgnoreCase("cancel")) {

                            if(sender instanceof Player) {

                                if (addBlock.containsKey(sender.getName())) {

                                    Parkour parkour = plugin.getParkourManager().getParkourByName(args[3].toString());
                                    Player player = (Player) sender;

                                    if (parkour != null) {

                                        addBlock.remove(sender.getName());
                                        blocks.clear();
                                        sender.sendMessage(plugin.getConfig().getString("options.prefix") + " §aBlock editing has been cancelled. Nothing has been saved!");

                                    } else {

                                        sender.sendMessage(plugin.chatColor(plugin.getConfig().getString("options.prefix") + " &cThis parkour does not exist!"));

                                    }

                                }
                            }


                        } else if (args[1].equalsIgnoreCase("remove")) {

                            Parkour parkour = plugin.getParkourManager().getParkourByName(args[3].toString());

                            if (parkour != null) {

                                if (args[2] == ("ALL")) {
                                    plugin.getConfig().set("parkours." + parkour.getName() + ".blocks", null);
                                    plugin.saveConfig();
                                    sender.sendMessage(plugin.getConfig().getString("options.prefix") + " §cSuccessfully removed all blocks from the parkour §6" + parkour.getName());
                                    parkour.setBlocks(new HashMap<>());
                                } else {

                                    ConfigurationSection inventorySection = plugin.getConfig().getConfigurationSection("parkours." + parkour.getName() + ".blocks");
                                    for (String block : inventorySection.getKeys(false)) {
                                        if (plugin.getConfig().getString("parkours." + parkour.getName() + ".blocks." + block) == "Block" + args[2]) {
                                            sender.sendMessage(plugin.getConfig().getString("options.prefix") + " §cSuccessfully removed Block §6#" + args[2] + " §7from the parkour §6" + parkour.getName());
                                            plugin.getConfig().set("parkours." + parkour.getName() + ".blocks." + block, null);
                                            plugin.saveConfig();
                                            return false;
                                        }
                                    }
                                    sender.sendMessage(plugin.getConfig().getString("options.prefix") + " §cCouldn't find any registered block with the number §6" + args[2] + " §cin the parkour §6" + parkour.getName());

                                }

                            } else {

                                sender.sendMessage(plugin.chatColor(plugin.getConfig().getString("options.prefix") + " &cThis parkour does not exist!"));

                            }

                        } else if (args[1].equalsIgnoreCase("save")) {

                            if(sender instanceof Player) {

                                if (addBlock.containsKey(sender.getName())) {

                                    String parkourName = args[2].toString();
                                    Player player = (Player) sender;

                                    if (plugin.getParkourManager().getParkourByName(parkourName) != null) {

                                        Parkour parkourObj = plugin.getParkourManager().getParkourByName(parkourName);

                                        for (Block b : blocks.get(parkourObj)) {
                                            parkourObj.addBlock(b);
                                            plugin.getConfig().set("parkours." + parkourName + ".blocks." + "Block" + parkourObj.getBlocks().size() + ".location", plugin.serializeLocation(b.getLocation()));
                                            plugin.getConfig().set("parkours." + parkourName + ".blocks." + "Block" + parkourObj.getBlocks().size() + ".material", plugin.serializeMaterial(b.getType()));
                                        }
                                        plugin.saveConfig();

                                        p.sendMessage(plugin.chatColor(plugin.getConfig().getString("options.prefix") + " &aSuccessfully added §6" + parkourObj.getBlocks().size() + " §ablocks to " + parkourName));
                                    } else {

                                        sender.sendMessage(plugin.chatColor(plugin.getConfig().getString("options.prefix") + " &cThis parkour does not exist!"));

                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return false;
    }


    public String getHelpMessage() {

        StringBuilder builder = new StringBuilder("§b§m-----------§r §aParkour Commands§r §b§m-----------\n");

        builder.append("§b/parkouradmin create <name> - §aCreate a parkour.\n");
        builder.append("§b/parkouradmin remove <name> - §aRemove a parkour.\n");
        builder.append("§b/parkouradmin setstart <name> - §aSet start point for the parkour.\n");
        builder.append("§b/parkouradmin blocks edit <name> - §aBegin registering blocks to the parkour.\n");
        builder.append("§b/parkouradmin blocks save <name> - §aSave the blocks you just registered.\n");
        builder.append("§b/parkouradmin blocks cancel <name> - §aCancel registering blocks the the parkour.\n");
        builder.append("§b/parkouradmin blocks remove <BlockID:ALL> <name> - §aRemove blocks from a parkour.\n");

        return (builder.toString());

    }

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent e) {

        //e.getPlayer().sendMessage("§eInteracted");

        if(e.getAction() == Action.LEFT_CLICK_BLOCK) {

            Player player = (Player) e.getPlayer();

            //player.sendMessage("§aLeft Clicked!");

            if(setStart.get(player.getName()) != null) {
                //player.sendMessage("§b§lYOU ARE IN setStart!");

                Parkour parkour = setStart.get(player.getName());

                player.sendMessage(plugin.getPrefix() + " §aSuccessfully set spawn for §6" + parkour.getName() + " §7to §6X:" + e.getClickedBlock().getLocation().getBlockX() + " Y:" + e.getClickedBlock().getLocation().getBlockY() + " Z:" + e.getClickedBlock().getLocation().getBlockZ() + "§7.");

                plugin.getConfig().set("parkours." + parkour.getName() + ".startLocation", plugin.serializeLocation(e.getClickedBlock().getLocation()));
                plugin.saveConfig();

                parkour.setStartLocation(e.getClickedBlock().getLocation());

                setStart.remove(player.getName(), parkour);

            } else {
                //player.sendMessage("§4You are not in setStart! :(");
            }

            /*else if(addBlock.containsKey(player.getName())) {

                Parkour parkour = addBlock.get(player.getName());

                //arenaObj.addSpawn(p.getLocation());
                player.sendMessage(plugin.getPrefix() + " §aAdded block #" + parkour.getBlocks().size() + " to " + parkour.getName());
                player.sendMessage(plugin.getPrefix() + " §7Write §66/parkouradmin blocks save " + parkour.getName() + " §7when you are finished!");

                List<Block> currentLocs = (List<Block>) blocks;
                currentLocs.add(e.getClickedBlock());
            }*/

        }

    }

}
