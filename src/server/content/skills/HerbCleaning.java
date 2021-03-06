package server.content.skills;

import server.game.players.Client;

public class HerbCleaning extends HerbData {

    public static void handleHerbCleaning(final Client c, final int itemId, final int itemSlot) {
        for (int i = 0; i < grimyHerbs.length; i++) {
            if (itemId == grimyHerbs[i][0]) {
                if (c.playerLevel[15] < grimyHerbs[i][2]) {
                    c.sendMessage("You don't have the herblore level to clean this herb.");
                    return;
                }
                c.getItems().deleteItem(itemId, itemSlot, 1);
                c.getItems().addItem(grimyHerbs[i][1], 1);
                c.getPA().addSkillXP((int) grimyHerbs[i][3] * 7, 15);
            }
        }
    }
}