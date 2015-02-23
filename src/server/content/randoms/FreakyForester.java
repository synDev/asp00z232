package server.content.randoms;

import core.util.Misc;
import server.Server;
import server.game.players.Client;

/**
 * @author someotherdude and Darkside
 */
public class FreakyForester {
    public static int forester = 2458;
    public static int[][] coords = {
            {2596, 4780},
    };
    public static int ledhosentop = 6180;
    public static int lederhosenshorts = 6181;
    public static int lederhosenhat = 6182;


    /**
     * Spawns the freaky forester. Start of randomevent
     *
     * @param c
     */
    public static void spawnForester(Client c) {
        Server.npcHandler.spawnNpc(c, forester, c.absX + Misc.random(1), c.absY
                        + Misc.random(1), c.heightLevel, 0, 0, 0, 0, 0, false,
                true);
        c.lastX = c.absX;
        c.lastY = c.absY;
        c.lastHeight = c.heightLevel;
    }

}
