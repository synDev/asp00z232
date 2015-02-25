package server.content.skills;


import server.game.players.Client;

/**
 * Prayer
 *
 * @author Izumi
 */
public class Prayer {

    Client c;

    public int[][] bonesExp = {{526, 9}, {532, 26}, {3181, 26}, {534, 48}, {536, 113}, {6729, 195}, {3125, 23}, {3179, 9}};

    public Prayer(Client c) {
        this.c = c;
    }

    private int boneAmount;

    public void buryBone(int id, int slot) {
        if (System.currentTimeMillis() - c.buryDelay > 1500) {
            c.getItems().deleteItem(id, slot, 1);
            c.sendMessage("You bury the bones.");
            c.getPA().addSkillXP(getExp(id) * 7, 5);
            c.buryDelay = System.currentTimeMillis();
            c.startAnimation(827);
        }
    }

    public boolean isBone(int id) {
        for (int j = 0; j < bonesExp.length; j++)
            if (bonesExp[j][0] == id)
                return true;
        return false;
    }

    public int getExp(int id) {
        for (int j = 0; j < bonesExp.length; j++) {
            if (bonesExp[j][0] == id)
                return bonesExp[j][1];
        }
        return 0;
    }
}