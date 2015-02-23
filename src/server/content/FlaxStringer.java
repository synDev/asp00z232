package server.content;

import server.Config;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.game.players.Client;

/**
 * Flax Stringer Class
 *
 * @Author Notepad/01053
 */

public class FlaxStringer {

    private Client c;

    public FlaxStringer(Client c) {
        this.c = c;
    }

    private final int FLAX[][] = {{1779, 1777, 1777, 1, 15}};
    private final int FLAXID[] = {1779};
    private final int EMOTE = 896;

    public void itemOnObject(int itemId) {
        for (int j = 0; j < FLAX.length; j++) {
            if (FLAX[j][0] == itemId)
                handleStringing(FLAX[j][0], j);
        }
    }

    public void handleStringing(int id, int slot) {
        if (c.playerLevel[12] < 1) {
            c.sendMessage("You need a Crafting level of " + FLAX[slot][3] + " to Craft this.");
            return;
        }
        c.startAnimation(EMOTE);
        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (!c.getItems().playerHasItem(1779, 1))
                    container.stop();
                if (c.getItems().playerHasItem(1779, 1)) {
                    c.getItems().deleteItem(id, c.getItems().getItemSlot(id), 1);
                    c.getItems().addItem(FLAX[slot][1], 1);
                    c.getPA().addSkillXP(FLAX[slot][4] * Config.CRAFTING_EXPERIENCE, c.playerCrafting);
                    c.startAnimation(EMOTE);
                }
            }

            @Override
            public void stop() {
                c.getPA().resetAnimation();
            }
        }, 3);
    }
}//disregard