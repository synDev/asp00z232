package server.game.items.actions.pouch;

import server.game.items.Item;
import server.game.items.ItemAssistant;
import server.game.players.Client;

/**
 * @author Austin Nixholm on 2/24/2015.
 */
public class EssencePouch {

    public static final int PURE_ESSENCE = 7936;

    public static final int SMALL_POUCH = 5509;
    public static final int MEDIUM_POUCH = 5510;
    public static final int LARGE_POUCH = 5512;
    public static final int GIANT_POUCH = 5514;

    private Pouch pouch;

    public EssencePouch(Pouch pouch) {
        this.pouch = pouch;
    }

    public void addToPouch(Client c, Pouch pouch) {
        int essenceToAdd = c.getItems().getItemAmount(PURE_ESSENCE);
        int essenceInPouch = pouch.getCurrentEssence();
        if (pouch.getCurrentEssence() < pouch.getCapacity()) {
            if (c.getItems().canAddToPouch()) {
                if (essenceInPouch == 0) {
                    c.sendMessage("You do not have any essence.");
                    return;
                }
                if (essenceToAdd > pouch.getCapacity()) {
                    essenceToAdd = pouch.getCapacity();
                }
                for (int i = 0; i < essenceInPouch; i++) {
                    essenceToAdd++;
                    c.getItems().deleteItem(PURE_ESSENCE, 1);
                    if (essenceInPouch + essenceToAdd >= pouch.getCapacity())
                        return;
                }

                pouch.setCurrentEssence(essenceInPouch + essenceInPouch);
            }
        } else {
            c.sendMessage("Your pouch is full.");
            return;
        }
    }

    public Pouch getPouch() {
        return pouch;
    }


}
