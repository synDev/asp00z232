package server.game.npcs;

import server.clip.region.Region;

/**
 * @author Drenkai
 *         Used to make sure NPC's can walk on a tile.
 */

public class NPCPathFinder {

    public static boolean samePosition(NPC npc, int gotoX, int gotoY) {
        return (npc.absX == gotoX && npc.absY == gotoY);
    }

    public static boolean goNorth(NPC npc, int gotoX, int gotoY) {
        return (npc.absY < gotoY);
    }

    public static boolean goSouth(NPC npc, int gotoX, int gotoY) {
        return (npc.absY > gotoY);
    }

    public static boolean goEast(NPC npc, int gotoX, int gotoY) {
        return (npc.absX < gotoX);
    }

    public static boolean goWest(NPC npc, int gotoX, int gotoY) {
        return (npc.absX > gotoX);
    }

    public static void findRoute(NPC npc, int gotoX, int gotoY) {
        if (npc.freezeTimer > 0) {
            return;
        }
        npc.randomWalk = false;
        if (!Region.blockedNorthEastNPC(npc.absX, npc.absY, npc.heightLevel, NPCSize.getNPCSize(npc.npcId)) && (samePosition(npc, gotoX, gotoY) || goNorth(npc, gotoX, gotoY) && goEast(npc, gotoX, gotoY))) {
            npc.moveX = 1;
            npc.moveY = 1;
        } else if (!Region.blockedNorthWestNPC(npc.absX, npc.absY, npc.heightLevel, NPCSize.getNPCSize(npc.npcId)) && (samePosition(npc, gotoX, gotoY) || goNorth(npc, gotoX, gotoY) && goWest(npc, gotoX, gotoY))) {
            npc.moveX = -1;
            npc.moveY = 1;
        } else if (!Region.blockedSouthEastNPC(npc.absX, npc.absY, npc.heightLevel, NPCSize.getNPCSize(npc.npcId)) && (samePosition(npc, gotoX, gotoY) || goSouth(npc, gotoX, gotoY) && goEast(npc, gotoX, gotoY))) {
            npc.moveX = 1;
            npc.moveY = -1;
        } else if (!Region.blockedSouthWestNPC(npc.absX, npc.absY, npc.heightLevel, NPCSize.getNPCSize(npc.npcId)) && (samePosition(npc, gotoX, gotoY) || goSouth(npc, gotoX, gotoY) && goWest(npc, gotoX, gotoY))) {
            npc.moveX = -1;
            npc.moveY = -1;
        } else if (!Region.blockedNorthNPC(npc.absX, npc.absY, npc.heightLevel, NPCSize.getNPCSize(npc.npcId)) && (samePosition(npc, gotoX, gotoY) || goNorth(npc, gotoX, gotoY))) {
            npc.moveX = 0;
            npc.moveY = 1;
        } else if (!Region.blockedEastNPC(npc.absX, npc.absY, npc.heightLevel, NPCSize.getNPCSize(npc.npcId)) && (samePosition(npc, gotoX, gotoY) || goEast(npc, gotoX, gotoY))) {
            npc.moveX = 1;
            npc.moveY = 0;
        } else if (!Region.blockedSouthNPC(npc.absX, npc.absY, npc.heightLevel, NPCSize.getNPCSize(npc.npcId)) && (samePosition(npc, gotoX, gotoY) || goSouth(npc, gotoX, gotoY))) {
            npc.moveX = 0;
            npc.moveY = -1;
        } else if (!Region.blockedWestNPC(npc.absX, npc.absY, npc.heightLevel, NPCSize.getNPCSize(npc.npcId)) && (samePosition(npc, gotoX, gotoY) || goWest(npc, gotoX, gotoY))) {
            npc.moveX = -1;
            npc.moveY = 0;
        } else {
            npc.moveX = 0;
            npc.moveY = 0;
        }
        npc.getNextNPCMovement(npc.npcId);
    }
}