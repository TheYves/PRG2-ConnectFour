package Gruppe7.ConnectFour;

/**
 *
 * @author Christoph
 */
public abstract class Player
{
    String nickname = "Player";
    
    private GameBoard gameBoard;
    
    public String getNickname(){
        return nickname;
    }

    /**
     * @return the gameBoard
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * @param gameBoard the gameBoard to set
     */
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
    
    public enum ComputerLevel { Low, Medium, High };
    
    public abstract int makeMove(int previousEnemyColumn);
}
