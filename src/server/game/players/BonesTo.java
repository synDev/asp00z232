package server.game.players;


/**
 * @author S Biggie M
 */
public class BonesTo {

    static int itemId;
    static int fromSlot;

    public static void handleBones(Client c, int actionButtonId) {
        switch (actionButtonId) {
            case 4135:
                if (c.playerLevel[c.playerMagic] >= 15) {
                    if (c.getItems().playerHasItem(526, 1)) {
                        if (!c.getItems().playerHasItem(557, 2) || !c.getItems().playerHasItem(561, 1) || !c.getItems().playerHasItem(555, 2)) {
                            c.getItems().deleteItem(557, 2);
                            c.getItems().deleteItem(561, 1);
                            c.getItems().deleteItem(555, 2);
                            c.getPA().addSkillXP(25 * 7, c.playerMagic);
                            c.startAnimation(722);
                            c.gfx100(141);
                            do {
                                c.getItems().deleteItem(526, 1);
                                c.getItems().addItem(1963, 1);
                            } while (c.getItems().addItem(1963, 1));
                            c.getPA().sendFrame106(6);
                        } else {
                            c.sendMessage("You do not have the correct runes to cast this spell.");
                        }
                    } else {
                        c.sendMessage("You don't have any bones!");
                    }
                } else {
                    c.sendMessage("You need a magic level of at least 15 to cast bones to bananas.");
                }
                break;


            case 62005:
                if (c.playerLevel[c.playerMagic] >= 60) {
                    if (c.getItems().playerHasItem(526, 1)) {
                        if (!c.getItems().playerHasItem(557, 4) || !c.getItems().playerHasItem(561, 2) || !c.getItems().playerHasItem(555, 4)) {
                            c.getItems().deleteItem(557, 4);
                            c.getItems().deleteItem(561, 2);
                            c.getItems().deleteItem(555, 4);
                            c.getPA().addSkillXP(35 * 7, c.playerMagic);
                            c.startAnimation(722);
                            c.gfx100(311);
                            do {
                                c.getItems().deleteItem(526, 1);
                                c.getItems().addItem(6883, 1);
                            } while (c.getItems().addItem(6883, 1));
                            c.getPA().sendFrame106(6);
                        } else {
                            c.sendMessage("You do not have the correct runes to cast this spell.");
                        }
                    } else {
                        c.sendMessage("You don't have any bones!");
                    }
                } else {
                    c.sendMessage("You need a magic level of at least 60 to cast bones to peaches.");
                }
                break;
        }
    }
}