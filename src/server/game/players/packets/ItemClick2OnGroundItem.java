package server.game.players.packets;

import server.game.players.Client;
import server.game.players.PacketType;

public class ItemClick2OnGroundItem implements PacketType {

    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        final int itemX = c.getInStream().readSignedWordBigEndian();
        final int itemY = c.getInStream().readSignedWordBigEndianA();
        final int itemId = c.getInStream().readUnsignedWord();
        System.out.println("ItemClick2OnGroundItem - " + c.playerName + " - " + itemId + " - " + itemX + " - " + itemY);
        c.sendMessage("Temporarily disabled, pick up and light.");
    }
}