package Server;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class SudokuServerInterfaceImpl extends UnicastRemoteObject implements SudokuServerInterface {
    private int[][] sudoku;
    private int[][] solvedSudoku;

    public SudokuServerInterfaceImpl() throws RemoteException {
        sudoku = new int[9][9];
        solvedSudoku = new int[9][9];
    }

    public int[][] removeNumbersByDifficulty(Difficulty difficulty) {
        int numChanges = 0;
        int difficultyGet = difficulty.getDifficulty();

        // Това може да ти е директно в enum-a, тук само заема място
        if (difficultyGet == 45) {
            numChanges = 5;
        } else if (difficultyGet == 54) {
            numChanges = 6;
        } else if (difficultyGet == 63) {
            numChanges = 7;
        }

        Random random = new Random();
        int numChangesTemp, randomInt;

        for (int i = 0; i < 9; i++) {
            numChangesTemp = numChanges;
            while (numChangesTemp > 0) {
                randomInt = random.nextInt(9);
                if (sudoku[i][randomInt] != 0) {
                    sudoku[i][randomInt] = 0;
                    numChangesTemp--;
                }
            }
        }
        return sudoku;
    }

    @Override
    public void generator(Difficulty d) throws RemoteException {
        Solver.generateSudoku(solvedSudoku);
        Sudoku solver = new Sudoku();
        solver.solver(solvedSudoku);

        for (int i = 0; i < 9; i++) {
            System.arraycopy(solvedSudoku[i], 0, sudoku[i], 0, 9);
        }

        removeNumbersByDifficulty(d);
    }

    @Override
    public int[][] getSudoku() throws RemoteException {
        return sudoku;
    }

    @Override
    public int[][] getSolvedSudoku() throws RemoteException {
        return solvedSudoku;
    }
}
