package server.content.skills;

import server.Config;
import server.content.skills.misc.SkillHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.game.players.Client;

public class Fletching extends SkillHandler {
    private Client c;

    public Fletching(Client c) {
        this.c = c;
    }

    private static final int[][] ITEM_ON_ITEM = {
            {52, 314, 53, 1, 1}, {53, 39, 882, 3, 1},
            {53, 40, 884, 4, 15}, {53, 41, 886, 6, 30},
            {53, 42, 888, 8, 45}, {53, 43, 890, 11, 60},
            {53, 44, 892, 14, 75}, {53, 11237, 11212, 16, 90},
            {314, 819, 806, 2, 1}, {314, 820, 807, 4, 22},
            {314, 821, 808, 8, 37}, {314, 822, 809, 11, 52},
            {314, 823, 810, 15, 67}, {314, 824, 811, 19, 81},
            {314, 11232, 11230, 25, 95},
    };

    public static boolean arrows(Client c, int item1, int item2) {
        for (int i = 0; i < ITEM_ON_ITEM.length; i++) {
            if ((item1 == ITEM_ON_ITEM[i][0] || item1 == ITEM_ON_ITEM[i][1]) &&
                    (item2 == ITEM_ON_ITEM[i][1] || item2 == ITEM_ON_ITEM[i][0])) {
                return true;
            }
        }
        return false;
    }

    public static void makeArrows(Client c, int item1, int item2) {
        for (int j = 0; j < ITEM_ON_ITEM.length; j++) {
            if ((item1 == ITEM_ON_ITEM[j][0] && item2 == ITEM_ON_ITEM[j][1])
                    || (item2 == ITEM_ON_ITEM[j][0] && item1 == ITEM_ON_ITEM[j][1])) {

                if (!hasRequiredLevel(c, c.playerFletching, ITEM_ON_ITEM[j][4], "fletching",
                        "make " + c.getItems().getItemName(ITEM_ON_ITEM[j][2]) + "")) {
                    return;
                }

                if (!noInventorySpace(c, "fletching")) {
                    return;
                }
                int amount1 = c.getItems().getItemAmount(item1);
                int amount2 = c.getItems().getItemAmount(item2);
                int otherAmount = 0;
                if (amount1 >= 15) {
                    amount1 = 15;
                }
                if (amount2 >= 15) {
                    amount2 = 15;
                }
                if (amount1 > amount2) {
                    otherAmount = amount1 - amount2;
                    amount1 = amount1 - otherAmount;
                } else if (amount2 > amount1) {
                    otherAmount = amount2 - amount1;
                    amount2 = amount2 - otherAmount;
                }
                int xp = 0;
                if (amount1 >= amount2) {
                    xp = amount1;
                } else {
                    xp = amount2;
                }
                if (c.getItems().playerHasItem(item1, amount1) && c.getItems().playerHasItem(item2, amount2)) {
                    c.getItems().deleteItem(item1, c.getItems().getItemSlot(item1), amount1);
                    c.getItems().deleteItem(item2, c.getItems().getItemSlot(item2), amount2);
                    c.getItems().addItem(ITEM_ON_ITEM[j][2], amount1);
                    c.getPA().addSkillXP((ITEM_ON_ITEM[j][3] * xp) * Config.FLETCHING_XP, c.playerFletching);
                }
            }
        }
    }

    public static void normal(Client c, int itemUsed, int useWith) {
        if ((itemUsed == 946 && useWith == 1511) || (itemUsed == 1151 && useWith == 946)) {
            showInterfaceFletching(c, new int[]{50, 48, 52}, 0);
        }
    }

    public static void others(Client c, int itemUsed, int useWith) {
        for (int i = 0; i < DATA.length; i++) {
            if ((itemUsed == 946 && useWith == DATA[i][0]) || (itemUsed == DATA[i][0] && useWith == 946)) {
                showInterfaceOthers(c, new int[]{DATA[i][1], DATA[i][4],}, DATA[i][7]);
            }
        }
    }

    private static int[][] DATA = {
            {1521, 54, 20, 17, 56, 25, 25, 0},
            {1519, 60, 35, 33, 58, 40, 41, 1},
            {1517, 64, 50, 50, 62, 55, 58, 2},
            {1515, 68, 65, 68, 66, 70, 75, 3},
            {1513, 72, 80, 83, 70, 85, 91, 4},
    };

