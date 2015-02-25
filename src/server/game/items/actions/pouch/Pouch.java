package server.game.items.actions.pouch;

import java.util.ArrayList;

/**
 * @author Austin Nixholm on 2/24/2015.
 */
public enum Pouch {

    SMALL_POUCH(EssencePouch.SMALL_POUCH, 3, 0, 1),
    MEDIUM_POUCH(EssencePouch.MEDIUM_POUCH, 9, 0, 25),
    LARGE_POUCH(EssencePouch.LARGE_POUCH, 18, 0, 50),
    GIANT_POUCH(EssencePouch.GIANT_POUCH, 30, 0, 75);

    private int pouchId;
    private int capacity;
    private int currentEssence;
    private int levelRequired;

    Pouch(int pouchId, int capacity, int currentEssence, int levelRequired) {
        this.pouchId = pouchId;
        this.capacity = capacity;
        this.currentEssence = currentEssence;
        this.levelRequired = levelRequired;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public void setLevelRequired(int levelRequired) {
        this.levelRequired = levelRequired;
    }

    public int getPouchId() {
        return pouchId;
    }

    public void setPouchId(int pouchId) {
        this.pouchId = pouchId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentEssence() {
        return currentEssence;
    }

    public void setCurrentEssence(int currentEssence) {
        this.currentEssence = currentEssence;
    }

}
