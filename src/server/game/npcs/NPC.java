package server.game.npcs;

import core.util.Misc;
import core.util.Stream;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.game.players.Mobile;
import server.world.Tile;
import server.world.WalkingCheck;

public class NPC extends Mobile {

    public boolean transformUpdateRequired = false;
    public int transformId;
    public int npcId;
    public int npcType;
    public boolean summoner = false;
    public int summonedBy;
    public int absX, absY;
    public static int lastX, lastY;
    public int heightLevel;
    public int makeX, makeY, maxHit, defence, attack, moveX, moveY, direction, walkingType;
    public int spawnX, spawnY;
    public int viewX, viewY;
    public int hp, maxHP;
    public boolean underAttackByNpc = false;
    public int attackingNpc;
    private Tile currentTile;
    public long singleCombatDelay = 0;

    /**
     * attackType: 0 = melee, 1 = range, 2 = mage
     */
    public int attackType, projectileId, endGfx, endGfxNew, spawnedBy, hitDelayTimer, HP, MaxHP, hitDiff, animNumber, actionTimer, enemyX, enemyY;
    public boolean applyDead, isDead, needRespawn, respawns;
    public boolean walkingHome, underAttack;
    public int freezeTimer, attackTimer, killerId, killedBy, oldIndex, underAttackBy, underAttackBy2, animCycle;
    public long lastDamageTaken;
    public int oldIndexNPC;
    public boolean randomWalk;
    public boolean dirUpdateRequired;
    public boolean animUpdateRequired;
    public boolean hitUpdateRequired;
    public boolean updateRequired;
    public boolean forcedChatRequired;
    public boolean faceToUpdateRequired;
    public int firstAttacker;
    public String forcedText;
    public int transformTime;
    public long lastTransformed;

    public NPC(int _npcId, int _npcType) {
        npcId = _npcId;
        npcType = _npcType;
        direction = -1;
        isDead = false;
        applyDead = false;
        actionTimer = 0;
        randomWalk = true;
    }

    public void faceTo() {
        //this.rawDirection = 32768 + playerId;
        this.dirUpdateRequired = true;
        this.updateRequired = true;
    }

    public void requestTransform(int Id) {
        transformId = Id;
        transformUpdateRequired = true;
        updateRequired = true;
    }

    public void updateNPCMovement(Stream str) {
        if (direction == -1) {

            if (updateRequired) {

                str.writeBits(1, 1);
                str.writeBits(2, 0);
            } else {
                str.writeBits(1, 0);
            }
        } else {

            str.writeBits(1, 1);
            str.writeBits(2, 1);
            str.writeBits(3, Misc.xlateDirectionToClient[direction]);
            if (updateRequired) {
                str.writeBits(1, 1);
            } else {
                str.writeBits(1, 0);
            }
        }
    }

    public void facenpc(int npc) {
        face = npc;
        dirUpdateRequired = true;
        updateRequired = true;
    }

    public void appendTransformUpdate(Stream str) {
        str.writeWordBigEndianA(transformId);
    }

    public void startAnim(final int anim, int cycle) {
        CycleEventHandler.getSingleton().addEvent(null, new CycleEvent() {// animation
            @Override
            public void execute(CycleEventContainer container) {
                animNumber = anim;
                animUpdateRequired = true;
                container.stop();
            }

            @Override
            public void stop() {

            }
        }, cycle);
    }

    /**
     * Text update
     */

    public void forceChat(String text) {
        forcedText = text;
        forcedChatRequired = true;
        updateRequired = true;
    }

    /**
     * @return the npcId
     */
    public int getNpcId() {
        return npcId;
    }

    /**
     * Graphics
     */

    public int mask80var1 = 0;
    public int mask80var2 = 0;
    protected boolean mask80update = false;

    public void appendMask80Update(Stream str) {
        str.writeWord(mask80var1);
        str.writeDWord(mask80var2);
    }

    public void gfx100(int gfx) {
        mask80var1 = gfx;
        mask80var2 = 6553600;
        mask80update = true;
        updateRequired = true;
    }

    public void gfx0(int gfx) {
        mask80var1 = gfx;
        mask80var2 = 65536;
        mask80update = true;
        updateRequired = true;
    }

    public void appendAnimUpdate(Stream str) {
        str.writeWordBigEndian(animNumber);
        str.writeByte(animCycle);
    }

    /**
     * Face
     */

    public int FocusPointX = -1, FocusPointY = -1;
    public int face = 0;

    private void appendSetFocusDestination(Stream str) {
        str.writeWordBigEndian(FocusPointX);
        str.writeWordBigEndian(FocusPointY);
    }

    public void turnNpc(int i, int j) {
        FocusPointX = 2 * i + 1;
        FocusPointY = 2 * j + 1;
        updateRequired = true;

    }

    public void deleteNPC(NPC npc) {
        this.setAbsX(0);
        this.setAbsY(0);
        npc = null;
    }

    public void appendFaceEntity(Stream str) {
        str.writeWord(face);
    }

    public void facePlayer(int player) {
        face = player + 32768;
        dirUpdateRequired = true;
        updateRequired = true;
    }

