package server.game.players.packets;

import core.util.Misc;
import server.game.players.Client;
import server.game.players.PacketType;
import server.game.players.PlayerHandler;


/**
 * Clicking stuff (interfaces)
 */
public class ClickingStuff implements PacketType {

    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        if (c.inTrade) {
            if (!c.acceptedTrade) {
                Misc.println("trade reset");
                c.getTradeAndDuel().declineTrade();
            }
        }

        Client o = (Client) PlayerHandler.players[c.duelingWith];
        if (o != null) {
            if (c.duelStatus >= 1 && c.duelStatus <= 4) {
                c.getTradeAndDuel().declineDuel();
                o.getTradeAndDuel().declineDuel();
            }
        }
        if (c.isBanking)
            c.isBanking = false;
        if (c.isShopping)
            c.isShopping = false;
        if (c.openDuel && c.duelStatus >= 1 && c.duelStatus <= 4) {
            if (o != null)
                o.getTradeAndDuel().declineDuel();
            c.getTradeAndDuel().declineDuel();
        }
        if (c.duelStatus == 6)
            c.getTradeAndDuel().claimStakedItems();
        if (c.inTrade) {
            if (!c.acceptedTrade) {
                c.getTradeAndDuel().declineTrade();
            }
        }
        if (c.duelStatus == 6) {
            c.getTradeAndDuel().claimStakedItems();
        }

    }

}
