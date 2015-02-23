package server.game.players;

/**
 * Request Help
 *
 * @author I'm A Jerk
 */

public class RequestHelp {

    public static boolean requestingHelp = false;
    public static String otherPlayer = "";

    public static void sendOnlineStaff(Client c) {
        String[][] Staff_Config = {{"Str very low"}, {"28000"}, {"28001"}};
        for (int i = 0; i < Staff_Config.length; i++) {
            c.getPA().sendFrame126(Staff_Config[i][0], Integer.parseInt(Staff_Config[i][1]));
            if (PlayerHandler.isPlayerOn(Staff_Config[i][0])) {
                c.getPA().sendFrame126("@gre@Online", Integer.parseInt(Staff_Config[i][2]));
            }
        }
    }

    public static void setInterface(Client c) {
        if (!requestingHelp) {
            sendOnlineStaff(c);
            c.setSidebarInterface(3, 24999);
            c.getPA().sendFrame106(3);
        } else if (requestingHelp) {
            c.setSidebarInterface(3, 3213);
            c.getPA().sendFrame106(3);
            requestingHelp = false;
        }
    }

    public static void callForHelp(Client c) {
        if (System.currentTimeMillis() - c.lastRequest < 30000) {
            c.sendMessage("It has only been " + getTimeLeft(c) + " seconds since your last request for help!");
            c.sendMessage("Please only request help from the staff every 30 seconds!");
            if (!requestingHelp) {
                c.setSidebarInterface(3, 3213);
                c.getPA().sendFrame106(3);
            }
            return;
        }
        requestingHelp = true;
        otherPlayer = c.playerName;
        c.lastRequest = System.currentTimeMillis();
        //setInterface(c);
        PlayerHandler.messageAllStaff("@blu@                                                              !Help!", true);
        PlayerHandler.messageAllStaff("@blu@!Help!" + c.playerName + " is requesting assistance! Type ::teletohelp to assist them. !Help!", true);
        PlayerHandler.messageAllStaff("@blu@                                                              !Help!", true);
    }

    public static long getTimeLeft(Client c) {
        return (System.currentTimeMillis() - c.lastRequest) / 1000;
    }

    public static Client getPlayer() {
        return PlayerHandler.getPlayerByName(otherPlayer);
    }

    public static String playerCoords() {
        return getPlayer().getX() + ", " + getPlayer().getY() + ", " + getPlayer().heightLevel;
    }

    public static void teleportToPlayer(Client c) {
        try {
            if (otherPlayer.equalsIgnoreCase(c.playerName)) {
                c.sendMessage("You can't teleport to yourself!");
                return;
            }
            if (otherPlayer != null && !otherPlayer.equalsIgnoreCase("")) {
                c.getPA().movePlayer(getPlayer().getX(), getPlayer().getY(), getPlayer().heightLevel);
                c.sendMessage("You teleported to " + otherPlayer + ".");
                otherPlayer = "";
            } else {
                c.sendMessage("There is no player to currently teleport to!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}