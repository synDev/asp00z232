package server.game.players.packets;

import core.util.Misc;
import server.Server;
import server.game.players.Client;
import server.game.players.PacketType;

public class ItemOnPlayer implements PacketType {
    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        int playerId = c.inStream.readUnsignedWord();
        int itemId = c.playerItems[c.inStream.readSignedWordBigEndian()] - 1;
        switch (itemId) {
            case 962:
                Client usedOn = (Client) Server.playerHandler.players[playerId];
                if (c.goodDistance(usedOn.getX(), usedOn.getY(), c.getX(), c.getY(), 1)) {
                    handleCrackers(c, itemId, playerId);
                    c.gfx0(176);
                    c.startAnimation(451);
                }
                break;
            default:
                c.sendMessage("Nothing interesting happens.");
                break;
        }
    }

    private void handleCrackers(Client c, int itemId, int playerId) {
        Client usedOn = (Client) Server.playerHandler.players[playerId];
        c.turnPlayerTo(usedOn.absX, usedOn.absY);
        if (!c.getItems().playerHasItem(itemId))
            return;

        if (usedOn.getItems().freeSlots() < 1) {
            c.sendMessage("The other player doesn't have enough inventory space!");
            return;
        }
        c.sendMessage("You crack the cracker...");
        c.getItems().deleteItem(itemId, 1);

        if (Misc.random(1) == 0) {
            c.getItems().addItem(getRandomPhat(), 1);
            c.forcedChat("Hey! I got the Cracker!");
            c.sendMessage("You got the prize!");
            usedOn.getItems().addItem(getLoser(), 1);
            usedOn.sendMessage("You didn't get the prize.");
        } else {
            usedOn.getItems().addItem(getRandomPhat(), 1);
            usedOn.forcedChat("Hey! I got the Cracker!");
            usedOn.sendMessage("You got the prize!");
            c.sendMessage("You didn't get the prize.");
            c.getItems().addItem(getLoser(), 1);
        }
    }

    private int getLoser() {
        int[] loser = {1071, 1085, 1109, 1121, 1143, 1159, 1181, 1197, 1329};
        return loser[(int) Math.floor(Math.random() * loser.length)];
    }

    private int getRandomPhat() {
        int[] phats = {1038, 1040, 1042, 1044, 1048};
        return phats[(int) Math.floor(Math.random() * phats.length)];
    }
}