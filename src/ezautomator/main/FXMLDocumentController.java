package ezautomator.main;

import com.jfoenix.controls.JFXTextField;
import ezautomator.alert.AlertController;
import ezautomator.delay.DelayFormController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.stage.Modality;
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
    private ImageView minBtn;

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

    private static int ID;

    private static Stage mainStage;

    private static Action tempAction;

    private static int actionDelay;

    private static ChoiceBox<String> tempCBox;

    private AlertController alertClass;

    @FXML
    void closeApp(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    void minApp(MouseEvent event) {
        Stage subStage = (Stage) minBtn.getScene().getWindow();
        subStage.setIconified(true);
    }

    @FXML
    void closeBtnChangeHover(MouseEvent event) {
        closeBtn.setImage(new Image("/ezautomator/icons/close-hover.png"));
    }

    @FXML
    void closeBtnChangeLeave(MouseEvent event) {
        closeBtn.setImage(new Image("/ezautomator/icons/close.png"));
    }

    @FXML
    void minBtnChangeHover(MouseEvent event) {
        minBtn.setImage(new Image("/ezautomator/icons/minimize-hover.png"));
    }

    @FXML
    void minBtnChangeLeave(MouseEvent event) {
        minBtn.setImage(new Image("/ezautomator/icons/minimize.png"));
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

    @FXML
    void onActionsBoxPressed(MouseEvent event) {
        if (actionsBox.getItems().isEmpty()) {
            populateActionsBox();
        }
    }

    @FXML
    void onActionAddition(MouseEvent event) {
        // Setting up an alert

//        AlertController alert = new AlertController();
//        alert.onResultFocus(getPrimaryStage());
        getPrimaryStage().setIconified(true);

//        loadAlert();
//        getAlertCls().onResultFocus(mainStage);
//        getAlertCls().setMessage("TestTwo");
//        getAlertCls().setYesNo(1, 0);

        AlertController alertClss = new AlertController();
        AlertController currAlertClss = alertClss.loadAlert();
        currAlertClss.setMessage("WORK PLEASE!");
        // Setting left button for yes and right for no
        currAlertClss.setYesNo(1, 0);
        currAlertClss.onResultFocus(mainStage);
        System.out.println(currAlertClss.getResult());
//        newOne.getResult();

//        if(test != null) {
//            System.out.println("not null!!!!!!");
//        }
        
//        boolean result = test.loadAlert();
//        if (test != null) {
//            boolean result = test.loadAlert();
//            System.out.println(result);
//        } else {
//            System.out.println("test is null");
//        }

        if (tempAction != null && !txtComment.getText().isEmpty()) {
            tempAction.setComment(txtComment.getText());
            addAction(tempAction);
            actionsBox.getItems().clear();
        } else {
            // Create my own custom expection
            System.out.println("null!");
        }

    }

    @FXML
    void onClickRunScript(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!EzAutomator.isSplashLoaded) {
            //loadSpalshScreen();
            populateActionsBox();
            populateColumns();
            tempCBox = actionsBox;

            ArrayList test1 = new ArrayList(Arrays.asList(1234, 3));
            ArrayList test2 = new ArrayList(Arrays.asList());
            addAction(new Action("Click", "iSYS", test1, test2, "50"));

            ArrayList test3 = new ArrayList(Arrays.asList("CTRL", "C"));
            addAction(new Action("Keys", "Notepad", test2, test3, "100"));
        }

        // Change Listener for choicebox
        if (!actionsBox.getItems().isEmpty()) {
            actionsBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number prevIndex, Number newIndex) {
                    String selectedAction = actionsBox.getItems().get((int) newIndex);

                    if (selectedAction.startsWith("Click")) {
                        setPaneID(1);
                    } else if (selectedAction.startsWith("Send")) {
                        setPaneID(2);
                    } else {
                        // Confirmation window goes here
                    }

                    try {
                        Stage currStage = (Stage) root.getScene().getWindow();
                        mainStage = currStage;
                        StackPane subRoot = FXMLLoader.load(getClass().getResource("/ezautomator/subForms/SetupWindow.FXML"));
                        Stage subStage = new Stage();
                        subStage.initStyle(StageStyle.UNDECORATED);
                        subStage.initModality(Modality.APPLICATION_MODAL);
                        subStage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
                        subStage.setTitle("EzAutomator");
                        subStage.setScene(new Scene(subRoot));
                        currStage.setIconified(true);
                        subStage.show();
//                        if (!actionsBox.getItems().isEmpty()) {
//                            actionsBox.getItems().clear();
//                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     *
     * @param newAction
     * @return
     */
    public static void recieveActionType(Action newAction) {
        if (!newAction.getAction().isEmpty() && !newAction.getCoordinates().isEmpty()) {
            tempAction = newAction;
            return;
        }
        // throw custom exception
        tempAction = null;
    }

    /**
     * Setting an action delay based on the user's preference
     *
     * @param delay
     */
    public static void recieveDelay(int delay) {
        if (delay != 0) {
            actionDelay = delay;
        }
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
        actionColumn.setSortable(false);
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));

        // Setting up the comment column
        TableColumn<Action, String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setMinWidth(100);
        commentColumn.setSortable(false);
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        // Setting up the coordinates column
        TableColumn<Action, ArrayList> coordinatesColumn = new TableColumn<>("Coordinates");
        coordinatesColumn.setMinWidth(100);
        coordinatesColumn.setSortable(false);
        coordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));

        // Setting up the keys column
        TableColumn<Action, ArrayList> sendKeysColumn = new TableColumn<>("Keys");
        sendKeysColumn.setMinWidth(50);
        sendKeysColumn.setSortable(false);
        sendKeysColumn.setCellValueFactory(new PropertyValueFactory<>("sendKeys"));

        // Setting up the delay column
        TableColumn<Action, String> delayColumn = new TableColumn<>("Delay");
        delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
        delayColumn.setMinWidth(50);
        delayColumn.setSortable(false);
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
        actionsBox.getItems().addAll("Click on specified coordinates", "Send Keys", "Confirmation");
    }

    /**
     * Tracking user's action choice
     *
     * @return
     */
    public static int getPaneID() {
        return ID;
    }

    /**
     * Tracking user's action choice
     *
     * @param ID
     */
    public static void setPaneID(int ID) {
        FXMLDocumentController.ID = ID;
    }

    /**
     * Returning main stage
     *
     * @return
     */
    public static Stage getPrimaryStage() {
        return mainStage;
    }

    /**
     * Returning actions box
     *
     * @return
     */
    public static ChoiceBox<String> getActionsBox() {
        if (tempCBox != null) {
            return tempCBox;
        } else {
            return null;
        }
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

    private void loadAlert() {
        try {
//            StackPane alertPane = FXMLLoader.load(getClass().getResource("/ezautomator/alert/Alert.fxml"));
            FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/alert/Alert.fxml"));
            StackPane alertPane = fxmlLoader.load();
            setAlertCls(fxmlLoader.getController());
            Stage alertStage = new Stage();
            alertStage.initStyle(StageStyle.UNDECORATED);
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
            alertStage.setTitle("EzAutomator");
            alertStage.setScene(new Scene(alertPane));
            alertStage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private AlertController getAlertCls() {
        return alertClass;
    }

    private void setAlertCls(AlertController alertCls) {
        if (alertCls != null) {
            this.alertClass = alertCls;
        }
    }
}
