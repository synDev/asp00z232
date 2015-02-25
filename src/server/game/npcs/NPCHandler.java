package server.game.npcs;

import core.util.Misc;
import server.Config;
import server.Server;
import server.clip.region.Region;
import server.content.GoblinVillage;
import server.content.quests.misc.QuestHandling;
import server.content.quests.misc.Tutorialisland;
import server.content.randoms.Genie;
import server.event.*;
import server.game.content.skills.Slayer;
import server.game.minigame.FightCaves;
import server.game.minigame.RFD;
import server.game.npcs.drops.NPCDrops;
import server.game.players.Client;
import server.game.players.DialogueHandler;
import server.game.players.PlayerHandler;
import server.world.TileControl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NPCHandler {

    public static int maxNPCs = 4000;
    public static int maxListedNPCs = 4000;
    public static NPC npcs[] = new NPC[maxNPCs];
    public static NPCList NpcList[] = new NPCList[maxListedNPCs];

    public NPCHandler() {
        for (@SuppressWarnings("unused") NPC i : npcs) {
            i = null;
        }
        for (@SuppressWarnings("unused") NPCList i : NpcList) {
            i = null;
        }
        loadNPCList("./Data/cfg/npc.cfg");
        loadAutoSpawn("./Data/cfg/spawn-config.cfg");
        loadNPCSounds("./Data/cfg/npcsounds.cfg");
    }

    public boolean checkSlayerMask(Client c, int i) {
        if (c.slayerTask == npcs[i].npcType && c.playerEquipment[c.playerHat] == 3432) {
            return true;
        }
        return false;
    }

    public static boolean pathBlocked(NPC attacker, Client victim) {

        double offsetX = Math.abs(attacker.absX - victim.absX);
        double offsetY = Math.abs(attacker.absY - victim.absY);

        int distance = TileControl.calculateDistance(attacker, victim);

        if (distance == 0) {
            return true;
        }

        offsetX = offsetX > 0 ? offsetX / distance : 0;
        offsetY = offsetY > 0 ? offsetY / distance : 0;

        int[][] path = new int[distance][5];

        int curX = attacker.absX;
        int curY = attacker.absY;
        int next = 0;
        int nextMoveX = 0;
        int nextMoveY = 0;

        double currentTileXCount = 0.0;
        double currentTileYCount = 0.0;

        while (distance > 0) {
            distance--;
            nextMoveX = 0;
            nextMoveY = 0;
            if (curX > victim.absX) {
                currentTileXCount += offsetX;
                if (currentTileXCount >= 1.0) {
                    nextMoveX--;
                    curX--;
                    currentTileXCount -= offsetX;
                }
            } else if (curX < victim.absX) {
                currentTileXCount += offsetX;
                if (currentTileXCount >= 1.0) {
                    nextMoveX++;
                    curX++;
                    currentTileXCount -= offsetX;
                }
            }
            if (curY > victim.absY) {
                currentTileYCount += offsetY;
                if (currentTileYCount >= 1.0) {
                    nextMoveY--;
                    curY--;
                    currentTileYCount -= offsetY;
                }
            } else if (curY < victim.absY) {
                currentTileYCount += offsetY;
                if (currentTileYCount >= 1.0) {
                    nextMoveY++;
                    curY++;
                    currentTileYCount -= offsetY;
                }
            }
            path[next][0] = curX;
            path[next][1] = curY;
            path[next][2] = attacker.heightLevel;//getHeightLevel();
            path[next][3] = nextMoveX;
            path[next][4] = nextMoveY;
            next++;
        }
        for (int i = 0; i < path.length; i++) {
            if (!Region./*getSingleton().*/getClipping(path[i][0], path[i][1], path[i][2], path[i][3], path[i][4]) && !Region.blockedShot(path[i][0], path[i][1], path[i][2])) {
                return true;
            }
        }
        return false;
    }

    public static int npcLoopCount;

    public void multiAttackGfx(int i, int gfx) {
        if (npcs[i].projectileId < 0)
            return;
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Client c = (Client) PlayerHandler.players[j];
                if (c.heightLevel != npcs[i].heightLevel)
                    continue;
                if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
                    int nX = NPCHandler.npcs[i].getX() + offset(i);
                    int nY = NPCHandler.npcs[i].getY() + offset(i);
                    int pX = c.getX();
                    int pY = c.getY();
                    int offX = (nY - pY) * -1;
                    int offY = (nX - pX) * -1;
                    c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i), npcs[i].projectileId, 43, 31, -c.getId() - 1, 65);
                }
            }
        }
    }

    public void appendZyg(Client c) {
        if (npcs[c.rememberNpcIndex] != null && (npcs[c.rememberNpcIndex].npcType == 3345)) {
            npcs[c.rememberNpcIndex].facePlayer(c.playerId);
            npcs[c.rememberNpcIndex].requestTransform(3347);
            npcs[c.rememberNpcIndex].underAttack = true;
            npcs[c.rememberNpcIndex].killerId = c.playerId;
        }
    }

    public void appendZyglil(Client c) {
        if (npcs[c.rememberNpcIndex] != null && (npcs[c.rememberNpcIndex].npcType == 3344)) {
            npcs[c.rememberNpcIndex].facePlayer(c.playerId);
            npcs[c.rememberNpcIndex].requestTransform(3346);
            npcs[c.rememberNpcIndex].underAttack = true;
            npcs[c.rememberNpcIndex].killerId = c.playerId;
        }
    }

    public static void appendRockCrab(Client p) {
        for (NPC crab : NPCHandler.npcs) {
            if (crab != null && (crab.npcType == 1266 || crab.npcType == 1268) && Server.npcHandler.goodDistance(crab.getX(), crab.getY(), p.absX, p.absY, 2)) {
                crab.facePlayer(p.playerId);
                crab.requestTransform(crab.npcType == 1266 ? 1265 : 1267);
                crab.underAttack = true;
                crab.killerId = p.playerId;
            }
        }
    }

    public boolean switchesAttackers(int i) {
        switch (npcs[i].npcType) {
            case 2551:
            case 2552:
            case 2553:
            case 2559:
            case 2560:
            case 2561:
            case 2563:
            case 2564:
            case 2565:
            case 2892:
            case 2894:
                return true;

        }

        return false;
    }

    public void multiAttackDamage(int i) {
        int max = getMaxHit(i);
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Client c = (Client) PlayerHandler.players[j];
                if (c.isDead || c.heightLevel != npcs[i].heightLevel)
                    continue;
                if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
                    if (npcs[i].attackType == 2) {
                        if (!c.prayerActive[16]) {
                            if (Misc.random(500) + 200 > Misc.random(c.getCombat().mageDef())) {
                                int dam = Misc.random(max);
                                c.dealDamage(dam);
                                c.handleHitMask(dam);
                            } else {
                                c.dealDamage(0);
                                c.handleHitMask(0);
                            }
                        } else {
                            c.dealDamage(0);
                            c.handleHitMask(0);
                        }
                    } else if (npcs[i].attackType == 1) {
                        if (!c.prayerActive[17]) {
                            int dam = Misc.random(max);
                            if (Misc.random(500) + 200 > Misc.random(c.getCombat().calculateRangeDefence())) {
                                c.dealDamage(dam);
                                c.handleHitMask(dam);
                            } else {
                                c.dealDamage(0);
                                c.handleHitMask(0);
                            }
                        } else {
                            c.dealDamage(0);
                            c.handleHitMask(0);
                        }
                    }
                    if (npcs[i].endGfx > 0) {
                        c.gfx0(npcs[i].endGfx);
                    }
                }
                c.getPA().refreshSkill(3);
            }
        }
    }

    public int getClosePlayer(int i) {
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                if (j == npcs[i].spawnedBy)
                    return j;
                if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].absX, npcs[i].absY, 2 + distanceRequired(i) + followDistance(i))) {
                    if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0) || PlayerHandler.players[j].inMulti())
                        if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
                            return j;
                }
            }
        }
        return 0;
    }

    public int getCloseRandomPlayer(int i) {
        ArrayList<Integer> players = new ArrayList<Integer>();
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].absX, npcs[i].absY, 2 + distanceRequired(i) + followDistance(i))) {
                    if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0) || PlayerHandler.players[j].inMulti())
                        if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
                            players.add(j);
                }
            }
        }

        if (players.size() > 0)
            return players.get(Misc.random(players.size() - 1));
        else
            return 0;
    }

    public int npcSize(int i) {
        switch (npcs[i].npcType) {
            case 2883:
            case 2882:
            case 2881:
                return 3;
        }
        return 0;
    }

    public static int npcSizes(int i) {
        return NPCSize.getNPCSize(npcs[i].npcType);
    }

    public void removeNpc(NPC npc) {
        npc.setAbsX(0);
        npc.setAbsY(0);
        npcs[npc.npcId] = null;
    }

    public void removeNpc(int i) {
        npcs[i] = null;
    }


    /**
     * Summon npc, barrows, etc
     */
    public void spawnNpc(final Client c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon) {
        // first, search for a free slot
        int slot = -1;
        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                for (int i = 1; i < maxNPCs; i++) {
                    if (npcs[i] != null) {
                        if (npcs[i].spawnedBy > 0) { // delete summons npc
                            if (PlayerHandler.players[npcs[i].spawnedBy] == null
                                    || PlayerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel
                                    || PlayerHandler.players[npcs[i].spawnedBy].respawnTimer > 0
                                    || !PlayerHandler.players[npcs[i].spawnedBy].goodDistance(npcs[i].getX(), npcs[i].getY(), PlayerHandler.players[npcs[i].spawnedBy].getX(), PlayerHandler.players[npcs[i].spawnedBy].getY(), 20)) {

                                if (PlayerHandler.players[npcs[i].spawnedBy] != null) {
                                    npcs[i] = null;
                                    container.stop();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void stop() {
            }
        }, 1);
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }

        if (slot == -1) {
            //Misc.println("No Free Slot");
            return;        // no free slot found
        }
        NPC newNPC = new NPC(slot, npcType);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.HP = HP;
        newNPC.MaxHP = HP;
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        newNPC.spawnedBy = c.getId();
        if (headIcon)
            c.getPA().drawHeadicon(1, slot, 0, 0);
        if (attackPlayer) {
            newNPC.underAttack = true;
            if (server.game.minigame.Barrows.COFFIN_AND_BROTHERS[c.randomCoffin][1] != newNPC.npcType) {
                if (newNPC.npcType == 2025 || newNPC.npcType == 2026 || newNPC.npcType == 2027 || newNPC.npcType == 2028 || newNPC.npcType == 2029 || newNPC.npcType == 2030) {
                    newNPC.forceChat("You dare disturb my rest!");
                }
            }
            if (server.game.minigame.Barrows.COFFIN_AND_BROTHERS[c.randomCoffin][1] == newNPC.npcType) {
                newNPC.forceChat("You dare steal from us!");
            }

            if (newNPC.npcType == 101 || newNPC.npcType == 2026 || newNPC.npcType == 2027 || newNPC.npcType == 2028 || newNPC.npcType == 2029 || newNPC.npcType == 2030) {
                newNPC.forceChat("I've come back for more!");
            }
            newNPC.killerId = c.playerId;
        }

        npcs[slot] = newNPC;
    }

    private void killedBarrow(int i) {
        Client c = (Client) Server.playerHandler.players[npcs[i].killedBy];
        if (c != null) {
            for (int o = 0; o < c.barrowsNpcs.length; o++) {
                if (npcs[i].npcType == c.barrowsNpcs[o][0]) {
                    c.barrowsNpcs[o][1] = 2; // 2 for dead
                    c.barrowsKillCount++;
                    c.getPA().sendFrame126("Kill Count: " + c.barrowsKillCount, 4536);

                }
            }
        }
    }

    public static int timer = 0;

    /**
     * Random events
     */
    public void spawnGenie(final Client c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon, boolean followplayer) {
        // first, search for a free slot
        int slot = -1;
        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                for (int i = 1; i < maxNPCs; i++) {
                    if (npcs[i] != null) {
                        if (npcs[i].spawnedBy > 0) { // delete summons npc
                            if (PlayerHandler.players[npcs[i].spawnedBy] == null
                                    || PlayerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel
                                    || PlayerHandler.players[npcs[i].spawnedBy].respawnTimer > 0
                                    || !PlayerHandler.players[npcs[i].spawnedBy].goodDistance(npcs[i].getX(), npcs[i].getY(), PlayerHandler.players[npcs[i].spawnedBy].getX(), PlayerHandler.players[npcs[i].spawnedBy].getY(), 20)) {

                                if (PlayerHandler.players[npcs[i].spawnedBy] != null) {
                                    npcs[i] = null;
                                    container.stop();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void stop() {
            }
        }, 1);
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            //Misc.println("No Free Slot");
            return;        // no free slot found
        }
        final NPC newNPC = new NPC(slot, npcType);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.HP = HP;
        newNPC.MaxHP = HP;
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        newNPC.spawnedBy = c.getId();
        if (headIcon)
            c.getPA().drawHeadicon(1, slot, 0, 0);
        if (attackPlayer) {
            newNPC.underAttack = true;
            newNPC.killerId = c.playerId;
        }
        if (followplayer) {
            newNPC.forceChat("Greetings, " + Misc.capitalize(c.playerName) + "!");
            CycleEventHandler.getSingleton().addEvent(c,
                    new CycleEvent() {
                        @Override
                        public void execute(CycleEventContainer container) {
                            if (!PlayerHandler.players[newNPC.spawnedBy].goodDistance(newNPC.getX(), newNPC.getY(), PlayerHandler.players[newNPC.spawnedBy].getX(), PlayerHandler.players[newNPC.spawnedBy].getY(), 20)) {
                                removeNpc(newNPC); // removed NPC
                                container.stop();
                                return;
                            }
                            if (c.talkedto) {
                                newNPC.forceChat("Enjoy your gift, " + c.playerName + ".");
                                newNPC.animNumber = 863;
                                newNPC.animUpdateRequired = true;
                                c.pgotLamp = true;
                            }
                            followPlayer(newNPC.npcId, c.playerId);
                            newNPC.turnNpc(c.getX(), c.getY());
                            timer++;
                            if (timer == 1000 || c.pgotLamp) {
                                c.getItems().addItem(Genie.lamp, 1);
                                c.talkedto = false;
                                container.stop();
                            }

                        }

                        @Override
                        public void stop() {

                        }
                    }, 1);
            CycleEventHandler.getSingleton().addEvent(c,
                    new CycleEvent() {
                        @Override
                        public void execute(CycleEventContainer container) {
                            if (timer == 1000 || c.pgotLamp) {
                                removeNpc(newNPC); // removed NPC
                                container.stop();
                            }
                        }

                        @Override
                        public void stop() {
                            timer = 0;
                            c.talkedto = false;
                            c.pgotLamp = false;
                        }
                    }, 7);
            CycleEventHandler.getSingleton().addEvent(c,
                    new CycleEvent() { // Random messages
                        @Override
                        public void execute(CycleEventContainer container) {
                            String[] genieRandomTalk = {
                                    "Please stop ignoring me, " + c.playerName + "!",
                                    "I'm here to grant you a wish, " + c.playerName + "!"
                            };
                            newNPC.forceChat(genieRandomTalk[Misc.random(1)]);
                            if (timer == 1000 || c.pgotLamp)
                                container.stop();
                        }

                        @Override
                        public void stop() {
                            timer = 0;
                        }
                    }, 20);
        }
        npcs[slot] = newNPC;

    }



	/*/public void getDtLastKill(int i) {
		int dtNpcs[] = {
				1975, 1914, 1977, 1913
		};
		for(int dtNpc : dtNpcs) {
			if(npcs[i].npcType == dtNpc) {
				Client p = (Client) PlayerHandler.players[npcs[i].killedBy];
				if(p != null) {
					p.lastDtKill = dtNpc;
				}
			}
		}
	}

/*/

    public void spawnNpc3(Client c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon, boolean summonFollow) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            //Misc.println("No Free Slot");
            return;        // no free slot found
        }
        NPC newNPC = new NPC(slot, npcType);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.HP = HP;
        newNPC.MaxHP = HP;
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        newNPC.spawnedBy = c.getId();
        newNPC.underAttack = true;
        newNPC.facePlayer(c.playerId);
        if (headIcon)
            c.getPA().drawHeadicon(1, slot, 0, 0);
        if (summonFollow) {
            newNPC.summoner = true;
            newNPC.summonedBy = c.playerId;
            c.hasNpc = true;
        }
        if (attackPlayer) {
            newNPC.underAttack = true;
            if (c != null) {
                newNPC.killerId = c.playerId;
            }
        }
        npcs[slot] = newNPC;
    }

    public void stepAway(int i) {

        if (Region.getClipping(npcs[i].getX(), npcs[i].getY() - 1, npcs[i].heightLevel, 0, -1)) {
            npcs[i].moveX = 0;
            npcs[i].moveY = -1;
        } else if (Region.getClipping(npcs[i].getX() - 1, npcs[i].getY(), npcs[i].heightLevel, -1, 0)) {
            npcs[i].moveX = -1;
            npcs[i].moveY = 0;
        } else if (Region.getClipping(npcs[i].getX() + 1, npcs[i].getY(), npcs[i].heightLevel, 1, 0)) {
            npcs[i].moveX = 1;
            npcs[i].moveY = 0;
        } else if (Region.getClipping(npcs[i].getX(), npcs[i].getY() + 1, npcs[i].heightLevel, 0, 1)) {
            npcs[i].moveX = 0;
            npcs[i].moveY = 1;
        }
        npcs[i].getNextNPCMovement(i);
    }

    public void spawnNpc2(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            //Misc.println("No Free Slot");
            return;        // no free slot found
        }
        NPC newNPC = new NPC(slot, npcType);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.HP = HP;
        newNPC.MaxHP = HP;
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        npcs[slot] = newNPC;
    }


    /**
     * Emotes
     */
    public static int getBlockEmote(int i) {
        switch (Server.npcHandler.npcs[i].npcType) {
            case 3068:
                return 2983;
            case 3619:
                return 3732;
            case 750:
                return 1156;
            case 3594:
                return 3710;
            case 135:
                return 305;
            case 122:
                return 165;
            case 136:
                return 323;
            case 2604:
            case 2610:
            case 2598:
                return 2605;
            case 15:
                return 1156;
            case 394:
                return 285;
            case 1620:
                return 1560;
            case 2494:
            case 2495:
                return 2399;
            case 1614:
                return 1542;
            case 1648:
                return 1591;
            case 1832:
                return 1775;
            case 101:
                return 312;
            case 105:
                return 42;
            case 1459:
                return 1403;
            case 1827:
                return 1803;
            case 113:
                return 129;
            case 58:
            case 59: //Giant Spider
            case 2491:
            case 60: //Giant Spider
            case 61: //Spider
            case 62: //Jungle Spider
            case 63: //Deadly Red Spider
            case 64: //Ice Spider
            case 134:
                return 144;
            case 119:
                return 100;
            case 103:
            case 655:
                return 124;
            case 941:
            case 1590:
            case 1591:
            case 1592:
                return 89;
            case 41:
                return 56;
            case 128:
                return 276;
            case 107:
            case 108:
                return 247;
            case 708:
                return 170;
            case 84:
            case 1472:
                return 65;
            case 51://baby drags
            case 52:
            case 1589:
            case 3376:
                return 26;
            case 1831:
                return 1791;
            case 1993:
                return 2035;
            case 90:
            case 91:
            case 92:
            case 93:
                return 261;
            case 3618:
                return 3619;
            case 1593:
            case 143:
                return 76;
            case 1338:
                return 1340;
            case 78:
                return 31;
            case 1622:
            case 1623:
                return 1566;
            case 416:
                return 154;
            case 3346:
            case 3347:
            case 3345:
            case 3344:
                return 3325;
            case 81:
                return 60;
            case 117:
            case 116:
            case 112:
            case 110:
                return 129;
            case 73:
                return 300;
            case 1612:
                return 1525;
            case 1633:
                return 1581;
            case 1616:
                return 1547;
            case 127:
                return 186;
            case 1677:
                return 1617;
            case 71:
                return 231;
            case 72:
                return 285;
            case 3649:
                return 3811;
            case 1995:
                return 2015;
            case 1265:
            case 1266:
            case 1267:
            case 1268:
                return 1313;
            case 3585:
                return 144;
            case 3655:
                return 3824;
            case 132:
                return 221;
            case 1618:
                return 1550;
            case 1637:
                return 1585;
            case 1627:
                return 1596;
            case 1615: //abby demon
                return 1536;
            case 1613:
                return 1529;
            case 1608:
                return 1514;
            case 1624:
                return 1555;
            case 1604:
                return 1509;
            case 191:
                return 415;
            default:
                return -1;

        }
    }

    public static int getRfdpoints(int i) {
        switch (NPCHandler.npcs[i].npcType) {
            case RFD.MONKEY:
                return 5;
            case RFD.COW:
            case RFD.GOBLIN:
                return 7;
            case RFD.LOCUST:
                return 13;
            case RFD.TRIBESMAN:
                return 14;
            case RFD.WIZARD:
                return 23;
            case RFD.GIANT_BAT:
                return 13;
            case RFD.CHAOS_DWARF:
                return 32;
            case RFD.MONKEY_ARCHER:
                return 40;
            case RFD.MAMMOTH:
                return 48;
            case RFD.GREEN_DRAGON:
                return 89;
            case RFD.SCORPION:
                return 11;
            case RFD.HILL_GIANT:
                return 24;
            case RFD.SKELETON:
                return 41;
            default:
                return 5;
        }
    }

    public static int getAttackEmote(int i) {
        String npc = Server.npcHandler.getNpcListName(NPCHandler.npcs[i].npcType).toLowerCase();
        if (npc.equalsIgnoreCase("goblin"))
            return 309;
        switch (NPCHandler.npcs[i].npcType) {
            case 2530:
                return 2066;
            case 191:
                return 412;
            case 1608:
                return 1512;
            case 1604:
                return 1507;
            case 3068:
                return 2986;
            case 1637:
                return 1586;
            case 3655:
                return 3823;
            case 3585:
                return 143;
            case 3618:
                return 3618;
            case 182:
            case 183:
            case 184:
            case 185:
                return 451;
            case 3619:
                return 3731;
            case 136:
                return 322;
            case 1614:
                return 1540;
            case 1643:
                return 711;
            case 15:
                return 451;
            case 394:
                return 284;
            case 1620:
                return 1562;
            case 1827:
                return 1802;
            case 3346:
            case 3347:
            case 3345:
            case 3344:
                return 3326;
            case 1633:
                return 1582;
            case 93:
            case 92:
                return 260;
            case 1832:
                return 1777;
            case 1831:
                return 1793;
            case 708:
                return 169;
            case 89:
                return 289;
            case 1115:
                return 1142;
            case 128:
                return 275;
            case 2494:
            case 2495:
                return 2397;
            case 107:
            case 108:
                return 247;
            case 1583:
                return 75;
            case 2529:
                return 1658;
            case 49:
                return 158;
            case 113:
                return 128;
            case 1618:
                return 1551;
            case 73:
                return 299;
            case 90:
                return 260;
            case 97:
            case 141:
            case 1558:
            case 143:
            case 96:
            case 1593:
                return 75;
            case 1459:
                return 1402;
            case 396:
                if (npcs[i].attackType == 1)
                    return 285;
                if (npcs[i].attackType == 0)
                    return 284;
            case 181:
                if (npcs[i].attackType == 2)
                    return 717;
                if (npcs[i].attackType == 0)
                    return 422;
            case 172:
            case 174:
                return 711;
            case 795:
                return 1979;
            case 115:
                return 359;
            case 3066:
                return 1658;
            case 13: //wizards
                return 711;

            case 103:
            case 655:
                return 123;

            case 1624:
                return 1557;


            case 2783: //dark beast
                return 2733;

            case 1615: //abby demon
                return 1537;

            case 1613: //nech
                return 1528;

            case 1610:
            case 1611: //garg
                return 1519;

            case 1616: //basilisk
                return 1546;

            case 50://drags
            case 53:
            case 54:
            case 55:
            case 941:
            case 1590:
            case 1591:
            case 1592:
                return 80;

            case 124: //earth warrior
                return 390;

            case 803: //monk
                return 422;

            case 52: //baby drag
                return 25;
            case 58: //Shadow Spider
            case 59: //Giant Spider
            case 2491:
            case 60: //Giant Spider
            case 61: //Spider
            case 62: //Jungle Spider
            case 63: //Deadly Red Spider
            case 64: //Ice Spider
            case 134:
                return 143;

            case 105: //Bear
            case 106:  //Bear
                return 41;

            case 412:
            case 78:
                return 30;

            case 2033: //rat
                return 138;

            case 2031: // bloodworm
                return 2070;

            case 101: // goblin
                return 310;

            case 81: // cow
                return 0x03B;

            case 21: // hero
                return 451;

            case 41: // chicken
            case 951:
                return 55;

            case 9: // guard
            case 32: // guard
            case 20: // paladin
                return 451;

            case 1338: // dagannoth
            case 1340:
            case 1342:
                return 1341;

            case 19: // white knight
                return 406;

            case 110:
            case 111: // ice giant
            case 112:
            case 117:
                return 128;

            case 2452:
                return 1312;

            case 2889:
                return 2859;

            case 118:
            case 119:
                return 99;

            case 82://Lesser Demon
            case 83://Greater Demon
            case 84://Black Demon
                return 64;
            case 1472:
                if (npcs[i].attackType == 2)
                    return 69;
                else if (npcs[i].attackType == 0)
                    return 64;
            case 1993:
                return 2039;
            case 1265:
            case 1266:
            case 1267:
            case 1268:
                return 1312;

            case 125: // ice warrior
            case 178:
                return 451;

            case 1153: //Kalphite Worker
            case 1154: //Kalphite Soldier
            case 1155: //Kalphite guardian
            case 1156: //Kalphite worker
            case 1157: //Kalphite guardian
                return 1184;

            case 123:
            case 122:
                return 164;

            case 2028: // karil
                return 2075;

            case 2025: // ahrim
                return 729;

            case 94:
            case 2262:
                return 1979;

            case 2026: // dharok
                return 2067;

            case 2027: // guthan
                return 2080;

            case 2029: // torag
                return 0x814;

            case 2030: // verac
                return 2062;
            case 127:
                return 185;
            case 2881: //supreme
                return 2855;
            case 1456:
                return 1394;
            case 2882: //prime
                return 2854;
            case 1648:
                return 1592;
            case 416:
                return 153;
            case 2883: //rex
                return 2851;
            case 3200:
                return 3146;
            case 1600:
                return 227;
            case 1622:
            case 1623:
                return 1567;
            case 2745:
                if (npcs[i].attackType == 2)
                    return 2656;
                else if (npcs[i].attackType == 1)
                    return 2652;
                else if (npcs[i].attackType == 0)
                    return 2655;
            case 1612:
                return 1523;
            case 1677:
                return 1616;
            case 71:
                return 230;
            case 72:
                return 284;
            case 3649:
                return 3812;
            case 2604:
                return 2612;
            case 2598:
                return 2609;
            case 2610:
                return 2610;
            case 1995:
                return 2014;
            case 135:
                return 304;
            case 3594:
                return 3709;
            case 750:
                return 412;
            case 132:
                return 220;
            case 1627:
                return 1595;
            default:
                return 0x326;
        }
    }


    public int getDeadEmote(int i) {
        switch (npcs[i].npcType) {
            case 1604:
                return 1508;
            case 1608:
                return 1513;
            case 3068:
                return 2987;
            case 1627:
                return 1597;
            case 3655:
                return 3827;
            case 1637:
                return 1587;
            case 132:
                return 223;
            case 3618:
                return 3620;
            case 3594:
                return 3711;
            case 3619:
                return 3733;
            case 3585:
                return 146;
            case 136:
                return 325;
            case 135:
                return 307;
            case 1995:
                return 2013;
            case 1265:
            case 1266:
            case 1267:
            case 1268:
                return 1314;
            case 2604:
            case 2610:
            case 2598:
                return 2607;
            case 71:
                return 233;
            case 1614:
                return 1541;
            case 3649:
                return 3813;
            case 1677:
                return 1618;
            case 93:
            case 92:
                return 263;
            case 127:
                return 196;
            case 1612:
                return 1524;
            case 1633:
                return 1580;
            case 708:
                return 172;
            case 1622:
            case 1623:
                return 1568;
            case 1616:
                return 1548;
            case 1832:
                return 1778;
            case 1831:
                return 1792;
            case 1600:
                return 228;
            case 1648:
                return 1590;
            case 113:
                return 131;
            case 89:
                return 292;
            case 2494:
            case 2495:
                return 2398;
            case 1827:
                return 1804;
            case 3346:
            case 3347:
            case 3345:
            case 3344:
                return 3327;
            case 1993:
                return 2038;
            case 110:
            case 111:
            case 112:
                return 131;
            case 1115:
                return 287;
            case 1593:
            case 143:
                return 78;
            case 128:
                return 276;
            case 2529:
                return 2304;
            case 49:
                return 161;
            case 107:
            case 108:
                return 248;
            case 1456://monkey archer
                return 1397;
            case 73:
                return 302;
            case 90:
                return 263;
            case 96:
            case 97:
            case 141:
            case 1558:
                return 78;
            case 1459:
                return 1404;
            case 396:
            case 72:
                return 287;
            case 115:
                return 361;
            case 117:
                return 131;
            case 3066:
                return 1342;
            case 2558:
                return 3503;
            case 2559:
            case 2560:
            case 2561:
                return 6956;
            case 2607:
                return 2607;
            case 2627:
                return 2620;
            case 2630:
                return 2627;
            case 2631:
                return 2630;
            case 2738:
                return 2627;
            case 2741:
                return 2638;
            case 2746:
                return 2638;
            case 2743:
                return 2646;
            case 2745:
                return 2654;

            case 3777:
            case 3778:
            case 3779:
            case 3780:
                return -1;

            case 3200:
                return 3147;

            case 2035: //spider
                return 146;

            case 2033: //rat
                return 141;

            case 2031: // bloodvel
                return 2073;

            case 101: //goblin
                return 313;

            case 81: // cow
                return 0x03E;

            case 41: // chicken
            case 951:
                return 57;

            case 1338: // dagannoth
            case 1340:
            case 1342:
                return 1342;

            case 2881:
            case 2882:
            case 2883:
                return 2856;

            case 125: // ice warrior
                return 843;

            case 751://Zombies!!
                return 302;

            case 1626:
            case 1628:
            case 1629:
            case 1630:
            case 1631:
            case 1632: //turoth!
                return 1597;

            case 1653: //hand
                return 1590;

            case 82://demons
            case 83:
            case 84:
            case 1472:
                return 67;

            case 1605://abby spec
                return 1508;

            case 51://baby drags
            case 52:
            case 1589:
            case 3376:
                return 28;

            case 1610:
            case 1611:
                return 1518;
            case 394:
                return 287;
            case 416:
                return 156;
            case 1618:
            case 1619:
                return 1553;

            case 1620:
            case 1621:
                return 1563;

            case 2783:
                return 2732;

            case 1615:
                return 1538;

            case 1624:
                return 1558;

            case 1613:
                return 1530;

            case 100:
            case 102:
            case 1823:
                return 313;

            case 105:
            case 106:
                return 44;

            case 412:
            case 78:
                return 36;

            case 122:
            case 123:
                return 167;

            case 58:
            case 2491:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 134:
                return 146;

            case 1153:
            case 1154:
            case 1155:
            case 1156:
            case 1157:
                return 1190;

            case 103:
            case 104:
            case 655:
                return 123;

            case 118:
            case 119:
                return 102;


            case 50://drags
            case 53:
            case 54:
            case 55:
            case 941:
            case 1590:
            case 1591:
            case 1592:
                return 92;


            default:
                return 2304;
        }
    }

    /**
     * Attack delays
     */
    public static int getNpcDelay(int i) {
        switch (npcs[i].npcType) {
            case 2025:
            case 2028:
            case 2530:
                return 7;
            case 94:
            case 2262:
                return 6;
            case 2745:
                return 8;
            case 2529:
                return 4;
            case 2558:
            case 2559:
            case 2560:
            case 2561:
            case 2550:
                return 6;
            //saradomin gw boss
            case 2562:
                return 2;

            default:
                return 5;
        }
    }

    /**
     * Hit delays
     */
    public static int getHitDelay(int i) {
        switch (npcs[i].npcType) {
            case 2881:
            case 2882:
            case 3200:
            case 2892:
            case 2894:
                return 3;
            case 94:
            case 2262:
                return 5;
            case 2743:
            case 2631:
            case 2558:
            case 2559:
            case 2560:
            case 1643:
            case 1472:
            case 172:
            case 174:
                return 3;
            case 2745:
                if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
                    return 5;
                else
                    return 2;
            case 2025:
                return 4;
            case 2028:
            case 795:
                return 3;
            default:
                return 1;
        }
    }

    /**
     * Npc respawn time
     */
    public int getRespawnTime(int i) {
        switch (npcs[i].npcType) {
            case 2881:
            case 2882:
            case 2883:
            case 2558:
            case 2559:
            case 2560:
            case 2561:
            case 2562:
            case 2563:
            case 2564:
            case 2550:
            case 2551:
            case 2552:
            case 2553:
                return 100;
            case 1472:
                return 100;
            case 3777:
            case 3778:
            case 3779:
            case 3780:
                return 500;
            default:
                return 25;
        }
    }



	/* 	public String[] guardRandomTalk = {
	"We must not fail!",
	"Take down the portals",
	"The Void Knights will not fall!",
	"Hail the Void Knights!",
	"We are beating these scum!"
	}; */

    public void newNPC(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }

        if (slot == -1) return;        // no free slot found

        NPC newNPC = new NPC(slot, npcType);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.HP = HP;
        newNPC.MaxHP = HP;
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        npcs[slot] = newNPC;
    }

    public void newNPCList(int npcType, String npcName, int combat, int HP) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcList[i] == null) {
                slot = i;
                break;
            }
        }

        if (slot == -1) return;        // no free slot found

        NPCList newNPCList = new NPCList(npcType);
        newNPCList.npcName = npcName;
        newNPCList.npcCombat = combat;
        newNPCList.npcHealth = HP;
        NpcList[slot] = newNPCList;
    }

    public void appendSlayerExperience(int i) {
        int npc = 0;
        Client c = (Client) Server.playerHandler.players[npcs[i].killedBy];
        if (c != null) {
            if (c.slayerTask == npcs[i].npcType) {
                c.getPA().addSkillXP(npcs[i].MaxHP * Config.SLAYER_EXPERIENCE, 18);
                c.taskAmount--;
                if (c.taskAmount <= 0) {
                    for (int j = 0; j < Slayer.lowTasks.length; j++) {
                        for (int k = 0; k < Slayer.lowMidTasks.length; k++) {
                            if (Slayer.lowTasks[j] == c.slayerTask || Slayer.lowMidTasks[k] == c.slayerTask) {
                                c.slayerTask = -1;
                                c.slPoints += (5 + -Misc.random(1));
                                c.sendMessage("@blu@You completed your Easy slayer task and now have " + c.slPoints + " Points.");
                                c.sendMessage("@blu@Please see a slayer master to get a new one.");
                                return;
                            }
                        }
                    }
                    for (int j = 0; j < Slayer.medTasks.length; j++) {
                        for (int k = 0; k < Slayer.HighmedTasks.length; k++) {
                            if (Slayer.medTasks[j] == c.slayerTask || Slayer.HighmedTasks[k] == c.slayerTask) {
                                c.slayerTask = -1;
                                c.slPoints += (10 + -Misc.random(3));
                                c.sendMessage("@blu@You completed your Medium slayer task and now have " + c.slPoints + " Points.");
                                c.sendMessage("@blu@Please see a slayer master to get a new one.");
                                return;
                            }
                        }
                    }
                    for (int j = 0; j < Slayer.highTasks.length; j++) {
                        if (Slayer.highTasks[j] == c.slayerTask) {
                            c.slayerTask = -1;
                            c.slPoints += (15 + -Misc.random(6));
                            c.sendMessage("@blu@You completed your High slayer task and now have " + c.slPoints + " Points.");
                            c.sendMessage("@blu@Please see a slayer master to get a new one.");
                            return;
                        }
                    }
                }

            }
        }
    }

    public void DuradelTeleport(Client c) {
        for (int i = 0; i < maxNPCs; i++) {
            if (npcs[i] == null)
                continue;
            npcs[i].clearUpdateFlags();

        }
        for (int i = 0; i < maxNPCs; i++) {
            if (npcs[i] != null) {
                if (npcs[i].npcType == 1599) {
                    npcs[i].facePlayer(c.playerId);
                    npcs[i].animNumber = 1818;
                    npcs[i].gfx0(343);
                    npcs[i].animUpdateRequired = true;
                    npcs[i].updateRequired = true;
                    break;
                }
            }
        }
    }

    public void auburyTeleport(Client c) {
        for (int i = 0; i < maxNPCs; i++) {
            if (npcs[i] == null)
                continue;
            npcs[i].clearUpdateFlags();

        }
        for (int i = 0; i < maxNPCs; i++) {
            if (npcs[i] != null) {
                if (npcs[i].npcType == 553) {
                    npcs[i].facePlayer(c.playerId);
                    npcs[i].animNumber = 1818;
                    npcs[i].gfx0(343);
                    npcs[i].forceChat("Senventior Disthine Molenko!");
                    npcs[i].animUpdateRequired = true;
                    npcs[i].updateRequired = true;
                    break;
                }
            }
        }
    }

    public void process() {
        for (NPC i : NPCHandler.npcs) {
            if (i == null) continue;
            i.clearUpdateFlags();

        }
        for (int i = 0; i < maxNPCs; i++) {
            if (npcs[i] != null) {

                Client slaveOwner = (Server.playerHandler.players[npcs[i].summonedBy] != null ? (Client) Server.playerHandler.players[npcs[i].summonedBy] : null);
                if (npcs[i] != null && slaveOwner == null && npcs[i].summoner) {
                    npcs[i].absX = 0;
                    npcs[i].absY = 0;
                }
                if (npcs[i] != null && slaveOwner != null && slaveOwner.hasNpc && (!slaveOwner.goodDistance(npcs[i].getX(), npcs[i].getY(), slaveOwner.absX, slaveOwner.absY, 15) || slaveOwner.heightLevel != npcs[i].heightLevel) && npcs[i].summoner) {
                    npcs[i].absX = slaveOwner.absX;
                    npcs[i].absY = slaveOwner.absY;
                    npcs[i].heightLevel = slaveOwner.heightLevel;
                }
                if (npcs[i] != null && slaveOwner != null && slaveOwner.hasNpc && slaveOwner.absX == npcs[i].getX() && slaveOwner.absY == npcs[i].getY() && npcs[i].summoner) {
                    stepAway(i);
                }
                if (npcs[i].actionTimer > 0) {
                    npcs[i].actionTimer--;
                }
                if (npcs[i].npcType == 951) { //NPC NAME HERE
                    if (Misc.random(107) <= 1) { //EX FOR TIMER: 20
                        npcs[i].forceChat("Bawk bawk");
                    }
                }
                if (npcs[i].npcType == 81) { //NPC NAME HERE
                    if (Misc.random(191) <= 2) { //EX FOR TIMER: 20
                        npcs[i].forceChat("Moo");
                    }
                }
                if (npcs[i].npcType == 1395) { //NPC NAME HERE
                    if (Misc.random(197) <= 3) { //EX FOR TIMER: 20
                        npcs[i].updateRequired = true;
                        if (npcs[i].absX == 2550 && npcs[i].absY == 3866)
                            npcs[i].forceChat("Lets go boys!");
                        if (npcs[i].absX == 2545 && npcs[i].absY == 3864)
                            npcs[i].forceChat("Keep it up!");
                        if (npcs[i].absX == 2543 && npcs[i].absY == 3869)
                            npcs[i].forceChat("Remember to sound the tree!");
                        if (npcs[i].absX == 2548 && npcs[i].absY == 3870)
                            npcs[i].forceChat("Don't forget the back cut!");
                    }
                }
                if (npcs[i].hitDelayTimer == 1) {
                    npcs[i].hitDelayTimer = 0;
                    applyDamage(i);
                }
                if (npcs[i].freezeTimer > 0) {
                    npcs[i].freezeTimer--;
                }

                if (npcs[i].hitDelayTimer > 0) {
                    npcs[i].hitDelayTimer--;
                }
                if (npcs[i].attackTimer > 0) {
                    npcs[i].attackTimer--;
                }
                if (npcs[i].lastX != npcs[i].getX()
                        || npcs[i].lastY != npcs[i].getY()) {
                    npcs[i].lastX = npcs[i].getX();
                    npcs[i].lastY = npcs[i].getY();
                }
                if (npcs[i].transformTime > 0 && System.currentTimeMillis() - npcs[i].lastTransformed > npcs[i].transformTime)
                    npcs[i].requestTransform(npcs[i].npcType);
                if (npcs[i] == null) continue;

                /**
                 * Attacking player
                 **/
                if (NPCAggressive.isAggressive(npcs[i].npcType) && !npcs[i].underAttack && !npcs[i].isDead && !switchesAttackers(i)) {
                    Client player = (Client) PlayerHandler.players[getCloseRandomPlayer(i)];
                    if (player != null && getNpcListCombat(npcs[i].npcType) * 2 > player.combatLevel) {
                        npcs[i].killerId = getCloseRandomPlayer(i);
                    }

                } else if (NPCAggressive.isAggressive(npcs[i].npcType) && !npcs[i].underAttack && !npcs[i].isDead && switchesAttackers(i)) {
                    Client player = (Client) PlayerHandler.players[getCloseRandomPlayer(i)];
                    if (player != null && getNpcListCombat(npcs[i].npcType) * 2 > player.combatLevel) {
                        npcs[i].killerId = getCloseRandomPlayer(i);
                    }
                } else if (npcs[i].inWild()) {
                    Client player = (Client) PlayerHandler.players[getCloseRandomPlayer(i)];
                    if (player != null && getNpcListCombat(npcs[i].npcType) * 2 > player.combatLevel) {
                        npcs[i].killerId = getCloseRandomPlayer(i);
                    }
                }
                if (npcs[i].npcType == 3618 || npcs[i].npcType == 3619) {
                    Client player = (Client) PlayerHandler.players[getCloseRandomPlayer(i)];
                    if (player != null) {
                        if (player.inRfd())
                            return;
                        //if (npcs[94].underAttack == true) {
                        npcs[i].killerId = getCloseRandomPlayer(i);
                        //npcs[i].forceChat("Arrrghhhhhhhh!");
                        //}
                    }
                }
                if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000)
                    npcs[i].underAttackBy = 0;
                //Fix for attacking
                if ((npcs[i].killerId > 0 || npcs[i].underAttack) && retaliates(npcs[i].npcType)) {
                    if (!npcs[i].isDead) {
                        int p = npcs[i].killerId;
                        if (Server.playerHandler.players[p] != null) {
                            if (npcs[i].summoner == false) {
                                Client c = (Client) Server.playerHandler.players[p];
                                followPlayer(i, c.playerId);
                                if (npcs[i] == null) continue;
                                if (npcs[i].attackTimer == 0) {
                                    if (c != null) {
                                        attackPlayer(c, i);
                                    } else {
                                        npcs[i].killerId = 0;
                                        npcs[i].underAttack = false;
                                        npcs[i].facePlayer(0);
                                    }
                                }
                            } else {
                                Client c = (Client) Server.playerHandler.players[p];
                                followPlayer(i, c.playerId);
                            }
                        } else {
                            npcs[i].killerId = 0;
                            npcs[i].underAttack = false;
                            npcs[i].facePlayer(0);
                        }
                    }
                }

                if (npcs[i] != null) {
                    GoblinVillage.followNpc(i, npcs[i].attackingNpc);
                    if (!npcs[i].underAttack && npcs[i].killerId <= 0) {
                        if (!npcs[i].isDead) {
                            if (npcs[i].underAttackByNpc) {
                                if (npcs[i].attackTimer == 0 && npcs[i].npcType != 3783) {
                                    //	GoblinVillage.followNpc(i, npcs[i].attackingNpc);
                                    GoblinVillage.attackNpc(i, npcs[i].attackingNpc);
                                }
                            }
                        }
                    }
                }
                if (!npcs[i].isDead) {
                    Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
                    if (c != null) {
                        //c.getPA().sound(getNpcListDieSound(npcs[i].npcType));
                        //c.getPA().sound(Sound.getNPCSound(npcs[i].npcType, "Death"));
                    }

                }
                if (npcs[i].isDead == true) {
                    if (npcs[i].actionTimer == 3 && npcs[i].applyDead && !npcs[i].needRespawn) {
                        Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
                        if (c != null) {
                            //  c.getPA().sound(getNpcListDieSound(npcs[i].npcType));
                            //c.getPA().sound(Sound.getNPCSound(npcs[i].npcType, "Death"));
                        }
                    }


                    if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false && npcs[i].needRespawn == false) {
                        npcs[i].updateRequired = true;
                        npcs[i].facePlayer(0);
                        npcs[i].killedBy = getNpcKillerId(i);
                        npcs[i].animNumber = getDeadEmote(i); // dead emote
                        npcs[i].animUpdateRequired = true;
                        npcs[i].freezeTimer = 0;
                        npcs[i].applyDead = true;
                        killedBarrow(i);
                        npcs[i].actionTimer = 4; // delete time
                        resetPlayersInCombat(i);
                    } else if (npcs[i].actionTimer == 0 && npcs[i].applyDead == true && npcs[i].needRespawn == false) {
                        npcs[i].needRespawn = true;
                        npcs[i].actionTimer = getRespawnTime(i); // respawn time
                        dropItems(i); // npc drops items!
                        appendSlayerExperience(i);
                        tzhaarDeathHandler(i);
                        RfdDeathHandler(i);
                        if (npcs[i].npcType == 2745) {
                            handleJadDeath(i);
                        }
                        Client player = (Client) PlayerHandler.players[npcs[i].killedBy];
                        if (player != null) {
                            if (player.tutorialprog == 24) {
                                handleratdeath(i);
                            } else if (player.tutorialprog == 25 && player.ratdied2 == true) {
                                handleratdeath2(i);
                            }

                        }
                        npcs[i].absX = npcs[i].makeX;
                        npcs[i].absY = npcs[i].makeY;
                        npcs[i].HP = npcs[i].MaxHP;
                        npcs[i].animNumber = 0x328;
                        npcs[i].updateRequired = true;
                        npcs[i].animUpdateRequired = true;
                        if (npcs[i].npcType == 757)
                            handleCountDray(i);
                        if (npcs[i].npcType == 101)
                            handleGoblin(i);
                        if (npcs[i].npcType >= 2440 && npcs[i].npcType <= 2446) {
                            Server.objectManager.removeObject(npcs[i].absX, npcs[i].absY);
                        }
                    } else if (npcs[i].actionTimer == 0 && npcs[i].needRespawn == true) {
                        Client player = (Client) PlayerHandler.players[npcs[i].spawnedBy];
                        if (player != null) {
                            npcs[i] = null;
                        } else {
                            int old1 = npcs[i].npcType;
                            int old2 = npcs[i].makeX;
                            int old3 = npcs[i].makeY;
                            int old4 = npcs[i].heightLevel;
                            int old5 = npcs[i].walkingType;
                            int old6 = npcs[i].MaxHP;
                            int old7 = npcs[i].maxHit;
                            int old8 = npcs[i].attack;
                            int old9 = npcs[i].defence;

                            npcs[i] = null;
                            newNPC(old1, old2, old3, old4, old5, old6, old7, old8, old9);

                        }
                    }
                }

                /**
                 * Random walking and walking home
                 **/
                if (npcs[i] == null) continue;
                if ((!npcs[i].underAttack || npcs[i].walkingHome) && npcs[i].randomWalk && !npcs[i].isDead) {
                    npcs[i].facePlayer(0);
                    npcs[i].killerId = 0;
                    if (npcs[i].spawnedBy == 0) {
                        if ((npcs[i].absX > npcs[i].makeX + Config.NPC_RANDOM_WALK_DISTANCE) || (npcs[i].absX < npcs[i].makeX - Config.NPC_RANDOM_WALK_DISTANCE) || (npcs[i].absY > npcs[i].makeY + Config.NPC_RANDOM_WALK_DISTANCE) || (npcs[i].absY < npcs[i].makeY - Config.NPC_RANDOM_WALK_DISTANCE)) {
                            npcs[i].walkingHome = true;
                        }
                    }

                    if (npcs[i].walkingHome && npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
                        npcs[i].walkingHome = false;
                    } else if (npcs[i].walkingHome) {
                        NPCPathFinder.findRoute(npcs[i], npcs[i].makeX, npcs[i].makeY);
                    }
                    if (npcs[i].npcType == 3618 || npcs[i].npcType == 3619)
                        npcs[i].turnNpc(npcs[i].absX - 1, npcs[i].absY);
                    if (npcs[i].npcType == 1395) {
                        if (npcs[i].absX == 2545 && npcs[i].absY == 3864)
                            npcs[i].turnNpc(npcs[i].absX, npcs[i].absY + 1);
                        if (npcs[i].absX == 2559 && npcs[i].absY == 3871)
                            npcs[i].turnNpc(npcs[i].absX, npcs[i].absY - 1);
                        if (npcs[i].absX == 2550 && npcs[i].absY == 3866)
                            npcs[i].turnNpc(npcs[i].absX, npcs[i].absY - 1);
                        if (npcs[i].absX == 2543 && npcs[i].absY == 3869)
                            npcs[i].turnNpc(npcs[i].absX - 1, npcs[i].absY);
                        if (npcs[i].absX == 2548 && npcs[i].absY == 3870)
                            npcs[i].turnNpc(npcs[i].absX - 1, npcs[i].absY - 1);
                    }
                    if (Misc.random(3) == 1 && !npcs[i].walkingHome) {
                        int Rnd = Misc.random(9);
                        switch (Rnd) {
                            case 1:
                                NPCPathFinder.findRoute(npcs[i], npcs[i].absX + 1, npcs[i].absY + 1);
                                break;
                            case 2:
                                NPCPathFinder.findRoute(npcs[i], npcs[i].absX - 1, npcs[i].absY);
                                break;
                            case 3:
                                NPCPathFinder.findRoute(npcs[i], npcs[i].absX, npcs[i].absY - 1);
                                break;
                            case 4:
                                NPCPathFinder.findRoute(npcs[i], npcs[i].absX + 1, npcs[i].absY);
                                break;
                            case 5:
                                NPCPathFinder.findRoute(npcs[i], npcs[i].absX, npcs[i].absY + 1);
                                break;
                            case 6:
                                NPCPathFinder.findRoute(npcs[i], npcs[i].absX - 1, npcs[i].absY - 1);
                                break;
                            case 7:
                                NPCPathFinder.findRoute(npcs[i], npcs[i].absX - 1, npcs[i].absY + 1);
                                break;
                            case 8:
                                NPCPathFinder.findRoute(npcs[i], npcs[i].absX + 1, npcs[i].absY - 1);
                                break;
                        }
                    }
                }


            }
        }
    }

    private void stepAway(Client c, int i) {
        // TODO Auto-generated method stub
        int otherX = NPCHandler.npcs[i].getX();
        int otherY = NPCHandler.npcs[i].getY();
        if (otherX == c.getX() && otherY == c.getY()) {
            if (Region.getClipping(c.getX() + 1, c
                    .getY(), c.heightLevel, 1, 0)) {
                npcs[i].moveX = 1;
            } else if (Region.getClipping(c.getX(), c
                    .getY() - 1, c.heightLevel, 0, -1)) {
                npcs[i].moveY = -1;
            } else if (Region.getClipping(c.getX() - 1, c.getY(),
                    c.heightLevel, -1, 0)) {
                npcs[i].moveX = -1;
            } else if (Region.getClipping(c.getX(), c
                    .getY() + 1, c.heightLevel, 0, 1)) {
                npcs[i].moveY = 1;
            }
            npcs[i].getNextNPCMovement(i);
            npcs[i].updateRequired = true;
        }
    }

    private void stepAwayNew(Client c, int i) {
        // TODO Auto-generated method stub
        int otherX = NPCHandler.npcs[i].getX();
        int otherY = NPCHandler.npcs[i].getY();
        if (otherX == c.getX() && otherY == c.getY()) {
            if (Region.getClipping(c.getX() + NPCHandler.npcSizes(i), c
                    .getY(), c.heightLevel, NPCHandler.npcSizes(i), 0)) {
                npcs[i].moveX = NPCHandler.npcSizes(i);
            } else if (Region.getClipping(c.getX(), c
                    .getY() - NPCHandler.npcSizes(i), c.heightLevel, 0, -NPCHandler.npcSizes(i))) {
                npcs[i].moveY = -NPCHandler.npcSizes(i);
            } else if (Region.getClipping(c.getX() - NPCHandler.npcSizes(i), c.getY(),
                    c.heightLevel, -NPCHandler.npcSizes(i), 0)) {
                npcs[i].moveX = -NPCHandler.npcSizes(i);
            } else if (Region.getClipping(c.getX(), c
                    .getY() + NPCHandler.npcSizes(i), c.heightLevel, 0, NPCHandler.npcSizes(i))) {
                npcs[i].moveY = NPCHandler.npcSizes(i);
            }
            npcs[i].getNextNPCMovement(i);
            npcs[i].updateRequired = true;
        }
    }

    private void handleCountDray(int i) {
        Client c = (Client) PlayerHandler.players[npcs[i].spawnedBy];
        if (c != null) {
            c.vampireslay = 5; //5 FINISH
            QuestHandling.VampireFinish(c);
            c.getPA().loadQuests();
            //c.getVS().finish

        }
    }

    private void handleGoblin(int i) {
        Client c = (Client) PlayerHandler.players[npcs[i].spawnedBy];
        if (c != null) {
            if (c.inRfd())
                return;
            c.SailA = 2; //5 FINISH
            c.startAnimation(827);
            c.sendMessage("You grab the directions from the corpse of the goblin.");
        }
    }

    private void handleratdeath(int i) {
        final Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
        if (c != null) {
            Tutorialisland.chatbox(c, 6180);
            Tutorialisland.chatboxText
                    (c, "",
                            "Pass through the gate and talk to the Combat Instructor, he",
                            "will give you your next task.",
                            "",
                            "Well done, you've made your first kill!");
            Tutorialisland.chatbox(c, 6179);
            c.getPA().drawHeadicon(1, 6, 0, 0); // draws headicon to combat ude
            c.tutorialprog = 25;
        }
    }

    private void tzhaarDeathHandler(int i) {
        if (isFightCaveNpc(i) && npcs[i].npcType != FightCaves.TZ_KEK)
            killedTzhaar(i);
        if (npcs[i].npcType == FightCaves.TZ_KEK_SPAWN) {
            int p = npcs[i].killerId;
            if (PlayerHandler.players[p] != null) {
                Client c = (Client) PlayerHandler.players[p];
                c.tzKekSpawn += 1;
                if (c.tzKekSpawn == 2) {
                    killedTzhaar(i);
                    c.tzKekSpawn = 0;
                }
            }
        }

        if (npcs[i].npcType == FightCaves.TZ_KEK) {
            int p = npcs[i].killerId;
            if (PlayerHandler.players[p] != null) {
                Client c = (Client) PlayerHandler.players[p];
                FightCaves.tzKekEffect(c, i);
            }
        }
    }

    private void killedTzhaar(int i) {
        final Client c2 = (Client) PlayerHandler.players[npcs[i].spawnedBy];
        c2.tzhaarKilled++;
        if (c2.tzhaarKilled == c2.tzhaarToKill) {
            c2.waveId++;
            EventManager.getSingleton().addEvent(new Event() {
                public void execute(EventContainer c) {
                    if (c2 != null) {
                        FightCaves.spawnNextWave(c2);
                    }
                    c.stop();
                }
            }, 6500);

        }
    }

    public boolean isFightCaveNpc(int i) {
        switch (npcs[i].npcType) {
            case FightCaves.TZ_KIH:
            case FightCaves.TZ_KEK:
            case FightCaves.TOK_XIL:
            case FightCaves.YT_MEJKOT:
            case FightCaves.KET_ZEK:
            case FightCaves.TZTOK_JAD:
                return true;
        }
        return false;
    }

    public void handleJadDeath(int i) {
        Client c = (Client) Server.playerHandler.players[npcs[i].spawnedBy];
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Client c2 = (Client) PlayerHandler.players[j];
                c2.sendMessage("@or2@Attention everyone " + c.playerName + " has slain the mighty Tztok-Jad!");
            }
        }
        c.getItems().addItem(6570, 1);
        DialogueHandler.sendDialogues(c, 69, 2617);
        c.getPA().resetTzhaar();
        c.waveId = 300;
    }

    private void RfdDeathHandler(int i) {
        int p = npcs[i].killerId;
        Client c = (Client) PlayerHandler.players[p];
        Client v = (Client) Server.playerHandler.players[npcs[i].spawnedBy];
        if (v != null) {
            if (isRfdNpc(i) && npcs[i].npcType != FightCaves.TZ_KEK && v.inRfd()) {
                v.RfdPoints += getRfdpoints(i);
                killedRfd(i);
            }
        }
    }

    /**
     * Raises the count of tzhaarKilled, if tzhaarKilled is equal to the amount
     * needed to kill to move onto the next wave it raises wave id then starts next
     * wave.
     *
     * @param i The npc.
     */
    private void killedRfd(int i) {
        final Client c2 = (Client) PlayerHandler.players[npcs[i].spawnedBy];
        c2.RfdKilled++;
        if (c2.RfdKilled == c2.RfdToKill) {
            c2.RfdwaveId++;
            CycleEventHandler.getSingleton().addEvent(c2, new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    if (c2 != null) {
                        RFD.spawnNextWave(c2);
                        container.stop();
                    }
                }

                @Override
                public void stop() {
                }
            }, 5);

        }
    }

    /**
     * Checks if something is a fight cave npc.
     *
     * @param i The npc.
     * @return Whether or not it is a fight caves npc.
     */
    public boolean isRfdNpc(int i) {
        switch (npcs[i].npcType) {
            case RFD.MONKEY:
            case RFD.IMP:
            case RFD.COW:
            case RFD.GOBLIN:
            case RFD.TRIBESMAN:
            case RFD.GIANT_BAT:
            case RFD.MAMMOTH:
            case RFD.GREEN_DRAGON:
            case RFD.WIZARD:
            case RFD.CHAOS_DWARF:
            case RFD.HILL_GIANT:
            case RFD.SCORPION:
            case RFD.MONKEY_ARCHER:
            case RFD.LOCUST:
            case RFD.SKELETON:
                return true;
        }
        return false;
    }

    private void handleratdeath2(int i) {
        Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
        if (c != null) {
            Tutorialisland.chatbox(c, 6180);
            Tutorialisland.chatboxText
                    (c, "You have completed the tasks here. To move on, click on the",
                            "ladder shown. If you need to go over any of what you learnt",
                            "here, just talk to the Combat Instructor and he'll tell you what",
                            "he can.",
                            "Moving on");
            Tutorialisland.chatbox(c, 6179);

            c.tutorialprog = 26;
            c.getPA().createArrow(3111, 9525, c.getHeightLevel(), 2); //send hint to furnace
        }
    }


    public boolean getsPulled(int i) {
        switch (npcs[i].npcType) {
            case 2550:
                if (npcs[i].firstAttacker > 0)
                    return false;
                break;
        }
        return true;
    }

    public boolean multiAttacks(int i) {
        switch (npcs[i].npcType) {
            case 2558:
                return true;
            case 2562:
                if (npcs[i].attackType == 2)
                    return true;
            case 2550:
                if (npcs[i].attackType == 1)
                    return true;
            default:
                return false;
        }


    }

    /**
     * Npc killer id?
     */

    public int getNpcKillerId(int npcId) {
        int oldDamage = 0;
        int killerId = 0;
        for (int p = 1; p < Config.MAX_PLAYERS; p++) {
            if (PlayerHandler.players[p] != null) {
                if (PlayerHandler.players[p].lastNpcAttacked == npcId) {
                    if (PlayerHandler.players[p].totalDamageDealt > oldDamage) {
                        oldDamage = PlayerHandler.players[p].totalDamageDealt;
                        killerId = p;
                    }
                    PlayerHandler.players[p].totalDamageDealt = 0;
                }
            }
        }
        return killerId;
    }


    /**
     * Dropping Items!
     */
    public boolean rareDrops(int i) {
        return Misc.random(NPCDrops.dropRarity.get(npcs[i].npcType)) == 0;
    }

    public boolean normalDrops(int i) {
        return Misc.random(NPCDrops.dropRarity.get(npcs[i].npcType)) != 0;
    }

    public boolean IsBone(int item) {
        switch (item) {
            case 526:
            case 532:
            case 536:
            case 592:
            case 3181:
                return true;
            default:
                return false;
        }
    }

    private int getRandomHItem() {
        int[] Hitems = {1050, 1050};
        return Hitems[(int) Math.floor(Math.random() * Hitems.length)];
    }

    public void dropItems(int i) {
        int npc = 0;
        Client c = (Client) Server.playerHandler.players[npcs[i].killedBy];
        if (c != null) {
            if (c.inRfd())
                return;
            if (npcs[i].npcType == 1613)
                Server.npcHandler.spawnNpc(c, 1614, c.absX + 1, c.absY, 0, 0, 60, 33,
                        33, 3, true, false);
            if (npcs[i].npcType == 912 || npcs[i].npcType == 913 || npcs[i].npcType == 914)
                c.magePoints += 1;
            if (c != null) {
                int cracker = Misc.random(1000 - getNpcListCombat(npcs[i].npcType));
                if (cracker == 0) {
                    Server.itemHandler.createGroundItem(c, getRandomHItem(), npcs[i].absX, npcs[i].absY, 1, c.playerId);
                    if (c.rareOn != false) {
                        c.getPA().handledropOn(c, getRandomHItem(), 1);
                        return;
                    }
                }
            }
            if (NPCDrops.constantDrops.get(npcs[i].npcType) != null) {
                for (int item : NPCDrops.constantDrops.get(npcs[i].npcType)) {
                    if (c.playerEquipment[c.playerCape] == 7918 && IsBone(item)) {
                        c.getPA().addSkillXP(c.getPrayer().getExp(item) * 7, 5);
                    } else if (npcs[i].npcType == 795) {
                        Server.itemHandler.createGroundItem(c, item, 2501, 3731, 1, c.playerId);
                    } else {
                        Server.itemHandler.createGroundItem(c, item, npcs[i].absX, npcs[i].absY, 1, c.playerId);
                    }
                }
            }

            if (NPCDrops.dropRarity.get(npcs[i].npcType) != null) {
                int random1 = Misc.random(NPCDrops.normalDrops.get(npcs[i].npcType).length - 1);
                if (NPCDrops.normalDrops.get(npcs[i].npcType)[random1][0] == -1) {
                    return;
                }
                if (rareDrops(i)) {
                    try {
                        int random = Misc.random(NPCDrops.rareDrops.get(npcs[i].npcType).length - 1);
                        if (npcs[i].npcType == 3618 || npcs[i].npcType == 3619) {
                            Server.itemHandler.createGroundItem(c, NPCDrops.rareDrops.get(npcs[i].npcType)[random][0], npcs[i].
                                    absX - 1, npcs[i].absY, NPCDrops.rareDrops.get(npcs[i].npcType)[random][1], c.playerId);
                        } else if (npcs[i].npcType == 795) {
                            Server.itemHandler.createGroundItem(c, NPCDrops.rareDrops.get(npcs[i].npcType)[random][0], 2501, 3731, NPCDrops.rareDrops.get(npcs[i].npcType)[random][1], c.playerId);
                        } else {
                            Server.itemHandler.createGroundItem(c, NPCDrops.rareDrops.get(npcs[i].npcType)[random][0], npcs[i].
                                    absX, npcs[i].absY, NPCDrops.rareDrops.get(npcs[i].npcType)[random][1], c.playerId);
                        }
                        if (c.rareOn != false) {
                            c.getPA().handledropOn(c, NPCDrops.rareDrops.get(npcs[i].npcType)[random][0], NPCDrops.rareDrops.get(npcs[i].npcType)[random][1]);
                            return;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        int random = Misc.random(NPCDrops.normalDrops.get(npcs[i].npcType).length - 1);
                        if (npcs[i].npcType == 3618 || npcs[i].npcType == 3619) {
                            Server.itemHandler.createGroundItem(c, NPCDrops.normalDrops.get(npcs[i].npcType)[random][0], npcs[i].absX - 1, npcs[i].absY, NPCDrops.normalDrops.get(npcs[i].npcType)[random][1], c.playerId);
                        } else if (npcs[i].npcType == 795) {
                            Server.itemHandler.createGroundItem(c, NPCDrops.normalDrops.get(npcs[i].npcType)[random][0], 2501, 3731, NPCDrops.normalDrops.get(npcs[i].npcType)[random][1], c.playerId);
                        } else {
                            Server.itemHandler.createGroundItem(c, NPCDrops.normalDrops.get(npcs[i].npcType)[random][0], npcs[i].absX, npcs[i].absY, NPCDrops.normalDrops.get(npcs[i].npcType)[random][1], c.playerId);
                        }
                        if (c.rareOn != false) {
                            if (NPCDrops.normalDrops.get(npcs[i].npcType)[random][0] == -1) return;
                            c.getPA().handledropOnCommon(c, NPCDrops.normalDrops.get(npcs[i].npcType)[random][0], NPCDrops.normalDrops.get(npcs[i].npcType)[random][1]);
                            return;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

        }
        //System.out.println("Took: " + (System.currentTimeMillis() - start));
    }


    /**
     * Resets players in combat
     */

    public void resetPlayersInCombat(int i) {
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null)
                if (PlayerHandler.players[j].underAttackBy2 == i)
                    PlayerHandler.players[j].underAttackBy2 = 0;
        }
    }

    /**
     * Npc names
     */


    public String getNpcListName(int npcId) {
        for (NPCList i : NpcList) {
            if (i != null) {
                if (i.npcId == npcId) {
                    return i.npcName;
                }
            }
        }
        return "nothing";
    }

    /**
     * Npc Follow Player
     */

    public int GetMove(int Place1, int Place2) {
        if ((Place1 - Place2) == 0) {
            return 0;
        } else if ((Place1 - Place2) < 0) {
            return 1;
        } else if ((Place1 - Place2) > 0) {
            return -1;
        }
        return 0;
    }

    public boolean followPlayer(int i) {

        switch (npcs[i].npcType) {
            case 2892:
            case 2894:
            case 1827:
            case 3618:
            case 3619:
                return false;
            case 1456:
                Client c = (Client) PlayerHandler.players[npcs[i].spawnedBy];
                if (c != null) {
                    if (!c.inRfd())
                        return false;
                }
        }
        return true;
    }

    public boolean recoilPlayer(int i) {
        switch (npcs[i].npcType) {
            case 135:
                return false;
        }
        return true;
    }

    /*public void followPlayer(int i, int playerId) {
		if (PlayerHandler.players[playerId] == null) {
			return;
		}
		if (PlayerHandler.players[playerId].respawnTimer > 0) {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			return;
		}

		if (!followPlayer(i)) {
			npcs[i].facePlayer(playerId);
			return;
		}
		int playerX = PlayerHandler.players[playerId].absX;
		int playerY = PlayerHandler.players[playerId].absY;
		npcs[i].randomWalk = false;
		if((npcs[i].spawnedBy > 0) || ((npcs[i].absX < npcs[i].makeX + Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absX > npcs[i].makeX - Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absY < npcs[i].makeY + Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absY > npcs[i].makeY - Config.NPC_FOLLOW_DISTANCE))) {
		int npcY = npcs[i].absY;
		int npcX = npcs[i].absX;
		if (!goodDistanceNew(npcs[i].getX(), npcs[i].getY(), NPCHandler.npcSizes(i), playerX, playerY, distanceRequired(i))) {
		if((npcs[i].absX < npcs[i].makeX + Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absX > npcs[i].makeX - Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absY < npcs[i].makeY + Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absY > npcs[i].makeY - Config.NPC_FOLLOW_DISTANCE)) {
										if(npcs[i].heightLevel == PlayerHandler.players[playerId].heightLevel) {
										NPCPathFinder.findRoute(npcs[i], npcs[i].getX(), npcs[i].getY());	
										}
									}
								}
		} else {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
		}
	}*/
    public void followPlayer(int i, int playerId) {
        if (PlayerHandler.players[playerId] == null) {
            return;
        }
        if (PlayerHandler.players[playerId].respawnTimer > 0) {
            npcs[i].facePlayer(0);
            npcs[i].randomWalk = true;
            npcs[i].underAttack = false;
            return;
        }

        if (!followPlayer(i)) {
            npcs[i].facePlayer(playerId);
            return;
        }
        int playerX = PlayerHandler.players[playerId].absX;
        int playerY = PlayerHandler.players[playerId].absY;
        npcs[i].randomWalk = false;
        if ((npcs[i].spawnedBy > 0) || ((npcs[i].absX < npcs[i].makeX + Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absX > npcs[i].makeX - Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absY < npcs[i].makeY + Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absY > npcs[i].makeY - Config.NPC_FOLLOW_DISTANCE))) {
            int npcY = npcs[i].absY;
            int npcX = npcs[i].absX;
            if (npcSize(i) > 1) {
                if ((playerX == npcX && playerY > npcY)
                        || (playerX > npcX && playerY == npcY)
                        || (playerX > npcX && playerY == npcY)
                        || (playerX > npcX && playerY > npcY))
                    if (goodDistance(npcX, npcY, playerX, playerY, npcSizes(i)))
                        return;
            } else {
                if (goodDistance(npcX, npcY, playerX, playerY, distanceRequired(i)))
                    return;
            }
            if (npcs[i].heightLevel == PlayerHandler.players[playerId].heightLevel) {
                if (PlayerHandler.players[playerId] != null && npcs[i] != null) {
                    if (playerY < npcs[i].absY) {
                        npcs[i].moveX = GetMove(npcs[i].absX, playerX);
                        npcs[i].moveY = GetMove(npcs[i].absY, playerY);
                    } else if (playerY > npcs[i].absY) {
                        npcs[i].moveX = GetMove(npcs[i].absX, playerX);
                        npcs[i].moveY = GetMove(npcs[i].absY, playerY);
                    } else if (playerX < npcs[i].absX) {
                        npcs[i].moveX = GetMove(npcs[i].absX, playerX);
                        npcs[i].moveY = GetMove(npcs[i].absY, playerY);
                    } else if (playerX > npcs[i].absX) {
                        npcs[i].moveX = GetMove(npcs[i].absX, playerX);
                        npcs[i].moveY = GetMove(npcs[i].absY, playerY);
                    } else if (playerX == npcs[i].absX || playerY == npcs[i].absY) {
                        int o = Misc.random(3);
                        switch (o) {
                            case 0:
                                npcs[i].moveX = GetMove(npcs[i].absX, playerX);
                                npcs[i].moveY = GetMove(npcs[i].absY, playerY + 1);
                                break;

                            case 1:
                                npcs[i].moveX = GetMove(npcs[i].absX, playerX);
                                npcs[i].moveY = GetMove(npcs[i].absY, playerY - 1);
                                break;

                            case 2:
                                npcs[i].moveX = GetMove(npcs[i].absX, playerX + 1);
                                npcs[i].moveY = GetMove(npcs[i].absY, playerY);
                                break;

                            case 3:
                                npcs[i].moveX = GetMove(npcs[i].absX, playerX - 1);
                                npcs[i].moveY = GetMove(npcs[i].absY, playerY);
                                break;
                        }
                    }
                    npcs[i].facePlayer(playerId);
                    handleClipping(i);
                    npcs[i].getNextNPCMovement(i);
                    npcs[i].facePlayer(playerId);
                    npcs[i].updateRequired = true;
                }
            }
        } else {
            npcs[i].facePlayer(0);
            npcs[i].randomWalk = true;
            npcs[i].underAttack = false;
        }
    }

    public static void handleClipping(int i) {
        NPC npc = npcs[i];
        if (npc.moveX == 1 && npc.moveY == 1) {
            if ((Region.getClipping(npc.absX + 1, npc.absY + 1, npc.heightLevel) & 0x12801e0) != 0) {
                npc.moveX = 0;
                npc.moveY = 0;
                if ((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) == 0)
                    npc.moveY = 1;
                else
                    npc.moveX = 1;
            }
        } else if (npc.moveX == -1 && npc.moveY == -1) {
            if ((Region.getClipping(npc.absX - 1, npc.absY - 1, npc.heightLevel) & 0x128010e) != 0) {
                npc.moveX = 0;
                npc.moveY = 0;
                if ((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) == 0)
                    npc.moveY = -1;
                else
                    npc.moveX = -1;
            }
        } else if (npc.moveX == 1 && npc.moveY == -1) {
            if ((Region.getClipping(npc.absX + 1, npc.absY - 1, npc.heightLevel) & 0x1280183) != 0) {
                npc.moveX = 0;
                npc.moveY = 0;
                if ((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) == 0)
                    npc.moveY = -1;
                else
                    npc.moveX = 1;
            }
        } else if (npc.moveX == -1 && npc.moveY == 1) {
            if ((Region.getClipping(npc.absX - 1, npc.absY + 1, npc.heightLevel) & 0x128013) != 0) {
                npc.moveX = 0;
                npc.moveY = 0;
                if ((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) == 0)
                    npc.moveY = 1;
                else
                    npc.moveX = -1;
            }
        } //Checking Diagonal movement.

        if (npc.moveY == -1) {
            if ((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) != 0)
                npc.moveY = 0;
        } else if (npc.moveY == 1) {
            if ((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) != 0)
                npc.moveY = 0;
        } //Checking Y movement.
        if (npc.moveX == 1) {
            if ((Region.getClipping(npc.absX + 1, npc.absY, npc.heightLevel) & 0x1280180) != 0)
                npc.moveX = 0;
        } else if (npc.moveX == -1) {
            if ((Region.getClipping(npc.absX - 1, npc.absY, npc.heightLevel) & 0x1280108) != 0)
                npc.moveX = 0;
        } //Checking X movement.
    }

    /**
     * load spell
     */
    public void loadSpell2(int i) {
        npcs[i].attackType = 3;
        int random = Misc.random(3);
        if (random == 0) {
            npcs[i].projectileId = 393; //red
            npcs[i].endGfx = 430;
        } else if (random == 1) {
            npcs[i].projectileId = 394; //green
            npcs[i].endGfx = 429;
        } else if (random == 2) {
            npcs[i].projectileId = 395; //white
            npcs[i].endGfx = 431;
        } else if (random == 3) {
            npcs[i].projectileId = 396; //blue
            npcs[i].endGfx = 428;
        }
    }

    public int random;
    public int random2;


    public void loadSpell(int i) {
        Client c = (Client) PlayerHandler.players[npcs[i].oldIndex];
        switch (npcs[i].npcType) {

            case 396:
                random = Misc.random(4);
                if (random == 0 || random == 1 || random == 2 || random == 3)
                    npcs[i].attackType = 0;
                else {
                    npcs[i].attackType = 1;
                    npcs[i].forceChat("Grrrhoaaar!");
                    //c.sendMessage("@red@River troll: @dbl@Grrrhoaaar!");
                }
                break;

            case 172:
            case 174:
                npcs[i].attackType = 2;
                npcs[i].gfx100(93);
                npcs[i].projectileId = 94;
                npcs[i].endGfx = 95;
                break;
            case 795:
                npcs[i].attackType = 2;
                npcs[i].projectileId = 396;
                npcs[i].endGfxNew = 364;
                break;
            case 2892:
                npcs[i].projectileId = 94;
                npcs[i].attackType = 2;
                npcs[i].endGfx = 95;
                break;
            case 2894:
                npcs[i].projectileId = 298;
                npcs[i].attackType = 1;
                break;
            case 54:
            case 55:
            case 53:
            case 941:
                int randoms = Misc.random(2);
                if (randoms == 0) {
                    npcs[i].projectileId = 393; //red
                    npcs[i].endGfx = 430;
                    npcs[i].attackType = 3;
                } else if (randoms == 1) {
                    npcs[i].projectileId = -1; //melee
                    npcs[i].endGfx = -1;
                    npcs[i].attackType = 0;
                }
                break;
            case 50:
                int random = Misc.random(4);
                if (random == 0) {
                    npcs[i].projectileId = 393; //red
                    npcs[i].endGfx = 430;
                    npcs[i].attackType = 3;
                } else if (random == 1) {
                    npcs[i].projectileId = 394; //green
                    npcs[i].endGfx = 429;
                    npcs[i].attackType = 3;
                } else if (random == 2) {
                    npcs[i].projectileId = 395; //white
                    npcs[i].endGfx = 431;
                    npcs[i].attackType = 3;
                } else if (random == 3) {
                    npcs[i].projectileId = 396; //blue
                    npcs[i].endGfx = 428;
                    npcs[i].attackType = 3;
                } else if (random == 4) {
                    npcs[i].projectileId = -1; //melee
                    npcs[i].endGfx = -1;
                    npcs[i].attackType = 0;
                }
                break;
            //arma npcs
            case 2561:
                npcs[i].attackType = 0;
                break;
            case 2560:
                npcs[i].attackType = 1;
                npcs[i].projectileId = 1190;
                break;
            case 2559:
                npcs[i].attackType = 2;
                npcs[i].projectileId = 1203;
                break;
            case 2558:
                random = Misc.random(1);
                npcs[i].attackType = 1 + random;
                if (npcs[i].attackType == 1) {
                    npcs[i].projectileId = 1197;
                } else {
                    npcs[i].attackType = 2;
                    npcs[i].projectileId = 1198;
                }
                break;
            //sara npcs
            case 2562: //sara
                random = Misc.random(1);
                if (random == 0) {
                    npcs[i].attackType = 2;
                    npcs[i].endGfx = 1224;
                    npcs[i].projectileId = -1;
                } else if (random == 1)
                    npcs[i].attackType = 0;
                break;
            case 2563: //star
                npcs[i].attackType = 0;
                break;
            case 2564: //growler
                npcs[i].attackType = 2;
                npcs[i].projectileId = 1203;
                break;
            case 2565: //bree
                npcs[i].attackType = 1;
                npcs[i].projectileId = 9;
                break;
            //bandos npcs
            case 2550:
                random = Misc.random(2);
                if (random == 0 || random == 1)
                    npcs[i].attackType = 0;
                else {
                    npcs[i].attackType = 1;
                    npcs[i].endGfx = 1211;
                    npcs[i].projectileId = 288;
                }
                break;
            case 2551:
                npcs[i].attackType = 0;
                break;
            case 2552:
                npcs[i].attackType = 2;
                npcs[i].projectileId = 1203;
                break;
            case 2553:
                npcs[i].attackType = 1;
                npcs[i].projectileId = 1206;
                break;
            case 1643:
                npcs[i].attackType = 2;
                npcs[i].gfx100(129);
                npcs[i].projectileId = 130;
                npcs[i].endGfx = 131;
                break;
            case 94:
            case 2262:
                npcs[i].attackType = 2;
                int w = Misc.random(3);
                if (w == 0) {
                    //npcs[i].gfx100(158);
                    //npcs[i].projectileId = 159;
                    npcs[i].forceChat("Celobra blaka dagshelgr!");
                    npcs[i].endGfx = 391;
                }
                if (w == 1) {
                    //npcs[i].gfx100(161);
                    //npcs[i].projectileId = 162;
                    npcs[i].forceChat("Thverr stenr un atra eka horra!!");
                    npcs[i].endGfx = 383;
                }
                if (w == 2) {
                    //npcs[i].gfx100(164);
                    //npcs[i].projectileId = 165;
                    npcs[i].forceChat("Brakka du vanyali!");
                    npcs[i].endGfx = 377;
                }
                if (w == 3) {
                    //npcs[i].gfx100(155);
                    //npcs[i].projectileId = 156;
                    npcs[i].endGfx = 369;
                    npcs[i].forceChat("Skolir nosu fra brisingr!");
                }
                break;
            case 2025:
                npcs[i].attackType = 2;
                int r = Misc.random(3);
                if (r == 0) {
                    npcs[i].gfx100(158);
                    npcs[i].projectileId = 159;
                    npcs[i].endGfx = 160;
                }
                if (r == 1) {
                    npcs[i].gfx100(161);
                    npcs[i].projectileId = 162;
                    npcs[i].endGfx = 163;
                }
                if (r == 2) {
                    npcs[i].gfx100(164);
                    npcs[i].projectileId = 165;
                    npcs[i].endGfx = 166;
                }
                if (r == 3) {
                    npcs[i].gfx100(155);
                    npcs[i].projectileId = 156;
                }
                break;
            case 1456:
                npcs[i].attackType = 1;
                break;
            case 2881://supreme
                npcs[i].attackType = 1;
                npcs[i].projectileId = 298;
                break;

            case 2882://prime
                npcs[i].attackType = 2;
                npcs[i].projectileId = 162;
                npcs[i].endGfx = 477;
                break;

            case 2028:
                npcs[i].attackType = 1;
                npcs[i].projectileId = 27;
                break;

            case 3200:
                int r2 = Misc.random(1);
                if (r2 == 0) {
                    npcs[i].attackType = 1;
                    npcs[i].gfx100(550);
                    npcs[i].projectileId = 551;
                    npcs[i].endGfx = 552;
                } else {
                    npcs[i].attackType = 2;
                    npcs[i].gfx100(553);
                    npcs[i].projectileId = 554;
                    npcs[i].endGfx = 555;
                }
                break;
            case 2745:
                int r3 = 0;
                if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].spawnedBy].absX, PlayerHandler.players[npcs[i].spawnedBy].absY, 1))
                    r3 = Misc.random(2);
                else
                    r3 = Misc.random(1);
                if (r3 == 0) {
                    npcs[i].attackType = 2;
                    npcs[i].endGfx = 157;
                    npcs[i].projectileId = 448;
                } else if (r3 == 1) {
                    npcs[i].attackType = 1;
                    npcs[i].endGfx = 451;
                    npcs[i].projectileId = -1;
                } else if (r3 == 2) {
                    npcs[i].attackType = 0;
                }
                break;
            case 1472:
                int r4 = 0;
                r4 = Misc.random(3);
                if (r4 == 0) {
                    npcs[i].gfx100(158);
                    npcs[i].projectileId = 159;
                    npcs[i].attackType = 2;
                    npcs[i].endGfx = 160;
                } else if (r4 == 1) {
                    npcs[i].gfx100(161);
                    npcs[i].projectileId = 162;
                    npcs[i].attackType = 2;
                    npcs[i].endGfx = 163;
                } else if (r4 == 2) {
                    npcs[i].gfx100(155);
                    npcs[i].projectileId = 156;
                    npcs[i].attackType = 2;
                    npcs[i].endGfx = 157;
                } else if (r4 == 3) {
                    npcs[i].gfx100(164);
                    npcs[i].projectileId = 165;
                    npcs[i].attackType = 2;
                    npcs[i].endGfx = 166;
                }
                break;
            case 2743:
                npcs[i].attackType = 2;
                npcs[i].projectileId = 445;
                npcs[i].endGfx = 446;
                break;

            case 2631:
                npcs[i].attackType = 1;
                npcs[i].projectileId = 443;
                break;
        }
    }

    /**
     * Distanced required to attack
     */
    public static int distanceRequired(int i) {
        switch (npcs[i].npcType) {
            case 2025:
            case 2028:
            case 94:
            case 2262:
            case 1643:
                return 6;
            case 50:
            case 2562:
                return 2;
            case 172:
            case 174:
            case 1456:
            case 795:
                return 8;
            case 2881://dag kings
            case 2882:
            case 3200://chaos ele
            case 2743:
            case 2631:
            case 2745:
                return 8;
            case 2883://rex
                return 1;
            case 2552:
            case 2553:
            case 2556:
            case 2557:
            case 2558:
            case 2559:
            case 2560:
            case 2564:
            case 2565:
                return 9;
            //things around dags
            case 2892:
            case 2894:
                return 10;
            case 1472:
                return 8;
            default:
                return 1;
        }
    }

    public int followDistance(int i) {
        switch (npcs[i].npcType) {
            case 2550:
            case 2551:
            case 2562:
            case 2563:
                return 8;
            case 2883:
                return 4;
            case 2881:
            case 2882:
                return 1;
            case 1456:
            case 3618:
            case 3619:
            case 1827:
            case 795:
                return 0;
            case 112:
            case 110:
                return 6;
        }
        return 0;


    }

    public int getProjectileSpeed(int i) {
        switch (npcs[i].npcType) {
            case 2881:
            case 2882:
            case 3200:
                return 85;

            case 2745:
                return 130;

            case 50:
                return 90;

            case 2025:
            case 94:
            case 2262:
                return 85;

            case 2028:
                return 80;

            default:
                return 85;
        }
    }

    /**
     * NPC Attacking Player
     */

    public void attackPlayer(Client c, int i) {
        if (npcs[i] != null) {
            for (int k = 0; k < maxNPCs; k++) {
                if (npcs[k] != null) {
                    int npcY = npcs[i].absY;
                    int npcX = npcs[i].absX;
                    int playerX = PlayerHandler.players[c.playerId].absX;
                    int playerY = PlayerHandler.players[c.playerId].absY;
                    if (npcs[i].lastX != npcs[i].getX()
                            || npcs[i].lastY != npcs[i].getY()) {
                        return;
                    }
                    if (npcSize(i) > 1) {
                        if ((playerX == npcs[i].getX() && playerY > npcs[i].getY())
                                || (playerX > npcs[i].getX() && playerY == npcs[i].getY())
                                || (playerX > npcs[i].getX() && playerY == npcs[i].getY())
                                || (playerX > npcs[i].getX() && playerY > npcs[i].getY()))
                            if (!goodDistance(npcs[i].getX(), npcs[i].getY(), playerX, playerY, npcSizes(i)+1))
                                return;
                    } else {
                        if (!goodDistance(npcs[i].getX(), npcs[i].getY(), playerX, playerY, distanceRequired(i)))
                            return;
                    }
                    boolean sameSpot = c.getX() == npcs[i].getX() && c.getY() == npcs[i].getY();
                    if (sameSpot) {
                        stepAway(i);
                        return;
                    }
                    if (npcs[k].npcType == 1459) {
                        int b = Misc.random(5);
                        if (npcs[k].isDead)
                            return;
                        if (b == 1) {
                            if (npcs[k].HP < 45) {
                                npcs[k].HP += 20;
                                npcs[k].animNumber = 1405;
                                npcs[k].attackTimer = 5;
                                npcs[k].animUpdateRequired = true;
                                npcs[k].updateRequired = true;
                            }
                        }
                    }
                    if (npcs[k].npcType == 222) {
                        int r = Misc.random(2);
                        if (npcs[k].isDead)
                            return;
                        if (r == 1) {
                            if (npcs[k].HP < 8) {
                                npcs[k].HP += 4;
                                npcs[k].gfx0(84);
                                npcs[k].animUpdateRequired = true;
                                npcs[k].updateRequired = true;
                            }
                        }
                    }
                }
            }
            if (npcs[i].isDead)
                return;
            if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0 && npcs[i].underAttackBy != c.playerId) {
                if (npcs[i].underAttackBy != 1614) {
                    npcs[i].killerId = 0;
                    return;
                }
                npcs[i].killerId = 0;
                return;
            }
            if (pathBlocked(npcs[i], c) && npcSizes(i) <= 1)
                return;
            if (!npcs[i].inMulti() && (c.underAttackBy > 0 || (c.underAttackBy2 > 0 && c.underAttackBy2 != i))) {
                npcs[i].killerId = 0;
                return;
            }
            if (npcs[i].heightLevel != c.heightLevel) {
                npcs[i].killerId = 0;
                return;
            }
            npcs[i].facePlayer(c.playerId);
            boolean special = false;//specialCase(c,i);
            if (goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), distanceRequired(i)) || special) {
                if (c.respawnTimer <= 0) {
                    npcs[i].facePlayer(c.playerId);
                    npcs[i].attackTimer = getNpcDelay(i);
                    npcs[i].hitDelayTimer = getHitDelay(i);
                    npcs[i].attackType = 0;
                    if (special)
                        loadSpell2(i);
                    else
                        loadSpell(i);
                    if (npcs[i].attackType == 3)
                        npcs[i].hitDelayTimer += 2;
                    if (multiAttacks(i)) {
                        multiAttackGfx(i, npcs[i].projectileId);
                        startAnimation(getAttackEmote(i), i);
                        npcs[i].oldIndex = c.playerId;
                        //c.getPA().sendSound(Server.npcHandler.getNpcListAttackSound(NPCHandler.npcs[i].npcType), 100 , 32);
                        //c.getPA().sendSound(getNpcListAttackSound(npcs[i].npcType), 100, 32);
                        return;
                    }
                    if (npcs[i].projectileId > 0) {
                        int nX = NPCHandler.npcs[i].getX() + offset(i);
                        int nY = NPCHandler.npcs[i].getY() + offset(i);
                        int pX = c.getX();
                        int pY = c.getY();
                        int offX = (nY - pY) * -1;
                        int offY = (nX - pX) * -1;
                        c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i), npcs[i].projectileId, 43, 31, -c.getId() - 1, 65);
                    }
                    c.underAttackBy2 = i;
                    c.singleCombatDelay2 = System.currentTimeMillis();
                    npcs[i].oldIndex = c.playerId;
                    startAnimation(getAttackEmote(i), i);
                    //		c.getPA().sendSound(getNpcListAttackSound(npcs[i].npcType), 100, 32);
                    //c.getPA().sendSound(Server.npcHandler.getNpcListAttackSound(NPCHandler.npcs[i].npcType), 100 , 32);
                    c.getPA().removeAllWindows();
                }
            }
        }
    }


    public int offset(int i) {
        switch (npcs[i].npcType) {
            case 50:
                return 2;
            case 2881:
            case 2882:
                return 1;
            case 2745:
            case 2743:
                return 1;
        }
        return 0;
    }

    public boolean specialCase(Client c, int i) { //responsible for npcs that much
        if (goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), 8) && !goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), distanceRequired(i)))
            return true;
        return false;
    }

    public boolean retaliates(int npcType) {
        return npcType < 3777 || npcType > 3780 && !(npcType >= 2440 && npcType <= 2446);
    }

    public static boolean weakFire(int npcType) {
        if (npcType == 111 || npcType == 64 || npcType == 795)
            return true;
        return false;
    }

    public static boolean weakAFBarrows(int npcType) {
        if (npcType >= 2025 && npcType <= 2030)
            return true;
        return false;
    }

    public void applyDamage(int i) {
        if (npcs[i] != null) {
            if (PlayerHandler.players[npcs[i].oldIndex] == null) {
                return;
            }
            if (npcs[i].isDead)
                return;
            Client c = (Client) PlayerHandler.players[npcs[i].oldIndex];
            if (multiAttacks(i)) {
                multiAttackDamage(i);
                return;
            }
            int damage = 0;
            if (damage >= 1)
                c.getCombat().applyRecoilNPC(damage, i);
            if (c.playerIndex <= 0 && c.npcIndex <= 0)
                if (c.autoRet == 1)
                    c.npcIndex = i;
            if (c.attackTimer <= 3 || c.attackTimer == 0 && c.npcIndex == 0 && c.oldNpcIndex == 0) {
                c.startAnimation(c.getCombat().getBlockEmote(c.getItems().getItemName(c.playerEquipment[c.playerShield]).toLowerCase()));
            }
            if (c.respawnTimer <= 0) {
                //int damage = 0;
                if (npcs[i].attackType == 0) {
                    damage = Misc.random(npcs[i].maxHit);
                    if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc.random(NPCHandler.npcs[i].attack)) {
                        damage = 0;
                    }
                    if (damage > 0)
                        //c.getPA().sendSound(72,100,30);
                        if (c.prayerActive[18]) { // protect from melee
                            if (npcs[i].npcType == 2030)
                                damage = (damage / 2);
                            else
                                damage = 0;
                        }
                    if (c.playerLevel[3] - damage < 0) {
                        damage = c.playerLevel[3];
                    }
                }

                if (npcs[i].attackType == 1) { // range
                    if (damage >= 1)
                        c.getCombat().applyRecoilNPC(damage, i);
                    damage = Misc.random(npcs[i].maxHit);
                    if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > Misc.random(NPCHandler.npcs[i].attack)) {
                        damage = 0;
                    }
                    if (c.prayerActive[17]) { // protect from range
                        damage = 0;
                    }
                    if (c.playerLevel[3] - damage < 0) {
                        damage = c.playerLevel[3];
                    }
                }

                if (npcs[i].attackType == 2) { // magic
                    if (damage >= 1)
                        c.getCombat().applyRecoilNPC(damage, i);
                    damage = Misc.random(npcs[i].maxHit);
                    boolean magicFailed = false;
                    if (10 + Misc.random(c.getCombat().mageDef()) > Misc.random(NPCHandler.npcs[i].attack)) {
                        damage = 0;
                        magicFailed = true;
                    }
                    if (c.prayerActive[16]) { // protect from magic
                        damage = 0;
                        magicFailed = true;
                    }
                    if (c.playerLevel[3] - damage < 0) {
                        damage = c.playerLevel[3];
                    }
                    if (npcs[i].endGfxNew > 0 && (!magicFailed)) {
                        c.gfx0(npcs[i].endGfxNew);
                    } else if (npcs[i].endGfx > 0 && (!magicFailed)) {
                        c.gfx100(npcs[i].endGfx);
                    } else {
                        c.gfx100(85);
                    }
                }

                if (npcs[i].attackType == 3) { //fire breath
                    int anti = c.getPA().antiFire();
                    if (anti == 0) {
                        damage = Misc.random(30) + 10;
                        if (damage >= 1)
                            c.getCombat().applyRecoilNPC(damage, i);
                        c.sendMessage("You are badly burnt by the dragon fire!");
                    } else if (anti == 1)
                        damage = Misc.random(20);
                    if (damage >= 1)
                        c.getCombat().applyRecoilNPC(damage, i);
                    else if (anti == 2)
                        damage = Misc.random(5);
                    if (damage >= 1)
                        c.getCombat().applyRecoilNPC(damage, i);
                    if (c.playerLevel[3] - damage < 0 && npcs[i].endGfx > 0)
                        damage = c.playerLevel[3];
                    if (damage >= 1)
                        c.getCombat().applyRecoilNPC(damage, i);
                    c.gfx100(npcs[i].endGfx);
                }
                handleSpecialEffects(c, i, damage);
                c.logoutDelay = System.currentTimeMillis(); // logout delay
                //c.setHitDiff(damage);
                c.handleHitMask(damage);
                c.playerLevel[3] -= damage;
                FightCaves.tzKihEffect(c, i, damage);
                c.getPA().refreshSkill(3);
                c.updateRequired = true;
                //c.setHitUpdateRequired(true);
            }
        }
    }

    public void handleSpecialEffects(Client c, int i, int damage) {
        if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
            if (damage > 0) {
                if (c != null) {
                    if (c.playerLevel[5] > 0) {
                        c.playerLevel[5]--;
                        c.getPA().refreshSkill(5);
                        c.getPA().appendPoison(12);
                    }
                }
            }
        }

    }


    public static void startAnimation(int animId, int i) {
        npcs[i].animNumber = animId;
        npcs[i].animUpdateRequired = true;
        npcs[i].updateRequired = true;
    }

    public boolean goodDistanceNew(int npcX, int npcY, int npcSize, int playerX, int playerY, int distance) {
        return playerX >= (npcX - distance) && playerX <= (npcX + npcSize + distance) && playerY >= (npcY - distance) && playerY <= (npcY + npcSize + distance);
    }

    public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
        return ((objectX - playerX <= distance && objectX - playerX >= -distance) && (objectY - playerY <= distance && objectY - playerY >= -distance));
    }


    public int getMaxHit(int i) {
        switch (npcs[i].npcType) {
            case 2558:
                if (npcs[i].attackType == 2)
                    return 28;
                else
                    return 68;
            case 2562:
                return 31;
            case 2530:
                return 22;
            case 2550:
                return 36;
            case 2529:
                return 13;
            case 172:
            case 174:
                return 4;
            case 1648:
            case 1995:
                return 3;
            case 1832:
            case 101:
                return 2;
        }
        return 1;
    }


    public boolean loadAutoSpawn(String FileName) {
        String line = "";
        String token = "";
        String token2 = "";
        String token2_2 = "";
        String[] token3 = new String[10];
        boolean EndOfFile = false;
        BufferedReader characterfile = null;
        try {
            characterfile = new BufferedReader(new FileReader("./" + FileName));
        } catch (FileNotFoundException fileex) {
            Misc.println(FileName + ": file not found.");
            return false;
        }
        try {
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            Misc.println(FileName + ": error loading file.");
            return false;
        }
        while (EndOfFile == false && line != null) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot);
                token = token.trim();
                token2 = line.substring(spot + 1);
                token2 = token2.trim();
                token2_2 = token2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token3 = token2_2.split("\t");
                int maxHit = 0;
                int attacklvl = 0;

                if (getNpcListCombat(Integer.parseInt(token3[0])) < 19)
                    attacklvl = (int) (getNpcListCombat(Integer.parseInt(token3[0])) * 6.5);
                else
                    attacklvl = (int) (getNpcListCombat(Integer.parseInt(token3[0])) * 2.5);

                if (getNpcListHP(Integer.parseInt(token3[0])) < 17)
                    maxHit = getNpcListHP(Integer.parseInt(token3[0])) / 3;
                else
                    maxHit = getNpcListHP(Integer.parseInt(token3[0])) / 9;

                if (token.equals("spawn")) {
                    newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]), getNpcListHP(Integer.parseInt(token3[0])),
                            maxHit, (attacklvl), (int) ((getNpcListCombat(Integer.parseInt(token3[0]))) * 1.7));
                }        // attack                                             //defence
            } else {
                if (line.equals("[ENDOFSPAWNLIST]")) {
                    try {
                        characterfile.close();
                    } catch (IOException ioexception) {
                    }
                    return true;
                }
            }
            try {
                line = characterfile.readLine();
            } catch (IOException ioexception1) {
                EndOfFile = true;
            }
        }
        try {
            characterfile.close();
        } catch (IOException ioexception) {
        }
        return false;
    }

    public int getNpcListHP(int npcId) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcList[i] != null) {
                if (NpcList[i].npcId == npcId) {
                    return NpcList[i].npcHealth;
                }
            }
        }
        return 0;
    }

    private int getNpcListAttackSound(int npcId) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcList[i] != null) {
                if (NpcList[i].npcId == npcId) {
                    return NpcList[i].attackSound;
                }
            }
        }

        return 477;
    }

    public int getNpcListBlockSound(int npcId) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcList[i] != null) {
                if (NpcList[i].npcId == npcId) {
                    return NpcList[i].blockSound;
                }
            }
        }
        return 477;
    }

    public int getNpcListDieSound(int npcId) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcList[i] != null) {
                if (NpcList[i].npcId == npcId) {
                    return NpcList[i].dieSound;
                }
            }
        }
        return 70;

    }

    public int getNpcListCombat(int npcId) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcList[i] != null) {
                if (NpcList[i].npcId == npcId) {
                    return NpcList[i].npcCombat;
                }
            }
        }
        return 0;
    }

    public boolean loadNPCSounds(String FileName) {
        String line = "";
        boolean EndOfFile = false;
        BufferedReader file = null;
        try {
            file = new BufferedReader(new FileReader("./" + FileName));
        } catch (FileNotFoundException fileex) {
            return false;
        }
        try {
            line = file.readLine();
        } catch (IOException ioexception) {
            return false;
        }
        while (EndOfFile == false && line != null) {
            line = line.trim();
            try {
                line = file.readLine();
                if (line.equals(".")) {
                    file.close();
                    return true;
                }
                String[] split = line.split("	");
                if (!line.startsWith("//") && !line.startsWith(".")) {
                    //newNPCSound(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
                }
            } catch (IOException ioexception1) {
                EndOfFile = true;
            }
        }
        try {
            file.close();
        } catch (IOException ioexception) {
        }
        return false;
    }

    public void newNPCSound(int npcId, int attack, int block, int die) {
        //System.out.println("New npc sound: "+npcId+", "+attack+", "+block+", "+die);
        int slot = -1;
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcList[i].npcId == npcId) {
                slot = i;
                break;
            }
        }

        if (slot == -1)

            return;        // npc entry not found

        NpcList[slot].attackSound = attack;
        NpcList[slot].blockSound = block;
        NpcList[slot].dieSound = die;
    }

    public boolean loadNPCList(String FileName) {
        String line = "";
        String token = "";
        String token2 = "";
        String token2_2 = "";
        String[] token3 = new String[10];
        boolean EndOfFile = false;
        BufferedReader characterfile = null;
        try {
            characterfile = new BufferedReader(new FileReader("./" + FileName));
        } catch (FileNotFoundException fileex) {
            Misc.println(FileName + ": file not found.");
            return false;
        }
        try {
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            Misc.println(FileName + ": error loading file.");
            try {
                characterfile.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        }
        while (EndOfFile == false && line != null) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot);
                token = token.trim();
                token2 = line.substring(spot + 1);
                token2 = token2.trim();
                token2_2 = token2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token3 = token2_2.split("\t");
                if (token.equals("npc")) {
                    newNPCList(Integer.parseInt(token3[0]), token3[1], Integer.parseInt(token3[2]), Integer.parseInt(token3[3]));
                }
            } else {
                if (line.equals("[ENDOFNPCLIST]")) {
                    try {
                        characterfile.close();
                    } catch (IOException ioexception) {
                    }
                    return true;
                }
            }
            try {
                line = characterfile.readLine();
            } catch (IOException ioexception1) {
                EndOfFile = true;
            }
        }
        try {
            characterfile.close();
        } catch (IOException ioexception) {
        }
        return false;
    }


}
