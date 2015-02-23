package server.content.quests;

import server.game.players.Client;

/*
 * Made by Vox`
 * 
 */

public class SailorinDistress {

    public static void showInformation(final Client c) {
        for (int i = 8144; i < 8195; i++) {
            c.getPA().sendFrame126("", i);
        }
        c.getPA().sendFrame126("@dre@Sailor in Distress", 8144);
        c.getPA().sendFrame126("", 8145);
        if (c.SailA == 0) {
            c.getPA().sendFrame126("Sailor in Distress", 8144);
            c.getPA().sendFrame126("I can start this quest by speaking to Owen the Sailor", 8147);
            c.getPA().sendFrame126("West of Shilo Village on the other side of the fence.", 8148);
            c.getPA().sendFrame126("", 8149);
            c.getPA().sendFrame126("Must be able to kill a level 5 goblin.", 8150);
        } else if (c.SailA == 1) {
            c.getPA().sendFrame126("Sailor in Distress", 8144);
            c.getPA().sendFrame126("I should talk with Owen about these directions.", 8147);
            c.getPA().sendFrame126("", 8148);
        } else if (c.SailA == 2) {
            c.getPA().sendFrame126("Sailor in Distress", 8144);
            c.getPA().sendFrame126("I should give Owen his directions from King Kiel.", 8147);
        } else if (c.SailA == 3) {
            c.getPA().sendFrame126("Sailor in Distress", 8144);
            c.getPA().sendFrame126("I should speak to Owen about continuing this quest.", 8147);
        } else if (c.SailA == 4) {
            c.getPA().sendFrame126("Sailor in Distress", 8144);
            c.getPA().sendFrame126("I have told the Sailor I will fetch these items for him", 8147);
            if (c.getItems().playerHasItem(2142, 10)) {
                c.getPA().sendFrame126("@str@10 cooked beef", 8148);
            } else {
                c.getPA().sendFrame126("@red@10 cooked beef", 8148);
            }
            if (c.getItems().playerHasItem(1277, 10)) {
                c.getPA().sendFrame126("@str@10 bronze swords", 8149);
            } else {
                c.getPA().sendFrame126("@red@10 bronze swords", 8149);
            }
            if (c.getItems().playerHasItem(3711, 1)) {
                c.getPA().sendFrame126("@str@A keg of beer", 8150);
            } else {
                c.getPA().sendFrame126("@red@A keg of beer", 8150);
            }
        }
        c.getPA().showInterface(8134);
    }


}