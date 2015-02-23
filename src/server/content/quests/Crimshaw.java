package server.content.quests;

import server.content.music.Music;
import server.game.players.Client;

/**
 * Sheep Shearer
 *
 * @author Justin
 */

public class Crimshaw {


    public static void crimReward(Client c) {
        c.getPA().sendFrame126("You have completed Crimshaw's Debt'!", 12144);
        c.getPA().sendFrame126("" + (c.questPoints), 12147);
        c.getPA().sendFrame126("1 Quest point", 12150);
        c.getPA().sendFrame126("150 Crafting XP.", 12151);
        c.getPA().sendFrame126("60 coins.", 12152);
        c.getPA().sendFrame126("", 12153);
        c.getPA().sendFrame126("", 12154);
        c.getPA().sendFrame126("", 12155);
        c.getPlayerAssistant().sendFrame246(12145, 250, 1265);
        c.getPA().showInterface(12140);
        Music.sendQuickSong(c, 71, 23);
        c.sheepS = 2;
    }


    public static void showInformation(Client c) {
        for (int i = 8144; i < 8195; i++) {
            c.getPA().sendFrame126("", i);
        }
        c.getPA().sendFrame126("@dre@Crimshaw's Debt'", 8144);
        c.getPA().sendFrame126("", 8145);
        if (c.sheepS == 0) {
            c.getPA().sendFrame126(
                    "I can start this quest by speaking to the Odd", 8147);
            c.getPA().sendFrame126("Old Man in Shilo Village.", 8148);
            c.getPA().sendFrame126("", 8149);
            c.getPA().sendFrame126("The minimum requirements is", 8150);
            c.getPA().sendFrame126(
                    "@blu@Completion of Sailor in Distress.", 8151);
            c.getPA().sendFrame126(
                    "@blu@Prayer level of 36.", 8152);
            c.getPA().sendFrame126(
                    "@blu@Ability to defeat multiple enemies up to level 27.", 8153);
        }
        if (c.sheepS == 1) {
            c.getPA().sendFrame126("@str@I can start this quest by speaking to Fred the Farmer", 8147);
            c.getPA().sendFrame126("@str@who lives just north of Lumbridge.", 8148);
            c.getPA().sendFrame126("@xtr@There aren't any requirements for thise quest", 8149);
            c.getPA().sendFrame126("", 8150);
            c.getPA().sendFrame126("Fred has asked me to shear his sheep for him and bring", 8151);
            c.getPA().sendFrame126("him 20 balls of wool.", 8152);
            c.getPA().sendFrame126("", 8153);
            c.getPA().sendFrame126("I can find shears at the general store and a spinning wheel", 8154);
            c.getPA().sendFrame126("on the second floor of the Lumbridge castle", 8155);
        }
        if (c.sheepS == 2) {
            c.getPA().sendFrame126("@str@I can start this quest by speaking to Fred the Farmer", 8147);
            c.getPA().sendFrame126("@str@who lives just north of Lumbridge.", 8148);
            c.getPA().sendFrame126("@xtr@There aren't any requirements for thise quest", 8149);
            c.getPA().sendFrame126("", 8150);
            c.getPA().sendFrame126("@str@Fred has asked me to shear his sheep for him and bring", 8151);
            c.getPA().sendFrame126("@str@him 20 balls of wool.", 8152);
            c.getPA().sendFrame126("", 8153);
            c.getPA().sendFrame126("Quest complete!", 8154);
            c.getPA().sendFrame126("", 8155);
        }
        c.getPA().showInterface(8134);
    }

}
