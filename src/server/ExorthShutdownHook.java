package server;

import server.game.players.Client;
import server.game.players.Player;
import server.game.players.PlayerHandler;
import server.game.players.PlayerSave;


/**
 * Exorth shutdown hook.
 *
 * @author Helium
 */
public class ExorthShutdownHook extends Thread {
    /**
     * The singleton.
     */
    private static final ExorthShutdownHook SINGLETON = new ExorthShutdownHook();

    /**
     * Constructs a new {@code ExorthShutdownHook} {@code Object}.
     */
    private ExorthShutdownHook() {
        System.out.println("Shutdown hook initialized!");
    }

    /**
     * If the shutdown hook is/has running/runned.
     */
    public boolean activated = false;

    @Override
    public void run() {
        activated = true;
        System.out.println("Shutting down Exorth...");
        int failCount = 0;
        System.out.println("Preparing players for shutdown...");
		 for(Player p : PlayerHandler.players) {
            if (p == null)
                continue;
			for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];	
            PlayerSave.saveGame(c);
			}}
            try {
                failCount = 0;  
               //while (!Player p : PlayerHandler.players) {
                    if (failCount++ > 2) {
                        //Your failed saved file here.
                        break;
                    }
            } catch (Throwable t) {
                t.printStackTrace();
            }
			}
    }

    /**
     * @return the singleton
     */
    public static ExorthShutdownHook getSingleton() {
        return SINGLETON;
    }
}