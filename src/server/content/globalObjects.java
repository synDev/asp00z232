package server.content;

import server.game.players.Client;
import server.game.players.PlayerHandler;

/**
 * @author `Brady
 */


public class globalObjects {


    public void loadglobalObjects(Client c) {
        if (c == null)
            return;
        loadCustomObjects(c);
    }

    public void loadCustomObjects(Client client) {
        //spawnObject(11405, 2862, 2962, 3, 10, 1); // Works (X, Y ,ID, Face, Type , H)
        //spawnObject(0, 0, 1, 0, 10, 1);
    }


    public void removeObjects() {
        objectToRemove(0, 0, 0); // Works (X, Y, H)
        objectToRemove2(0, 0, 0);
    }

    public void objectToRemove(final int X, final int Y, final int H) {
        spawnObject(-1, X, Y, 10, 10, H);
    }


    public void objectToRemove2(final int X, final int Y, final int H) {
        spawnObject(-1, X, Y, -1, 0, H);
    }


    public void spawnObject(final int objectId, final int objectX,
                            final int objectY, final int face, final int objectType, final int height) {
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Client c = (Client) PlayerHandler.players[j];
                if (c.distanceToPoint(objectX, objectY) > 60) {
                    return;
                }
                synchronized (c) {
                    if (c.getOutStream() != null && c != null) {
                        c.getOutStream().createFrame(85);
                        c.getOutStream().writeByteC(objectY - c.getMapRegionY() * 8);
                        c.getOutStream().writeByteC(objectX - c.getMapRegionX() * 8);
                        c.getOutStream().createFrame(101);
                        c.getOutStream().writeByteC((objectType << 2) + (face & 3));
                        c.getOutStream().writeByte(height);


                        if (objectId != -1) {
                            c.getOutStream().createFrame(151);
                            c.getOutStream().writeByteS(height);
                            c.getOutStream().writeWordBigEndian(objectId);
                            c.getOutStream().writeByteS((objectType << 2) + (face & 3));
                        }
                        c.flushOutStream();
                    }
                }
                //Region.addObject(objectId, objectX, objectY, objectType, face, height);
            }
        }
    }
}
