package server.game.players.packets;

import core.util.Misc;
import server.content.SpinningPlates;
import server.content.randoms.Genie;
import server.content.skills.HerbCleaning;
import server.game.items.actions.pouch.EssencePouch;
import server.game.items.actions.pouch.Pouch;
import server.game.items.food.Food;
import server.game.items.food.SpecialConsumables;
import server.game.minigame.TreasureTrails;
import server.game.players.Client;
import server.game.players.DialogueHandler;
import server.game.players.PacketType;

/**
 * Clicking an item, bury bone, eat food etc
 */
public class ClickItem implements PacketType {

    private static final int[] CASKET_REWARDS = {2577, 2581, 7388, 7392, 7396,
            2583, 2585, 2587, 2589, 2599, 2601, 2603, 2605, 2623, 2625, 2627,
            2629, 3472, 3474, 3477, 7364, 7368, 7372, 7376, 7380, 7384, 7388,
            7392, 7396};

    public static int getLength() {
        return CASKET_REWARDS.length;
    }

    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        c.getInStream().readSignedWordBigEndianA();
        int itemSlot = c.getInStream().readUnsignedWordA();
        int itemId = c.getInStream().readUnsignedWordBigEndian();
        // ScriptManager.callFunc("itemClick_"+itemId, c, itemId, itemSlot);
        if (itemId != c.playerItems[itemSlot] - 1) {
            return;
        }

