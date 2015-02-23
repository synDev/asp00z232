package server.game.players.packets;

import server.Server;
import server.game.items.GlobalDropsHandler;
import server.game.players.Client;
import server.game.players.PacketType;
import server.game.players.PlayerSave;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {

    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        Server.itemHandler.reloadItems(c);
        Server.objectHandler.updateObjects(c);
        GlobalDropsHandler.load(c);
        Server.objectManager.loadObjects(c);
        //Server.globalobjects.loadglobalObjects(c);
        PlayerSave.saveGame(c);// temp

        if (c.skullTimer > 0) {
            c.isSkulled = true;
            c.headIconPk = 0;
            c.getPA().requestUpdates();
        }

    }

}
