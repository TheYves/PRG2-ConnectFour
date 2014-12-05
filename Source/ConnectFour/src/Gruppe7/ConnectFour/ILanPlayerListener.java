package Gruppe7.ConnectFour;

/**
 * Created by Yves Hohl (yves.hohl@stud.hslu.ch) on 05.12.2014.
 */
public interface ILanPlayerListener extends IPlayerListener {

	public void isReady();
	public void connectionLost();

}
