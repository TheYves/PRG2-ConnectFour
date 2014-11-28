/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Christoph
 */
public abstract class Player 
{
    protected int thisPlayer; //initialized @ constructor
    protected int enemyPlayer;
    public static Token[][] board = new Token[7][6]; //first x, then y coordinate
    protected GameBoard gb;
    
    public abstract int getColumn();

    public Player(GameBoard gb)
    {
        this.gb = gb;
    }
    
    // ----- helper method: check if there are X in a Row --------
    protected boolean checkXInARow(int col, int row, int x, Player checkPlayer, Token[][] board)
    {
        if (checkVertically(col, row, x, checkPlayer, board)
            || checkHorizontally(col, row, x, checkPlayer, board)
            || checkDiagonally1(col, row, x, checkPlayer, board)
            || checkDiagonally2(col, row, x, checkPlayer, board))
        {
            return true;
        }
        
        return false;
    }

  //------checking nrOfTokens in  a row
  private boolean checkDiagonally1(int col, int row, int nrOfTokens, Player checkPlayer, Token[][] board)
  {
    for (int j = 0; j < nrOfTokens; j++)
    {
        int adjacentSameTokens = 1;
        for (int i = 0; i < nrOfTokens; i++)
        {
            if ((col + i - j) >= 0 && (col + i - j) < board.length
                && (row + i - j) >= 0
                && (row + i - j) < board[col].length
                && board[col + i - j][row + i - j].getPlayer() == checkPlayer)
            {
                adjacentSameTokens++;
            }
        }
        if (adjacentSameTokens == nrOfTokens)
            return true;
    }
    return false;
  }
  
  private boolean checkDiagonally2(int col, int row, int nrOfTokens, Player checkPlayer, Token[][] board)
  {
    for (int j = 0; j < nrOfTokens; j++)
    {
        int adjacentSameTokens = 1;
        for (int i = 0; i < nrOfTokens; i++)
        {
            if ((col - i + j) >= 0 && (col - i + j) < board.length
                && (row + i - j) >= 0
                && (row + i - j) < board[col].length
                && board[col - i + j][row + i - j].getPlayer() == checkPlayer)
            {
                adjacentSameTokens++;
            }
        }
        if (adjacentSameTokens == nrOfTokens)
            return true;
    }
    return false;
  }

    private boolean checkHorizontally(int col, int row, int nrOfTokens, Player checkPlayer, Token[][] board)
    {
        for (int j = 0; j < nrOfTokens; j++)
        {
            int adjacentSameTokens = 1;
            for (int i = 0; i < nrOfTokens; i++)
            {
                if ((col + i - j) >= 0 && (col + i - j) < board.length
                    && board[col + i - j][row].getPlayer() == checkPlayer)
                {
                    adjacentSameTokens++;
                }
            }
            if (adjacentSameTokens == nrOfTokens)
                return true;
        }
        return false;
    }

    private boolean checkVertically(int col, int row, int nrOfTokens, Player checkPlayer, Token[][] board)
    {
        for (int j = 0; j < nrOfTokens; j++)
        {
            int adjacentSameTokens = 1;
            for (int i = 0; i < nrOfTokens; i++)
            {
                if ((row + i - j) >= 0 && (row + i - j) < board[col].length
                    && board[col][row + i - j].getPlayer() == checkPlayer)
                {
                    adjacentSameTokens++;
                }
            }
            if (adjacentSameTokens == nrOfTokens)
              return true;
        }
        return false;
    }

    protected boolean isBoardEmpty()
    {
        for (int i = 0; i < board.length; i++)
        {
            if (board[i][0].getPlayer() != null)
                return false;
        }
        return true;
    }
}
