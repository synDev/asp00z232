package server.game.npcs;

import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.game.players.Client;

public class NPCTransform {

    public static void TransformNPC(final Client c, final int i, final int newId, int anim, final String forceChat, final String msg, final int addItem, final int timer, final int timer2) {
        if (c == null || i < 0 || NPCHandler.npcs[i].lastTransformed < 0)
            return;
        NPCHandler.npcs[i].transformTime = timer2 * 1000;
        int npcId = NPCHandler.npcs[i].npcId;
        final int x = NPCHandler.npcs[i].getX(), y = NPCHandler.npcs[i].getY();

        if (anim != -1)
            c.startAnimation(anim);

        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (addItem != -1)
                    c.getItems().addItem(addItem, 1);
                if (forceChat.length() > 1)
                    NPCHandler.npcs[i].forceChat(forceChat);
                if (msg.length() > 1)
                    c.sendMessage(msg);
                NPCHandler.npcs[i].requestTransform(newId);

                container.stop();
            }

            @Override
            public void stop() {
                NPCHandler.npcs[i].lastTransformed = System.currentTimeMillis();
            }
        }, timer);
    }
}