package ezautomator.main;

import com.jfoenix.controls.JFXTextField;
import ezautomator.alert.AlertController;
import ezautomator.confirmation.ConfirmationControllerSetup;
import ezautomator.delay.DelayFormController;
import ezautomator.subForms.SetupWindowController;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
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

    @FXML
    private MenuItem mnuEdit;

    @FXML
    private MenuItem mnuRemove;

    @FXML
    private MenuItem mnuRefresh;

    private static int ID;

    private static Stage mainStage;

    private static Action tempAction;

    private static int actionDelay;

    private static ChoiceBox<String> tempCBox;

    private static TableView<Action> tempTable;

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
        removeItem();
    }

    @FXML
    void onActionsBoxPressed(MouseEvent event) {
        if (actionsBox.getItems().isEmpty()) {
            populateActionsBox();
        }
    }

    @FXML
    void onActionAddition(MouseEvent event) {
        if (tempAction != null && !txtComment.getText().isEmpty()) {
            tempAction.setComment(txtComment.getText());

            boolean resultAlert = new AlertController().loadAlert().showDialog("Yes", "No", "Would you like to set a delay?",
                    "exclamation", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);

            if (resultAlert == true) {
                // Opening the delay form
                DelayFormController delayClss = new DelayFormController();
                DelayFormController currDelayClss = delayClss.loadAlert();
                currDelayClss.setHideUponLoad(mainStage);
                currDelayClss.onResultFocus(mainStage);
                tempAction.setDelay(String.valueOf(currDelayClss.getDelay()));
            }

            addAction(tempAction);
            // Clearing previous action
            tempAction = null;
            txtComment.setText("");
            actionsBox.getItems().clear();
        } else {
            // Clearing the comment field
            txtComment.setText("");
            // Create my own custom expection
            AlertController alertForm = new AlertController();
            AlertController currAlert = alertForm.loadAlert();
            currAlert.setFontSize(17.5);
            currAlert.showDialog("Ok", "Cancel", "Please pick an action and fill all fields before trying again.",
                    "warning", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
            System.out.println("tempAction is: " + tempAction);
        }

    }

    @FXML
    void onClickRunScript(MouseEvent event) {
        ScriptExecutor script = new ScriptExecutor(tempTable);
        script.run();
    }

    // Table Menu Items
    @FXML
    void onItemEditClick(ActionEvent event) {
        Action selAction = actionTable.getSelectionModel().getSelectedItem();

        if (actionTable.getSelectionModel().getSelectedIndex() != -1) {
            switch (selAction.getAction()) {
                case "Click":
                    setPaneID(1);
                    selAction.turnInto(new SetupWindowController().loadForm().showClickForm(selAction.getCoordinates(), EzAutomator.getMainStage(), 0.5));
                    break;

                case "Click x2":
                    setPaneID(1);
                    selAction.turnInto(new SetupWindowController().loadForm().showClickForm(selAction.getCoordinates(), EzAutomator.getMainStage(), 0.5));
                    break;

                case "Hover":
                    setPaneID(1);
                    selAction.turnInto(new SetupWindowController().loadForm().showClickForm(selAction.getCoordinates(), EzAutomator.getMainStage(), 0.5));
                    break;

                case "Keys":
                    setPaneID(2);
                    selAction.turnInto(new SetupWindowController().loadForm().showKeyForm(selAction.getSendKeys(), EzAutomator.getMainStage(), 0.5));
                    break;

                case "Confirmation":   
                    // doesn't work properly --> action is not being updated correctly
                    // bug - comment becomes the confirmation's main message (???) --> might not be involved to this
                    // code right here. Double check tho!
                    Confirmation selConfirmation = (Confirmation)selAction;
                    ArrayList<String> confirmationInfo = new ConfirmationControllerSetup().loadForm().showSetup(selConfirmation, EzAutomator.getMainStage(), 0.5);
                    selConfirmation.turnInto(new Confirmation("", Integer.parseInt(confirmationInfo.get(0)),
                                    Integer.parseInt(confirmationInfo.get(1)), confirmationInfo.get(2)));
                    selAction.turnInto(selConfirmation);
                    break;
            }
        } else if (actionTable.getItems().isEmpty()) {
             new AlertController().loadAlert().showDialog("Ok", "Cancel", "There are no items to edit!",
                    "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
        } else {
            // Displaying an alert
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "There are no selected items to edit!",
                    "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);

        }

        actionTable.refresh();
        System.out.println("clicked");
    }

    @FXML
    void onItemRemoveClick(ActionEvent event) {
        removeItem();
    }

    @FXML
    void onItemDuplicateClick(ActionEvent event) {
        if (actionTable.getItems().size() > 0) {
            // Duplicate to where (INSERT ABOVE / BELOW)
        } else {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "There are no items in the table to refresh!",
                    "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
        }
    }

    @FXML
    void onTableRefreshClick(ActionEvent event) {
        actionTable.refresh();
        // Displaying an alert
        if (actionTable.getItems().size() > 0) {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "Table was refreshed successfully!",
                    "exclamation", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
        } else {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "There are no items in the table to refresh!",
                    "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (EzAutomator.isSplashLoaded) {
            populateActionsBox();
            populateColumns();
            tempTable = actionTable;
            tempCBox = actionsBox;

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
                            setPaneID(3);
                        }

                        // Opening the subStage form for
                        if (getPaneID() == 1 || getPaneID() == 2) {
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

                            // Adding confirmation to the script
                        } else {
                            ConfirmationControllerSetup confirmationClss = new ConfirmationControllerSetup();
                            // Setting up the alert form before displaying it
                            ConfirmationControllerSetup currConfirmationClss = confirmationClss.loadForm();
                            currConfirmationClss.setHideUponLoad(EzAutomator.getMainStage());
                            currConfirmationClss.onResultFocus(EzAutomator.getMainStage());
                            ArrayList<String> confirmationInfo = currConfirmationClss.getConfirmationInfo();
                            tempAction = new Confirmation("", Integer.parseInt(confirmationInfo.get(0)),
                                    Integer.parseInt(confirmationInfo.get(1)), confirmationInfo.get(2));

                        }
                    }
                });

                // Table tooltip
                actionTable.setRowFactory(new Callback<TableView<Action>, TableRow<Action>>() {
                    @Override
                    public TableRow<Action> call(TableView<Action> param) {
                        return new TooltipTableRow<Action>((Action tempAction) -> {
                            // Converting to minutes
                            double aDelay = (Double.parseDouble(tempAction.getDelay().replace(" m/s", "")) / 60000);
                            String dataT = "minutes";

                            // Checking if the number is better be shown in seconds rather than minutes
                            if (aDelay < 0.9) {
                                dataT = "seconds";
                                // Converting to seconds
                                aDelay *= 60;
                            } else if (aDelay >= 60) {
                                dataT = "hours";
                                // Converting to hours
                                aDelay /= 60;
                            }

                            // Checking whether the number of seconds or minutes is only 1
                            dataT = (aDelay == 1.0) ? dataT.substring(0, dataT.length() - 1) : dataT;

                            // Returning the text to display in the tooltip
                            String actionT = tempAction.getAction();
                            if (actionT.equals("Keys")) {
                                actionT = "Send Keys";
                            } else if (Character.toUpperCase(tempAction.getType()) == 'C'
                                    || Character.toUpperCase(tempAction.getType()) == 'H') {
                                actionT = "Mouse " + actionT;
                            }

                            // Confirmation info displayed
                            if (tempAction.getAction().equals("Confirmation")) {
                                return "Action (" + actionT + ") | Continue (" + EzAutomator.getKeyTextRep(tempAction.getSendKeys().get(0)) + ") | Terminate ("
                                        + EzAutomator.getKeyTextRep(tempAction.getSendKeys().get(1)) + ") | Delay (" + aDelay + ") " + dataT + ".";
                            }

                            return "Action (" + actionT + ") | Delay (" + aDelay + ") " + dataT + ".";
                        });
                    }
                });

                // Table Menu Items
