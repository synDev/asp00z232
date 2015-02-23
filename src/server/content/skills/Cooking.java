package server.content.skills;

import server.clip.region.ObjectDef;
import server.content.skills.misc.SkillHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.game.players.Client;

public class Cooking extends SkillHandler {

    private static enum cookingData {

        BEEF_MEAT(2132, 2142, 2146, 1, 30, 7, true),
        RAT_MEAT(2134, 2142, 2146, 1, 30, 7, true),
        BEAR_MEAT(2136, 2142, 2146, 1, 30, 7, true),
        CHICKEN(2138, 2140, 2144, 1, 30, 7, true),
        RABBIT(3226, 3228, 7222, 1, 30, 7, true),
        UTHANKI(1859, 1861, 323, 1, 40, 7, true),
        RED_BERRY_PIE(2321, 2325, 2329, 10, 78, 15, false),
        MEAT_PIE(2317, 2327, 2329, 20, 110, 25, false),
        MUD_PIE(2319, 7170, 2329, 29, 128, 35, false),
        APPLE_PIE(7168, 2323, 2329, 30, 180, 35, false),
        GARDEN_PIE(7186, 7188, 2329, 47, 164, 52, false),
        FISH_PIE(7186, 7188, 2329, 47, 164, 52, false),
        ADMIRIAL_PIE(7196, 1798, 2329, 70, 210, 77, false),
        WILD_PIE(7206, 7208, 2329, 85, 140, 90, false),
        SUMMER_PIE(7216, 7218, 2329, 95, 260, 100, false),
        PIZZA_(2287, 2289, 2305, 35, 143, 38, true),
        CAKE(1889, 1891, 1903, 40, 180, 38, false),
        BREAD(2307, 2309, 2311, 1, 40, 5, false),
        PIITA_BREAD(1863, 1865, 1867, 58, 40, 65, true),
        SHRIMP(317, 315, 323, 1, 30, 34, true),
        SARDINE(327, 325, 369, 1, 40, 38, true),
        ANCHOVIES(321, 319, 323, 1, 30, 34, true),
        HERRING(345, 347, 357, 5, 50, 37, true),
        MACKEREL(353, 355, 357, 10, 60, 35, true),
        TROUT(335, 333, 343, 15, 70, 50, true),
        COD(341, 339, 343, 17, 75, 39, true),
        PIKE(349, 351, 343, 20, 80, 52, true),
        SALMON(331, 329, 343, 25, 90, 58, true),
        TUNA(359, 361, 367, 30, 100, 65, true),
        LOBSTER(377, 379, 381, 40, 120, 74, true),
        BASS(363, 365, 367, 43, 130, 80, true),
        SWORDFISH(371, 373, 375, 45, 140, 86, true),
        SHARK(383, 385, 387, 80, 210, 104, true),
        MANTA_RAY(389, 391, 393, 91, 216, 112, true),
        ROCKTAIL(15270, 15272, 15274, 93, 225, 120, true);

        private int rawId, cookedId, burntId, level, xp, burnStop;
        private boolean fire;

        private cookingData(final int rawId, final int cookedId, final int burntId, final int level, final int xp, final int burnStop, final boolean fire) {
            this.rawId = rawId;
            this.cookedId = cookedId;
            this.burntId = burntId;
            this.level = level;
            this.xp = xp;
            this.burnStop = burnStop;
            this.fire = fire;
        }

        public int getRaw() {
            return rawId;
        }

        public int getCooked() {
            return cookedId;
        }

        public int getBurnt() {
            return burntId;
        }

        public int getLevel() {
            return level;
        }

        public int getXP() {
            return xp;
        }

        public int getBurnStop() {
            return burnStop;
        }

        public boolean getFire() {
            return fire;
        }
    }

    private static int[][] cookingGauntlets = {
            {359, 63},
            {377, 68},
            {371, 81},
            {383, 94},
            {15270, 94},
    };

    private static int burnChance(final Client c, final int level, final int levelNeeded, final int burnStop) {
        int burnChance = 55;
        int burnFactor = (burnStop - levelNeeded);
        int levelFactor = (level - levelNeeded);
        burnChance -= (levelFactor * (burnChance / burnFactor));
        return burnChance;
    }

    public static void resetCooking(Client c) {
        c.playerSkilling[7] = false;
        c.stopPlayerSkill = false;
        for (int i = 0; i < 6; i++) {
            c.playerSkillProp[7][i] = -1;
        }
    }

    private static int burnStop;

    public static void cook(final Client c, final int itemId, final int objectId, final int objectX, final int objectY) {
        final String name = ObjectDef.getObjectDef(objectId).name;
        for (final cookingData co : cookingData.values()) {
            if (itemId == co.getRaw()) {
                if (c.playerLevel[7] < co.getLevel()) {
                    c.sendMessage("You need a cooking level of " + co.getLevel() + " to cook this meat.");
                    return;
                }
                if (!name.equalsIgnoreCase("fire") && !name.equalsIgnoreCase("stove") && !name.equalsIgnoreCase("cooking range") && !name.equalsIgnoreCase("cooking pot")) {
                    c.sendMessage("You cannot cook on a " + name.toLowerCase() + ".");
                    return;
                }
                if (name.equalsIgnoreCase("fire") && co.getFire() == false) {
                    c.sendMessage("You cannot cook a " + name.toLowerCase() + " on a fire.");
                    return;
                }
                isSkilling[7] = true;
                boolean fire;
                if (name.equalsIgnoreCase("fire")) {
                    fire = true;
                } else {
                    fire = false;
                }
                final int animation;
                if (fire == true) {
                    animation = 897;
                } else {
                    animation = 896;
                }
                burnStop = co.getBurnStop();
                if (c.playerEquipment[c.playerHands] == 775) {
                    for (int i = 0; i < cookingGauntlets.length; i++) {
                        if (itemId == cookingGauntlets[i][0]) {
                            burnStop = cookingGauntlets[i][1];
                        }
                    }
                }
                final boolean noBurn = c.playerLevel[7] >= burnStop;
                //c.startAnimation(animation);
                c.playerSkilling[7] = true;
                c.stopPlayerSkill = true;
                c.getPA().removeAllWindows();
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (!c.getItems().playerHasItem(itemId)) {
                            container.stop();
                        }
                        c.startAnimation(animation);
                        c.getItems().deleteItem(itemId, 1);
                        if (((Math.random() * 175) < burnChance(c, c.playerLevel[7], co.getLevel(), burnStop)) && noBurn == false) {
                            c.getItems().addItem(co.getBurnt(), 1);
                            c.sendMessage("You accidentally burn the " + co.toString().toLowerCase().replaceAll("_", " ") + ".");
                        } else {
                            c.getItems().addItem(co.getCooked(), 1);
                            c.getPA().addSkillXP(co.getXP() * 7, 7);
                            c.sendMessage("You successfully cook the " + co.toString().toLowerCase().replaceAll("_", " ") + ".");
                        }
                    }

                    @Override
                    public void stop() {
                        resetCooking(c);
                    }
                }, 4);
            }
        }
    }
}