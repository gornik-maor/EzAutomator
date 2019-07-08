package ezautomator.main;

import com.jfoenix.controls.JFXTextField;
import ezautomator.subForms.SetupWindowController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Abwatts
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private ChoiceBox<String> actionsBox;

    @FXML
    private ToggleButton btnExecutions;

    @FXML
    private Button btnAdd;

    @FXML
    private ImageView closeBtn;

    @FXML
    private RadioButton rdoRunF;

    @FXML
    private StackPane root;

    @FXML
    private ImageView btnRemove;

    @FXML
    private TableView<Action> actionTable;

    @FXML
    private JFXTextField txtComment;

    @FXML
    private Button btnRun;

    @FXML
    void closeApp() {
        Stage tempStage = (Stage) closeBtn.getScene().getWindow();
        tempStage.close();
    }

    @FXML
    void closeBtnChangeHover() {
        closeBtn.setImage(new Image("/ezautomator/icons/close-hover.png"));
    }

    @FXML
    void closeBtnChangeLeave() {
        closeBtn.setImage(new Image("/ezautomator/icons/close.png"));
    }

    @FXML
    void removeAction(MouseEvent event) {
        // Are you sure?
        ObservableList<Action> selectedActions = actionTable.getSelectionModel().getSelectedItems();
        ObservableList<Action> allActions = actionTable.getItems();

        // For each action in selectedActions --> remove from the list
        if (actionTable.getItems().size() > 0) {
            selectedActions.forEach(allActions::remove);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!EzAutomator.isSplashLoaded) {
            //loadSpalshScreen();
            populateActionsBox();
            populateColumns();
            ArrayList test1 = new ArrayList(Arrays.asList(1234, 3));
            ArrayList test2 = new ArrayList(Arrays.asList());
            addAction(new Action("Click", "iSYS", test1, test2, "50"));

            ArrayList test3 = new ArrayList(Arrays.asList("CTRL", "C"));
            addAction(new Action("Keys", "Notepad", test2, test3, "100"));
        }

        actionsBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number prevIndex, Number newIndex) {
                String selectedAction = actionsBox.getItems().get((int) newIndex);

                SetupWindowController subController = new SetupWindowController();

                if (selectedAction.startsWith("Click")) {
                    //subController.disablePane(1);
                } else {
                    //subController.disablePane(2);
                }

                try {
                    StackPane subRoot = FXMLLoader.load(getClass().getResource("/ezautomator/subForms/SetupWindow.FXML"));
                    Stage subStage = new Stage();
                    subStage.initStyle(StageStyle.UNDECORATED);
                    subStage.setScene(new Scene(subRoot));
                    subStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Initializing the actions table
     *
     */
    public void populateColumns() {
        // Clearing all default columns first
        actionTable.getColumns().clear();

        // Enabling multi-seleciton
        actionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Setting up the action column
        TableColumn<Action, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setMinWidth(80);
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));

        // Setting up the comment column
        TableColumn<Action, String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setMinWidth(100);
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        // Setting up the coordinates column
        TableColumn<Action, ArrayList> coordinatesColumn = new TableColumn<>("Coordinates");
        coordinatesColumn.setMinWidth(100);
        coordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));

        // Setting up the keys column
        TableColumn<Action, ArrayList> sendKeysColumn = new TableColumn<>("Keys");
        sendKeysColumn.setMinWidth(50);
        sendKeysColumn.setCellValueFactory(new PropertyValueFactory<>("sendKeys"));

        // Setting up the delay column
        TableColumn<Action, String> delayColumn = new TableColumn<>("Delay");
        delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
        delayColumn.setMinWidth(50);
        actionTable.getColumns().addAll(actionColumn, commentColumn, coordinatesColumn, sendKeysColumn, delayColumn);
    }

    /**
     *
     * @param action
     */
    public void addAction(Action action) {
        actionTable.getItems().add(action);
    }

    /**
     * Populating the choice box
     */
    public void populateActionsBox() {
        actionsBox.getItems().addAll("Click on specified coordinates", "Send Keys");
    }

    /**
     * Loading the welcome screen
     */
    private void loadSpalshScreen() {
        try {
            StackPane loginPane = FXMLLoader.load(getClass().getResource("/ezautomator/login/SplashFXML.FXML"));
            root.getChildren().setAll(loginPane);

            // Creating fade in transition 
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), loginPane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);
            fadeIn.play();

            // Creating fade out transition 
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), loginPane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // Playing the fade out transition
                    fadeOut.play();
                }
            });

            fadeOut.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // Setting the scene back to the original
                    EzAutomator.isSplashLoaded = true;

                    try {
                        StackPane mainPane = FXMLLoader.load(getClass().getResource("/ezautomator/main/FXMLDocument.fxml"));
                        root.getChildren().setAll(mainPane);

                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