//                actionTable.setRowFactory(new Callback<TableView<Action>, TableRow<Action>>() {
//                    @Override
//                    public TableRow<Action> call(TableView<Action> param) {
//
//                        final TableRow<Action> tableRow = new TableRow<>();
//
//                        tableRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                            @Override
//                            public void handle(MouseEvent event) {
//                                MouseButton button = event.getButton();
//                                if (button == MouseButton.SECONDARY) {
//
//                                }
//                            }
//                        });
//
//                        final ContextMenu tableMenu = new ContextMenu();
//                        MenuItem editItem = new MenuItem("Edit");
//                        editItem.setStyle("-fx-text-fill: #FFFF8D;");
//                        editItem.setOnAction(new EventHandler<ActionEvent>() {
//                            @Override
//                            public void handle(ActionEvent event) {
//                                // 
//                                Action selAction = tableRow.getItem();
//                                switch (selAction.getAction()) {
//                                    case "Click":
//                                        setPaneID(1);
//                                        selAction.turnInto(new SetupWindowController().loadForm().showClickForm(selAction.getCoordinates(), EzAutomator.getMainStage(), 0.5));
//                                        break;
//
//                                    case "Click x2":
//                                        setPaneID(1);
//                                        selAction.turnInto(new SetupWindowController().loadForm().showClickForm(selAction.getCoordinates(), EzAutomator.getMainStage(), 0.5));
//                                        break;
//
//                                    case "Hover":
//                                        setPaneID(1);
//                                        selAction.turnInto(new SetupWindowController().loadForm().showClickForm(selAction.getCoordinates(), EzAutomator.getMainStage(), 0.5));
//                                        break;
//
//                                    case "Keys":
//                                        setPaneID(2);
//                                        selAction.turnInto(new SetupWindowController().loadForm().showKeyForm(selAction.getSendKeys(), EzAutomator.getMainStage(), 0.5));
//                                        System.out.println("BUSUDFHASCOASHC");
//                                        break;
//
//                                    case "Confirmation":
//
//                                        break;
//                                }
//
//                                actionTable.refresh();
//                                System.out.println("clicked");
//                            }
//                        });
//
//                        MenuItem removeItem = new MenuItem("Delete");
//                        removeItem.setStyle("-fx-text-fill: #FFFF8D;");
//                        removeItem.setOnAction(new EventHandler<ActionEvent>() {
//                            @Override
//                            public void handle(ActionEvent event) {
//                                removeItem();
//                            }
//                        });
//
//                        tableMenu.getItems().addAll(editItem, removeItem);
//                        // Only display context menu for non-null items:
//                        tableRow.contextMenuProperty().bind(
//                                Bindings.when(Bindings.isNotNull(tableRow.itemProperty()))
//                                        .then(tableMenu)
//                                        .otherwise((ContextMenu) null));
//                        return tableRow;
//                    }
//                });
            }
        } else {
            loadSpalshScreen();
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
     * Initializing the actions table
     *
     */
    public void populateColumns() {
        // Clearing all default columns first
        actionTable.getColumns().clear();

//        // Enabling multi-seleciton
//        actionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // Setting up the action column
        TableColumn<Action, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setMinWidth(60);
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

        // Sotring selected keys as string instead of their raw code representation
        sendKeysColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Action, ArrayList>, ObservableValue<ArrayList>>() {
            @Override
            public ObservableValue<ArrayList> call(TableColumn.CellDataFeatures<Action, ArrayList> param) {

                ArrayList<Integer> keyCodes = param.getValue().getSendKeys();
                ArrayList<String> keyList = new ArrayList(Arrays.asList());

                // Populating the key list with the actual string representation of each key code
                keyCodes.forEach((Integer keyCode) -> {
                    String keyRep = KeyEvent.getKeyText(keyCode);
                    if (keyRep.startsWith("Unknown keyCode:")) {
                        keyRep = keyRep.replace("Unknown keyCode: ", "");
                    }

                    keyRep = (keyRep.startsWith("Right B")) ? "CTRL" : keyRep;
                    keyRep = (keyRep.equals("0xa4")) ? "ALT" : keyRep;
                    keyRep = (keyRep.equals("0xd")) ? "ENTER" : keyRep;

                    keyList.add(keyRep);
                });

                return new ReadOnlyObjectWrapper<>(keyList);
            }
        });
//        coordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("sendKeys"));

        // Setting up the delay column
        TableColumn<Action, String> delayColumn = new TableColumn<>("Delay");
        delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
        delayColumn.setMinWidth(100);
        delayColumn.setSortable(false);
        actionTable.getColumns().addAll(actionColumn, commentColumn, coordinatesColumn, sendKeysColumn, delayColumn);
    }

    /**
     * Returning a list containing all the confirmation actions
     *
     * @return
     */
    public static ArrayList getConfirmations() {
        ArrayList<Action> confirmations = new ArrayList<>(Arrays.asList());
        tempTable.getItems().forEach((Action action) -> {
            if (action.getAction().equals("Confirmation")) {
                confirmations.add(action);
            }
        });

        return confirmations;
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
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), loginPane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);
            fadeIn.play();

            // Creating fade out transition 
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), loginPane);
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
                        // Loading the main pane instead of the one used for the animation
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

    /**
     * Remove selected item from the action table
     */
    private void removeItem() {
        if (!actionTable.getItems().isEmpty()) {
            if (actionTable.getSelectionModel().getSelectedIndex() != -1) {
                boolean result = new AlertController().loadAlert().showDialog("Yes", "No", "Are you sure you wish to continue?", "exclamation",
                        EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);

                // If the user confirms the removal
                if (result) {
                    ObservableList<Action> selectedActions = actionTable.getSelectionModel().getSelectedItems();
                    ObservableList<Action> allActions = actionTable.getItems();

                    // For each action in selectedActions --> remove from the list
                    if (actionTable.getItems().size() > 0) {
                        selectedActions.forEach(allActions::remove);
                    }
                }
            } else {
                // Displaying an alert
                new AlertController().loadAlert().showDialog("Ok", "Cancel", "Please select one of the items!",
                        "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
            }
        } else {
            // Displaying an alert
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "There are no items to remove!",
                    "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
        }
    }
}
