package Gruppe7.ConnectFour;

/**
 *
 * @author Christoph
 */
public abstract class Player
{
    protected String nickname = "Player";
    private TokenColor tokenColor;
    
    private GameBoard gameBoard;
    
    public Player(String nickname, TokenColor tokenColor){
        if (tokenColor == TokenColor.None)
            throw new NoColorException();
        this.nickname = nickname;
        this.tokenColor = tokenColor;
    }
    
    public String getNickname(){
        return nickname;
    }
    
    public abstract int makeMove(GameBoard gameBoard);

    /**
     * @return the tokenOwner
     */
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
}
