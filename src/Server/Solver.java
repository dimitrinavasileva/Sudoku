package Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Solver {
    public static void generateSudoku(int[][] board) {
        List<Integer> numbers = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(numbers);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (i == 0) {
                    board[i][j] = numbers.get(j);
                } else {
                    board[i][j] = 0;
                }
            }
        }
    }

    /**
     * Main method for testing.
     *
     * @param args
     */
    public static void main(String[] args) {
        int[][] board = new int[9][9];

        generateSudoku(board);

        Sudoku solver = new Sudoku();
        solver.solver(board);
        solver.printBoard(board);
    }
}
