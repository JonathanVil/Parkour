package xyz.jonathanvil.parkour.listeners;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Material;
import xyz.jonathanvil.parkour.ParkourMain;
import xyz.jonathanvil.parkour.game.Parkour;

/**
 * Lavet af Jonathan på 14-09-2016.
 */
public class PlayerInteract implements Listener {

    @EventHandler
    public void PlayerInteract (PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK){
            if(e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) e.getClickedBlock();

                if (sign.getLine(1) == "§d§lFINISH PARKOUR") {

                    Parkour parkour = ParkourMain.getInstance().getParkourManager().getParkourByName(sign.getLine(2));

                    if (parkour != null) {

                        ParkourMain.getInstance().getParkourManager().startParkour(e.getPlayer(), parkour);

                    }

                }
                return;
        }

        } else if (e.getAction().equals(Action.PHYSICAL)) {
            if (e.getClickedBlock().getType() == Material.STONE_PLATE || e.getClickedBlock().getType() == Material.GOLD_PLATE || e.getClickedBlock().getType() == Material.IRON_PLATE || e.getClickedBlock().getType() == Material.WOOD_PLATE) {
                Parkour parkour = ParkourMain.getInstance().getParkourManager().getParkourByStartLocation(e.getClickedBlock().getLocation());

                if(parkour != null) {
                    ParkourMain.getInstance().getParkourManager().startParkour(e.getPlayer(), parkour);
                }
            }
        }
    }

}
