package ch.hslu.prg2.hs14.team7.player;

/**
 * Created by Yves Hohl (yves.hohl@stud.hslu.ch) on 05.12.2014.
 */
public interface ILanPlayerListener extends IPlayerListener {

	public void isReady();
	public void connectionLost();

}
