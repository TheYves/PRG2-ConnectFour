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
    
    public Computer(GameModel game, ComputerLevel level)
    {
        this.level = level;
    }

    @Override
    public int makeMove(int previousEnemyColumn)
    {
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
            if (isBoardEmpty(board))
            {
                return (int)(board.length / 2) + 1; 
            }

            // Finaler Zug?
            for (column = 0; column < board.length; column++)
            {
                row = insertToken(column, board);
                if (row != -1 && checkXInARow(column, row, 4, this.thisPlayer, board))
                {
                    return column; // Gewonnen
                }
            }

            // Kann der Gegner mit dem einem Zug gewinnen? -> Blockieren
            for (column = 0; column < board.length; column++)
            {
                row = insertToken(column, board);
                if (row != -1 && checkXInARow(column, row, 4, enemyPlayer, board))
                {
                    return column;
                }
            }

            // Alle Gewinnmoeglichkeiten des Gegners sammeln
            for (column = 0; column < 7; column++)
            {
                row = insertToken(column, board);

                if (row != -1 && checkXInARow(column, row, 3, enemyPlayer, board))
                {
                    possibleSolutions.add(column);
                }

                if (row != -1 && checkXInARow(column, row, 2, enemyPlayer, board))
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
                int nextRow = insertToken(possibleColumn, board) + 1;
                if (nextRow <= topRow
                    && checkXInARow(possibleColumn, nextRow, 4, enemyPlayer, board))
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
                    if (board[col][topRow].getPlayer() != null)
                        veryBadIdeas.add(col);
                    else
                    { 
                        int nextRow = insertToken(col, board) + 1;
                        if (nextRow <= topRow
                            && checkXInARow(col, nextRow, 4, enemyPlayer, board))
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
                    while (board[column][topRow].getPlayer() != null);

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

    @Override
    public int getColumn(GameBoard board) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
