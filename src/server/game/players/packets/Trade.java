package server.game.players.packets;

import server.Config;
import server.game.players.Client;
import server.game.players.PacketType;

/**
 * Trading
 */
public class Trade implements PacketType {

    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        int tradeId = c.getInStream().readSignedWordBigEndian();
        c.getPA().resetFollow();
        if (tradeId < 1)
            return;
        if (tradeId != c.playerId)
            c.getTradeAndDuel().requestTrade(tradeId);
        if (c.arenas()) {
            c.sendMessage("You can't trade inside the arena!");
            return;
        }


        if (c.playerRights == 2 && !Config.ADMIN_CAN_TRADE) {
            c.sendMessage("Trading as an admin has been disabled.");
            return;
        }

    }

}
