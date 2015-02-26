package server.game.players;

import core.util.Misc;
import core.util.Stream;
import server.Config;
import server.content.BankPins;
import server.event.Event;
import server.event.EventContainer;
import server.event.EventManager;
import server.game.items.Item;
import server.game.npcs.NPC;
import server.game.npcs.NPCHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Player extends Mobile {
    public abstract void updateWalkEntities();

    public boolean hasNpc = false;
    public int summonId;
    public int rememberNpcIndex;
    // Report stuff
    /**
     * System to optimize sendFrame126 performance.
     *
     * @author MikeRSPS
     * UltimateScape
     * http://ultimatescape2.com
     */
    private Map<Integer, TinterfaceText> interfaceText = new HashMap<Integer, TinterfaceText>();
    public int recoilHits = 0;

    public class TinterfaceText {
        public int id;
        public String currentState;

        public TinterfaceText(String s, int id) {
            this.currentState = s;
            this.id = id;
        }

    }

    public static boolean playerBlowing;

    public boolean checkPacket126Update(String text, int id) {
        if (!interfaceText.containsKey(id)) {
            interfaceText.put(id, new TinterfaceText(text, id));
        } else {
            TinterfaceText t = interfaceText.get(id);
            if (text.equals(t.currentState)) {
                return false;
            }
            t.currentState = text;
        }
        return true;
    }

    public int[][] barrowsNpcs = {
            {2030, 0}, //verac
            {2029, 0}, //toarg
            {2028, 0}, // karil
            {2027, 0}, // guthan
            {2026, 0}, // dharok
            {2025, 0} // ahrim
    };
    public int barrowsKillCount;

    public boolean inBarrows() {
        if (absX > 3520 && absX < 3598 && absY > 9653 && absY < 9750 ||
                absX >= 3540 && absX <= 3587 && absY >= 3264 && absY <= 3312) {
            return true;
        }
        return false;
    }

    public int usedItemID;
    public boolean playerFletch;
    public int RfdPoints = 0;
    public boolean walkingPath = false;
    public int tzhaarToKill = 0, tzhaarKilled = 0, tzKekSpawn = 0, tzKekTimer = 0, waveId, tzhaarNpcs;
    public int RfdToKill = 0, RfdKilled = 0, RfdwaveId, RfdNpcs;
    public long lastRequest;
    public static long lastHome = 0;
    public long lastReport = 0;
    public static int[] dungeonTasks = {1648, 1832, 1600, 1623, 1620, 1633, 1612, 1831, 1616, 1643, 1618, 1637, 1627, 1604, 1624, 1608, 1610, 3068, 1613, 1615};
    String lastReported = "";
    public ArrayList<String> lastConnectedFrom = new ArrayList<String>();
    public ArrayList<Integer> addPlayerList = new ArrayList<Integer>();
    ArrayList<String> killedPlayers = new ArrayList<String>();
    ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();
    public boolean openDuel = false;
    boolean ghostSpawned = false;
    public boolean canDoStuff = true;
    public boolean unfired, fired;
    public int breadID;
    public boolean rareOn = false;
    public long lastButton, lastObjectClick;
    public boolean slayerMaskEffect;
    public int totalLevel;
    int xpTotal;
    public int itemOnNpcItemId, itemOnNpcItemSlot;
    public long lastTeleport;
    public long miscTimer;
    public BankPins.State state = BankPins.State.ONE;
    public int slayerTask, taskAmount;
    public String enteredPin = "";
    public int previousDamage = 0;
    public String fullPin = "";
    public long lastClick;
    public int lastX;
    public int pLastX, pLastY, pLastH;
    public double weight = 0.0;
    public int lastY;
    public int lastHeight;
    public int selectedSkill;
    int bananalimit;
    public int chatDialogue = 0;
    public boolean isFullBody = false;
    public boolean isFullHelm = false;
    public boolean isFullMask = false;
    public boolean attackOthers = false;
    public boolean xpLock;
    public boolean splitChat;
    public boolean walkingtoObject = false;
    public int lastWalkableInterface = 0;
    public int brightness = 2;
    public long lastThieve;
    public boolean playerStun = false;
    public boolean cantClimbLadder, playerisSmelting, stopPlayerPacket = false,
            ratdied2 = false, playerIsFiremaking, playerIsWoodcutting,
            isUsingSpecial, stopPlayerSkill, isNpc = false,
            initialized = false, disconnected = false, ruleAgreeButton = false,
            RebuildNPCList = false, isActive = false, isSkulled = false,
            friendUpdate = false, newPlayer = false, hasMultiSign = false,
            saveCharacter = false, mouseButton = false, chatEffects = true,
            acceptAid = false, autocasting = false, mageFollow = false,
            dbowSpec = false, properLogout = false, maxNextHit = false,
            ssSpec = false, vengOn = false, addStarter = false,
            accountFlagged = false, msbSpec = false,
            guildDoor, moltenglass = false, hardbodies = false, spinningPlate, soundEnabled = false;
    // random events
    public boolean hasMimeMask, hasMimeGloves, hasMimeTop, hasMimePants,
            hasMimeBoots, hasLederhosenTop, hasLederhosenHat,
            hasLederhosenPants, hasCamoTop, hasCamoPants, hasCamoHat,
            hasPrinceTunic, hasfrogMask, hasPrinceLegs, hasPrincessBlouse,
            hasPrincessSkirt;
    public int tutorialprog;
    public boolean isBanking = false;
    public int log = -1;
    public int Cookstage1 = 1;
    public int tradeTime;
    public boolean aubury = false;
    public int chatDialogueAction;
    public boolean sedridor = false;
    public String bankPin = "";
    public int attempts = 3;
    public boolean setPin = false;
    public boolean DSOption, DSOption2, DSOption3, DSOption4, DSOption5, DSOption6, DSOption7,
            DSOption8, captainRovin, boneSearch, trailborn, prysin,
            incantationOption;
    public int leatherType = -1, SailA, demonS, Incantation, ernestC, goblinD, piratesT,
            princeR, npcDroppingItems, romeoJ, randomCoffin, shieldA, cooksA, dragSlayer, impC, knightS,
            restlessG, runeM, sheepS, Crim, lastGrab = 0, witchspot, vampireslay,
            lastLoginDate = 0, pTime, npcId2 = 0, woodcuttingTree, miningRock, doAmount,
            playerKilled, totalPlayerDamageDealt, killedBy, lastChatId = 1,
            privateChat, dialogueId, newLocation, specEffect, specBarId,
            runecraftingLevelReq, attackLevelReq, defenceLevelReq,
            strengthLevelReq, rangeLevelReq, magicLevelReq, slayerLevelReq, prayerLevelReq, followId,
            skullTimer, nextChat = 0, talkingNpc = -1, slPoints, dialogueAction = 0,
            autocastId, followDistance, followId2, barrageCount = 0,
            delayedDamage = 0, delayedDamage2 = 0, pcPoints = 0,
            magePoints = 0, lastArrowUsed = -1, autoRet = 0, pcDamage = 0,
            xInterfaceId = 0, xRemoveId = 0, xRemoveSlot = 0, frozenBy = 0,
            poisonDamage = 0, teleAction = 0, bonusAttack = 0,
            lastNpcAttacked = 0, luthas = 0, bananacrate = 0,

    doricsQ = 0, teleGrabItem, teleGrabX, teleGrabY, duelCount,
            underAttackBy, underAttackBy2, wildLevel, teleTimer, respawnTimer,
            saveTimer = 0, teleBlockLength, grain = 0, flourAmount = 0;
    public boolean slayerHelmAffect;
    public boolean storing = false;
    public int[] voidStatus = new int[5];
    public int[] itemKeptId = new int[4];
    public int[] pouches = new int[4];
    public int[][] playerSkillProp = new int[20][15];
    public boolean[] playerSkilling = new boolean[20];
    public long lastFire;
    public final int[] POUCH_SIZE = {3, 6, 9, 12};
    public boolean[] invSlot = new boolean[28], equipSlot = new boolean[14];
    public long friends[] = new long[200];
    public double specAmount = 0;
    public double specAccuracy = 1;
    public double specDamage = 1;
    public double prayerPoint = 1.0;
    public long lastPoisonSip, poisonImmune, lastSpear, lastProtItem, dfsDelay,
            lastVeng, teleGrabDelay, protMageDelay, protMeleeDelay,
            protRangeDelay, lastLockPick, alchDelay, specDelay = System
            .currentTimeMillis(), duelDelay, teleBlockDelay,
            godSpellDelay, singleCombatDelay, singleCombatDelay2, reduceStat,
            restoreStatsDelay, logoutDelay, buryDelay, foodDelay, potDelay;
    public boolean canChangeAppearance = false;
    public boolean mageAllowed;
    public byte poisonMask = 0;
    public int focusPointX = -1, focusPointY = -1;
    public int questPoints = 0;

    public int DirectionCount = 0;
    public boolean appearanceUpdateRequired = true;
    public int hitDiff2;
    public int hitDiff = 0;
    public boolean hitUpdateRequired2;
    public boolean hitUpdateRequired = false;
    public boolean isDead = false, invisible;

    /*
     * Custom global Dueling minigame stuff.
     */
    public int duelType(String type) {
        if (type.equals("Safe"))
            return 0;
        else if (type.equals("Dangerous"))
            return 1;
        return -1;
    }

    public int duelWith;
    public boolean inDuel, duelAccepted; // duelrequested


    /**
     * Random event varibles
     */
    public int entTimer, mimeTimer = 0, failure, mimeEmote, mimeevent, fishNPC, whirlTime, expGained, mimeSuccess, mimeFail;
    public boolean pgotLamp = false, talkedto = false, whirlpool, fishingWhirlPool, curMimeEmote;

    /*
     * End*
     */
    public void activateTradeTimer() {
        EventManager.getSingleton().addEvent(new Event() {
            public void execute(EventContainer e) {
                if (tradeTime > 0)
                    tradeTime--;
                if (tradeTime <= 0)
                    e.stop();
            }
        }, 0);
    }

    public final int[] BOWS = {837, 767, 839, 845, 847, 851, 855, 859, 841, 843,
            849, 853, 857, 861, 4212, 4214, 4215, 11235, 4216, 4217, 4218,
            4219, 4220, 4221, 4222, 4223, 6724, 4734, 4934, 4935, 4936, 4937};
    public final int[] ARROWS = {882, 884, 886, 888, 890, 892, 78, 4160, 4740, 11212,
            9140, 9141, 4142, 9143, 9144, 9240, 9241, 9242, 9243, 9244, 9245};
    public final int[] NO_ARROW_DROP = {4212, 4214, 4215, 4216, 4217, 4218,
            4219, 4220, 4221, 4222, 4223, 4734, 4934, 4935, 4936, 4937};
    public final int[] OTHER_RANGE_WEAPONS = {863, 864, 865, 866, 867, 868,
            869, 806, 807, 808, 809, 810, 811, 825, 826, 827, 828, 829, 830,
            800, 801, 802, 803, 804, 805, 6522};

    public final int[][] MAGIC_SPELLS = {

            // Modern Spells
            {1152, 1, 711, 90, 91, 92, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0}, //wind strike
            {1154, 5, 711, 93, 94, 95, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0}, // water strike
            {1156, 9, 711, 96, 97, 98, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0},// earth strike
            {1158, 13, 711, 99, 100, 101, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0}, // fire strike
            {1160, 17, 711, 117, 118, 119, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0}, // wind bolt
            {1163, 23, 711, 120, 121, 122, 10, 16, 556, 2, 555, 2, 562, 1, 0, 0}, // water bolt
            {1166, 29, 711, 123, 124, 125, 11, 20, 556, 2, 557, 3, 562, 1, 0, 0}, // earth bolt
            {1169, 35, 711, 126, 127, 128, 12, 22, 556, 3, 554, 4, 562, 1, 0, 0}, // fire bolt
            {1172, 41, 711, 132, 133, 134, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0}, // wind blast
            {1175, 47, 711, 135, 136, 137, 14, 28, 556, 3, 555, 3, 560, 1, 0, 0}, // water blast
            {1177, 53, 711, 138, 139, 140, 15, 31, 556, 3, 557, 4, 560, 1, 0, 0}, // earth blast
            {1181, 59, 711, 129, 130, 131, 16, 35, 556, 4, 554, 5, 560, 1, 0, 0}, // fire blast
            {1183, 62, 727, 158, 159, 160, 17, 36, 556, 5, 565, 1, 0, 0, 0, 0}, // wind wave
            {1185, 65, 727, 161, 162, 163, 18, 37, 556, 5, 555, 7, 565, 1, 0, 0},  // water wave
            {1188, 70, 727, 164, 165, 166, 19, 40, 556, 5, 557, 7, 565, 1, 0, 0}, // earth wave
            {1189, 75, 727, 155, 156, 157, 20, 42, 556, 5, 554, 7, 565, 1, 0, 0}, // fire wave
            {1153, 3, 716, 102, 103, 104, 0, 91, 555, 3, 557, 2, 559, 1, 0, 0},  // confuse
            {1157, 11, 716, 105, 106, 107, 0, 140, 555, 3, 557, 2, 559, 1, 0, 0},  // weaken
            {1161, 19, 716, 108, 109, 110, 0, 203, 555, 2, 557, 3, 559, 1, 0, 0}, // curse
            {1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0}, // vulnerability
            {1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0}, // enfeeble
            {1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0, 0},  // stun
            {1572, 20, 710, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0}, // bind
            {1582, 50, 710, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0}, // snare
            {1592, 79, 710, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0}, // entangle
            {1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0, 0},  // crumble undead
            {1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0}, // iban blast
            {12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0, 0}, // magic dart
            {1190, 60, 811, 0, 0, 76, 20, 60, 554, 2, 565, 2, 556, 4, 0, 0}, // sara strike
            {1191, 60, 811, 0, 0, 77, 20, 60, 554, 1, 565, 2, 556, 4, 0, 0}, // cause of guthix
            {1192, 60, 811, 0, 0, 78, 20, 60, 554, 4, 565, 2, 556, 1, 0, 0}, // flames of zammy
            {12445, 85, 1819, 0, 344, 345, 0, 65, 563, 1, 562, 1, 560, 1, 0, 0}, // teleblock

            // Ancient Spells
            {12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1, 556, 1}, // smoke rush
            {12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1, 556, 1}, // shadow rush
            {12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0},  // blood rush
            {12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0, 0},  // ice rush
            {12963, 62, 1979, 0, 0, 389, 19, 36, 560, 2, 562, 4, 556, 2, 554, 2}, // smoke burst
            {13011, 64, 1979, 0, 0, 382, 20, 37, 560, 2, 562, 4, 556, 2, 566, 2}, // shadow burst
            {12919, 68, 1979, 0, 0, 376, 21, 39, 560, 2, 562, 4, 565, 2, 0, 0},  // blood burst
            {12881, 70, 1979, 0, 0, 363, 22, 40, 560, 2, 562, 4, 555, 4, 0, 0}, // ice burst
            {12951, 74, 1978, 0, 386, 387, 23, 42, 560, 2, 554, 2, 565, 2, 556, 2}, // smoke blitz
            {12999, 76, 1978, 0, 380, 381, 24, 43, 560, 2, 565, 2, 556, 2, 566, 2}, // shadow blitz
            {12911, 80, 1978, 0, 374, 375, 25, 45, 560, 2, 565, 4, 0, 0, 0, 0}, // blood blitz
            {12871, 82, 1978, 366, 0, 367, 26, 46, 560, 2, 565, 2, 555, 3, 0, 0}, // ice blitz
            {12975, 86, 1979, 0, 0, 391, 27, 48, 560, 4, 565, 2, 556, 4, 554, 4}, // smoke barrage
            {13023, 88, 1979, 0, 0, 383, 28, 49, 560, 4, 565, 2, 556, 4, 566, 3}, // shadow barrage
            {12929, 92, 1979, 0, 0, 377, 29, 51, 560, 4, 565, 4, 566, 1, 0, 0},  // blood barrage
            {12891, 94, 1979, 0, 0, 369, 30, 52, 560, 4, 565, 2, 555, 6, 0, 0}, // ice barrage

            {-1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0}, // charge
            {-1, 21, 712, 112, 0, 0, 0, 10, 554, 3, 561, 1, 0, 0, 0, 0}, // low alch
            {-1, 55, 713, 113, 0, 0, 0, 20, 554, 5, 561, 1, 0, 0, 0, 0}, // high alch
            {-1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0} // telegrab

    };

    public boolean isAutoButton(int button) {
        for (int j = 0; j < autocastIds.length; j += 2) {
            if (autocastIds[j] == button)
                return true;
        }
        return false;
    }

    public int[] autocastIds = {51133, 32, 51185, 33, 51091, 34, 24018, 35,
            51159, 36, 51211, 37, 51111, 38, 51069, 39, 51146, 40, 51198, 41,
            51102, 42, 51058, 43, 51172, 44, 51224, 45, 51122, 46, 51080, 47,
            7038, 0, 7039, 1, 7040, 2, 7041, 3, 7042, 4, 7043, 5, 7044, 6,
            7045, 7, 7046, 8, 7047, 9, 7048, 10, 7049, 11, 47019, 27, 47020,
            25, 47021, 12, 47022, 13, 47023, 14, 47024, 15};

    // public String spellName = "Select Spell";
    public void assignAutocast(int button) {
        for (int j = 0; j < autocastIds.length; j++) {
            if (autocastIds[j] == button) {
                Client c = (Client) PlayerHandler.players[this.playerId];
                autocasting = true;
                autocastId = autocastIds[j + 1];
                c.getPA().sendFrame36(108, 1);
                c.setSidebarInterface(0, 328);
                // spellName = getSpellName(autocastId);
                // spellName = spellName;
                // c.getPA().sendFrame126(spellName, 354);
                c = null;
                break;
            }
        }
    }

    public boolean fullVoidRange() {
        return playerEquipment[playerHat] == 11664
                && playerEquipment[playerLegs] == 8840
                && playerEquipment[playerChest] == 8839
                && playerEquipment[playerHands] == 8842;
    }

    public boolean fullVoidMage() {
        return playerEquipment[playerHat] == 11663
                && playerEquipment[playerLegs] == 8840
                && playerEquipment[playerChest] == 8839
                && playerEquipment[playerHands] == 8842;
    }

    public boolean fullVoidMelee() {
        return playerEquipment[playerHat] == 11665
                && playerEquipment[playerLegs] == 8840
                && playerEquipment[playerChest] == 8839
                && playerEquipment[playerHands] == 8842;
    }

    public int reduceSpellId;
    public final int[] REDUCE_SPELL_TIME = {250000, 250000, 250000, 500000,
            500000, 500000}; // how long does the other player stay immune to
    // the spell
    public long[] reduceSpellDelay = new long[6];
    public final int[] REDUCE_SPELLS = {1153, 1157, 1161, 1542, 1543, 1562};
    public boolean[] canUseReducingSpell = {true, true, true, true, true, true};

    public int prayerId = -1;
    public int headIcon = -1;
    public long stopPrayerDelay;
    public boolean usingPrayer;
    public final int[] PRAYER_LEVEL_REQUIRED = {1, 4, 7, 8, 9, 10, 13, 16, 19,
            22, 25, 26, 27, 28, 31, 34, 37, 40, 43, 44, 45, 46, 49, 52, 60, 70};
    public final int[] PRAYER = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
            14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
    public final String[] PRAYER_NAME = {"Thick Skin", "Burst of Strength",
            "Clarity of Thought", "Sharp Eye", "Mystic Will", "Rock Skin",
            "Superhuman Strength", "Improved Reflexes", "Rapid Restore",
            "Rapid Heal", "Protect Item", "Hawk Eye", "Mystic Lore",
            "Steel Skin", "Ultimate Strength", "Incredible Reflexes",
            "Protect from Magic", "Protect from Missiles",
            "Protect from Melee", "Eagle Eye", "Mystic Might", "Retribution",
            "Redemption", "Smite", "Chivalry", "Piety"};
    public final int[] PRAYER_GLOW = {83, 84, 85, 601, 602, 86, 87, 88, 89,
            90, 91, 603, 604, 92, 93, 94, 95, 96, 97, 605, 606, 98, 99, 100,
            607, 608};
    public final int[] PRAYER_HEAD_ICONS = {-1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 0, -1, -1, 3, 5, 4, -1, -1};
    // {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,3,2,1,4,6,5};

    public boolean[] prayerActive = {false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false,
            false, false};

    public int duelTeleX, duelTeleY, duelSlot, duelSpaceReq, duelOption,
            duelingWith, duelStatus;
    public int headIconPk = -1, headIconHints;
    public boolean duelRequested;
    public boolean[] duelRule = new boolean[22];
    public final int[] DUEL_RULE_ID = {1, 2, 16, 32, 64, 128, 256, 512, 1024,
            4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 2097152,
            8388608, 16777216, 67108864, 134217728};

    public boolean doubleHit, usingSpecial, usingRangeWeapon, usingBow,
            usingMagic, castingMagic;
    public int specMaxHitIncrease, freezeDelay, freezeTimer = -6, killerId,
            playerIndex, oldPlayerIndex, lastWeaponUsed, projectileStage,
            crystalBowArrowCount, playerMagicBook, teleGfx, teleEndAnimation,
            teleHeight, teleX, teleY, rangeItemUsed, killingNpcIndex,
            totalDamageDealt, oldNpcIndex, fightMode, attackTimer, npcIndex,
            npcClickIndex, npcType, oldSpellId, spellId, hitDelay;
    public boolean magicFailed;
    public int bowSpecShot, clickNpcType, clickObjectType, objectId, objectFace, objectX,
            objectY, objectXOffset, objectYOffset, objectDistance;
    public int pItemX, pItemY, pItemId, pItemHeight;
    public boolean isMoving, walkingToItem;
    public boolean updateShop;
    public boolean isShopping = false;
    public int myShopId;
    public int tradeStatus, tradeWith;
    public boolean inTrade = false;
    public boolean forcedChatUpdateRequired, tradeAccepted, goodTrade,
            tradeRequested, tradeResetNeeded, tradeConfirmed, tradeConfirmed2,
            canOffer, acceptedTrade;
    public int animationRequest = -1, animationWaitCycles;
    public int[] playerBonus = new int[12];
    public boolean isRunning2 = true;
    public boolean takeAsNote;
    public int combatLevel;
    public boolean saveFile = false;
    public int playerAppearance[] = new int[13];
    public int apset;
    public int actionID;
    public int wearId, wearSlot, interfaceId;
    public boolean usingGlory = false;

    public boolean antiFirePot = false;

    /**
     * SouthWest, NorthEast, SouthWest, NorthEast
     */

    public boolean isInTut() {
        if (absX >= 2625 && absX <= 2687 && absY >= 4670 && absY <= 4735) {
            return true;
        }
        return false;
    }

    public boolean inArea(int x, int y, int x1, int y1) {
        if (absX > x && absX < x1 && absY < y && absY > y1) {
            return true;
        }
        return false;
    }

    public boolean safeAreas(int x, int y, int x1, int y1) {
        return (absX >= x && absX <= x1 && absY >= y && absY <= y1);
    }

    public boolean inWild() {
        if (absX >= 2757 && absX <= 2990 && absY >= 2871 && absY <= 2940) {
            return true;
        }
        return false;
    }

    public boolean safezone() {
        if (absX >= 2757 && absX <= 2761 && absY >= 2911 && absY <= 2922) {
            return true;
        }
        return false;
    }

    public boolean arenas() {
        if (absX > 3331 && absX < 3391 && absY > 3242 && absY < 3260) {
            return true;
        }
        return false;
    }

    public boolean inDuelArena() {
        if ((absX > 3322 && absX < 3394 && absY > 3195 && absY < 3291)
                || (absX > 3311 && absX < 3323 && absY > 3223 && absY < 3248)) {
            return true;
        }
        return false;
    }

    public boolean inFightCaves() {
        return absX >= 2360 && absX <= 2445 && absY >= 5045 && absY <= 5125;
    }

    public boolean fireSpell() {
        if (MAGIC_SPELLS[oldSpellId][0] == 1158 || MAGIC_SPELLS[oldSpellId][0] == 1169 || MAGIC_SPELLS[oldSpellId][0] == 1181 || MAGIC_SPELLS[oldSpellId][0] == 1189) {
            return true;
        }
        return false;
    }

    public boolean inRfd() {
        if (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711) {
            return true;
        }
        return false;
    }

    public boolean Birth = false;

    public boolean onBirth() {
        if (absX >= 2488 && absX <= 2562 && absY >= 3692 && absY <= 3780) {
            return true;
        }
        return false;
    }

    public boolean inMulti() {
        if (InAlK())
            return true;
        if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
                // absX >= 2759 && absX <= 2981 && absY >= 2939 && absY <= 2878
                || (absX >= 2858 && absX <= 2862 && absY >= 3270 && absY <= 3275)
                || (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
                || (absX >= 2813 && absX <= 2826 && absY >= 3285 && absY <= 3299)
                || (absX >= 3120 && absX <= 3132 && absY >= 9857 && absY <= 9868)
                || (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
                || (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
                || (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
                || (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
                || (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
                || (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
                || (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
                || (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
                || (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
                || (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
                || (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)
                || (absX >= 2973 && absX <= 2982 && absY >= 2901 && absY <= 2918)) {
            return true;
        }
        return false;
    }

    private boolean InAlK() {
        if (getX() / 64 == 51 && getY() / 64 == 49)
            return true;
        return false;
    }

    public boolean inMime() {
        if (absX / 64 == 31 && absY / 64 == 74) {
            return true;
        }
        return false;
    }

    public boolean cantLeave() {
        if (absX == 3565 && absY == 3312) {
            return true;
        }
        return false;
    }

    public String connectedFrom = "";

    public abstract void initialize();

    public abstract void update();

    public int playerId = -1;
    public String playerName = null;
    public String playerName2 = null;
    public String playerPass = null;
    public int playerRights;
    public PlayerHandler handler = null;
    public int playerItems[] = new int[28];
    public int playerItemsN[] = new int[28];
    public int bankItems[] = new int[Config.BANK_SIZE];
    public int bankItemsN[] = new int[Config.BANK_SIZE];
    public int overloadcounter = 0;
    public int playerStandIndex = 0x328;
    public int playerTurnIndex = 0x337;
    public int playerWalkIndex = 0x333;
    public int playerTurn180Index = 0x334;
    public int playerTurn90CWIndex = 0x335;
    public int playerTurn90CCWIndex = 0x336;
    public int playerRunIndex = 0x338;

    public int playerHat = 0;
    public int playerCape = 1;
    public int playerAmulet = 2;
    public int playerWeapon = 3;
    public int playerChest = 4;
    public int playerShield = 5;
    public int playerLegs = 7;
    public int playerHands = 9;
    public int playerFeet = 10;
    public int playerRing = 12;
    public int playerArrows = 13;

    public int playerAttack = 0;
    public int playerDefence = 1;
    public int playerStrength = 2;
    public int playerHitpoints = 3;
    public int playerRanged = 4;
    public int playerPrayer = 5;
    public int playerMagic = 6;
    public int playerCooking = 7;
    public int playerWoodcutting = 8;
    public int playerFletching = 9;
    public int playerFishing = 10;
    public int playerFiremaking = 11;
    public int playerCrafting = 12;
    public int playerSmithing = 13;
    public int playerMining = 14;
    public int playerHerblore = 15;
    public int playerAgility = 16;
    public int playerThieving = 17;
    public int playerSlayer = 18;
    public int playerFarming = 19;
    public int playerRunecrafting = 20;

    public int[] playerEquipment = new int[14];
    public int[] playerEquipmentN = new int[14];
    public int[] playerLevel = new int[25];
    public int[] playerXP = new int[25];

    public void updateshop(int i) {
        Client p = (Client) PlayerHandler.players[playerId];
        p.getShops().resetShop(i);
    }

    public void println_debug(String str) {
        System.out.println("[player-" + playerId + "]: " + str);
    }

    public Player(int _playerId) {
        playerId = _playerId;
        playerRights = 0;

        for (int i = 0; i < playerItems.length; i++) {
            playerItems[i] = 0;
        }
        for (int i = 0; i < playerItemsN.length; i++) {
            playerItemsN[i] = 0;
        }

        for (int i = 0; i < playerLevel.length; i++) {
            if (i == 3) {
                playerLevel[i] = 10;
            } else {
                playerLevel[i] = 1;
            }
        }

        for (int i = 0; i < playerXP.length; i++) {
            if (i == 3) {
                playerXP[i] = 1300;
            } else {
                playerXP[i] = 0;
            }
        }
        for (int i = 0; i < Config.BANK_SIZE; i++) {
            bankItems[i] = 0;
        }

        for (int i = 0; i < Config.BANK_SIZE; i++) {
            bankItemsN[i] = 0;
        }

        playerAppearance[0] = 0; // gender
        playerAppearance[1] = 7; // head
        playerAppearance[2] = 25;// Torso
        playerAppearance[3] = 29; // arms
        playerAppearance[4] = 35; // hands
        playerAppearance[5] = 39; // legs
        playerAppearance[6] = 44; // feet
        playerAppearance[7] = 14; // beard
        playerAppearance[8] = 7; // hair colour
        playerAppearance[9] = 8; // torso colour
        playerAppearance[10] = 9; // legs colour
        playerAppearance[11] = 5; // feet colour
        playerAppearance[12] = 0; // skin colour

        apset = 0;
        actionID = 0;
        

        playerEquipment[playerHat] = -1;
        playerEquipment[playerCape] = -1;
        playerEquipment[playerAmulet] = -1;
        playerEquipment[playerChest] = -1;
        playerEquipment[playerShield] = -1;
        playerEquipment[playerLegs] = -1;
        playerEquipment[playerHands] = -1;
        playerEquipment[playerFeet] = -1;
        playerEquipment[playerRing] = -1;
        playerEquipment[playerArrows] = -1;
        playerEquipment[playerWeapon] = -1;

        heightLevel = 0;

        teleportToX = Config.START_LOCATION_X;
        teleportToY = Config.START_LOCATION_Y;

        absX = absY = -1;
        mapRegionX = mapRegionY = -1;
        currentX = currentY = 0;
        resetWalkingQueue();
    }

    void destruct() {
        playerListSize = 0;
        for (int i = 0; i < maxPlayerListSize; i++)
            playerList[i] = null;
        absX = absY = -1;
        mapRegionX = mapRegionY = -1;
        currentX = currentY = 0;
        resetWalkingQueue();
    }

    public static final int maxPlayerListSize = Config.MAX_PLAYERS;
    public Player playerList[] = new Player[maxPlayerListSize];
    public int playerListSize = 0;

    public byte playerInListBitmap[] = new byte[(Config.MAX_PLAYERS + 7) >> 3];

    public static final int maxNPCListSize = NPCHandler.maxNPCs;
    public NPC npcList[] = new NPC[maxNPCListSize];
    public int npcListSize = 0;

    public byte npcInListBitmap[] = new byte[(NPCHandler.maxNPCs + 7) >> 3];

    public boolean withinDistance(Player otherPlr) {
        if (heightLevel != otherPlr.heightLevel)
            return false;
        int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
        return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
    }

    public boolean withinDistance(NPC npc) {
        if (heightLevel != npc.heightLevel)
            return false;
        if (npc.needRespawn == true)
            return false;
        int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
        return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
    }

    public boolean withinDistance(int absX, int getY, int getHeightLevel) {
        if (this.getHeightLevel() != getHeightLevel)
            return false;
        int deltaX = this.getX() - absX, deltaY = this.getY() - getY;
        return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
    }

    public int getHeightLevel;

    public int getHeightLevel() {
        return getHeightLevel;
    }

    public int distanceToPoint(int pointX, int pointY) {
        return (int) Math.sqrt(Math.pow(absX - pointX, 2)
                + Math.pow(absY - pointY, 2));
    }

    public int mapRegionX, mapRegionY;
    public int absX, absY;
    public int currentX, currentY;

    public int heightLevel;

    public boolean updateRequired = true;

    public final int walkingQueueSize = 50;
    public int walkingQueueX[] = new int[walkingQueueSize];
    private int[] walkingQueueY = new int[walkingQueueSize];
    public int wQueueReadPtr = 0;
    public int wQueueWritePtr = 0;
    public boolean isRunning = true;
    public int teleportToX = -1, teleportToY = -1;

    public void resetWalkingQueue() {
        wQueueReadPtr = wQueueWritePtr = 0;

        for (int i = 0; i < walkingQueueSize; i++) {
            walkingQueueX[i] = currentX;
            walkingQueueY[i] = currentY;
        }
    }

    public void addToWalkingQueue(int x, int y) {
        // if (VirtualWorld.I(heightLevel, absX, absY, x, y, 0)) {
        int next = (wQueueWritePtr + 1) % walkingQueueSize;
        if (next == wQueueWritePtr)
            return;
        walkingQueueX[wQueueWritePtr] = x;
        walkingQueueY[wQueueWritePtr] = y;
        wQueueWritePtr = next;
        // }
    }

    public boolean goodDistance(int objectX, int objectY, int playerX,
                                int playerY, int distance) {
        for (int i = 0; i <= distance; i++) {
            for (int j = 0; j <= distance; j++) {
                if ((objectX + i) == playerX
                        && ((objectY + j) == playerY
                        || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                } else if ((objectX - i) == playerX
                        && ((objectY + j) == playerY
                        || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                } else if (objectX == playerX
                        && ((objectY + j) == playerY
                        || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                } /*else if (objectX == playerX - 1 || objectX == playerX + 1 || objectY == playerY - 1 || objectY == playerY + 1
                        || objectX == playerX - 1 && objectY == playerY - 1 || objectX == playerX + 1 && objectY == playerY + 1
                        || objectX == playerX - 1 && objectY == playerY + 1 || objectX == playerX + 1 && objectY == playerY - 1) {
                    return true;
                }*/

            }
        }
        return false;
    }

    public int getNextWalkingDirection() {
        if (wQueueReadPtr == wQueueWritePtr)
            return -1;
        int dir;
        do {
            dir = Misc.direction(currentX, currentY,
                    walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
            if (dir == -1) {
                wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
            } else if ((dir & 1) != 0) {
                println_debug("Invalid waypoint in walking queue!");
                resetWalkingQueue();
                return -1;
            }
        } while ((dir == -1) && (wQueueReadPtr != wQueueWritePtr));
        if (dir == -1)
            return -1;
        dir >>= 1;
        currentX += Misc.directionDeltaX[dir];
        currentY += Misc.directionDeltaY[dir];
        absX += Misc.directionDeltaX[dir];
        absY += Misc.directionDeltaY[dir];
        updateWalkEntities();
        return dir;
    }

    public long lastIncrease;
    public int playerEnergy = 100;
    public boolean isResting = false;
    public boolean didTeleport = false;
    public boolean mapRegionDidChange = false;
    public int dir1 = -1, dir2 = -1;
    public int poimiX = 0, poimiY = 0;

    public synchronized void getNextPlayerMovement() {
        mapRegionDidChange = false;
        didTeleport = false;
        dir1 = dir2 = -1;

        if (teleportToX != -1 && teleportToY != -1) {
            mapRegionDidChange = true;
            if (mapRegionX != -1 && mapRegionY != -1) {
                int relX = teleportToX - mapRegionX * 8, relY = teleportToY
                        - mapRegionY * 8;
                if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8
                        && relY < 11 * 8)
                    mapRegionDidChange = false;
            }
            if (mapRegionDidChange) {
                mapRegionX = (teleportToX >> 3) - 6;
                mapRegionY = (teleportToY >> 3) - 6;
            }
            currentX = teleportToX - 8 * mapRegionX;
            currentY = teleportToY - 8 * mapRegionY;
            absX = teleportToX;
            absY = teleportToY;
            resetWalkingQueue();

            teleportToX = teleportToY = -1;
            didTeleport = true;
            updateWalkEntities();
        } else {
            dir1 = getNextWalkingDirection();
            if (dir1 == -1)
                return;
            if (isRunning) {
                dir2 = getNextWalkingDirection();
            }
            // c.sendMessage("Cycle Ended");
            int deltaX = 0, deltaY = 0;
            if (currentX < 2 * 8) {
                deltaX = 4 * 8;
                mapRegionX -= 4;
                mapRegionDidChange = true;
            } else if (currentX >= 11 * 8) {
                deltaX = -4 * 8;
                mapRegionX += 4;
                mapRegionDidChange = true;
            }
            if (currentY < 2 * 8) {
                deltaY = 4 * 8;
                mapRegionY -= 4;
                mapRegionDidChange = true;
            } else if (currentY >= 11 * 8) {
                deltaY = -4 * 8;
                mapRegionY += 4;
                mapRegionDidChange = true;
            }

            if (mapRegionDidChange/*
                   * && VirtualWorld.I(heightLevel, currentX,
				   * currentY, currentX + deltaX, currentY +
				   * deltaY, 0)
				   */) {
                currentX += deltaX;
                currentY += deltaY;
                for (int i = 0; i < walkingQueueSize; i++) {
                    walkingQueueX[i] += deltaX;
                    walkingQueueY[i] += deltaY;
                }
            }
            // CoordAssistant.processCoords(this);

        }
    }

    public void updateThisPlayerMovement(Stream str) {
        // synchronized(this) {
        if (mapRegionDidChange) {
            str.createFrame(73);
            str.writeWordA(mapRegionX + 6);
            str.writeWord(mapRegionY + 6);
        }

        if (didTeleport) {
            str.createFrameVarSizeWord(81);
            str.initBitAccess();
            str.writeBits(1, 1);
            str.writeBits(2, 3);
            str.writeBits(2, heightLevel);
            str.writeBits(1, 1);
            str.writeBits(1, (updateRequired) ? 1 : 0);
            str.writeBits(7, currentY);
            str.writeBits(7, currentX);
            return;
        }

        if (dir1 == -1) {
            // don't have to update the character position, because we're just
            // standing
            str.createFrameVarSizeWord(81);
            str.initBitAccess();
            isMoving = false;
            if (updateRequired) {
                // tell client there's an update block appended at the end
                str.writeBits(1, 1);
                str.writeBits(2, 0);
            } else {
                str.writeBits(1, 0);
            }
            if (DirectionCount < 50) {
                DirectionCount++;
            }
        } else {
            DirectionCount = 0;
            str.createFrameVarSizeWord(81);
            str.initBitAccess();
            str.writeBits(1, 1);

            if (dir2 == -1) {
                isMoving = true;
                str.writeBits(2, 1);
                str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
                if (updateRequired)
                    str.writeBits(1, 1);
                else
                    str.writeBits(1, 0);
            } else {
                isMoving = true;
                str.writeBits(2, 2);
                str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
                str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
                if (updateRequired)
                    str.writeBits(1, 1);
                else
                    str.writeBits(1, 0);
                if (playerEnergy > 0) {
                    if (weight > 0.0)
                        //playerEnergy -= 1 + Misc.random(3);
                        playerEnergy -= 0;
                    else
                        //playerEnergy -= 1;
                        playerEnergy -= 0;
                } else {
                    playerEnergy = 0;
                    isRunning2 = false;
                }
            }
        }

    }

    public void updatePlayerMovement(Stream str) {
        // synchronized(this) {
        if (dir1 == -1) {
            if (updateRequired || isChatTextUpdateRequired()) {

                str.writeBits(1, 1);
                str.writeBits(2, 0);
            } else
                str.writeBits(1, 0);
        } else if (dir2 == -1) {

            str.writeBits(1, 1);
            str.writeBits(2, 1);
            str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
            str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1
                    : 0);
        } else {

            str.writeBits(1, 1);
            str.writeBits(2, 2);
            str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
            str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
            str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1
                    : 0);
        }

    }

    public void addNewNPC(NPC npc, Stream str, Stream updateBlock) {
        // synchronized(this) {
        int id = npc.npcId;
        npcInListBitmap[id >> 3] |= 1 << (id & 7);
        npcList[npcListSize++] = npc;

        str.writeBits(14, id);

        int z = npc.absY - absY;
        if (z < 0)
            z += 32;
        str.writeBits(5, z);
        z = npc.absX - absX;
        if (z < 0)
            z += 32;
        str.writeBits(5, z);

        str.writeBits(1, 0);
        str.writeBits(12, npc.npcType);

        boolean savedUpdateRequired = npc.updateRequired;
        npc.updateRequired = true;
        npc.appendNPCUpdateBlock(updateBlock);
        npc.updateRequired = savedUpdateRequired;
        str.writeBits(1, 1);
    }

    public void addNewPlayer(Player plr, Stream str, Stream updateBlock) {
        // synchronized(this) {
        if (playerListSize >= 255) {
            return;
        }
        int id = plr.playerId;
        playerInListBitmap[id >> 3] |= 1 << (id & 7);
        playerList[playerListSize++] = plr;
        str.writeBits(11, id);
        str.writeBits(1, 1);
        boolean savedFlag = plr.isAppearanceUpdateRequired();
        boolean savedUpdateRequired = plr.updateRequired;
        plr.setAppearanceUpdateRequired(true);
        plr.updateRequired = true;
        plr.appendPlayerUpdateBlock(updateBlock);
        plr.setAppearanceUpdateRequired(savedFlag);
        plr.updateRequired = savedUpdateRequired;
        str.writeBits(1, 1);
        int z = plr.absY - absY;
        if (z < 0)
            z += 32;
        str.writeBits(5, z);
        z = plr.absX - absX;
        if (z < 0)
            z += 32;
        str.writeBits(5, z);
    }

    protected static Stream playerProps;

    static {
        playerProps = new Stream(new byte[100]);
    }

    protected void appendPlayerAppearance(Stream str) {
        synchronized (this) {
            playerProps.currentOffset = 0;
            playerProps.writeByte(playerAppearance[0]);
            playerProps.writeByte(headIcon);
            playerProps.writeByte(headIconPk);
            // playerProps.writeByte(headIconHints);
            // playerProps.writeByte(bountyIcon);
            if (isNpc == false) {
                if (playerEquipment[playerHat] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerHat]);
                } else {
                    playerProps.writeByte(0);
                }

                if (playerEquipment[playerCape] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerCape]);
                } else {
                    playerProps.writeByte(0);
                }

                if (playerEquipment[playerAmulet] > 1) {
                    playerProps
                            .writeWord(0x200 + playerEquipment[playerAmulet]);
                } else {
                    playerProps.writeByte(0);
                }

                if (playerEquipment[playerWeapon] > 1) {
                    playerProps
                            .writeWord(0x200 + playerEquipment[playerWeapon]);
                } else {
                    playerProps.writeByte(0);
                }

                if (playerEquipment[playerChest] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerChest]);
                } else {
                    playerProps.writeWord(0x100 + playerAppearance[2]);
                }

                if (playerEquipment[playerShield] > 1) {
                    playerProps
                            .writeWord(0x200 + playerEquipment[playerShield]);
                } else {
                    playerProps.writeByte(0);
                }

                if (!isFullBody) {
                    playerProps.writeWord(0x100 + playerAppearance[3]);
                } else {
                    playerProps.writeByte(0);
                }

                if (playerEquipment[playerLegs] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerLegs]);
                } else {
                    playerProps.writeWord(0x100 + playerAppearance[5]);
                }

                if (!isFullHelm && !isFullMask) {
                    playerProps.writeWord(0x100 + playerAppearance[1]);
                } else {
                    playerProps.writeByte(0);
                }

                if (playerEquipment[playerHands] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerHands]);
                } else {
                    playerProps.writeWord(0x100 + playerAppearance[4]);
                }

                if (playerEquipment[playerFeet] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerFeet]);
                } else {
                    playerProps.writeWord(0x100 + playerAppearance[6]);
                }

                if (playerAppearance[0] != 1
                        && !Item.isFullMask(playerEquipment[playerHat])) {
                    playerProps.writeWord(0x100 + playerAppearance[7]);
                } else {
                    playerProps.writeByte(0);
                }
            } else {
                playerProps.writeWord(-1);
                playerProps.writeWord(npcId2);
            }
            playerProps.writeByte(playerAppearance[8]);
            playerProps.writeByte(playerAppearance[9]);
            playerProps.writeByte(playerAppearance[10]);
            playerProps.writeByte(playerAppearance[11]);
            playerProps.writeByte(playerAppearance[12]);
            playerProps.writeWord(playerStandIndex); // standAnimIndex
            playerProps.writeWord(playerTurnIndex); // standTurnAnimIndex
            playerProps.writeWord(playerWalkIndex); // walkAnimIndex
            playerProps.writeWord(playerTurn180Index); // turn180AnimIndex
            playerProps.writeWord(playerTurn90CWIndex); // turn90CWAnimIndex
            playerProps.writeWord(playerTurn90CCWIndex); // turn90CCWAnimIndex
            playerProps.writeWord(playerRunIndex); // runAnimIndex

            playerProps.writeQWord(Misc.playerNameToInt64(playerName));
            combatLevel = calculateCombatLevel();
            playerProps.writeByte(combatLevel); // combat level
            playerProps.writeWord(0);
            str.writeByteC(playerProps.currentOffset);
            str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);
        }
    }

    public int calculateCombatLevel() {
        int j = getLevelForXP(playerXP[playerAttack]);
        int k = getLevelForXP(playerXP[playerDefence]);
        int l = getLevelForXP(playerXP[playerStrength]);
        int i1 = getLevelForXP(playerXP[playerHitpoints]);
        int j1 = getLevelForXP(playerXP[playerPrayer]);
        int k1 = getLevelForXP(playerXP[playerRanged]);
        int l1 = getLevelForXP(playerXP[playerMagic]);
        int combatLevel = (int) (((k + i1) + Math.floor(j1 / 2)) * 0.25D) + 1;
        double d = (j + l) * 0.32500000000000001D;
        double d1 = Math.floor(k1 * 1.5D) * 0.32500000000000001D;
        double d2 = Math.floor(l1 * 1.5D) * 0.32500000000000001D;
        if (d >= d1 && d >= d2) {
            combatLevel += d;
        } else if (d1 >= d && d1 >= d2) {
            combatLevel += d1;
        } else if (d2 >= d && d2 >= d1) {
            combatLevel += d2;
        }
        return combatLevel;
    }

    public int getLocalX() {
        return getX() - 8 * getMapRegionX();
    }

    public int getLocalY() {
        return getY() - 8 * getMapRegionY();
    }


    public int getLevelForXP(int exp) {
        int points = 0;
        int output = 0;

        for (int lvl = 1; lvl <= 99; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if (output >= exp)
                return lvl;
        }
        return 99;
    }

    private boolean chatTextUpdateRequired = false;
    private byte chatText[] = new byte[4096];
    private byte chatTextSize = 0;
    private int chatTextColor = 0;
    private int chatTextEffects = 0;

    protected void appendPlayerChatText(Stream str) {
        // synchronized(this) {
        str.writeWordBigEndian(((getChatTextColor() & 0xFF) << 8)
                + (getChatTextEffects() & 0xFF));
        str.writeByte(playerRights);
        str.writeByteC(getChatTextSize());
        str.writeBytes_reverse(getChatText(), getChatTextSize(), 0);

    }

    public void forcedChat(String text) {
        forcedText = text;
        forcedChatUpdateRequired = true;
        updateRequired = true;
        setAppearanceUpdateRequired(true);
    }

    public String forcedText = "null";

    public void appendForcedChat(Stream str) {
        // synchronized(this) {
        str.writeString(forcedText);
    }

    /**
     * Graphics
     */

    public int mask100var1 = 0;
    public int mask100var2 = 0;
    protected boolean mask100update = false;

    public void appendMask100Update(Stream str) {
        // synchronized(this) {
        str.writeWordBigEndian(mask100var1);
        str.writeDWord(mask100var2);

    }

    public void gfx100(int gfx) {
        mask100var1 = gfx;
        mask100var2 = 6553600;
        mask100update = true;
        updateRequired = true;
    }

    public void gfx0(int gfx) {
        mask100var1 = gfx;
        mask100var2 = 65536;
        mask100update = true;
        updateRequired = true;
    }

    public boolean wearing2h() {
        Client c = (Client) this;
        String s = c.getItems().getItemName(c.playerEquipment[c.playerWeapon]);
        if (s.contains("2h"))
            return true;
        else if (s.contains("godsword"))
            return true;
        return false;
    }

    /**
     * Animations
     */
    public void startAnimation(int animId) {
        if (wearing2h() && animId == 829)
            return;
        animationRequest = animId;
        animationWaitCycles = 0;
        updateRequired = true;
    }

    public void startAnimation(int animId, int cycle) {
        if (wearing2h() && animId == 829)
            return;
        animationRequest = animId;
        animationWaitCycles = cycle;
        updateRequired = true;
    }


    public void appendAnimationRequest(Stream str) {
        str.writeWordBigEndian((animationRequest == -1) ? 65535
                : animationRequest);
        str.writeByteC(animationWaitCycles);

    }

    /**
     * Face Update
     */

    protected boolean faceUpdateRequired = false;
    public int face = -1;
    public int FocusPointX = -1, FocusPointY = -1;

    public void faceUpdate(int index) {
        face = index;
        faceUpdateRequired = true;
        updateRequired = true;
    }

    public void appendFaceUpdate(Stream str) {
        // synchronized(this) {
        str.writeWordBigEndian(face);

    }

    public void turnPlayerTo(int pointX, int pointY) {
        FocusPointX = 2 * pointX + 1;
        FocusPointY = 2 * pointY + 1;
        updateRequired = true;
    }

    private void appendSetFocusDestination(Stream str) {
        // synchronized(this) {
        str.writeWordBigEndianA(FocusPointX);
        str.writeWordBigEndian(FocusPointY);

    }

    /**
     * Hit Update
     */

    private void appendHitUpdate(Stream str) {
        // synchronized(this) {
        str.writeByte(getHitDiff()); // What the perseon got 'hit' for
        if (poisonMask == 1) {
            str.writeByteA(2);
        } else if (getHitDiff() > 0) {
            str.writeByteA(1); // 0: red hitting - 1: blue hitting
        } else {
            str.writeByteA(0); // 0: red hitting - 1: blue hitting
        }
        if (playerLevel[3] <= 0) {
            playerLevel[3] = 0;
            isDead = true;
        }
        str.writeByteC(playerLevel[3]); // Their current hp, for HP bar
        str.writeByte(getLevelForXP(playerXP[3])); // Their max hp, for HP bar

    }

    private void appendHitUpdate2(Stream str) {
        // synchronized(this) {
        str.writeByte(hitDiff2); // What the perseon got 'hit' for
        if (poisonMask == 2) {
            str.writeByteS(2);
            poisonMask = -1;
        } else if (hitDiff2 > 0) {
            str.writeByteS(1); // 0: red hitting - 1: blue hitting
        } else {
            str.writeByteS(0); // 0: red hitting - 1: blue hitting
        }
        if (playerLevel[3] <= 0) {
            playerLevel[3] = 0;
            isDead = true;
        }
        str.writeByte(playerLevel[3]); // Their current hp, for HP bar
        str.writeByteC(getLevelForXP(playerXP[3])); // Their max hp, for HP bar

    }

    void appendPlayerUpdateBlock(Stream str) {
        // synchronized(this) {
        if (!updateRequired && !isChatTextUpdateRequired())
            return; // nothing required
        int updateMask = 0;
        if (forceMovementUpdateRequired) {
            updateMask |= 0x400;
        }
        if (mask100update) {
            updateMask |= 0x100;
        }
        if (animationRequest != -1) {
            updateMask |= 8;
        }
        if (forcedChatUpdateRequired) {
            updateMask |= 4;
        }
        if (isChatTextUpdateRequired()) {
            updateMask |= 0x80;
        }
        if (isAppearanceUpdateRequired()) {
            updateMask |= 0x10;
        }
        if (faceUpdateRequired) {
            updateMask |= 1;
        }
        if (FocusPointX != -1) {
            updateMask |= 2;
        }
        if (isHitUpdateRequired()) {
            updateMask |= 0x20;
        }

        if (hitUpdateRequired2) {
            updateMask |= 0x200;
        }

        if (updateMask >= 0x100) {
            updateMask |= 0x40;
            str.writeByte(updateMask & 0xFF);
            str.writeByte(updateMask >> 8);
        } else {
            str.writeByte(updateMask);
        }

        // now writing the various update blocks itself - note that their order
        // crucial
        if (forceMovementUpdateRequired) {
            appendMask400Update(str);
        }
        if (mask100update) {
            appendMask100Update(str);
        }
        if (animationRequest != -1) {
            appendAnimationRequest(str);
        }
        if (forcedChatUpdateRequired) {
            appendForcedChat(str);
        }
        if (isChatTextUpdateRequired()) {
            appendPlayerChatText(str);
        }
        if (faceUpdateRequired) {
            appendFaceUpdate(str);
        }
        if (isAppearanceUpdateRequired()) {
            appendPlayerAppearance(str);
        }
        if (FocusPointX != -1) {
            appendSetFocusDestination(str);
        }
        if (isHitUpdateRequired()) {
            appendHitUpdate(str);
        }
        if (hitUpdateRequired2) {
            appendHitUpdate2(str);
        }

    }

    public boolean forceMovementUpdateRequired = false;
    private int x1 = -1;
    private int y1 = -1;
    private int x2 = -1;
    private int y2 = -1;
    private int speed1 = -1;
    private int speed2 = -1;
    private int direction = -1;

    public void setForceMovement(final int x2, final int y2, boolean x1, boolean y1, final int speed1, final int speed2, final int direction, final int emote) {
        canWalk = false;
        this.x1 = currentX;
        this.y1 = currentY;
        this.x2 = x1 ? currentX + x2 : currentX - x2;
        this.y2 = y1 ? currentY + y2 : currentY - y2;
        this.speed1 = speed1;
        this.speed2 = speed2;
        this.direction = direction;
        updateRequired = true;
        forceMovementUpdateRequired = true;
        final Client c = (Client) this;
        EventManager.getSingleton().addEvent(new Event() {
            public void execute(EventContainer even) {
                c.getCombat().getPlayerAnimIndex(c.getItems().getItemName(playerEquipment[playerWeapon]).toLowerCase());
                //c.getPA().movePlayer(c.absX,c.absY,c.heightLevel);
                updateRequired = true;
                forceMovementUpdateRequired = false;
                canWalk = true;
                even.stop();
            }
        }, 100);
    }

    public int finalDestX, finalDestY;
    public boolean walkingToObject;
    public boolean canWalk = true;

    public void appendMask400Update(Stream str) {
        str.writeByteS(x1);
        str.writeByteS(y1);
        str.writeByteS(x2);
        str.writeByteS(y2);
        str.writeWordBigEndianA(speed1);
        str.writeWordA(speed2);
        str.writeByteS(direction);
    }

    void clearUpdateFlags() {
        forceMovementUpdateRequired = false;
        updateRequired = false;
        setChatTextUpdateRequired(false);
        setAppearanceUpdateRequired(false);
        setHitUpdateRequired(false);
        hitUpdateRequired2 = false;
        forcedChatUpdateRequired = false;
        mask100update = false;
        animationRequest = -1;
        FocusPointX = -1;
        FocusPointY = -1;
        poisonMask = -1;
        faceUpdateRequired = false;
        face = 65535;
    }

    public void stopMovement() {
        if (teleportToX <= 0 && teleportToY <= 0) {
            teleportToX = absX;
            teleportToY = absY;
        }
        newWalkCmdSteps = 0;
        getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
        getNextPlayerMovement();
    }

    public int newWalkCmdX[] = new int[walkingQueueSize];
    public int newWalkCmdY[] = new int[walkingQueueSize];
    public int newWalkCmdSteps = 0;
    private boolean newWalkCmdIsRunning = false;
    private int travelBackX[] = new int[walkingQueueSize];
    private int travelBackY[] = new int[walkingQueueSize];
    private int numTravelBackSteps = 0;

    void preProcessing() {
        newWalkCmdSteps = 0;
    }

    public abstract void process();

    public abstract boolean processQueuedPackets();

    public void postProcessing() {
        if (newWalkCmdSteps > 0) {
            int firstX = getNewWalkCmdX()[0], firstY = getNewWalkCmdY()[0];

            int lastDir = 0;
            boolean found = false;
            numTravelBackSteps = 0;
            int ptr = wQueueReadPtr;
            int dir = Misc.direction(currentX, currentY, firstX, firstY);
            if (dir != -1 && (dir & 1) != 0) {
                do {
                    lastDir = dir;
                    if (--ptr < 0)
                        ptr = walkingQueueSize - 1;

                    travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
                    travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
                    dir = Misc.direction(walkingQueueX[ptr], walkingQueueY[ptr], firstX, firstY);


                    if (lastDir != dir) {
                        found = true;
                        break;
                    }


                } while (ptr != wQueueWritePtr);
            } else found = true;


            if (!found) println_debug("");
            else {
                wQueueWritePtr = wQueueReadPtr;


                addToWalkingQueue(currentX, currentY);


                if (dir != -1 && (dir & 1) != 0) {


                    for (int i = 0; i < numTravelBackSteps - 1; i++) {
                        addToWalkingQueue(travelBackX[i], travelBackY[i]);
                    }
                    int wayPointX2 = travelBackX[numTravelBackSteps - 1], wayPointY2 = travelBackY[numTravelBackSteps - 1];
                    int wayPointX1, wayPointY1;
                    if (numTravelBackSteps == 1) {
                        wayPointX1 = currentX;
                        wayPointY1 = currentY;
                    } else {

                        wayPointX1 = travelBackX[numTravelBackSteps - 2];
                        wayPointY1 = travelBackY[numTravelBackSteps - 2];
                    }


                    dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2, wayPointY2);


                    if (dir == -1 || (dir & 1) != 0) {
//println_debug("Fatal: The walking queue is corrupt! wp1=("+wayPointX1+", "+wayPointY1+"), "+
//"wp2=("+wayPointX2+", "+wayPointY2+")");
                    } else {


                        dir >>= 1;
                        found = false;
                        int x = wayPointX1, y = wayPointY1;
                        while (x != wayPointX2 || y != wayPointY2) {
                            x += Misc.directionDeltaX[dir];
                            y += Misc.directionDeltaY[dir];
                            if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
//println_debug("Fatal: Internal error: unable to determine connection vertex!"+
//" wp1=("+wayPointX1+", "+wayPointY1+"), wp2=("+wayPointX2+", "+wayPointY2+"), "+
//"first=("+firstX+", "+firstY+")");


                        } else addToWalkingQueue(wayPointX1, wayPointY1);
                    }


                } else {
                    for (int i = 0; i < numTravelBackSteps; i++) {
                        addToWalkingQueue(travelBackX[i], travelBackY[i]);
                    }

                }


                for (int i = 0; i < newWalkCmdSteps; i++) {
                    addToWalkingQueue(getNewWalkCmdX()[i], getNewWalkCmdY()[i]);
                }

            }

            isRunning = isNewWalkCmdIsRunning() || isRunning2;
        }
    }

    public int getMapRegionX() {
        return mapRegionX;
    }

    public int getMapRegionY() {
        return mapRegionY;
    }

    public int getX() {
        return absX;
    }

    public int getY() {
        return absY;
    }

    public int getId() {
        return playerId;
    }

    public void setHitDiff(int hitDiff) {
        this.hitDiff = hitDiff;
    }

    public void setHitDiff2(int hitDiff2) {
        this.hitDiff2 = hitDiff2;
    }

    public int getHitDiff() {
        return hitDiff;
    }

    public void setHitUpdateRequired(boolean hitUpdateRequired) {
        this.hitUpdateRequired = hitUpdateRequired;
    }

    public void setHitUpdateRequired2(boolean hitUpdateRequired2) {
        this.hitUpdateRequired2 = hitUpdateRequired2;
    }

    public boolean isHitUpdateRequired() {
        return hitUpdateRequired;
    }

    public boolean getHitUpdateRequired() {
        return hitUpdateRequired;
    }

    public boolean getHitUpdateRequired2() {
        return hitUpdateRequired2;
    }

    public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
        this.appearanceUpdateRequired = appearanceUpdateRequired;
    }

    public boolean isAppearanceUpdateRequired() {
        return appearanceUpdateRequired;
    }

    public void setChatTextEffects(int chatTextEffects) {
        this.chatTextEffects = chatTextEffects;
    }

    public int getChatTextEffects() {
        return chatTextEffects;
    }

    public void setChatTextSize(byte chatTextSize) {
        this.chatTextSize = chatTextSize;
    }

    public byte getChatTextSize() {
        return chatTextSize;
    }

    public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
        this.chatTextUpdateRequired = chatTextUpdateRequired;
    }

    public boolean isChatTextUpdateRequired() {
        return chatTextUpdateRequired;
    }

    public byte[] getChatText() {
        return chatText;
    }

    public void setChatTextColor(int chatTextColor) {
        this.chatTextColor = chatTextColor;
    }

    public int getChatTextColor() {
        return chatTextColor;
    }

    public int[] getNewWalkCmdX() {
        return newWalkCmdX;
    }

    public int[] getNewWalkCmdY() {
        return newWalkCmdY;
    }

    public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning) {
        this.newWalkCmdIsRunning = newWalkCmdIsRunning;
    }

    public boolean isNewWalkCmdIsRunning() {
        return newWalkCmdIsRunning;
    }

    /*/ public boolean samePlayer() {
      for (int j = 0; j < PlayerHandler.players.length; j++) {
     if (j == playerId)
      continue;
      if (PlayerHandler.players[j] != null) {
      if (PlayerHandler.players[j].playerName
      .equalsIgnoreCase(playerName)) {
      disconnected = true;
      return true;
      }
      }
      }
      return false;
      }
 /*/
    public void dealDamage(int damage) {
        if (teleTimer <= 0)
            playerLevel[3] -= damage;
        else {
            if (hitUpdateRequired)
                hitUpdateRequired = false;
            if (hitUpdateRequired2)
                hitUpdateRequired2 = false;
        }

    }

    public int[] damageTaken = new int[Config.MAX_PLAYERS];

    public void handleHitMask(int damage) {
        if (!hitUpdateRequired) {
            hitUpdateRequired = true;
            hitDiff = damage;
        } else if (!hitUpdateRequired2) {
            hitUpdateRequired2 = true;
            hitDiff2 = damage;
        }
        updateRequired = true;
    }

}