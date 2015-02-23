package server.game.players;

import core.util.Misc;

import java.util.ArrayList;

/**
 * Flaxpicking. OUTDATED
 *
 * @author Acquittal
 */
public class Flax {

    public static ArrayList<int[]> flaxRemoved = new ArrayList<int[]>();

    public static void pickFlax(final Client c, final int x, final int y) {
        if (c.getItems().freeSlots() != 0) {
            c.getItems().addItem(1779, 1);
            c.startAnimation(827);
            c.sendMessage("You pick some flax.");
            if (Misc.random(3) == 1) {
                //ew Object(-1, x, y, 0, 1, 10, 2646, 10);
            }
        } else {
            c.sendMessage("Not enough space in your inventory.");
            return;
        }

    }
}