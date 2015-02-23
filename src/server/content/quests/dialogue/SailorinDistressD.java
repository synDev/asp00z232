package server.content.quests.dialogue;

import server.Server;
import server.content.quests.misc.QuestHandling;
import server.game.players.Client;
import server.game.players.DialogueHandler;

/**
 * Made by someotherdude
 */
public class SailorinDistressD {
    /**
     * Cook's Assistant dialogue
     *
     * @param c
     * @param dialogue
     * @param npcId
     */
    public static void dialogue(Client c, int dialogue, int npcId) {
        c.talkingNpc = npcId;
        switch (dialogue) {
            case 250:
                DialogueHandler.sendNpcChat1(c, "Darn Goblins just won't give me a break!",
                        c.talkingNpc, "Owen the Sailor");
                c.nextChat = 251;
                break;
            case 251:
                DialogueHandler.sendPlayerChat1(c, "What's wrong?");
                c.nextChat = 252;
                break;
            case 252:
                DialogueHandler
                        .sendNpcChat2(
                                c,
                                "I was sent some directions from King Kiel by messenger.",
                                "Before I could get them a goblin killed the messenger",
                                c.talkingNpc, "Owen the Sailor");
                c.nextChat = 253;
                break;
            case 253:
                DialogueHandler.sendNpcChat2(c,
                        "Without those directions I don't know",
                        "what supplies I need to bring to our army!",
                        c.talkingNpc, "Owen the Sailor");
                c.nextChat = 254;
                break;
            case 254:
                DialogueHandler.sendOption2(c, "I could try to help.",
                        "Oh well good luck with that.");
                c.dialogueAction = 254;
                break;
            case 256: // accept quest
                c.SailA = 1;
                c.getPA().loadQuests();
                Server.npcHandler.spawnNpc(c, 101, 2763, 2953, 1, 0, 5, 1,
                        10, 3, true, true);
                c.nextChat = 0;
                break;
            case 255:
                DialogueHandler.sendNpcChat1(c,
                        "Go and try to find the Goblin that took the directions...", c.talkingNpc,
                        "Owen the Sailor");
                c.nextChat = 260;
                break;
            case 260:
                DialogueHandler.sendNpcChat1(c,
                        " ........... There he is now!", c.talkingNpc,
                        "Owen the Sailor");
                c.nextChat = 256;
                break;
            case 257:// refuse quest
                DialogueHandler.sendPlayerChat1(c, "Oh well good luck with that.");
                c.nextChat = 0;
                break;
            case 258:
                DialogueHandler.sendPlayerChat1(c, "Here are the directions Owen.");
                c.nextChat = 259;
                break;
            case 259:
                DialogueHandler.sendNpcChat2(c,
                        "Thank you very much, now just let me have a look at it",
                        "  ",
                        c.talkingNpc, "Owen the Sailor");
                c.nextChat = 261;
                break;
            case 261:
                DialogueHandler.sendNpcChat2(c,
                        "The directions say that I need to bring food, extra",
                        "weapons and beer if I can get my hands on any of it.", c.talkingNpc, "Owen the Sailor");
                c.nextChat = 262;
                c.SailA = 3;
                break;
            case 262:
                DialogueHandler.sendNpcChat1(c,
                        "Would you mind going and getting the materials I need?", c.talkingNpc, "Owen the Sailor");
                c.nextChat = 263;
                break;
            case 263:
                DialogueHandler.sendOption2(c, "Sure thing!",
                        "No I'm done helping you.");
                c.dialogueAction = 254;
                break;
            case 264:// refuse quest
                DialogueHandler.sendPlayerChat1(c, "Sure thing!");
                c.nextChat = 266;
                c.SailA = 4;
                break;
            case 265:// refuse quest
                DialogueHandler.sendPlayerChat1(c, "No I'm done helping you.");
                c.nextChat = 0;
                break;
            case 266:
                DialogueHandler.sendNpcChat2(c,
                        "Okay great, now for the food 10 pieces of cooked beef ",
                        "should be enough to suffice.", c.talkingNpc, "Owen the Sailor");
                c.nextChat = 267;
                break;
            case 267:
                DialogueHandler.sendNpcChat1(c,
                        "As far as the weapons go, I'll need 10 bronze swords ",
                        c.talkingNpc, "Owen the Sailor");
                c.nextChat = 268;
                break;
            case 268:
                DialogueHandler.sendNpcChat2(c,
                        "Lastly, the troops will need a good quantity of beer",
                        "so if you could find a keg, that would be fantastic",
                        c.talkingNpc, "Owen the Sailor");
                c.nextChat = 269;
                break;
            case 269:
                DialogueHandler.sendNpcChat2(c,
                        "You might be able to find a keg of beer in one of the",
                        "crates near the furnace in Shilo Village.",
                        c.talkingNpc, "Owen the Sailor");
                c.nextChat = 270;
                break;
            case 270://
                DialogueHandler.sendPlayerChat1(c, "Alright I'll do my best to get what you need.");
                c.nextChat = 0;
                break;
            case 271://
                DialogueHandler.sendPlayerChat1(c, "Here are the items you needed.");
                c.nextChat = 273;
                c.getItems().deleteItem3(2142, 10);
                c.getItems().deleteItem3(1277, 10);
                c.getItems().deleteItem(3711, 1);
                c.SailA = 5;
                break;
            case 272://
                DialogueHandler.sendNpcChat1(c,
                        "Good luck in finding the items!",
                        c.talkingNpc, "Owen the Sailor");
                c.nextChat = 0;
                break;
            case 273:
                DialogueHandler.sendNpcChat2(c,
                        "Thank you so much for all your help, there's a",
                        "small island not too far from here.",
                        c.talkingNpc, "Owen the Sailor");
                c.nextChat = 274;
                break;
            case 274:
                DialogueHandler.sendNpcChat2(c,
                        "It has many good spots for gathering, if you ever want",
                        "to go there just talk to me and i'll sail you there.",
                        c.talkingNpc, "Owen the Sailor");
                c.nextChat = 275;
                break;
            case 275:
                c.SailA = 6;
                QuestHandling.sailFinish(c);
                break;
            case 277://
                DialogueHandler.sendNpcChat1(c,
                        "Would you like to Sail to Entrana?",
                        c.talkingNpc, "Owen the Sailor");
                c.nextChat = 278;
                break;
            case 278:
                DialogueHandler.sendOption2(c, "Yes",
                        "No");
                c.dialogueAction = 254;
                break;
            case 279:
                DialogueHandler.sendPlayerChat1(c, "No thanks");
                c.nextChat = 0;
                break;

        }

    }
}
