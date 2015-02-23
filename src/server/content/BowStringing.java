package server.content;

import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.game.players.Client;

/**
 * Class BowStringing contains the data for stringing the bow and the bow
 * string together.
 */

public class BowStringing extends StringingData {

    private static int BOW_STRING = 1777;

    /**
     * This void contains the cycle event to string the
     * unstrung bow and the bow string together
     * playingSkilling[9] = fletching,
     */

    public static void stringBow(final Client c, final int itemUsed, final int usedWith) {
        if (c.playerSkilling[9]) {
            return;
        }
        final int itemId = (itemUsed == BOW_STRING ? usedWith : itemUsed);
        for (final stringingData g : stringingData.values()) {
            if (itemId == g.unStrung()) {
                if (c.playerLevel[9] < g.getLevel()) {
                    c.sendMessage("You need a fletching level of " + g.getLevel() + " to string this bow.");
                    return;
                }
                if (!c.getItems().playerHasItem(itemId)) {
                    return;
                }
                c.playerSkilling[9] = true;
                c.startAnimation(713);
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (c.playerSkilling[9] == true) {
                            if (c.getItems().playerHasItem(itemId) && c.getItems().playerHasItem(BOW_STRING)) {
                                c.startAnimation(713);
                            } else {
                                container.stop();
                            }
                        } else {
                            container.stop();
                        }
                    }

                    @Override
                    public void stop() {
                        c.playerSkilling[9] = false;
                    }
                }, 3);
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (c.playerSkilling[9] == true) {
                            if (c.getItems().playerHasItem(itemId) && c.getItems().playerHasItem(BOW_STRING)) {
                                c.getItems().deleteItem(itemId, 1);
                                c.getItems().deleteItem(BOW_STRING, 1);
                                c.getItems().addItem(g.Strung(), 1);
                                c.getPA().addSkillXP(g.getXP() * 7, 9);
                                c.sendMessage("You attach the bow string to the " + c.getItems().getItemName(itemId).toLowerCase() + ".");
                                c.startAnimation(713);
                            } else {
                                container.stop();
                            }
                        } else {
                            container.stop();
                        }
                    }

                    @Override
                    public void stop() {
                        c.playerSkilling[9] = false;
                    }
                }, 3);
            }
        }
    }
}