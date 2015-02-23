package server.game.players.packets;


import core.util.Misc;
import server.game.items.UseItem;
import server.game.players.Client;
import server.game.players.PacketType;

public class ItemOnObject implements PacketType {

    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        /*
		 * a = ?
		 * b = ?
		 */

        int a = c.getInStream().readUnsignedWord();
        c.objectId = c.getInStream().readSignedWordBigEndian();
        c.objectY = c.getInStream().readSignedWordBigEndianA();
        int b = c.getInStream().readUnsignedWord();
        c.objectX = c.getInStream().readSignedWordBigEndianA();
        c.usedItemID = c.getInStream().readUnsignedWord();

        if (c.playerRights >= 3) {
            Misc.println("objectId: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: " + c.objectY + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
            c.sendMessage("objectId: " + c.objectId + " objectX: " + c.objectX + " objectY: " + c.objectY);
        }

        switch (c.objectId) {


            default:
                c.objectDistance = 1;
                c.objectXOffset = 0;
                c.objectYOffset = 0;
                break;

        }
        if (!c.getItems().playerHasItem(c.usedItemID, 1)) {
            return;
        }
        if (c.goodDistance(c.objectX + c.objectXOffset, c.objectY + c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
            c.turnPlayerTo(c.objectX, c.objectY);
            UseItem.ItemonObject(c, c.objectId, c.objectX, c.objectY, c.usedItemID);
        } else {
            c.clickObjectType = 4;
        }

    }

}