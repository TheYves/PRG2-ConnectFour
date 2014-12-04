package Gruppe7.ConnectFour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Christoph
 */
public class Computer extends Player {
      
    private ComputerLevel level = ComputerLevel.Low;
    
    public Computer(ComputerLevel level, TokenColor tokenColor) {
        super("Computer", tokenColor);
        this.level = level;
    }

    @Override
    public int makeMove(GameBoard gameBoard)
    {
        Token[][] board = gameBoard.getBoard();
        Random grn = new Random();
        
        if (this.level == ComputerLevel.Low)
        {
            return grn.nextInt(board.length);
        }
        else
        {
            ArrayList<Integer> possibleSolutions = new ArrayList<>();
            ArrayList<Integer> veryBadIdeas = new ArrayList<>();
            int topRow = board[0].length - 1;
            int column;
            int row;

            // Wenn das Board leer ist, dann immer die Mitte
            if (gameBoard.isBoardEmpty())
            {
                return (int)(board.length / 2) + 1; 
            }

            // Finaler Zug?
            for (column = 0; column < board.length; column++)
            {
                row = gameBoard.getTokenRow(column);
                if (row != -1 && gameBoard.checkXInARow(column, row, 4, getTokenColor()))
                {
                    return column; // Gewonnen
                }
            }

            // Kann der Gegner mit dem einem Zug gewinnen? -> Blockieren
            for (column = 0; column < board.length; column++)
            {
                row = gameBoard.getTokenRow(column);
                if (row != -1 && gameBoard.checkXInARow(column, row, 4, getEnemyColor()))
                {
                    return column;
                }
            }

            // Alle Gewinnmoeglichkeiten des Gegners sammeln
            for (column = 0; column < 7; column++)
            {
                row = gameBoard.getTokenRow(column);

                if (row != -1 && gameBoard.checkXInARow(column, row, 3, getEnemyColor()))
                {
                    possibleSolutions.add(column);
                }

                if (row != -1 && gameBoard.checkXInARow(column, row, 2, getEnemyColor()))
                {
                    possibleSolutions.add(column);
                }
            }

            // Fuehrt mein Zug dazu, dass der Gegner gewinnen kann?
            Iterator<Integer> posSolu = possibleSolutions.iterator();
            int possibleColumn;
            while (posSolu.hasNext())
            {
                possibleColumn = posSolu.next();
                int nextRow = gameBoard.getTokenRow(possibleColumn) + 1;
                if (nextRow <= topRow
                    && gameBoard.checkXInARow(possibleColumn, nextRow, 4, getEnemyColor()))
                {
                    posSolu.remove();
                }
            }

            if (this.level == ComputerLevel.High)
            {
                // Mitte bevorzugen
                int nrOfSolutions = possibleSolutions.size();
                for (int i = 0; i < nrOfSolutions; i++)
                {
                    if (possibleSolutions.get(i) > 1 && possibleSolutions.get(i) < 5)
                        possibleSolutions.add(possibleSolutions.get(i));
                }
            }
            
            // Sind immernoch mehrere Zuege vorhanden, eine zufaellige waehlen
            if (!possibleSolutions.isEmpty())
            {
                Collections.shuffle(possibleSolutions);
                return (int)possibleSolutions.get(0);
            }
            
            if (this.level == ComputerLevel.Medium)
            {
                return grn.nextInt(board.length);
            }

            if (this.level == ComputerLevel.High)
            {
                // schlechte Zuege filtern
                for (int col = 0; col < 7; col++)
                {
                    if (board[col][topRow].getTokenColor() != TokenColor.None)
                        veryBadIdeas.add(col);
                    else
                    { 
                        int nextRow = gameBoard.getTokenRow(col) + 1;
                        if (nextRow <= topRow
                            && gameBoard.checkXInARow(col, nextRow, 4, getTokenColor()))
                        {
                            veryBadIdeas.add(col);
                        }
                    }
                }

                // Wenn es nur schlechte Zuege gibt, eine zufaellige waehlen
                if (veryBadIdeas.size() == board.length)
                {
                    do
                    {
                        column = grn.nextInt(board.length);
                    }
                    while (board[column][topRow].getTokenColor() != TokenColor.None);

                    return column;
                  }
                else
                {
                    do
                    {
                        column = grn.nextInt(4) + grn.nextInt(4);
                    }
                    while (veryBadIdeas.contains(column));
                }

                return column;
            }
        }
        
        return grn.nextInt(board.length);
    }
}
