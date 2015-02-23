package server.game.players;

import server.Config;
import server.game.players.packets.*;

public class PacketHandler {

    private static PacketType packetId[] = new PacketType[256];

    static {

        SilentPacket u = new SilentPacket();
        packetId[3] = u;
        packetId[202] = u;
        packetId[77] = u;
        packetId[86] = u;
        packetId[78] = u;
        packetId[36] = u;
        packetId[226] = u;
        packetId[246] = u;
        packetId[148] = u;
        packetId[183] = u;
        packetId[230] = u;
        packetId[136] = u;
        packetId[189] = u;
        packetId[152] = u;
        packetId[200] = u;
        packetId[85] = u;
        packetId[165] = u;
        packetId[238] = u;
        packetId[150] = u;
        PacketHandler.packetId[253] = new ItemClick2OnGroundItem();
        packetId[120] = new ClickTab();
        packetId[40] = new Dialogue();
        ClickObject co = new ClickObject();
        packetId[132] = co;
        packetId[252] = co;
        packetId[70] = co;
        packetId[57] = new ItemOnNpc();
        packetId[14] = new ItemOnPlayer();
        ClickNPC cn = new ClickNPC();
        packetId[18] = cn;
        packetId[72] = cn;
        packetId[131] = cn;
        packetId[155] = cn;
        packetId[17] = cn;
        packetId[21] = cn;
        packetId[16] = new ItemClick2();
        packetId[75] = new ItemClick3();
        packetId[122] = new ClickItem();
        packetId[241] = new ClickingInGame();
        packetId[4] = new Chat();
        packetId[236] = new PickupItem();
        packetId[87] = new DropItem();
        packetId[185] = new ClickingButtons();
        packetId[130] = new ClickingStuff();
        packetId[103] = new Commands();
        packetId[214] = new MoveItems();
        packetId[237] = new MagicOnItems();
        packetId[181] = new MagicOnFloorItems();
        packetId[202] = new IdleLogout();
        AttackPlayer ap = new AttackPlayer();
        packetId[73] = ap;
        packetId[249] = ap;
        packetId[128] = new ChallengePlayer();
        packetId[39] = new FollowPlayer();
        packetId[139] = new Trade();
        packetId[41] = new WearItem();
        packetId[145] = new RemoveItem();
        packetId[117] = new Bank5();
        packetId[43] = new Bank10();
        packetId[129] = new BankAll();
        packetId[101] = new ChangeAppearance();
        PrivateMessaging pm = new PrivateMessaging();
        packetId[188] = pm;
        packetId[126] = pm;
        packetId[215] = pm;
        packetId[59] = pm;
        packetId[95] = pm;
        packetId[133] = pm;
        packetId[135] = new BankX1();
        packetId[208] = new BankX2();
        packetId[218] = new Report();
        Walking w = new Walking();
        packetId[98] = w;
        packetId[164] = w;
        packetId[248] = w;
        packetId[53] = new ItemOnItem();
        packetId[192] = new ItemOnObject();
        packetId[25] = new ItemOnGroundItem();
        ChangeRegions cr = new ChangeRegions();
        packetId[121] = u;// silent
        packetId[210] = cr;
    }


    public static void processPacket(Client c, int packetType, int packetSize) {
        PacketType p = packetId[packetType];
        if (p != null && packetType > 0 && packetType < 257 && packetType == c.packetType && packetSize == c.packetSize) {
            if (Config.sendServerPackets && c.playerRights == 3) {
                c.sendMessage("PacketType: " + packetType + ". PacketSize: " + packetSize + ".");
            }
            try {
                p.processPacket(c, packetType, packetSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            c.disconnected = true;
            System.out.println(c.playerName + "is sending invalid PacketType: " + packetType + ". PacketSize: " + packetSize);
        }
    }


}
