package Client;

import Server.Difficulty;
import Server.SudokuServerInterface;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Stack;

public class Main extends Application {
    private SudokuServerInterface sudokuServerObject;
    private int[][] sudoku;
    TextField[][] textFields = new TextField[9][9];

    Difficulty chosenDifficulty;

    private static final int STARTTIME = 0;
    private Timeline timeline;
    private final IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

    private void updateTime() {
        int seconds = timeSeconds.get();
        timeSeconds.set(seconds + 1);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sudoku");
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root, 580, 380);
        GridPane grid = (GridPane) scene.lookup("#gridID");

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                textFields[x][y] = new TextField();
                textFields[x][y].setStyle("-fx-pref-width: 2em;");
                grid.add(textFields[x][y], y, x);
            }
        }

        Label timerLabel = (Label) scene.lookup("#lblTimer");
        ChoiceBox difficultyBox = (ChoiceBox) scene.lookup("#difficultyBox");
        Button play = (Button) scene.lookup("#btnPlay");
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                chosenDifficulty = Difficulty.valueOf((String) difficultyBox.getValue());
                try {
                    sudokuServerObject.generator(chosenDifficulty);
                    sudoku = new int[9][9];
                    sudoku = sudokuServerObject.getSudoku();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        textFields[i][j].clear();
                        if (sudoku[i][j] != 0) {
                            textFields[i][j].setText(Integer.toString(sudoku[i][j]));
                        }
                    }
                }

                play.setDisable(true);
                timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeSeconds.set(STARTTIME);
                timeline.play();

                timerLabel.textProperty().bind(timeSeconds.asString());
            }
        });

        Button solve = (Button) scene.lookup("#btnSolve");
        solve.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    sudoku = sudokuServerObject.getSolvedSudoku();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        textFields[i][j].clear();
                        textFields[i][j].setText(Integer.toString(sudoku[i][j]));
                    }
                }

                timeline.stop();
            }
        });

        Button clear = (Button) scene.lookup("#btnClear");
        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        textFields[i][j].clear();
                    }
                }

                timeline.stop();
            }
        });

        Stack<String> newValues = new Stack<>();
        Stack<String> oldValues = new Stack<>();
        Stack<int[]> positions = new Stack<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int finalI = i;
                int finalJ = j;
                textFields[i][j].textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {

                        if (!newValue.equals("")) {
                            int[] position = new int[2];
                            position[0] = finalI;
                            position[1] = finalJ;
                            positions.push(position);
                            newValues.push(newValue);
                            oldValues.push(oldValue);
                        }

                    }
                })
                ;
            }
        }
        Button undo = (Button) scene.lookup("#btnUndo");
        undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (81 - chosenDifficulty.getDifficulty() >= positions.size()) return;

                int[] position = positions.pop();
                int x = position[0];
                int y = position[1];

                if (!oldValues.peek().equals("")) {
                    int b = Integer.parseInt(oldValues.pop());
                    textFields[x][y].setText(Integer.toString(b));
                } else {
                    textFields[x][y].clear();
                    oldValues.pop();
                }
            }

        });

        Button redo = (Button) scene.lookup("#btnRedo");
        redo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (81 - chosenDifficulty.getDifficulty() >= positions.size()) return;

                int[] position = positions.pop();
                int x = position[0];
                int y = position[1];

                if (!newValues.peek().equals("")) {
                    int b = Integer.parseInt(newValues.pop());
                    textFields[x][y].setText(Integer.toString(b));
                } else {
                    textFields[x][y].clear();
                    newValues.pop();
                }
            }

        });

        primaryStage.setScene(scene);
        primaryStage.show();
        initializeRMI();
    }

    /**
     * Initialize RMI
     */
    protected void initializeRMI() {
        String host = "localhost";
        System.out.println(host);
        try {
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            sudokuServerObject = (SudokuServerInterface) registry.lookup("SudokuServerInterfaceImpl");
            System.out.println("Server object " + sudokuServerObject + " found");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
