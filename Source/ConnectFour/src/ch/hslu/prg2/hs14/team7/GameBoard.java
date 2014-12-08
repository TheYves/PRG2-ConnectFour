/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7;

/**
 *
 * @author Christoph
 */
public class GameBoard {
    private Token[][] board;
    
    private static final int defaultSizeX = 7;
    private static final int defaultSizeY = 6;
    
    public Token[][] getBoard(){
        return board;
    }
    
    public GameBoard()
    {
        this(defaultSizeX, defaultSizeY);
    }
    
    public GameBoard(int x, int y)
    {
        this.board = new Token[x][y];
        
        for (Token[] column : board){
            for (int columnIndex = 0; columnIndex < y; columnIndex++){
                column[columnIndex] = new Token(TokenColor.None);
            }
        }
    }
    
    // ----- helper method: check if there are X in a Row --------
    public boolean checkXInARow(int col, int row, int x, TokenColor tokenColor)
    {
        if (checkVertically(col, row, x, tokenColor)
            || checkHorizontally(col, row, x, tokenColor)
            || checkDiagonally1(col, row, x, tokenColor)
            || checkDiagonally2(col, row, x, tokenColor))
        {
            return true;
        }
        
        return false;
    }

  //------checking nrOfTokens in  a row
  private boolean checkDiagonally1(int col, int row, int nrOfTokens, TokenColor tokenColor)
  {
    for (int j = 0; j < nrOfTokens; j++)
    {
        int adjacentSameTokens = 1;
        for (int i = 0; i < nrOfTokens; i++)
        {
            if ((col + i - j) >= 0 && (col + i - j) < board.length
                && (row + i - j) >= 0
                && (row + i - j) < board[col].length
                && board[col + i - j][row + i - j].getTokenColor() == tokenColor)
            {
                adjacentSameTokens++;
            }
        }
        if (adjacentSameTokens == nrOfTokens)
            return true;
    }
    return false;
  }
  
  private boolean checkDiagonally2(int col, int row, int nrOfTokens, TokenColor tokenColor)
  {
    for (int j = 0; j < nrOfTokens; j++)
    {
        int adjacentSameTokens = 1;
        for (int i = 0; i < nrOfTokens; i++)
        {
            if ((col - i + j) >= 0 && (col - i + j) < board.length
                && (row + i - j) >= 0
                && (row + i - j) < board[col].length
                && board[col - i + j][row + i - j].getTokenColor()== tokenColor)
            {
                adjacentSameTokens++;
            }
        }
        if (adjacentSameTokens == nrOfTokens)
            return true;
    }
    return false;
  }

    private boolean checkHorizontally(int col, int row, int nrOfTokens, TokenColor tokenColor)
    {
        for (int j = 0; j < nrOfTokens; j++)
        {
            int adjacentSameTokens = 1;
            for (int i = 0; i < nrOfTokens; i++)
            {
                if ((col + i - j) >= 0 && (col + i - j) < board.length
                    && board[col + i - j][row].getTokenColor()== tokenColor)
                {
                    adjacentSameTokens++;
                }
            }
            if (adjacentSameTokens == nrOfTokens)
                return true;
        }
        return false;
    }

    private boolean checkVertically(int col, int row, int nrOfTokens, TokenColor tokenColor)
    {
        for (int j = 0; j < nrOfTokens; j++)
        {
            int adjacentSameTokens = 1;
            for (int i = 0; i < nrOfTokens; i++)
            {
                if ((row + i - j) >= 0 && (row + i - j) < board[col].length
                    && board[col][row + i - j].getTokenColor()== tokenColor)
                {
                    adjacentSameTokens++;
                }
            }
            if (adjacentSameTokens == nrOfTokens)
              return true;
        }
        return false;
    }

    public boolean isBoardEmpty()
    {
        for (int i = 0; i < board.length; i++)
        {
            if (board[i][0].getTokenColor() != TokenColor.None)
                return false;
        }
        return true;
    }

    /**
     *
     * @param column
     * @param tokenColor
     * @param commitMove
     * @return Die Row (Zeile), in der das Token zum Liegen kommt.
     */
    public int getTokenRow(int column)
    {
        int rowCount = 0;
        Token[] insertingColumn = this.board[column];
        for (Token row : insertingColumn)
        {
            if (row.getTokenColor() == TokenColor.None)
            {
                return rowCount;
            }
            
            rowCount++;
        }
        return -1; // Row is full of tokens
    }
    
    public boolean insertToken(int column, TokenColor tokenColor){
        int row = getTokenRow(column);
        if (row >= 0){
            this.board[column][row].setTokenColor(tokenColor);
            return true;
        }
        return false;
    }
}
