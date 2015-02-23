package server.game.npcs;

import server.Server;
import server.clip.region.Region;
import server.game.players.Client;

/**
 * @author DF
 */

public class PetHandler {

    public static final int RATS_NEEDED_TO_GROW = 25;

    private static enum Pets {

        KITTEN_A(1555, 761),
        KITTEN_B(1556, 762),
        KITTEN_C(1557, 763),
        KITTEN_D(1558, 764),
        KITTEN_E(1559, 765),
        KITTEN_F(1560, 766),
        CAT_A(1561, 768),
        CAT_B(1562, 769),
        CAT_C(1563, 770),
        CAT_D(1564, 771),
        CAT_E(1565, 772),
        CAT_F(1566, 773),
        HAND(4133, 1649),
        BUG(4521, 1833),
        BANSHEE(4135, 1660),
        SLIME(4520, 1661),
        PYRE(4138, 1634),
        COCKATRICE(4137, 1621),
        SLUG(4136, 1622),
        CRAWLER(4134, 1601),
        BASILISK(4139, 1617);
        private int itemId, npcId;

        private Pets(int itemId, int npcId) {
            this.itemId = itemId;
            this.npcId = npcId;
        }
    }

    public static Pets forItem(int id) {
        for (Pets t : Pets.values()) {
            if (t.itemId == id) {
                return t;
            }
        }
        return null;
    }

    public static Pets forNpc(int id) {
        for (Pets t : Pets.values()) {
            if (t.npcId == id) {
                return t;
            }
        }
        return null;
    }

    public static int getItemIdForNpcId(int npcId) {
        return forNpc(npcId).itemId;
    }

    public static boolean spawnPet(Client c, int itemId, int slot, boolean ignore) {
        Pets pets = forItem(itemId);
        if (pets != null) {
            int npcId = pets.npcId;
            if (c.hasNpc && !ignore && c.summonId < 1) {
                c.sendMessage("You already have a follower!");
                return true;
            }
            int offsetX = 0;
            int offsetY = 0;
            if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
                offsetX = -1;
            } else if (Region.getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1, 0)) {
                offsetX = 1;
            } else if (Region.getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0, -1)) {
                offsetY = -1;
            } else if (Region.getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0, 1)) {
                offsetY = 1;
            }
            Server.npcHandler.spawnNpc3(c, npcId, c.absX + offsetX, c.absY + offsetY, c.heightLevel, 0, 120, 25, 200, 200, true, false, true);
            c.getPA().followPlayer();
            if (!ignore) {
                c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
                c.hasNpc = true;
                c.summonId = itemId;
                c.SaveGame();
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean pickupPet(Client c, int npcId) {
        Pets pets = forNpc(npcId);
        if (pets != null) {
            if (Server.npcHandler.npcs[c.rememberNpcIndex].spawnedBy == c.playerId && Server.npcHandler.npcs[c.rememberNpcIndex].spawnedBy > 0) {
                int itemId = pets.itemId;
                if (c.getItems().freeSlots() > 0) {
                    Server.npcHandler.npcs[c.rememberNpcIndex].absX = 0;
                    Server.npcHandler.npcs[c.rememberNpcIndex].absY = 0;
                    Server.npcHandler.npcs[c.rememberNpcIndex] = null;
                    c.startAnimation(827);
                    c.getItems().addItem(itemId, 1);
                    c.summonId = -1;
                    c.hasNpc = false;
                    c.sendMessage("You pick up your pet.");
                } else {
                    c.sendMessage("You do not have enough inventory space to do this.");
                }
            } else {
                c.sendMessage("This is not your pet.");
            }
            return true;
        } else {
            return false;
        }
    }


}