        HerbCleaning.handleHerbCleaning(c, itemId, itemSlot);
        if (SpecialConsumables.isConsumable(c, itemId, itemSlot))
            return;
        switch (itemId) {
            /* Essence pouches */
            /*case 5509:
                EssencePouch smallPouch = new EssencePouch(Pouch.SMALL_POUCH);
                smallPouch.addToPouch(c, smallPouch.getPouch());
                break;
            case 5510:
                EssencePouch mediumPouch = new EssencePouch(Pouch.MEDIUM_POUCH);
                mediumPouch.addToPouch(c, mediumPouch.getPouch());
                break;
            case 5512:
                EssencePouch largePouch = new EssencePouch(Pouch.LARGE_POUCH);
                largePouch.addToPouch(c, largePouch.getPouch());
                break;
            case 5514:
                EssencePouch giantPouch = new EssencePouch(Pouch.GIANT_POUCH);
                giantPouch.addToPouch(c, giantPouch.getPouch());
                break;*/
            case 407:
                int clam = Misc.random(2);
                c.getItems().deleteItem(407, 1);
                if (clam <= 1)
                    c.getItems().addItem(409, 1);
                if (clam == 2)
                    c.getItems().addItem(411, 1);
                break;
            case 550://newcomer
                c.getPA().showInterface(5392);
                break;
            case 4155:// genie
                if (c.slayerTask <= 0) {
                    c.sendMessage("@blu@You don't currently have a slayer task");
                } else {
                    DialogueHandler.sendDialogues(c, 40, 1599);
                }
                c.sendMessage("@blu@You currently have " + c.slPoints + " slayer points.");
                break;
            case 2528:// genie
                Genie.rubLamp(c, itemId);
                break;
            case 4613:// spinning plate
                if (itemId == 4613) {
                    if (!c.spinningPlate)
                        SpinningPlates.spinningPlate(c, itemId, itemSlot);
                }
                break;
        }
        if (itemId == 2520) {
            c.forcedChat(horseChat[Misc.random(0, 2)]);
            c.startAnimation(918);
        }
        if (itemId == 2522) {
            c.forcedChat(horseChat[Misc.random(0, 2)]);
            c.startAnimation(919);
        }
        if (itemId == 2524) {
            c.forcedChat(horseChat[Misc.random(0, 2)]);
            c.startAnimation(920);
        }
        if (itemId == 2526) {
            c.forcedChat(horseChat[Misc.random(0, 2)]);
            c.startAnimation(921);
        }
        if (itemId == 2714) { // Easy Clue Scroll Casket
            c.getItems().deleteItem(itemId, 1);
            TreasureTrails.addClueReward(c, 0);
        }
        if (itemId == 2802) { // Medium Clue Scroll Casket
            c.getItems().deleteItem(itemId, 1);
            TreasureTrails.addClueReward(c, 1);
        }
        if (itemId == 2775) { // Hard Clue Scroll Casket
            c.getItems().deleteItem(itemId, 1);
            TreasureTrails.addClueReward(c, 2);
        }
        /*if(itemId == 2713) {
			c.getPA().showInterface(17537);
		}
		if(itemId == 2712) {
			c.getPA().showInterface(9043);
		}
		if(itemId == 2711) {
			c.getPA().showInterface(7271);
		}
		if(itemId == 2710) {
			c.getPA().showInterface(7045);
		}
		if(itemId == 2709) {
		c.getPA().showInterface(9275);
		}
		if(itemId == 2708) {
		c.getPA().showInterface(7113);
		}
		if(itemId == 2707) {
		c.getPA().showInterface(17634);
		}
		if(itemId == 2706) {
		c.getPA().showInterface(17620);
		}
		if(itemId == 2705) {
		c.getPA().showInterface(4305);
		}*/
        if (itemId == 2704) {
            for (int i = 6968; i < 6976; i++) {
                c.getPA().sendFrame126("", i);
            }
            c.getPA().sendFrame126("In a liar of a Boss lies", 6971);
            c.getPA().sendFrame126("the next clue scroll!", 6972);
            c.getPA().showInterface(6965);
        }
        if (itemId == 2703) {
            for (int i = 6968; i < 6976; i++) {
                c.getPA().sendFrame126("", i);
            }
            c.getPA().sendFrame126("Search the blacksmith's", 6971);
            c.getPA().sendFrame126("bookcase located in Shilo Village", 6972);
            c.getPA().showInterface(6965);
        }
        if (itemId == 2702) {
            for (int i = 6968; i < 6976; i++) {
                c.getPA().sendFrame126("", i);
            }
            c.getPA().sendFrame126("We are here lying to protect", 6971);
            c.getPA().sendFrame126("the castle that we truly love!", 6972);
            c.getPA().showInterface(6965);
        }
        if (itemId == 2701) {
            for (int i = 6968; i < 6976; i++) {
                c.getPA().sendFrame126("", i);
            }
            c.getPA().sendFrame126("This has to be bob's favorite", 6971);
            c.getPA().sendFrame126("training spot in-game.", 6972);
            c.getPA().showInterface(6965);
        }
        if (itemId == 2700) {
            for (int i = 6968; i < 6976; i++) {
                c.getPA().sendFrame126("", i);
            }
            c.getPA().sendFrame126("We all love water, especially", 6971);
            c.getPA().sendFrame126("from big, clean, fountains!", 6972);
            c.getPA().showInterface(6965);
        }
        if (itemId == 2699) {
            for (int i = 6968; i < 6976; i++) {
                c.getPA().sendFrame126("", i);
            }
            c.getPA().sendFrame126("I love to eat cake, maybe", 6971);
            c.getPA().sendFrame126("you want to seal some?", 6972);
            c.getPA().showInterface(6965);
        }
        if (itemId == 2698) {
            for (int i = 6968; i < 6976; i++) {
                c.getPA().sendFrame126("", i);
            }
            c.getPA().sendFrame126("We stall seek history within", 6971);
            c.getPA().sendFrame126("the ancient museum.", 6972);
            c.getPA().showInterface(6965);
        }
		/*if(itemId == 2697) {
		for(int i = 6968; i < 6976; i++) {
			c.getPA().sendFrame126("", i);
		}
			c.getPA().sendFrame126("We pay to Pk, especially", 6971);
			c.getPA().sendFrame126("within a city named Falador.", 6972);
			c.getPA().showInterface(6965);
		}
		if(itemId == 2696) {
		for(int i = 6968; i < 6976; i++) {
			c.getPA().sendFrame126("", i);
		}
			c.getPA().sendFrame126("Rats! Rats! Rats!", 6971);
			c.getPA().sendFrame126("The sewers are full of them!", 6972);
			c.getPA().showInterface(6965);
		}
		if(itemId == 2695) {
		for(int i = 6968; i < 6976; i++) {
			c.getPA().sendFrame126("", i);
		}
			c.getPA().sendFrame126("These fish must be hot!", 6971);
			c.getPA().sendFrame126("We shall call this, Lava Fishing", 6972);
			c.getPA().showInterface(6965);
		}
		//if(itemId == 2694) {
		//c.sendMessage("My loved one..Once murdered in front of my eyes..I couldn't save her..");
		//}
		if(itemId == 2693) {
		for(int i = 6968; i < 6976; i++) {
			c.getPA().sendFrame126("", i);
		}
			c.getPA().sendFrame126("This village contains torches,", 6971);
			c.getPA().sendFrame126("rocks, and some kind of stronghold.", 6972);
			c.getPA().showInterface(6965);
		}
		if(itemId == 2692) {
		for(int i = 6968; i < 6976; i++) {
			c.getPA().sendFrame126("", i);
		}
			c.getPA().sendFrame126("We shall thieve Master Farmers!", 6971);
			c.getPA().sendFrame126("I wonder where I can find them...", 6972);
			c.getPA().showInterface(6965);
		}
		if(itemId == 2691) {
		for(int i = 6968; i < 6976; i++) {
			c.getPA().sendFrame126("", i);
		}
			c.getPA().sendFrame126("arggggghhh mate,", 6971);
			c.getPA().sendFrame126("Would you like some beer?", 6972);
			c.getPA().showInterface(6965);
		}
		if(itemId == 2690) {
		for(int i = 6968; i < 6976; i++) {
			c.getPA().sendFrame126("", i);
		}
			c.getPA().sendFrame126("Cabbage!", 6971);
			c.getPA().sendFrame126("Lots, and lots of Cabbage!", 6972);
			c.getPA().showInterface(6965);
		}
		if(itemId == 2689) {
		for(int i = 6968; i < 6976; i++) {
			c.getPA().sendFrame126("", i);
		}
			c.getPA().sendFrame126("Ew, a scorpian.", 6971);
			c.getPA().sendFrame126("Why are these mines so messed up!", 6972);
			c.getPA().showInterface(6965);
		}
		if(itemId == 2688) {
		for(int i = 6968; i < 6976; i++) {
			c.getPA().sendFrame126("", i);
		}
			c.getPA().sendFrame126("I seek many, many, banana trees.", 6971);
			c.getPA().sendFrame126("Do you know where it is?", 6972);
			c.getPA().showInterface(6965);
		}*/
        /**Hard Clue Scroll**/
		/*if(c.safeAreas(2969, 3411, 2974, 3415) & (c.getItems().playerHasItem(2713,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2713, c.getItems().getItemSlot(2713), 1);
				c.getItems().addItem(2712,1);
				c.sendMessage("You recieve another scroll.");
			} else if(c.safeAreas(2613, 3075, 2619, 3080) & (c.getItems().playerHasItem(2712,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2712, c.getItems().getItemSlot(2712), 1);
				c.getItems().addItem(2711,1);
				c.sendMessage("You recieve another scroll.");
			} else if(c.safeAreas(3030, 3394, 3049, 3401) & (c.getItems().playerHasItem(2711,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2711, c.getItems().getItemSlot(2711), 1);
				c.getItems().addItem(2775,1);
				c.sendMessage("You recieve a HARD Casket!");
			} else if(c.safeAreas(3285, 3371, 3291, 3375) & (c.getItems().playerHasItem(2710,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2710, c.getItems().getItemSlot(2710), 1);
				c.getItems().addItem(2709,1);
				c.sendMessage("You recieve another scroll.");
			} else if(c.safeAreas(3106, 3148, 3113, 3154) & (c.getItems().playerHasItem(2709,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2709, c.getItems().getItemSlot(2709), 1);
				c.getItems().addItem(2708,1);
				c.sendMessage("You recieve another scroll.");
			} else if(c.safeAreas(3092, 3213, 3104, 3225) & (c.getItems().playerHasItem(2708,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2708, c.getItems().getItemSlot(2708), 1);
				c.getItems().addItem(2775,1);
				c.sendMessage("You recieve a HARD Casket!");
			} else if(c.safeAreas(2719, 3336, 2725, 3339) & (c.getItems().playerHasItem(2707,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2707, c.getItems().getItemSlot(2707), 1);
				c.getItems().addItem(2706,1);
				c.sendMessage("You recieve another scroll.");
			} else if(c.safeAreas(3301, 3684, 3313, 3698) & (c.getItems().playerHasItem(2706,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2706, c.getItems().getItemSlot(2706), 1);
				c.getItems().addItem(2705,1);
				c.sendMessage("You receive another scroll.");
			} else if(c.safeAreas(2903, 3287, 2909, 3300) & (c.getItems().playerHasItem(2705,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2705, c.getItems().getItemSlot(2705), 1);
				c.getItems().addItem(2775,1);
				c.sendMessage("You receive a HARD Casket!");*/
        /**Easy Clue Scrolls**/
			/*} else */
        if (c.safeAreas(2259, 4680, 2287, 4711) & (c.getItems().playerHasItem(2704, 1))) {
            c.getPA().removeAllWindows();
            c.getItems().deleteItem(2704, c.getItems().getItemSlot(2704), 1);
            c.getItems().addItem(2703, 1);
            c.sendMessage("You receive another scroll.");
        } else if (c.safeAreas(3217, 3207, 3225, 3213) & (c.getItems().playerHasItem(2703, 1))) {
            c.getPA().removeAllWindows();
            c.getItems().deleteItem(2703, c.getItems().getItemSlot(2703), 1);
            c.getItems().addItem(2702, 1);
            c.sendMessage("You receive another scroll.");
        } else if (c.safeAreas(2962, 3331, 2987, 3351) & (c.getItems().playerHasItem(2702, 1))) {
            c.getPA().removeAllWindows();
            c.getItems().deleteItem(2702, c.getItems().getItemSlot(2702), 1);
            c.getItems().addItem(2714, 1);
            c.sendMessage("You receive a EASY Casket!");
        } else if (c.safeAreas(3253, 3256, 3265, 3296) & (c.getItems().playerHasItem(2701, 1))) {
            c.getPA().removeAllWindows();
            c.getItems().deleteItem(2701, c.getItems().getItemSlot(2701), 1);
            c.getItems().addItem(2700, 1);
            c.sendMessage("You receive another scroll.");
        } else if (c.safeAreas(3208, 3421, 3220, 3435) & (c.getItems().playerHasItem(2700, 1))) {
            c.getPA().removeAllWindows();
            c.getItems().deleteItem(2700, c.getItems().getItemSlot(2700), 1);
            c.getItems().addItem(2699, 1);
            c.sendMessage("You receive another scroll.");
        } else if (c.safeAreas(3084, 3486, 3086, 3488) & (c.getItems().playerHasItem(2699, 1))) {
            c.getPA().removeAllWindows();
            c.getItems().deleteItem(2699, c.getItems().getItemSlot(2699), 1);
            c.getItems().addItem(2698, 1);
            c.sendMessage("You receive another scroll.");
        } else if (c.safeAreas(3253, 3445, 3261, 3453) & (c.getItems().playerHasItem(2698, 1))) {
            c.getPA().removeAllWindows();
            c.getItems().deleteItem(2698, c.getItems().getItemSlot(2698), 1);
            c.getItems().addItem(2714, 1);
            c.sendMessage("You receive a EASY Casket!");
            /**Medium Clue Scrolls**/
			/*} else if(c.safeAreas(2953, 3365, 2977, 3392) & (c.getItems().playerHasItem(2697,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2697, c.getItems().getItemSlot(2697), 1);
				c.getItems().addItem(2696,1);
				c.sendMessage("You recieve another scroll.");
			} else if(c.safeAreas(3228, 9860, 3259, 9873) & (c.getItems().playerHasItem(2696,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2696, c.getItems().getItemSlot(2696), 1);
				c.getItems().addItem(2695,1);
				c.sendMessage("You recieve another scroll.");
			} else if(c.safeAreas(2875, 9763, 2904, 9776) & (c.getItems().playerHasItem(2695,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2695, c.getItems().getItemSlot(2695), 1);
				c.getItems().addItem(2802,1);
				c.sendMessage("You recieve a MEDIUM Casket!");
			} else if(c.safeAreas(3074, 3407, 3085, 3436) & (c.getItems().playerHasItem(2693,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2693, c.getItems().getItemSlot(2693), 1);
				c.getItems().addItem(2692,1);
				c.sendMessage("You recieve another scroll.");
			} else if(c.safeAreas(3074, 3245, 3085, 3255) & (c.getItems().playerHasItem(2692,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2692, c.getItems().getItemSlot(2692), 1);
				c.getItems().addItem(2691,1);
				c.sendMessage("You recieve another scroll.");
			} else if(c.safeAreas(3044, 3255, 3055, 3259) & (c.getItems().playerHasItem(2691,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2691, c.getItems().getItemSlot(2691), 1);
				c.getItems().addItem(2802,1);
				c.sendMessage("You recieve a MEDIUM Casket!");
			} else if(c.safeAreas(3041, 3284, 3067, 3298) & (c.getItems().playerHasItem(2690,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2690, c.getItems().getItemSlot(2690), 1);
				c.getItems().addItem(2689,1);
				c.sendMessage("You recieve another scroll.");
			} else if(c.safeAreas(3032, 9756, 3056, 9804) & (c.getItems().playerHasItem(2689,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2689, c.getItems().getItemSlot(2689), 1);
				c.getItems().addItem(2688,1);
				c.sendMessage("You recieve another scroll.");
			} else if(c.safeAreas(2906, 3155, 2926, 3175) & (c.getItems().playerHasItem(2688,1))) {
				c.getPA().removeAllWindows();
				c.getItems().deleteItem(2688, c.getItems().getItemSlot(2688), 1);
				c.getItems().addItem(2802,1);
				c.sendMessage("You recieve a MEDIUM Casket!");*/
        }
        if (itemId >= 5509 && itemId <= 5514) {
            int pouch = -1;
            int a = itemId;
            if (a == 5509)
                pouch = 0;
            if (a == 5510)
                pouch = 1;
            if (a == 5512)
                pouch = 2;
            if (a == 5514)
                pouch = 3;
            c.getPA().fillPouch(pouch);
            return;
        }
        if (Food.isFood(itemId))
            Food.eat(c, itemId, itemSlot);
        if (itemId == 1971)
            Food.eatKebab(c, itemSlot);
        if (c.getPrayer().isBone(itemId))
            c.getPrayer().buryBone(itemId, itemSlot);
        if (itemId == 405) {
            c.getItems().deleteItem(405, 1);
            c.getItems().addItem(995, Misc.random(50000));
            c.sendMessage("You find some coins in the casket.");
            if (Misc.random(19) == 0) {
                c.getItems().addItem(
                        CASKET_REWARDS[Misc.random(getLength() - 1)], 1);
                c.sendMessage("Congratulations, you find a rare item in the casket!");
            }
        }
        if (c.getPotions().isPotion(c, itemId))
            c.getPotions().handlePotion(c, itemId, itemSlot);
        if (itemId == 952) {
            c.sendMessage("You start digging...");
            if (c.inArea(3553, 3301, 3561, 3294)) {
                c.teleTimer = 3;
                c.newLocation = 1;
            } else if (c.inArea(3550, 3287, 3557, 3278)) {
                c.teleTimer = 3;
                c.newLocation = 2;
            } else if (c.inArea(3561, 3292, 3568, 3285)) {
                c.teleTimer = 3;
                c.newLocation = 3;
            } else if (c.inArea(3570, 3302, 3579, 3293)) {
                c.teleTimer = 3;
                c.newLocation = 4;
            } else if (c.inArea(3571, 3285, 3582, 3278)) {
                c.teleTimer = 3;
                c.newLocation = 5;
            } else if (c.inArea(3562, 3279, 3569, 3273)) {
                c.teleTimer = 3;
                c.newLocation = 6;
            } else if (c.inArea(2835, 3336, 2835, 3336)) {
                c.teleTimer = 3;
                c.newLocation = 7;
            } else if (c.inArea(2834, 3336, 2834, 3336)) {
                c.teleTimer = 3;
                c.newLocation = 8;
            }
        }
    }

    public String[] horseChat = {"Come on Dobbin, we can win the race!",
            "Hi-ho Silver, and away!", "Neaahhhyyy! Giddy-up horsey!"};

}
