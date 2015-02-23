package server.game.minigame;

import server.Server;
import server.game.players.Client;

public class RFD {

    public static final int GOBLIN = 101, COW = 81, SCORPION = 107, MONKEY_ARCHER = 1456, LOCUST = 1995, MONKEY = 132, WIZARD = 172, SKELETON = 93, HILL_GIANT = 117, IMP = 708, TRIBESMAN = 191, GIANT_BAT = 78, CHAOS_DWARF = 119, MAMMOTH = 135, GREEN_DRAGON = 941;
    /**
     * Holds the data for the 63 waves fight cave.
     */
    private static final int[][] WAVES = {
            {COW, MONKEY},
            {GOBLIN, COW, MONKEY, IMP},
            {GOBLIN, COW, MONKEY, IMP, SCORPION},
            {LOCUST, SCORPION},
            {LOCUST, SCORPION, SCORPION},
            {TRIBESMAN, IMP},
            {TRIBESMAN, IMP, SCORPION},
            {TRIBESMAN, TRIBESMAN},
            {TRIBESMAN, TRIBESMAN, WIZARD},
            {TRIBESMAN, HILL_GIANT},
            {TRIBESMAN, TRIBESMAN, HILL_GIANT},
            {HILL_GIANT, CHAOS_DWARF},
            {HILL_GIANT, WIZARD, CHAOS_DWARF},
            {TRIBESMAN, WIZARD, HILL_GIANT, CHAOS_DWARF},
            {CHAOS_DWARF, CHAOS_DWARF},
            {CHAOS_DWARF, SKELETON},
            {MAMMOTH, WIZARD, HILL_GIANT},
            {MAMMOTH, MONKEY_ARCHER, CHAOS_DWARF},
            {MAMMOTH, SKELETON, CHAOS_DWARF, MONKEY_ARCHER},
            {MAMMOTH, MAMMOTH},
            {MAMMOTH, MAMMOTH, MONKEY_ARCHER, SKELETON},
            {MAMMOTH, MAMMOTH, CHAOS_DWARF, CHAOS_DWARF},
            {GREEN_DRAGON},
            {GREEN_DRAGON, CHAOS_DWARF},
            {GREEN_DRAGON, MAMMOTH},
    };

    private final static int[][] COORDINATES = {{2271, 4695}, {2265, 4701}, {2266, 4691}, {2274, 4688}, {2282, 4695}, {2279, 4703}, {2271, 4707}, {2262, 4697}};

    /**
     * Handles spawning the next fightcave wave.
     *
     * @param c The player.
     */
    public static void spawnNextWave(Client c) {
        if (c != null) {
            if (c.RfdwaveId >= WAVES.length) {
                c.RfdwaveId = 0;
                return;
            }
            if (c.RfdwaveId < 0) {
                return;
            }
            int npcAmount = WAVES[c.RfdwaveId].length;
            for (int j = 0; j < npcAmount; j++) {
                int npc = WAVES[c.RfdwaveId][j];
                int X = COORDINATES[j][0];
                int Y = COORDINATES[j][1];
                int H = c.heightLevel;
                int hp = getHp(npc);
                int max = getMax(npc);
                int atk = getAtk(npc);
                int def = getDef(npc);
                Server.npcHandler.spawnNpc(c, npc, X, Y, H, 0, hp, max, atk, def, true, false);
            }
            c.RfdToKill = npcAmount;
            c.RfdKilled = 0;
        }
    }

    public static int getHp(int npc) {
        switch (npc) {
            case GOBLIN:
                return 5;
            case MONKEY_ARCHER:
                return 50;
            case COW:
            case IMP:
                return 8;
            case MONKEY:
                return 6;
            case WIZARD:
                return 24;
            case LOCUST:
                return 28;
            case TRIBESMAN:
                return 32;
            case GIANT_BAT:
                return 32;
            case HILL_GIANT:
                return 35;
            case CHAOS_DWARF:
                return 62;
            case SKELETON:
                return 59;
            case MAMMOTH:
                return 130;
            case GREEN_DRAGON:
                return 85;
            case SCORPION:
                return 17;
        }
        return 5;
    }

    public static int getMax(int npc) {
        switch (npc) {
            case GOBLIN:
                return 2;
            case COW:
            case IMP:
                return 1;
            case MONKEY:
                return 1;
            case TRIBESMAN:
            case GIANT_BAT:
            case LOCUST:
                return 3;
            case CHAOS_DWARF:
                return 5;
            case WIZARD:
            case HILL_GIANT:
                return 4;
            case MAMMOTH:
            case SKELETON:
            case MONKEY_ARCHER:
                return 6;
            case GREEN_DRAGON:
                return 10;
            case SCORPION:
                return 3;
        }
        return 1;
    }

    public static int getAtk(int npc) {
        switch (npc) {
            case GOBLIN:
                return 10;
            case COW:
            case IMP:
                return 4;
            case MONKEY:
                return 6;
            case WIZARD:
            case LOCUST:
            case MONKEY_ARCHER:
                return 80;
            case TRIBESMAN:
            case GIANT_BAT:
                return 54;
            case HILL_GIANT:
                return 72;
            case CHAOS_DWARF:
                return 96;
            case SKELETON:
                return 110;
            case MAMMOTH:
                return 82;
            case GREEN_DRAGON:
                return 158;
            case SCORPION:
                return 38;
        }
        return 5;
    }

    public static int getDef(int npc) {
        switch (npc) {
            case GOBLIN:
                return 5;
            case COW:
            case IMP:
                return 2;
            case MONKEY:
                return 3;
            case WIZARD:
                return 20;
            case LOCUST:
                return 25;
            case TRIBESMAN:
            case GIANT_BAT:
                return 27;
            case CHAOS_DWARF:
            case MONKEY_ARCHER:
            case SKELETON:
                return 48;
            case HILL_GIANT:
                return 28;
            case MAMMOTH:
                return 41;
            case GREEN_DRAGON:
                return 79;
            case SCORPION:
                return 14;
        }
        return 5;
    }
}