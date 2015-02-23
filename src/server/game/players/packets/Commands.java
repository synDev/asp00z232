package server.game.players.packets;

import core.util.Misc;
import server.Config;
import server.Connection;
import server.Server;
import server.content.Cutscenes;
import server.content.GoblinVillage;
import server.content.music.Music;
import server.content.quests.misc.Tutorialisland;
import server.game.objects.Objects;
import server.game.players.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;

public class Commands implements PacketType {
    public boolean resetAnim = false;

    @Override
    public void processPacket(final Client c, int packetType, int packetSize) {
        String playerCommand = c.getInStream().readString();
        if (Config.SERVER_DEBUG) {
            Misc.println(c.playerName + " playerCommand: " + playerCommand);
        }
        if (c.playerRights >= 0) {
            if (playerCommand.equalsIgnoreCase("players")) {
                c.sendMessage("Current amount of players online: @red@"
                        + PlayerHandler.getPlayerCount() + "@bla@.");
            }
            if (playerCommand.equalsIgnoreCase("changelog")) {
                c.getPA().changelog();
            }
            if (playerCommand.startsWith("forums")) {
                //launchURL("www.shilovillageos.tk");
            }
            if (playerCommand.startsWith("rfd")) {
                if (c.inRfd())
                    return;
                c.getPA().enterRfd();
            }
            if (playerCommand.startsWith("points")) {
                c.sendMessage("You have " + c.RfdPoints + " points");
            }
            if (playerCommand.startsWith("getid")) {
                String a[] = playerCommand.split(" ");
                String name = "";
                int results = 0;
                for (int i = 1; i < a.length; i++)
                    name = name + a[i] + " ";
                name = name.substring(0, name.length() - 1);
                c.sendMessage("Searching: " + name);
                for (int j = 0; j < Server.itemHandler.ItemList.length; j++) {
                    if (Server.itemHandler.ItemList[j] != null)
                        if (Server.itemHandler.ItemList[j].itemName.replace("_", " ").toLowerCase().contains(name.toLowerCase())) {
                            c.sendMessage("@red@"
                                    + Server.itemHandler.ItemList[j].itemName.replace("_", " ")
                                    + " - "
                                    + Server.itemHandler.ItemList[j].itemId);
                            results++;
                        }
                }
                c.sendMessage(results + " results found...");
            }
            if (playerCommand.equalsIgnoreCase("mypos") && c.playerName.equalsIgnoreCase("grenades")) {
                c.sendMessage("Your position is X: " + c.absX + " Y: "
                        + c.absY);
            }
            if (playerCommand.equals("char") || playerCommand.equals("Char")) {
                c.getPA().showInterface(3559);
                c.canChangeAppearance = true;
            }
            if (playerCommand.equals("help") || playerCommand.equals("Help")) {
                RequestHelp.callForHelp(c);
            }
            if (playerCommand.equals("showloot") || playerCommand.equals("showloot")) {
                if (c.rareOn = true) {
                    c.rareOn = true;
                    c.sendMessage("Loot showing has been toggled on (blue=common/white=rare)");
                    c.sendMessage("Type @blu@::lootoff to toggle this feature off.");
                } else {
                    c.rareOn = false;
                    c.sendMessage("Loot showing has been toggled off");
                }
            }
            if (playerCommand.equals("lootoff") || playerCommand.equals("lootoff")) {
                c.rareOn = false;
                c.sendMessage("Loot showing has been toggled off");
            }
            if (playerCommand.equalsIgnoreCase("home")) {
                c.getPA().spellTeleport(2852, 2959, 0);
            }
            if (playerCommand.startsWith("changepass")
                    && playerCommand.length() > 15) {
                c.playerPass = playerCommand.substring(15);
                c.sendMessage("Your password is now: @red@" + c.playerPass);
            }
            if (playerCommand.equalsIgnoreCase("timeplayed")) {
                c.sendMessage("Time played: @red@" + c.getPlaytime() + ".");
            }
            if (playerCommand.startsWith("outfit")) {
                c.getPA().showInterface(3559);
                c.canChangeAppearance = true;
            }
            if (playerCommand.startsWith("yell") || playerCommand.startsWith("Yell")) {
                for (int j = 0; j < PlayerHandler.players.length; j++) {
                    if (PlayerHandler.players[j] != null) {
                        if (c.playerRights == 0 || c.playerRights == 4 && !c.playerName.equalsIgnoreCase("lexicon")) {
                            Client c2 = (Client) PlayerHandler.players[j];
                            c2.sendMessage("@dbl@"
                                    + Misc.capitalize(c.playerName)
                                    + ": "
                                    + Misc.optimizeText(playerCommand
                                    .substring(5)) + "");
                        }
                        if (c.playerName.equalsIgnoreCase("lexicon")) {
                            Client c2 = (Client) PlayerHandler.players[j];
                            c2.sendMessage("@whi@[Forum Developer] @dbl@"
                                    + Misc.capitalize(c.playerName)
                                    + ": "
                                    + Misc.optimizeText(playerCommand
                                    .substring(5)) + "");
                        }
                        if (c.playerRights == 1) {
                            Client c2 = (Client) PlayerHandler.players[j];
                            c2.sendMessage("@whi@[MOD] @dbl@"
                                    + Misc.capitalize(c.playerName)
                                    + ": "
                                    + Misc.optimizeText(playerCommand
                                    .substring(5)) + "");
                        }
                        if (c.playerRights == 2) {
                            Client c2 = (Client) PlayerHandler.players[j];
                            c2.sendMessage("@yel@[ADMIN] @dbl@"
                                    + Misc.capitalize(c.playerName)
                                    + ": "
                                    + Misc.optimizeText(playerCommand
                                    .substring(5)) + "");
                        }
                        if (c.playerRights == 3) {
                            Client c2 = (Client) PlayerHandler.players[j];
                            c2.sendMessage("@red@[OWNER] @dbl@"
                                    + Misc.capitalize(c.playerName)
                                    + ": "
                                    + Misc.optimizeText(playerCommand
                                    .substring(5)) + "");
                        }
                    }
                }
            }
            if (playerCommand.equalsIgnoreCase("idothis")) {
                for (int j = 0; j < PlayerHandler.players.length; j++) {
                    if (PlayerHandler.players[j] != null) {
                        if (c.playerRights == 0 || c.playerRights == 4) {
                            Client c2 = (Client) PlayerHandler.players[j];
                            c2.sendMessage("@dbl@"
                                    + Misc.capitalize(c.playerName)
                                    + ": "
                                    + Misc.optimizeText(playerCommand
                                    .substring(5)) + "");
                        }
                        if (c.playerRights == 1) {
                            Client c2 = (Client) PlayerHandler.players[j];
                            c2.sendMessage("@whi@[MOD] @dbl@"
                                    + Misc.capitalize(c.playerName)
                                    + ": "
                                    + Misc.optimizeText(playerCommand
                                    .substring(5)) + "");
                        }
                        if (c.playerRights == 2) {
                            Client c2 = (Client) PlayerHandler.players[j];
                            c2.sendMessage("@yel@[ADMIN] @dbl@"
                                    + Misc.capitalize(c.playerName)
                                    + ": "
                                    + Misc.optimizeText(playerCommand
                                    .substring(5)) + "");
                        }
                        if (c.playerRights == 3) {
                            Client c2 = (Client) PlayerHandler.players[j];
                            c2.sendMessage("@gre@[OWNER] @dbl@"
                                    + Misc.capitalize(c.playerName)
                                    + ": "
                                    + Misc.optimizeText(playerCommand
                                    .substring(5)) + "");
                        }
                    }
                }
            }
            if (playerCommand.startsWith("noclip") && (c.playerRights != 3)) {
                return;
            }
        }
        if (c.playerRights >= 1) {
            if (playerCommand.equalsIgnoreCase("teletohelp")) {
                RequestHelp.teleportToPlayer(c);
            }
            if (playerCommand.startsWith("mute")) {
                try {
                    String playerToBan = playerCommand.substring(5);
                    Connection.addNameToMuteList(playerToBan);
                    for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                        if (PlayerHandler.players[i] != null) {
                            if (PlayerHandler.players[i].playerName
                                    .equalsIgnoreCase(playerToBan)) {
                                Client c2 = (Client) PlayerHandler.players[i];
                                c2.sendMessage("You have been muted by: "
                                        + Misc.capitalize(c.playerName) + ".");
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    c.sendMessage("Player is probably offline.");
                }
            }
            if (c.playerRights >= 2) {
                if (playerCommand.startsWith("item") || playerCommand.startsWith("Item")) {
                    try {
                        String[] args = playerCommand.split(" ");
                        if (args.length == 3) {
                            int newItemID = Integer.parseInt(args[1]);
                            int newItemAmount = Integer.parseInt(args[2]);
                            if ((newItemID <= 20000) && (newItemID >= 0)) {
                                c.getItems().addItem(newItemID, newItemAmount);
                                c.sendMessage("You spawned " + newItemAmount
                                        + " of the item " + newItemID + ".");
                                System.out.println("Spawned: " + newItemID
                                        + " by: "
                                        + Misc.capitalize(c.playerName));
                            } else {
                                c.sendMessage("Could not complete spawn request.");
                            }
                        } else {
                            c.sendMessage("Use as ::item 4151 1");
                        }
                    } catch (Exception e) {
                    }
                }
                if (playerCommand.startsWith("ipmute")) {
                    try {
                        String playerToBan = playerCommand.substring(7);
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            if (PlayerHandler.players[i] != null) {
                                if (PlayerHandler.players[i].playerName
                                        .equalsIgnoreCase(playerToBan)) {
                                    Connection
                                            .addIpToMuteList(PlayerHandler.players[i].connectedFrom);
                                    c.sendMessage("You have IP Muted the user: "
                                            + PlayerHandler.players[i].playerName);
                                    Client c2 = (Client) PlayerHandler.players[i];
                                    c2.sendMessage("You have been muted by: "
                                            + Misc.capitalize(c.playerName));
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        c.sendMessage("Player is probably offline.");
                    }
                    if (playerCommand.startsWith("unipban")) {
                        try {
                            String playerToBan = playerCommand.substring(9);
                            for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                                if (PlayerHandler.players[i] != null) {
                                    if (PlayerHandler.players[i].playerName
                                            .equalsIgnoreCase(playerToBan)) {
                                        Connection
                                                .unIPBanUser(PlayerHandler.players[i].connectedFrom);
                                        c.sendMessage("You have un-IPbanned the user: "
                                                + PlayerHandler.players[i].playerName);
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            c.sendMessage("Player is probably offline.");
                        }
                    }
                    if (playerCommand.startsWith("unipmute")) {
                        try {
                            String playerToBan = playerCommand.substring(9);
                            for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                                if (PlayerHandler.players[i] != null) {
                                    if (PlayerHandler.players[i].playerName
                                            .equalsIgnoreCase(playerToBan)) {
                                        Connection
                                                .unIPMuteUser(PlayerHandler.players[i].connectedFrom);
                                        c.sendMessage("You have un IP-muted the user: "
                                                + PlayerHandler.players[i].playerName);
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            c.sendMessage("Player is probably offline.");
                        }
                    }
                    if (playerCommand.startsWith("unmute")) {
                        try {
                            String playerToBan = playerCommand.substring(7);
                            Connection.unMuteUser(playerToBan);
                        } catch (Exception e) {
                            c.sendMessage("Player is probably offline.");
                        }
                    }
                }
            }
            if (c.playerRights >= 3) {
                if (playerCommand.startsWith("xteleto")) {
                    String name = playerCommand.substring(8);
                    for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                        if (PlayerHandler.players[i] != null) {
                            if (PlayerHandler.players[i].playerName
                                    .equalsIgnoreCase(name)) {
                                c.getPA().movePlayer(
                                        PlayerHandler.players[i].getX(),
                                        PlayerHandler.players[i].getY(),
                                        PlayerHandler.players[i].heightLevel);
                            }
                        }
                    }
                }
                if (playerCommand.equals("test")) {
                    c.playerWalkIndex = 744;
                }
                if (playerCommand.equals("resetanim")) {
                    c.playerWalkIndex = 0x333;
                }
                if (playerCommand.startsWith("ban")
                        && playerCommand.charAt(7) == ' ') { // use as ::ban
                    // name
                    try {
                        String playerToBan = playerCommand.substring(8);
                        Connection.addNameToBanList(playerToBan);
                        Connection.addNameToFile(playerToBan);
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            if (PlayerHandler.players[i] != null) {
                                if (PlayerHandler.players[i].playerName
                                        .equalsIgnoreCase(playerToBan)) {
                                    PlayerHandler.players[i].disconnected = true;
                                }
                            }
                        }
                    } catch (Exception e) {
                        c.sendMessage("Player is not online.");
                    }
                }
                if (playerCommand.startsWith("kick")) {
                    try {
                        String playerToKick = playerCommand.substring(5);
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            if (PlayerHandler.players[i] != null) {
                                if (PlayerHandler.players[i].playerName
                                        .equalsIgnoreCase(playerToKick)) {
                                    PlayerHandler.players[i].disconnected = true;
                                    PlayerHandler.players[i].properLogout = true;
                                }
                            }
                        }
                    } catch (Exception e) {
                        c.sendMessage("Player is not online.");
                    }
                }
                if (playerCommand.startsWith("teletome")) {
                    try {
                        String playerToBan = playerCommand.substring(9);
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            if (PlayerHandler.players[i] != null) {
                                if (PlayerHandler.players[i].playerName
                                        .equalsIgnoreCase(playerToBan)) {
                                    Client c2 = (Client) PlayerHandler.players[i];
                                    c2.getPA().spellTeleport(c.getX(), c.getY(), c.heightLevel);
                                    c.sendMessage("You have teleported "
                                            + Misc.capitalize(c2.playerName)
                                            + " to you.");
                                    c2.sendMessage("You have been teleported to "
                                            + Misc.capitalize(c.playerName)
                                            + "");
                                }
                            }
                        }
                    } catch (Exception e) {
                        c.sendMessage("Player is probably offline.");
                    }
                }


                if (playerCommand.startsWith("xteleto")) {
                    String name = playerCommand.substring(8);
                    for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                        if (PlayerHandler.players[i] != null) {
                            if (PlayerHandler.players[i].playerName
                                    .equalsIgnoreCase(name)) {
                                c.getPA().movePlayer(
                                        PlayerHandler.players[i].getX(),
                                        PlayerHandler.players[i].getY(),
                                        PlayerHandler.players[i].heightLevel);
                            }
                        }
                    }
                }
                if (playerCommand.equalsIgnoreCase("mypos")) {
                    c.sendMessage("Your position is X: " + c.absX + " Y: "
                            + c.absY);
                }
                if (playerCommand.startsWith("smsg")) {
                    playerCommand.split(" ");
                    for (int j = 0; j < PlayerHandler.players.length; j++) {
                        if (PlayerHandler.players[j] != null) {
                            Client c2 = (Client) PlayerHandler.players[j];
                            c2.sendMessage(""
                                    + Misc.optimizeText(playerCommand
                                    .substring(5)));
                        }
                    }
                }
                if (playerCommand.startsWith("pnpc")) {
                    int npc = Integer.parseInt(playerCommand.substring(5));
                    if (npc < 9999) {
                        c.npcId2 = npc;
                        c.isNpc = true;
                        c.updateRequired = true;
                        c.appearanceUpdateRequired = true;
                    }
                }
                if (playerCommand.startsWith("unpc")) {
                    c.isNpc = false;
                    c.updateRequired = true;
                    c.appearanceUpdateRequired = true;
                }
                if (playerCommand.startsWith("object")) {
                    String[] args = playerCommand.split(" ");
                    c.getPA().object(Integer.parseInt(args[1]), c.absX, c.absY,
                            0, 10);
                }
                if (playerCommand.startsWith("dobject")) {
                    String[] args = playerCommand.split(" ");
                    c.getPA().object(Integer.parseInt(args[1]), c.absX, c.absY,
                            Integer.parseInt(args[2]),
                            Integer.parseInt(args[3]));
                }
                if (playerCommand.startsWith("empty")) {
                    c.getItems().removeAllItems();
                    c.sendMessage("You empty your inventory");
                }
                if (playerCommand.startsWith("wildy")) {
                    c.getPA().sendFrame177(c, 2759, 2916, 0, 2, 1);
                }
                if (playerCommand.startsWith("master")) {
                    c.getPA().addSkillXP((15000000), 0);
                    c.getPA().addSkillXP((15000000), 1);
                    c.getPA().addSkillXP((15000000), 2);
                    c.getPA().addSkillXP((15000000), 3);
                    c.getPA().addSkillXP((15000000), 4);
                    c.getPA().addSkillXP((15000000), 5);
                    c.getPA().addSkillXP((15000000), 6);
                    c.playerXP[3] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                    c.getPA().refreshSkill(3);
                }
                if (playerCommand.startsWith("tele")) {
                    String[] arg = playerCommand.split(" ");
                    if (arg.length > 3)
                        c.getPA().movePlayer(Integer.parseInt(arg[1]),
                                Integer.parseInt(arg[2]),
                                Integer.parseInt(arg[3]));
                    else if (arg.length == 3)
                        c.getPA().movePlayer(Integer.parseInt(arg[1]),
                                Integer.parseInt(arg[2]), c.heightLevel);
                }
                if (playerCommand.startsWith("telereg")) {
                    String[] arg = playerCommand.split(" ");
                    if (arg.length > 3)
                        c.getPA().movePlayer(Integer.parseInt(arg[1]) * 64,
                                Integer.parseInt(arg[2]) * 64,
                                Integer.parseInt(arg[3]));
                    else if (arg.length == 3)
                        c.getPA().movePlayer(Integer.parseInt(arg[1]) * 64,
                                Integer.parseInt(arg[2]) * 64, c.heightLevel);
                }
                if (playerCommand.startsWith("switch")) {
                    if (c.playerMagicBook == 0) {
                        c.playerMagicBook = 1;
                        c.setSidebarInterface(6, 12855);
                        c.sendMessage("An ancient wisdomin fills your mind.");
                        c.getPA().resetAutocast();
                    } else {
                        c.setSidebarInterface(6, 1151);
                        c.playerMagicBook = 0;
                        c.sendMessage("You feel a drain on your memory.");
                        c.autocastId = -1;
                        c.getPA().resetAutocast();
                    }
                }
                if (playerCommand.startsWith("interface")) {
                    try {
                        String[] args = playerCommand.split(" ");
                        int a = Integer.parseInt(args[1]);
                        c.getPA().showInterface(a);
                    } catch (Exception e) {
                        c.sendMessage("::interface id");
                    }
                }
                if (playerCommand.startsWith("npc")) {
                    try {
                        int newNPC = Integer.parseInt(playerCommand
                                .substring(4));
                        if (newNPC > 0) {
                            Server.npcHandler.spawnNpc(c, newNPC, c.absX,
                                    c.absY, 0, 0, 1, 1, 1, 1, false, false);
                        } else {
                            c.sendMessage("Requested NPC does not exist.");
                        }
                    } catch (Exception e) {
                    }
                }
                if (playerCommand.startsWith("ipban")) {
                    try {
                        String playerToBan = playerCommand.substring(6);
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            if (PlayerHandler.players[i] != null) {
                                if (PlayerHandler.players[i].playerName
                                        .equalsIgnoreCase(playerToBan)) {
                                    Connection
                                            .addIpToBanList(PlayerHandler.players[i].connectedFrom);
                                    Connection
                                            .addIpToFile(PlayerHandler.players[i].connectedFrom);
                                    c.sendMessage("You have IP banned the user: "
                                            + PlayerHandler.players[i].playerName
                                            + " with the host: "
                                            + PlayerHandler.players[i].connectedFrom);
                                    PlayerHandler.players[i].disconnected = true;
                                }
                            }
                        }
                    } catch (Exception e) {
                        c.sendMessage("Player is probably offline.");
                    }
                }

                if (playerCommand.startsWith("gfx")) {
                    String[] args = playerCommand.split(" ");
                    c.gfx0(Integer.parseInt(args[1]));
                }
                if (playerCommand.startsWith("update")) {
                    for (Player p : PlayerHandler.players) {
                        if (p == null)
                            continue;
                        PlayerSave.saveGameBackup((Client) p);
                    }
                    String[] args = playerCommand.split(" ");
                    int a = Integer.parseInt(args[1]);
                    PlayerHandler.updateSeconds = a;
                    PlayerHandler.updateAnnounced = false;
                    PlayerHandler.updateRunning = true;
                    PlayerHandler.updateStartTime = System.currentTimeMillis();
                }
                if (playerCommand.startsWith("ban")
                        && playerCommand.charAt(3) == ' ') {
                    try {
                        String playerToBan = playerCommand.substring(4);
                        Connection.addNameToBanList(playerToBan);
                        Connection.addNameToFile(playerToBan);
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            if (PlayerHandler.players[i] != null) {
                                if (PlayerHandler.players[i].playerName
                                        .equalsIgnoreCase(playerToBan)) {
                                    PlayerHandler.players[i].disconnected = true;
                                }
                            }
                        }
                    } catch (Exception e) {
                        c.sendMessage("Player is probably offline.");
                    }
                }
                if (playerCommand.startsWith("unban")) {
                    try {
                        String playerToBan = playerCommand.substring(6);
                        Connection.removeNameFromBanList(playerToBan);
                        c.sendMessage(playerToBan + " has been unbanned.");
                    } catch (Exception e) {
                        c.sendMessage("Player is probably offline.");
                    }
                }

                if (playerCommand.startsWith("range")) {
                    c.getItems().addItem(868, 1000);
                    c.getItems().addItem(861, 1);
                    c.getItems().addItem(892, 1000);
                    c.getItems().addItem(2491, 1);
                    c.getItems().addItem(2497, 1);
                    c.getItems().addItem(2503, 1);
                    c.getItems().addItem(6585, 1);
                }
                if (playerCommand.startsWith("mage")) {
                    c.getItems().addItem(554, 1000);
                    c.getItems().addItem(555, 1000);
                    c.getItems().addItem(556, 1000);
                    c.getItems().addItem(557, 1000);
                    c.getItems().addItem(558, 1000);
                    c.getItems().addItem(559, 1000);
                    c.getItems().addItem(560, 1000);
                    c.getItems().addItem(561, 1000);
                    c.getItems().addItem(562, 1000);
                    c.getItems().addItem(563, 1000);
                    c.getItems().addItem(564, 1000);
                }

                if (playerCommand.startsWith("anim")) {
                    String[] args = playerCommand.split(" ");
                    c.startAnimation(Integer.parseInt(args[1]));
                    c.getPA().requestUpdates();
                }
                if (playerCommand.startsWith("shop")) {
                    String[] args = playerCommand.split(" ");
                    c.getShops().openShop(Integer.parseInt(args[1]));
                }
                if (playerCommand.startsWith("outfit")) {
                    c.getPA().showInterface(3559);
                    c.canChangeAppearance = true;
                }
                if (playerCommand.equals("obank")) {
                    c.getPA().openUpBank();
                }
                if (playerCommand.equalsIgnoreCase("checkregion")) {
                    c.sendMessage("RegionX: " + c.getX() / 64);
                    c.sendMessage("RegionY: " + c.getY() / 64);
                }
                if (playerCommand.equalsIgnoreCase("skiptut")) {
                    Tutorialisland.sendDialogue(c, 3115, 0);
                    c.setSidebarInterface(1, 3917);
                    c.setSidebarInterface(2, 638);
                    c.setSidebarInterface(3, 3213);
                    c.setSidebarInterface(4, 1644);
                    c.setSidebarInterface(5, 5608);
                    if (c.playerMagicBook == 0) {
                        c.setSidebarInterface(6, 1151); // modern
                    } else {
                        c.setSidebarInterface(6, 12855); // ancient
                    }
                    c.setSidebarInterface(7, 18128);
                    c.setSidebarInterface(8, 5065);
                    c.setSidebarInterface(9, 5715);
                    c.setSidebarInterface(11, 904); // wrench tab
                    c.setSidebarInterface(12, 147); // run tab
                    c.setSidebarInterface(13, 962);
                    c.setSidebarInterface(0, 2423);
                }
                if (playerCommand.startsWith("tutprog")) {
                    String[] args = playerCommand.split(" ");
                    int id = Integer.parseInt(args[1]);

                    c.tutorialprog = id;
                }
                if (playerCommand.startsWith("nvn")) {
                    GoblinVillage.updateGoblinFights();
                }
                if (playerCommand.startsWith("song")) {
                    String[] args = playerCommand.split(" ");
                    int id = Integer.parseInt(args[1]);

                    Music.playMusic(c, id);
                }

                if (playerCommand.startsWith("sf")) {
                    String[] args = playerCommand.split(" ");
                    int id = Integer.parseInt(args[1]);
                    int delay = Integer.parseInt(args[2]);
                    Music.sendQuickSong(c, id, delay);
                }
                if (playerCommand.startsWith("attackpower")) {
                    if (!c.attackOthers) {
                        c.getPA().showOption(3, 0, "Attack", 1);
                        c.attackOthers = true;
                        c.sendMessage("You can now attack other players!");
                    } else {
                        c.attackOthers = false;
                        c.getPA().showOption(3, 0, "Null", 1);
                        c.sendMessage("You cannot attack other players!");
                    }

                }
                if (playerCommand.startsWith("attackspeed")) {
                    String[] args = playerCommand.split(" ");
                    int id = Integer.parseInt(args[1]);
                    c.attackTimer = id;
                    c.sendMessage("Your attack speed is now " + c.attackTimer);

                }
                if (playerCommand.startsWith("hide")) {
                    c.invisible = c.invisible == true ? false : true;
                    c.sendMessage(c.invisible == true ? "Hidden = " + c.invisible : "Not hidden. = " +
                            c.invisible);

                }
                if (playerCommand.startsWith("cutscene")) {
                    Cutscenes.handleTutorial(c);
                }
                if (playerCommand.startsWith("energy")) {
                    c.playerEnergy = 100554545;
                }
                if (playerCommand.startsWith("getnpc")) {
                    String a[] = playerCommand.split(" ");
                    String name = "";
                    int results = 0;
                    for (int i = 1; i < a.length; i++)
                        name = name + a[i] + " ";
                    name = name.substring(0, name.length() - 1);
                    c.sendMessage("Searching: " + name);
                    for (int j = 0; j < Server.npcHandler.NpcList.length; j++) {
                        if (Server.npcHandler.NpcList[j] != null)
                            if (Server.npcHandler.NpcList[j].npcName.replace("_", " ").toLowerCase().contains(name.toLowerCase())) {
                                c.sendMessage("@blu@"
                                        + Server.npcHandler.NpcList[j].npcName.replace("_", " ")
                                        + " - "
                                        + Server.npcHandler.NpcList[j].npcId);
                                results++;
                            }
                    }
                    c.sendMessage(results + " results found...");
                }
                if (playerCommand.startsWith("spawn")) {
                    try {
                        BufferedWriter spawn = new BufferedWriter(
                                new FileWriter(
                                        "./Data/cfg/spawn-config.cfg",
                                        true));
                        String Test123 = playerCommand.substring(6);
                        int Test124 = Integer.parseInt(playerCommand
                                .substring(6));
                        if (Test124 > 0) {
                            Server.npcHandler.spawnNpc(c, Test124,
                                    c.absX, c.absY, 0, 1, 0, 0, 0, 0,
                                    false, false);
                            c.sendMessage("You spawn a Npc.");
                        } else {
                            c.sendMessage("No such NPC.");
                        }
                        try {
                            spawn.newLine();
                            spawn.write("spawn = " + Test123 + "		"
                                    + c.getX() + "	" + c.getY() + "	"
                                    + c.heightLevel + "	" + 1 + "		"
                                    + "Spawned by Server.");
                            for (int j = 0; j < PlayerHandler.players.length; j++) {
                                if (PlayerHandler.players[j] != null) {
                                    Client c2 = (Client) PlayerHandler.players[j];
                                    //c2.sendMessage("@red@[SERVER]An Npc has been added to the map!");
                                }
                            }
                        } finally {
                            spawn.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (playerCommand.startsWith("sound")) {
                    String[] args = playerCommand.split(" ");
                    c.getPA().sendSound(Integer.parseInt(args[1]), 100, 1);
                }

                if (playerCommand.startsWith("exp")) {
                    c.sendMessage("[Gained] = " + c.expGained + " DoAmount = " + c.doAmount +
                            " ObjectTime = " + c.woodcuttingTree);

                }
                if (playerCommand.equalsIgnoreCase("restart") && (c.playerRights == 3 || c.playerRights == 5)) {
                    for (Player p : PlayerHandler.players) {
                        if (p == null)
                            continue;
                        if (p.inTrade) {
                            ((Client) p).getTradeAndDuel().declineTrade();
                            System.out.println(p.playerName + " was trading and has just been saved :)");
                        }
                        PlayerSave.saveGameBackup((Client) p);
                        PlayerSave.saveGame((Client) p);
                        System.out.println("Saved game for " + p.playerName + ".");
                    }
                    try {
                        String File = "run.bat";
                        String ext = "cmd /c start ";
                        String Dir = "\"C:/Users/Administrator/Dropbox/Shilo Source July 28/\"";
                        //Runtime.getRuntime().exec(ext + Dir + File);
                        Runtime.getRuntime().exec(ext + File);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    System.exit(0);
                }
                if (playerCommand.equalsIgnoreCase("compile") && (c.playerRights == 3 || c.playerRights == 5)) {
                    try {
                        String File = "build.bat";
                        String ext = "cmd /c start ";
                        String Dir = "\"C:/Users/Administrator/Dropbox/Shilo Source July 28/\"";
                        //Runtime.getRuntime().exec(ext + Dir + File);
                        Runtime.getRuntime().exec(ext + File);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                if (playerCommand.startsWith("tutisland")) {
                    c.getPA().movePlayer(3094, 3107, 0);
                    Tutorialisland.sendDialogue(c, 3000, 0);
                }
                if (playerCommand.startsWith("setlevel")) {
                    try {
                        String[] args = playerCommand.split(" ");
                        int skill = Integer.parseInt(args[1]);
                        int level = Integer.parseInt(args[2]);
                        if (level < 0) {
                            level = 1;
                        }
                        c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
                        c.playerLevel[skill] = c.getPA().getLevelForXP(
                                c.playerXP[skill]);
                        c.getPA().refreshSkill(skill);
                    } catch (Exception e) {
                    }
                }
                if (playerCommand.equals("reloaditems") && (c.playerRights == 3)) {
                    for (int i = 0; i < Config.ITEM_LIMIT; i++)
                        Server.itemHandler.ItemList[i] = null;
                    Server.itemHandler.loadItemList("item.cfg");
                    Server.itemHandler.loadItemPrices("prices.txt");
                    c.sendMessage("Items reloaded.");
                }

                if (playerCommand.equals("reloadnpcs") && (c.playerRights == 3)) {
                    for (int i = 0; i < Server.npcHandler.maxNPCs; i++) {
                        Server.npcHandler.npcs[i] = null;
                    }
                    for (int i = 0; i < Server.npcHandler.maxListedNPCs; i++) {
                        Server.npcHandler.NpcList[i] = null;
                    }
                    Server.npcHandler.loadNPCList("./Data/CFG/npc.cfg");
                    Server.npcHandler.loadAutoSpawn("./Data/CFG/spawn-config.cfg");
                    c.sendMessage("NPCs reloaded.");
                }

                if (playerCommand.startsWith("reloaddrops") && (c.playerRights == 3)) {
                    Server.npcDrops = null;
                    Server.npcDrops = new server.game.npcs.drops.NPCDrops();
                    c.sendMessage("Drops reloaded.");
                }

                if (playerCommand.startsWith("reloadshops") && (c.playerRights == 3)) {
                    Server.shopHandler = new server.world.ShopHandler();
                }
                if (playerCommand.startsWith("lvl")) {
                    try {
                        String[] args = playerCommand.split(" ");
                        int skill = Integer.parseInt(args[1]);
                        int level = Integer.parseInt(args[2]);
                        if (level < 0) {
                            level = 1;
                        }
                        c.playerLevel[skill] = level;
                        c.getPA().refreshSkill(skill);
                    } catch (Exception e) {
                    }
                }

                // other commands
                if (playerCommand.startsWith("takeitem")) {
                    try {
                        String[] args = playerCommand.split(" ");
                        int takenItemID = Integer.parseInt(args[1]);
                        int takenItemAmount = Integer.parseInt(args[2]);
                        String otherplayer = args[3];
                        Client c2 = null;
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            if (PlayerHandler.players[i] != null) {
                                if (PlayerHandler.players[i].playerName
                                        .equalsIgnoreCase(otherplayer)) {
                                    c2 = (Client) PlayerHandler.players[i];
                                    break;
                                }
                            }
                        }
                        if (c2 == null) {
                            c.sendMessage("Player doesn't exist.");
                            return;
                        }
                        c.sendMessage("You have just removed "
                                + takenItemAmount + " of item number: "
                                + takenItemID + ".");
                        c2.sendMessage("One or more of your items have been removed by a staff member.");
                        c2.getItems().deleteItem(takenItemID,
                                takenItemAmount);
                    } catch (Exception e) {
                        c.sendMessage("Use as ::takeitem ID AMOUNT PLAYERNAME.");
                    }
                }
                if (playerCommand.startsWith("checkbank")) {
                    try {
                        String[] args = playerCommand.split(" ", 2);
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            Client o = (Client) Server.playerHandler.players[i];
                            if (Server.playerHandler.players[i] != null) {
                                if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(args[1])) {
                                    c.getPA().otherBank(c, o);
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        c.sendMessage("Player Must Be Offline.");
                    }
                }
                if (playerCommand.startsWith("hitplayer")) {
                    try {
                        String[] args = playerCommand.split(" ");
                        int hit = Integer.parseInt(args[1]);
                        String otherplayer = args[2];
                        String text = args[3];
                        Client c2 = null;
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            if (PlayerHandler.players[i] != null) {
                                if (PlayerHandler.players[i].playerName
                                        .equalsIgnoreCase(otherplayer)) {
                                    c2 = (Client) PlayerHandler.players[i];
                                    break;
                                }
                            }
                        }
                        if (c2 == null) {
                            c.sendMessage("Player doesn't exist.");
                            return;
                        }
                        c2.handleHitMask(hit);
                        c2.dealDamage(hit);
                        c2.getPA().refreshSkill(3);
                        c2.forcedText = text;
                        c2.forcedChatUpdateRequired = true;
                        c2.updateRequired = true;
                    } catch (Exception e) {
                        c.sendMessage("Use as ::hitplayer hit player");
                    }
                }
                if (playerCommand.startsWith("deleteitems")) {
                    try {
                        String[] args = playerCommand.split(" ");
                        String otherplayer = args[1];
                        Client c2 = null;
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            if (PlayerHandler.players[i] != null) {
                                if (PlayerHandler.players[i].playerName
                                        .equalsIgnoreCase(otherplayer)) {
                                    c2 = (Client) PlayerHandler.players[i];
                                    break;
                                }
                            }
                        }
                        if (c2 == null) {
                            c.sendMessage("Player doesn't exist.");
                            return;
                        }
                        c2.getItems().removeAllItems();
                        for (int i = 0; i < 13; i++) {
                            c2.getItems().removeItem(c.playerEquipment[i], i);
                        }
                        c2.getItems().removeAllItems();
                        for (int i = 0; i < c.bankItems.length; i++) { // Setting bank items
                            c2.bankItems[i] = 0;
                            c2.bankItemsN[i] = 0;
                        }
                        c2.getItems().resetBank();
                        c2.getItems().resetItems(5064);
                        c2.sendMessage("You have been reset! Sorry but aha.");
                    } catch (Exception e) {
                        c.sendMessage("Use as player");
                    }
                }
                if (playerCommand.startsWith("otherlvl")) {
                    try {
                        String[] args = playerCommand.split(" ");
                        String otherplayer = args[1];
                        int skill = Integer.parseInt(args[2]);
                        int level = Integer.parseInt(args[3]);
                        Client c2 = null;
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            if (PlayerHandler.players[i] != null) {
                                if (PlayerHandler.players[i].playerName
                                        .equalsIgnoreCase(otherplayer)) {
                                    c2 = (Client) PlayerHandler.players[i];
                                    break;
                                }
                            }
                        }
                        if (c2 == null) {
                            c.sendMessage("Player doesn't exist.");
                            return;
                        }
                        if (level < 0) {
                            level = 1;
                        }
                        c2.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
                        c2.playerLevel[skill] = c.getPA().getLevelForXP(
                                c.playerXP[skill]);
                        c2.getPA().refreshSkill(skill);
                        c2.sendMessage("Your level has been adjusted.");
                    } catch (Exception e) {
                        c.sendMessage("Use as adjustlevel,player, skill, level");
                    }
                }
                if (playerCommand.startsWith("pobject")) {
                    try {
                        String[] args = playerCommand.split(" ");
                        int object = Integer.parseInt(args[1]);
                        Objects OBJECT = new Objects(object, c.getX(), c.getY(), 0, 0, 10, 0);
                        if (object == -1) {
                            Server.objectHandler.removeObject(OBJECT);
                        } else {
                            Server.objectHandler.addObject(OBJECT);
                        }
                        Server.objectHandler.placeObject(OBJECT);
                    } catch (Exception e) {
                        c.sendMessage("Use as pobject - objectid - timer");
                    }
                }
                if (playerCommand.startsWith("takeitem")) {

                    try {
                        String[] args = playerCommand.split(" ");
                        int takenItemID = Integer.parseInt(args[1]);
                        int takenItemAmount = Integer.parseInt(args[2]);
                        String otherplayer = args[3];
                        Client c2 = null;
                        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                            if (Server.playerHandler.players[i] != null) {
                                if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(otherplayer)) {
                                    c2 = (Client) Server.playerHandler.players[i];
                                    break;
                                }
                            }
                        }
                        if (c2 == null) {
                            c.sendMessage("Player doesn't exist or isn't online.");
                            return;
                        }
                        c.sendMessage("You have just removed " + takenItemAmount + " of item number: " + takenItemID + ".");
                        c2.getItems().deleteItem(takenItemID, takenItemAmount);
                    } catch (Exception e) {
                        c.sendMessage("Use as ::takeitem ID AMOUNT PLAYERNAME.");
                    }
                }

                if (playerCommand.equalsIgnoreCase("listplayers")) {
                    c.sendMessage("There are currently " + PlayerHandler.getPlayerCount() + " players online.");
                    c.getPA().sendFrame126(Config.SERVER_NAME + " - Online Players", 8144);
                    c.getPA().sendFrame126("@dbl@Online players(" + PlayerHandler.getPlayerCount() + "):", 8145);
                    int line = 8147;
                    for (int i = 1; i < Config.MAX_PLAYERS; i++) {
                        Client p = c.getClient(i);
                        if (!c.validClient(i))
                            continue;
                        if (p.playerName != null) {
                            String title = "";
                            if (p.playerRights == 1) {
                                title = "Mod, ";
                            } else if (p.playerRights == 2) {
                                title = "Admin, ";
                            }
                            title += "level-" + p.combatLevel;
                            String extra = "";
                            if (c.playerRights > 0) {
                                extra = "(" + p.playerId + ") ";
                            }
                            c.getPA().sendFrame126("@dre@" + extra + p.playerName + "@dbl@ (" + title + ")", line);
                            line++;
                        }
                    }
                    c.getPA().showInterface(8134);
                    c.flushOutStream();
                }
            }
            if (playerCommand.startsWith("checkinv")) {
                try {
                    String[] args = playerCommand.split(" ", 2);
                    for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                        Client o = (Client) Server.playerHandler.players[i];
                        if (Server.playerHandler.players[i] != null) {
                            if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(args[1])) {
                                c.getPA().otherInv(c, o);
                                DialogueHandler.sendDialogues(c, 206, 0);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    c.sendMessage("Player Must Be Offline.");
                }
            }
            if (playerCommand.equals("alltome")) {
                for (int j = 0; j < PlayerHandler.players.length; j++) {
                    if (PlayerHandler.players[j] != null) {
                        Client c2 = (Client) PlayerHandler.players[j];
                        c2.getPA().spellTeleport(c.getX(), c.getY(), c.heightLevel);
                        c2.sendMessage("You have been teleported to: " + c.playerName + "");
                    }
                }
            }
        }
        // end of special commands
    }

    public void launchURL(String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});
            } else if (osName.startsWith("Windows"))
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            else { //assume Unix or Linux
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "safari"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0)
                        browser = browsers[count];
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else
                    Runtime.getRuntime().exec(new String[]{browser, url});
            }
        } catch (Exception e) {
            //c.sendMessage("Failed to open URL.", 0, "");
        }
    }
}
