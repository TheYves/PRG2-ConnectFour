/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gruppe7.ConnectFour;

/**
 *
 * @author Christoph
 */
public class GameBoard {
    private Token[][] board;
    private Player thisPlayer;
    private Player enemyPlayer;
    
    private static final int defaultSizeX = 7;
    private static final int defaultSizeY = 6;
    
    public GameBoard(Player thisPlayer, Player enemyPlayer)
    {
        this(thisPlayer, enemyPlayer, defaultSizeX, defaultSizeY);
    }
    
    public GameBoard(Player thisPlayer, Player enemyPlayer, int x, int y)
    {
        this.board = new Token[x][y];
    }
    
    // ----- helper method: check if there are X in a Row --------
    public boolean checkXInARow(int col, int row, int x, Player checkPlayer)
    {
        if (checkVertically(col, row, x, checkPlayer)
            || checkHorizontally(col, row, x, checkPlayer)
            || checkDiagonally1(col, row, x, checkPlayer)
            || checkDiagonally2(col, row, x, checkPlayer))
        {
            return true;
        }
        
        return false;
    }

  //------checking nrOfTokens in  a row
  private boolean checkDiagonally1(int col, int row, int nrOfTokens, Player checkPlayer)
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
  
  private boolean checkDiagonally2(int col, int row, int nrOfTokens, Player checkPlayer)
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

    private boolean checkHorizontally(int col, int row, int nrOfTokens, Player checkPlayer)
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

    private boolean checkVertically(int col, int row, int nrOfTokens, Player checkPlayer)
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
