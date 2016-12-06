package xyz.jonathanvil.parkour.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import xyz.jonathanvil.parkour.ParkourMain;
import xyz.jonathanvil.parkour.game.Parkour;

/**
 * Lavet af Jonathan på 14-09-2016.
 */
public class SignChange implements Listener {

    @EventHandler
    public void SignChange(SignChangeEvent e) {

        if(e.getPlayer() != null) {

            if(e.getLine(0) == "[P-FINISH]") {

                Parkour parkour = ParkourMain.getInstance().getParkourManager().getParkourByName(e.getLine(1));

                if(parkour != null) {

                    Sign sign = (Sign) e.getBlock();

                    sign.setLine(1, "§d§lFINISH PARKOUR");
                    sign.setLine(2, ChatColor.DARK_PURPLE + parkour.getName());

                } else {
                    e.getPlayer().sendMessage(ParkourMain.getInstance().getPrefix() + " §cThere is no parkour by that name. Parkour names are CaSe SeNsItIvE");
                }

            }

        }

    }

}
