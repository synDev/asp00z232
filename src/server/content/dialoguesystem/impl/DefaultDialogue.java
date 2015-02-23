package server.content.dialoguesystem.impl;

import server.content.dialoguesystem.DialogueSystem;
import server.game.players.Client;

/**
 * @author somedude Default dialogue for every npc
 * 
 */
public class DefaultDialogue extends DialogueSystem {

	public static void handle(Client c, int dialogueId) {
		switch (dialogueId) {
		case 1:
			sendNpcChat1(c, "What a nice day!",  DEFAULT);
			resetChatDialogue(c);
			break;	
		}
	}
}