    private static void showInterfaceOthers(Client c, int[] items, int type) {
        for (int i = 0; i < DATA.length; i++) {
            if (type == DATA[i][7]) {
                for (int l = 0; l < DATA.length; l++) {
                    c.playerSkillProp[9][l] = DATA[i][l];
                }
            }
        }
        c.getPA().sendFrame164(8866);
        c.getPA().sendFrame246(8869, 190, items[0]);
        c.getPA().sendFrame246(8870, 190, items[1]);
        c.getPA().sendFrame126("" + c.getItems().getItemName(items[0]) + "", 8874);
        c.getPA().sendFrame126("" + c.getItems().getItemName(items[1]) + "", 8878);
    }

    private static void showInterfaceFletching(Client c, int[] items, int type) {
        c.getPA().sendFrame164(8880);
        c.getPA().sendFrame126("What would you like to make?", 8879);
        c.getPA().sendFrame246(8884, 190, 48);
        c.getPA().sendFrame246(8883, 190, 50);
        c.getPA().sendFrame246(8885, 190, 52);
        c.getPA().sendFrame126("" + c.getItems().getItemName(50) + "", 8889);
        c.getPA().sendFrame126("" + c.getItems().getItemName(48) + "", 8893);
        c.getPA().sendFrame126("" + c.getItems().getItemName(52) + "", 8897);
        c.playerFletch = true;
    }

    public static int[][] normal = {
            {1511, 50, 48, 50, 1, 0, 6684, 52},
    };

    public static void attemptData(Client c, int amount, int type) {
        c.playerSkillProp[9][0] = normal[0][0];    //LOG
        int[][] i = {{50, 5}, {48, 10}, {52, 1},};
        c.playerSkillProp[9][1] = i[type][0];
        c.playerSkillProp[9][2] = i[type][1];
        c.playerSkillProp[9][3] = i[type][1];
        attemptData(c, amount, false);
    }

    public static void attemptData(final Client c, int amount, boolean second) {
        if (!hasRequiredLevel(c, 9, c.playerSkillProp[9][2], "fletching", "fletch this")) {
            return;
        }
        int item = c.getItems().getItemAmount(c.playerSkillProp[9][0]);
        if (amount > item) {
            amount = item;
        }
        c.doAmount = amount;
        if (c.playerSkilling[9]) {
            return;
        }
        c.playerSkilling[9] = true;
        c.stopPlayerSkill = true;
        c.getPA().removeAllWindows();

        final int itemToDelete = c.playerSkillProp[9][0];

        int itemToAdd = c.playerSkillProp[9][1];
        int xpToAdd = c.playerSkillProp[9][3];
        int xpToAdd2 = c.playerSkillProp[9][3] + 8;
        if (second) {
            itemToAdd = c.playerSkillProp[9][4];
        }

        final int addItem = itemToAdd;
        final int addXP = (xpToAdd);
        final int addXP2 = (xpToAdd2);
        /*
		final int addItem = second ? c.playerSkillProp[9][4] : c.playerSkillProp[9][1];
		final int addXP = second ? c.playerSkillProp[9][6] : c.playerSkillProp[9][3];
		*/
        c.startAnimation(1248);
        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (c.playerSkillProp[9][0] > 0) {
                    c.getItems().deleteItem(itemToDelete, c.getItems().getItemSlot(itemToDelete), 1);
                    c.getItems().addItem(addItem, c.playerSkillProp[9][1] == 52 ? 15 : 1);
                    c.sendMessage("You make a " + c.getItems().getItemName(addItem).toLowerCase() + ".");
                    if (second) {
                        c.getPA().addSkillXP(addXP2 * Config.FLETCHING_XP, 9);
                    } else {
                        c.getPA().addSkillXP(addXP * Config.FLETCHING_XP, 9);
                    }
                }
                c.startAnimation(1248);
                deleteTime(c);
                if (!c.getItems().playerHasItem(c.playerSkillProp[9][0], 1) || c.doAmount <= 0) {
                    resetFletching(c);
                    container.stop();
                }
                if (!c.stopPlayerSkill) {
                    resetFletching(c);
                    container.stop();
                }
            }

            @Override
            public void stop() {

            }
        }, 3);
    }

    public static void resetFletching(Client c) {
        c.playerSkilling[9] = false;
        c.stopPlayerSkill = false;
        c.playerFletch = false;
        for (int i = 0; i < 9; i++) {
            c.playerSkillProp[9][i] = -1;
        }
        c.startAnimation(65535);
    }
}