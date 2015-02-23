package server.content.skills.crafting;

import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.game.players.Client;

public class Glass extends GlassData {

    private static int amount;

    public static void makeGlass(final Client c, final int buttonId) {
        if (c.playerBlowing) {
            return;
        }
        for (final glassData g : glassData.values()) {
            if (buttonId == g.getButtonId(buttonId)) {
                if (c.playerLevel[12] < g.getLevelReq()) {
                    c.sendMessage("You need a crafting level of "
                            + g.getLevelReq() + " to make this.");
                    c.getPA().removeAllWindows();
                    return;
                }
                if (!c.getItems().playerHasItem(1775, 1)) {
                    c.sendMessage("You have run out of molten glass.");
                    return;
                }
                c.startAnimation(884);
                c.getPA().removeAllWindows();
                c.playerBlowing = true;
                amount = g.getAmount(buttonId);
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (c.playerBlowing) {
                            if (amount == 0) {
                                container.stop();
                                return;
                            }
                            if (!c.getItems().playerHasItem(1775, 1)) {
                                c.sendMessage("You have run out of molten glass.");
                                container.stop();
                                return;
                            }
                            c.getItems().deleteItem(1775,
                                    c.getItems().getItemSlot(1775), 1);
                            c.getItems().addItem(g.getNewId(), 1);
                            c.sendMessage("You make a "
                                    + c.getItems().getItemName(g.getNewId())
                                    + ".");
                            c.getPA().addSkillXP((int) g.getXP() * 7, 12);
                            c.startAnimation(884);
                            amount--;
                        } else {
                            container.stop();
                        }
                    }

                    @Override
                    public void stop() {
                        c.startAnimation(65535);
                        c.playerBlowing = false;
                    }
                }, 5);
            }
        }
    }

    public static void makeItem(final Client c, final int itemUsed,
                                final int usedWith) {
        final int blowPipeId = (itemUsed == 1785 ? usedWith : itemUsed);
        c.getPA().showInterface(11462);
        for (final glassData g : glassData.values()) {
            if (blowPipeId == g.getNewId()) {
                c.getItems().deleteItem(1759, 1);
                c.getItems().addItem(g.getNewId(), 1);
                c.getPA().addSkillXP(4 * 7, 12);
            }
        }
    }

}