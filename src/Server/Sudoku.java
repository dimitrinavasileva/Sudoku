package Server;

import java.util.stream.IntStream;

/**
 * Program that sloves SUDOKU using Backtracking Algorithm.
 */

public class Sudoku {
    private static final int BOARD_SIZE = 9;
    private static final int SUBSECTION_SIZE = 3;
    private static final int START_INDEX = 0;

    private static final int EMPTY_CELL = 0;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;

    /**
     * Method that prints the solution.
     *
     * @param board
     */
    public void printBoard(int[][] board) {
        for (int row = START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = START_INDEX; column < BOARD_SIZE; column++) {
                System.out.print(board[row][column] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Method that iterates through rows, columns and values
     * testing each cell for a valid solution
     *
     * @param board
     * @return
     */
    public boolean solver(int[][] board) {
        for (int row = START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = START_INDEX; column < BOARD_SIZE; column++) {
                if (board[row][column] == EMPTY_CELL) {
                    for (int k = MIN_VALUE; k <= MAX_VALUE; k++) {
                        board[row][column] = k;
                        if (isValid(board, row, column) && solver(board)) {
                            return true;
                        }
                        board[row][column] = EMPTY_CELL;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Boolean method that checks the row, column, and 3 x 3 grid are valid
     *
     * @param board
     * @param row
     * @param column
     * @return
     */
    private boolean isValid(int[][] board, int row, int column) {
        return rowConstraint(board, row) &&
                columnConstraint(board, column) &&
                subsectionConstraint(board, row, column);
    }

    /**
     * Check subsection constraint.
     *
     * @param board
     * @param row
     * @param column
     * @return
     */
    private boolean subsectionConstraint(int[][] board, int row, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        int subsectionRowStart = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionRowEnd = subsectionRowStart + SUBSECTION_SIZE;

        int subsectionColumnStart = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionColumnEnd = subsectionColumnStart + SUBSECTION_SIZE;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColumnStart; c < subsectionColumnEnd; c++) {
                if (!checkConstraint(board, r, constraint, c)) return false;
            }
        }
        return true;
    }

    /**
     * Check column constraint.
     *
     * @param board
     * @param column
     * @return
     */
    private boolean columnConstraint(int[][] board, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(START_INDEX, BOARD_SIZE)
                .allMatch(row -> checkConstraint(board, row, constraint, column));
    }

    /**
     * Check row constraint.
     *
     * @param board
     * @param row
     * @return
     */
    private boolean rowConstraint(int[][] board, int row) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(START_INDEX, BOARD_SIZE)
                .allMatch(column -> checkConstraint(board, row, constraint, column));
    }

    private boolean checkConstraint(int[][] board, int row, boolean[] constraint, int column) {
        if (board[row][column] != EMPTY_CELL) {
            if (!constraint[board[row][column] - 1]) {
                constraint[board[row][column] - 1] = true;
            } else {
                return false;
            }
        }
        return true;
    }
}
