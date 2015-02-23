package server.content;

/**
 * Class Stringing Data Contains the data for stringing.
 */

public class StringingData {

    /**
     * This enum contains the stringing data.
     * Unstrung ID, Strung ID, Level required, XP, Animation
     */

    public static enum stringingData {
        SHORT_BOW(50, 841, 5, 5, 6678),
        LONG_BOW(48, 839, 10, 10, 6684),
        OAK_SHORT_BOW(54, 843, 20, 17, 6679),
        OAK_LONG_BOW(56, 845, 25, 25, 6685),
        COMPOSITE_BOW(4825, 4827, 30, 45, 6686),
        WILLOW_SHORT_BOW(60, 849, 35, 33, 6680),
        WILLOW_LONG_BOW(58, 847, 40, 42, 6686),
        MAPLE_SHORT_BOW(64, 853, 50, 50, 6681),
        MAPLE_LONG_BOW(62, 851, 55, 58, 6687),
        YEW_SHORT_BOW(68, 857, 65, 69, 6682),
        YEW_LONG_BOW(66, 855, 70, 75, 6688),
        MAGIC_SHORT_BOW(72, 861, 80, 83, 6683),
        MAGIC_LONG_BOW(70, 859, 85, 92, 6689);

        private int unstrung, strung, level, animation;
        private int xp;

        private stringingData(final int unstrung, final int strung, final int level, final int xp, final int animation) {
            this.unstrung = unstrung;
            this.strung = strung;
            this.level = level;
            this.xp = xp;
            this.animation = animation;
        }

        public int unStrung() {
            return unstrung;
        }

        public int Strung() {
            return strung;
        }

        public int getLevel() {
            return level;
        }

        public int getXP() {
            return xp;
        }

        public int getAnimation() {
            return animation;
        }
    }
}