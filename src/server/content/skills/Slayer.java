package server.game.content.skills;

import core.util.Misc;
import server.Server;
import server.game.players.Client;

/**
 * Slayer.java
 *
 * @author Sanity
 */

public class Slayer {

    private Client c;

    public Slayer(Client c) {
        this.c = c;
    }

    public static int[] lowTasks = {128, 81, 101, 1648, 1832};
    public int[] lowReqs = {1, 1, 1, 5, 7};
    public static int[] lowMidTasks = {1995, 181, 1600, 78, 117, 103, 107, 89};
    public int[] lowMidReqs = {1, 1, 10, 1, 1, 1, 1, 1};
    public static int[] medTasks = {136, 1623, 1620, 1633, 1612, 1831, 191, 178};
    public int[] medReqs = {1, 20, 25, 30, 15, 17, 1, 1};
    public static int[] HighmedTasks = {1627, 1637, 1618, 1616, 1643, 119, 58, 124, 135, 119};
    public int[] HighmedReqs = {55, 52, 50, 40, 45, 1, 1, 1, 1, 1};
    public static int[] highTasks = {3594, 2529, 416, 394, 84, 1459, 941, 1338};
    public int[] highReqs = {1, 1, 1, 1, 1, 1, 1, 1};

    public void giveTask() {
        if (c.combatLevel <= 15)
            giveTask(1);
        else if (c.combatLevel >= 16 && c.combatLevel <= 39)
            giveTask(2);
        else if (c.combatLevel >= 40 && c.combatLevel <= 60)
            giveTask(3);
        else if (c.combatLevel >= 61 && c.combatLevel <= 89)
            giveTask(4);
        else if (c.combatLevel >= 90 && c.combatLevel <= 128)
            giveTask(5);
        else
            giveTask(2);
    }

    public void giveTask(int taskLevel) {
        int given = 0;
        int random = 0;
        c.taskAmount = Misc.random(50) + 25;
        if (taskLevel == 1) {
            random = (int) (Math.random() * (lowTasks.length - 1));
            given = lowTasks[random];
        } else if (taskLevel == 2) {
            random = (int) (Math.random() * (lowMidTasks.length - 1));
            given = lowMidTasks[random];
        } else if (taskLevel == 3) {
            random = (int) (Math.random() * (medTasks.length - 1));
            given = medTasks[random];
        } else if (taskLevel == 4) {
            random = (int) (Math.random() * (HighmedTasks.length - 1));
            given = HighmedTasks[random];
        } else if (taskLevel == 5) {
            random = (int) (Math.random() * (highTasks.length - 1));
            given = highTasks[random];
        }
        if (!canDoTask(taskLevel, random)) {
            giveTask(taskLevel);
            return;
        }
        c.slayerTask = given;
        c.sendMessage("You have been assigned to kill " + c.taskAmount + " " + Server.npcHandler.getNpcListName(given) + " as a slayer task.");
    }

    public boolean canDoTask(int taskLevel, int random) {
        if (taskLevel == 1) {
            return c.playerLevel[c.playerSlayer] >= lowReqs[random];
        } else if (taskLevel == 2) {
            return c.playerLevel[c.playerSlayer] >= lowMidReqs[random];
        } else if (taskLevel == 3) {
            return c.playerLevel[c.playerSlayer] >= medReqs[random];
        } else if (taskLevel == 4) {
            return c.playerLevel[c.playerSlayer] >= HighmedReqs[random];
        } else if (taskLevel == 5) {
            return c.playerLevel[c.playerSlayer] >= highReqs[random];
        }
        return false;
    }
}