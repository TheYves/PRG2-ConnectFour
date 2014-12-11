package ch.hslu.prg2.hs14.team7.player;

import ch.hslu.prg2.hs14.team7.GameBoard;
import ch.hslu.prg2.hs14.team7.NoColorException;
import ch.hslu.prg2.hs14.team7.TokenColor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christoph
 */
public abstract class Player
{
    protected String nickname = "Player";
    private TokenColor tokenColor;
    protected List<IPlayerListener> listeners = new ArrayList<IPlayerListener>();

    public Player(String nickname, TokenColor tokenColor){
        if (tokenColor == TokenColor.None)
            throw new NoColorException();
        this.nickname = nickname;
        this.tokenColor = tokenColor;
    }

    public String getNickname(){
        return nickname;
    }

    public abstract void makeMove(GameBoard gameBoard);

    protected void moveMade(GameBoard gameBoard) {
		for (IPlayerListener listener : listeners) {
			listener.moveMade(gameBoard);
		}
	}

    public void addPlayerListener(IPlayerListener listener) {
        listeners.add(listener);
    }

    public TokenColor getTokenColor() {
        return tokenColor;
    }

    public TokenColor getEnemyColor(){
        if (tokenColor == TokenColor.Red)
            return TokenColor.Yellow;
        else if (tokenColor == TokenColor.Yellow)
            return TokenColor.Red;
        else
            return TokenColor.None;
    }
    
    public List<IPlayerListener> getListeners(){
        return this.listeners;
    }
}
