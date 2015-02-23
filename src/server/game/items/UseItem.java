package server.game.items;

import core.util.Misc;
import server.clip.region.ObjectDef;
import server.content.BowStringing;
import server.content.FlourMill;
import server.content.quests.dialogue.RestlessGhostD;
import server.content.quests.misc.Tutorialisland;
import server.content.skills.Fletching;
import server.content.skills.Grinding;
import server.content.skills.PotionMaking;
import server.content.skills.Runecrafting;
import server.content.skills.cooking.Cooking;
import server.content.skills.crafting.*;
import server.content.skills.misc.SmithingInterface;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.game.items.food.PotionMixing;
import server.game.npcs.NPCHandler;
import server.game.players.Client;

/**
 * @author Ryan / Lmctruck30 & 1% Izumi
 */

public class UseItem {

    public static void ItemonObject(Client c, int objectID, int objectX, int objectY, int itemId) {
        c.clickObjectType = 0;
        // Object Names
        final String name = ObjectDef.getObjectDef(objectID).name;
        if (name.equalsIgnoreCase("fire") || name.equalsIgnoreCase("cooking range")
                || name.equalsIgnoreCase("stove")) {
            Cooking.cookThisFood(c, itemId, objectID);
        }
        // END
        if (!c.getItems().playerHasItem(itemId, 1))
            return;
        switch (objectID) {
            case 884:
                if (itemId == 229)
                    c.startAnimation(832);
                c.getItems().deleteItem(229, 1);
                c.getItems().addItem(227, 1);
                break;
            case 2781:
                if (itemId == 2357) {
                    JewelryMaking.jewelryInterface(c);
                    return;
                } else if (itemId == 1781 || itemId == 1783) {
                    MoltenGlass.sendInterface(c, objectID);
                }
                break;
            case 2645:
                if (c.getItems().playerHasItem(1925, 1)) {
                    if (itemId == 1925)
                        c.getItems().deleteItem(1925, 1);
                    c.getItems().addItem(1783, 1);
                    c.sendMessage("You fill the bucket with sand.");
                }
                break;
            case 2644:
                c.getFlaxStringer().itemOnObject(itemId);
                break;

            case 2642:// potters table
                Pottery.sendPotteryInterface(c, itemId, "unfired");
                break;
            case 11601: // furnace for pottery
                Pottery.sendPotteryInterface(c, itemId, "fired");
                break;
            case 2714:
                FlourMill.grainOnHopper(c, objectID, itemId);
                break;
            case 2782:
                if (c.doricsQ == 3) {
                    if (!c.getItems().playerHasItem(2347, 1)) {
                        c.sendMessage("You need a hammer to smith on an anvil.");
                        return;
                    }
                    SmithingInterface.showSmithInterface(c, itemId);
                } else {
                    c.sendMessage("Doric will not let you use his anvils");
                }
                break;
            case 2783:
                if (!c.getItems().playerHasItem(2347, 1)) {
                    c.sendMessage("You need a hammer to smith on an anvil.");
                    return;
                }
                switch (itemId) {
                    case 686:
                        c.startAnimation(898);
                        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                c.getPA().rustySword(c);
                                container.stop();
                            }

                            @Override
                            public void stop() {

                            }
                        }, 4);
                        break;
                    case 697:
                        c.startAnimation(898);
                        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                c.getPA().brokenArmour(c);
                                container.stop();
                            }

                            @Override
                            public void stop() {

                            }
                        }, 4);
                        break;
                }
                SmithingInterface.showSmithInterface(c, itemId);
                break;
            case 8689:
                if (c.getItems().playerHasItem(1925, 1)) {
                    c.startAnimation(2305);
                    c.getItems().deleteItem(1925, 1);
                    c.getItems().addItem(1927, 1);
                    c.sendMessage("You milked the cow to get a fresh bucket of milk.");
                } else {
                    c.sendMessage("You need a bucket to milk this cow!");
                }
                break;

            case 2452:
            case 2453:
            case 2454:
            case 2455:
            case 2456:
            case 2457:
            case 2458:
            case 2459:
            case 2460:
            case 2461:
            case 2462:
                Runecrafting.enterAltar(c, objectID, itemId);
                break;
            case 9374:
            case 2728:
            case 25465:
            case 11404:
            case 11406:
                Cooking.cookThisFood(c, itemId, objectID);
                break;

            case 3039:
                Cooking.cookThisFood(c, itemId, objectID);
                Tutorialisland.sendDialogue(c, 3037, 942);
                break;
            case 2145:
                switch (itemId) {
                    case 553:
                        c.sendMessage("You should open the coffin first");
                    default:
                        c.sendMessage("Nothing interesting happens.");
                        break;
                }
                break;
            case 2146:
                switch (itemId) {
                    case 553:
                        if (c.restlessG == 3) {
                            RestlessGhostD.dialogue(c, 606, 507);
                        } else {
                            c.sendMessage("Nothing interesting happens.");
                        }
                        break;
                    default:
                        c.sendMessage("Nothing interesting happens.");
                        break;
                }
                break;
            default:
                if (c.playerRights >= 2)
                    Misc.println("Player At Object id: " + objectID + " with Item id: " + itemId);
                if (name.equalsIgnoreCase("furnace") && itemId == 2357) {
                    JewelryMaking.jewelryInterface(c);
                }
                break;

        }
    }

    public static void ItemonItem(final Client c, final int itemUsed, final int useWith, final int itemUsedSlot, final int usedWithSlot) {
        if (c.getItems().getItemName(itemUsed).contains("(") && c.getItems().getItemName(useWith).contains("("))
            PotionMixing.mixPotion2(c, itemUsed, useWith);
        if (itemUsed == 1755 || useWith == 1755) {
            GemCutting.cutGem(c, itemUsed, useWith);
        }
        if (itemUsed == 1733 || useWith == 1733) {
            LeatherMaking.craftLeatherDialogue(c, itemUsed, useWith);
        }
        if (itemUsed == 227 || useWith == 227) {
            PotionMaking.createUnfinishedPotion(c, itemUsed, useWith);
        }
        PotionMaking.createPotion(c, itemUsed, useWith);
        if (itemUsed == 233 || useWith == 233) {
            Grinding.grindItem(c, itemUsed, useWith, (itemUsed == 233 ? usedWithSlot : itemUsedSlot));
        }
        if ((useWith == 1511 || itemUsed == 1511) && (useWith == 946 || itemUsed == 946)) {
            Fletching.normal(c, itemUsed, useWith);
        } else if (useWith == 946 || itemUsed == 946) {
            Fletching.others(c, itemUsed, useWith);
        } else if (Fletching.arrows(c, itemUsed, useWith)) {
            Fletching.makeArrows(c, itemUsed, useWith);
        }
        if (itemUsed == 1777 || useWith == 1777) {
            BowStringing.stringBow(c, itemUsed, useWith);
        }
        if (itemUsed == 590 && useWith == 36 || itemUsed == 36 && useWith == 1929) {
            c.getItems().deleteItem(36, 1);
            c.getItems().addItem(33, 1);
        }
// TUTORIAL ISLAND
        if (itemUsed == 1929 && useWith == 1933 || itemUsed == 1933 && useWith == 1929) {
            c.getItems().deleteItem(1929, 1);
            c.getItems().deleteItem(1933, 1);
            c.getItems().addItem(2307, 1);
            c.getItems().addItem(1925, 1);
            c.getItems().addItem(1931, 1);
            if (c.tutorialprog == 8)
                Tutorialisland.sendDialogue(c, 3026, 0);
        }


        switch (itemUsed) {

            default:
                if (c.playerRights == 3)
                    Misc.println("Player used Item id: " + itemUsed + " with Item id: " + useWith);
                break;
        }
    }

    public static void ItemonNpc(Client c, int itemId, int npcId, int slot) {
        switch (itemId) {
            case 1735:
                if (npcId == 43 || npcId == 1765) {
                    SpinningWheel.sheerNPC(c);
                }
                break;
            case 7421:
                if (npcId == 3345 || npcId == 3344) {
                    for (int k = 0; k < NPCHandler.maxNPCs; k++) {
                        if (NPCHandler.npcs[k] != null) {
                            if (NPCHandler.npcs[k].HP < NPCHandler.npcs[k].HP / 10) {
                                c.sendMessage("Hello");
                                return;
                            }
                            if (NPCHandler.npcs[k].isDead)
                                return;
                            if (NPCHandler.npcs[k].HP >= NPCHandler.npcs[k].HP / 10) {
                                int damg = 200;
                                NPCHandler.npcs[k].handleHitMask(damg);
                                NPCHandler.npcs[k].HP -= damg;
                                NPCHandler.npcs[k].hitUpdateRequired2 = true;
                                NPCHandler.npcs[k].hitUpdateRequired = true;
                            }
                        }
                    }
                }
                break;
            default:
                if (c.playerRights >= 3)
                    Misc.println("Player used Item id: " + itemId + " with Npc id: " + npcId + " With Slot : " + slot);
                break;
        }

    }


}
