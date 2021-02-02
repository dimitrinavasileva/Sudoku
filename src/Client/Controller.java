package Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Controller {
    ObservableList<String> difficultyList = FXCollections.observableArrayList("EASY", "MEDIUM", "DIFFICULT");

    @FXML
    public GridPane idGrid;
    @FXML
    public GridPane gridID;
    @FXML
    public ChoiceBox difficultyBox;
    @FXML
    public Button btnPlay;
    @FXML
    public Button btnSolve;
    @FXML
    public Button btnUndo;
    @FXML
    public Button btnRedo;
    @FXML
    public Button btnClear;
    @FXML
    public Label lblTimer;
    @FXML
    public Label lblTime;

    public void initialize() {
        difficultyBox.setValue("EASY");
        difficultyBox.setItems(difficultyList);
    }

}
