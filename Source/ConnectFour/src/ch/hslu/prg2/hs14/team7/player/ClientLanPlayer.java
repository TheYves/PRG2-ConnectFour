package ch.hslu.prg2.hs14.team7.player;

import ch.hslu.prg2.hs14.team7.TokenColor;

/**
 * Created by Yves Hohl (yves.hohl@stud.hslu.ch) on 05.12.2014.
 */
public class ClientLanPlayer extends LanPlayer {

	public ClientLanPlayer(int port, String ip, TokenColor tokenColor) {
		super(tokenColor);

		// verbindung zu host aufbauen und isReady aufrufen
		// super.isReady();
		// oder falls es nicht geklappt hat:
		// super.connectionLost();
	}

}
