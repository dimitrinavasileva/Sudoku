package Server;

import java.rmi.*;

public interface SudokuServerInterface extends Remote {
    public void generator(Difficulty d) throws RemoteException;

    public int[][] getSudoku() throws RemoteException;

    public int[][] getSolvedSudoku() throws RemoteException;
}