    public void appendFaceToUpdate(Stream str) {
        str.writeWordBigEndian(viewX);
        str.writeWordBigEndian(viewY);
    }


    public void appendNPCUpdateBlock(Stream str) {
        if (!updateRequired) return;
        int updateMask = 0;
        if (animUpdateRequired) updateMask |= 0x10;
        if (hitUpdateRequired2) updateMask |= 8;
        if (mask80update) updateMask |= 0x80;
        if (dirUpdateRequired) updateMask |= 0x20;
        if (forcedChatRequired) updateMask |= 1;
        if (hitUpdateRequired) updateMask |= 0x40;
        if (transformUpdateRequired) updateMask |= 2;
        if (FocusPointX != -1) updateMask |= 4;

        str.writeByte(updateMask);

        if (animUpdateRequired) appendAnimUpdate(str);
        if (hitUpdateRequired2) appendHitUpdate2(str);
        if (mask80update) appendMask80Update(str);
        if (dirUpdateRequired) appendFaceEntity(str);
        if (forcedChatRequired) {
            str.writeString(forcedText);
        }
        if (hitUpdateRequired) appendHitUpdate(str);
        if (transformUpdateRequired) appendTransformUpdate(str);
        if (FocusPointX != -1) appendSetFocusDestination(str);

    }

    public void clearUpdateFlags() {
        updateRequired = false;
        forcedChatRequired = false;
        hitUpdateRequired = false;
        hitUpdateRequired2 = false;
        animUpdateRequired = false;
        dirUpdateRequired = false;
        transformUpdateRequired = false;
        mask80update = false;
        forcedText = null;
        moveX = 0;
        moveY = 0;
        direction = -1;
        FocusPointX = -1;
        FocusPointY = -1;
    }

    public int getNextWalkingDirection() {
        currentTile = new Tile(absX + moveX, absY + moveY, heightLevel);
        int dir1;
        dir1 = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
        if (npcType == 146) {
            absX += moveX;
            absY += moveY;
            return dir1;
        }
        if (!WalkingCheck.tiles.containsKey(currentTile.getTileHeight() << 28 | currentTile.getTileX() << 14 | currentTile.getTileY())) {
            int dir;
            dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
            if (dir == -1)
                return -1;
            dir >>= 1;
            absX += moveX;
            absY += moveY;
            return dir;
        } else if (WalkingCheck.tiles.get(currentTile.getTileHeight() << 28 | currentTile.getTileX() << 14 | currentTile.getTileY()) == true) {
            return -1;
        } else {
            return -1;
        }
    }

    public void getNextNPCMovement(int i) {
        direction = -1;
        if (NPCHandler.npcs[i].freezeTimer == 0) {
            direction = getNextWalkingDirection();
        }
    }


    public void appendHitUpdate(Stream str) {

        if (HP <= 0) {
            isDead = true;

        }
        str.writeByteC(hitDiff);
        if (hitDiff > 0) {
            str.writeByteS(1);
        } else {
            str.writeByteS(0);
        }
        str.writeByteS(HP);
        str.writeByteC(MaxHP);
    }

    public int hitDiff2 = 0;
    public boolean hitUpdateRequired2 = false;

    public void appendHitUpdate2(Stream str) {
        if (HP <= 0) {
            isDead = true;
        }
        str.writeByteA(hitDiff2);
        if (hitDiff2 > 0) {
            str.writeByteC(1);
        } else {
            str.writeByteC(0);
        }
        str.writeByteA(HP);
        str.writeByte(MaxHP);
    }

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

    public int getX() {
        return absX;
    }

    public int getY() {
        return absY;
    }

    public int getAbsX() {
        return absX;
    }

    public int getAbsY() {
        return absY;
    }

    public int getLastX() {
        return lastX;
    }

    public int getLastY() {
        return lastY;
    }

    public void setAbsY(int absY) {
        NPC.lastY = this.absY;
        this.absY = absY;
    }

    public void setAbsX(int absX) {
        NPC.lastX = this.absX;
        this.absX = absX;
    }

	/*	public NPCDefinition getDefinition() {
        return definition;
	}*/


    public boolean inMulti() {
        if (InAlK())
            return true;
        if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607) ||
                (absX >= 3120 && absX <= 3132 && absY >= 9857 && absY <= 9868) ||
                (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839) ||
                (absX >= 2858 && absX <= 2862 && absY >= 3270 && absY <= 3275) ||
                (absX >= 2813 && absX <= 2826 && absY >= 3285 && absY <= 3299) ||
                (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967) ||
                (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967) ||
                (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831) ||
                (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903) ||
                (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711) ||
                (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647) ||
                (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619) ||
                (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117) ||
                (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630) ||
                (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464) ||
                (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711) ||
                (absX >= 3281 && absX <= 3305 && absY >= 3179 && absY <= 3157)) {
            return true;
        }
        return false;
    }

    public boolean InAlK() {
        if (getX() / 64 == 51 && getY() / 64 == 49)
            return true;
        return false;
    }

    public boolean inWild() {
        if (absX >= 2757 && absX <= 2990 && absY >= 2871 && absY <= 2940) {
            return true;
        }
        return false;
    }
}
