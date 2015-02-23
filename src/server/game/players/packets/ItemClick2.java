package server.game.players.packets;

import core.util.Misc;
import server.game.players.Client;
import server.game.players.PacketType;

/**
 * Item Click 2 Or Alternative Item Option 1
 *
 * @author Ryan / Lmctruck30
 *         <p>
 *         Proper Streams
 */

public class ItemClick2 implements PacketType {

    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        int itemId = c.getInStream().readSignedWordA();

        if (!c.getItems().playerHasItem(itemId, 1))
            return;

        switch (itemId) {

            default:
                if (c.playerRights == 3)
                    Misc.println(c.playerName + " - Item3rdOption: " + itemId);
                break;
        }

    }

}
