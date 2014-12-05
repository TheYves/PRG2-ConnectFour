package ch.hslu.prg2.hs14.team7.player;

import ch.hslu.prg2.hs14.team7.TokenColor;

/**
 * Created by Yves Hohl (yves.hohl@stud.hslu.ch) on 05.12.2014.
 */
public class HostLanPlayer extends LanPlayer {

	public HostLanPlayer(int port, TokenColor tokenColor) {
		super(tokenColor);

		// auf client verbindung warten und isReady aufrufen
		// super.isReady();
		// oder falls es nicht geklappt hat:
		// super.connectionLost();
	}

}
