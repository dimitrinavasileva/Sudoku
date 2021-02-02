package Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegisterWithRMI {
    public static void main(String[] args) {
        try {
            SudokuServerInterface sudokuServerInterface = new SudokuServerInterfaceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("SudokuServerInterfaceImpl", sudokuServerInterface);
            System.out.println("Sudoku server " + sudokuServerInterface + " registered");
            System.out.println("Press Return to quit.");
            int key = System.in.read();
            System.exit(0);
        } catch (Exception ex) {
        }
    }
}