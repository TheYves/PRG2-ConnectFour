package ch.hslu.prg2.hs14.team7.player;

import ch.hslu.prg2.hs14.team7.GameBoard;
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
    private List<IPlayerListener> listeners = new ArrayList<>();
    private GameBoard gameBoard;

    public Player(String nickname, TokenColor tokenColor){
        this.nickname = nickname;
        this.tokenColor = tokenColor;
    }

    public void makeMove(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void chooseColumn(int col) {
        if(gameBoard.insertToken(col, getTokenColor())) {
            moveMade(gameBoard);
        }
    }

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
