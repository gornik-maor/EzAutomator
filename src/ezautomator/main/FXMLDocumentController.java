package ezautomator.main;

import ezautomator.main.script.ScriptExecutor;
import ezautomator.file.XMLController;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import ezautomator.alert.AlertController;
import ezautomator.confirmation.ConfirmationControllerSetup;
import ezautomator.delay.DelayFormController;
import ezautomator.file.NameXMLController;
import ezautomator.insertion.InsertionFormController;
import ezautomator.main.script.ExecutionChooserController;
import ezautomator.subForms.SetupWindowController;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
    private JFXTabPane tabPane;

    @FXML
    private MenuItem mnuEdit;

    @FXML
    private MenuItem mnuInsert;

    @FXML
    private MenuItem mnuRemove;

    @FXML
    private MenuItem mnuRefresh;

    @FXML
    private MenuItem mMnuCT;

    @FXML
    private MenuItem mMnuRT;

    @FXML
    private MenuItem mMnuRS;

    @FXML
    private MenuItem mMnuNew;

    @FXML
    private MenuItem mMnuLoad;

    @FXML
    private MenuItem mMnuSave;

    @FXML
    private MenuItem mMnuExit;

    private static int ID;

    private static Stage mainStage;

    private static Action tempAction;

    private static int actionDelay;

    private static ChoiceBox<String> tempCBox;

    private static TableView<Action> tempTable;

    private AlertController alertClass;

    private boolean isEditing, isAbove;

    private int selIndex;

    @FXML
    void closeApp(MouseEvent event) {
        System.exit(1);
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
        removeItem();
    }

    @FXML
    void onActionsBoxPressed(MouseEvent event) {
        if (actionsBox.getItems().isEmpty()) {
            populateActionsBox();
        }

        if (isEditing) {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "Please insert an action before continuing!",
                    "exclamation", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
            actionsBox.setDisable(true);
        }
    }

    @FXML
    void onMenuItemNew(ActionEvent event) {
        if (!actionTable.getItems().isEmpty()) {
            if (new AlertController().loadAlert().showDialog("Yes", "No", "Are you sure you want to delete all the actions?",
                    "exclamation", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5)) {
                actionTable.getItems().clear();
            }
        } else {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "There are no actions in the table to remove!",
                    "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
        }
    }

    @FXML
    void onMenuItemLoad(ActionEvent event) {
        onLoadXML();
    }

    @FXML
    void onMenuItemSave(ActionEvent event) {
        onSaveXML();
    }

    @FXML
    void onMenuItemExit(ActionEvent event) {
        boolean result = new AlertController().loadAlert().showDialog("Yes", "No", "Are you sure you want to exit?",
                "exclamation", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
        if (result) {
            System.exit(0);
        }
    }

    @FXML
    void onRemoveSelected(ActionEvent event) {
        removeItem();
    }

    @FXML
    void onExecutionSelectionPress(MouseEvent event) {
        if (!actionTable.getItems().isEmpty()) {
            int numExcs = new ExecutionChooserController().LoadForm().showDialog(EzAutomator.getMainStage());
            switch (numExcs) {
                case 0:
                    numExcs = 1;
                    break;
                case 1:
                    btnExecutions.setText("Select # of Executions");
                    break;
                default:
                    btnExecutions.setText("Executing: " + String.valueOf(numExcs) + " times.");
                    break;
            }
            ScriptExecutor.setExecs(numExcs);
        } else {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "Please add an action before continuing!",
                    "warning", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
        }
    }

    @FXML
    void onActionAddition(MouseEvent event) {
        if (!isEditing) {
            if (tempAction != null && !txtComment.getText().isEmpty()) {
                tempAction.setComment(txtComment.getText());

                boolean resultAlert = new AlertController().loadAlert().showDialog("Yes", "No", "Would you like to set a delay?",
                        "exclamation", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);

                if (resultAlert) {
                    // Opening the delay form
                    DelayFormController delayClss = new DelayFormController();
                    DelayFormController currDelayClss = delayClss.loadAlert();
                    currDelayClss.blurUponLoad(mainStage);
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
            isEditing = false;

            // If the user is trying to insert an action instead of adding it normally
        } else {
            tempAction.setComment(txtComment.getText());

            boolean resultAlert = new AlertController().loadAlert().showDialog("Yes", "No", "Would you like to set a delay?",
                    "exclamation", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);

            if (resultAlert) {
                // Opening the delay form
                DelayFormController delayClss = new DelayFormController();
                DelayFormController currDelayClss = delayClss.loadAlert();
                currDelayClss.setHideUponLoad(mainStage);
                currDelayClss.onResultFocus(mainStage);
                tempAction.setDelay(String.valueOf(currDelayClss.getDelay()));
            }

            int newIndex = selIndex;

            if (isAbove) {
//                newIndex = selIndex + 1;

            } else {
                newIndex = selIndex + 1;
            }

            // Adding the action at the specified index based on the user choice
            actionTable.getItems().add(newIndex, tempAction);
        }

        tempAction = null;
        txtComment.setText("");
        actionsBox.getItems().clear();
        actionsBox.setDisable(false);
        isEditing = false;
    }

    @FXML
    void onClickRunScript(MouseEvent event) {
        if (!actionTable.getItems().isEmpty()) {
            isEditing = false;
            ScriptExecutor script = new ScriptExecutor(actionTable);
            script.run();
        } else {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "There are no actions in the table to run!",
                    "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
        }
    }

    @FXML
    void onLoadXMLPress(MouseEvent event) {
        onLoadXML();
    }

    @FXML
    void onSaveXMLPress(MouseEvent event) {
        onSaveXML();
    }

    // Table Menu Items
    @FXML
    void onItemEditClick(ActionEvent event) {
        Action selAction = actionTable.getSelectionModel().getSelectedItem();
        if (actionTable.getSelectionModel().getSelectedIndex() != -1) {
            boolean result = new AlertController().loadAlert().showDialog("Action", "Delay", "What would you like to edit?",
                    "exclamation", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);

            if (result) {
                switch (selAction.getAction()) {
                    case "Click":
                        setPaneID(1);
                        selAction.turnIntoAction(new SetupWindowController().loadForm().showClickForm(selAction.getCoordinates(), EzAutomator.getMainStage(), 0.5));
                        break;

                    case "Click x2":
                        setPaneID(1);
                        selAction.turnIntoAction(new SetupWindowController().loadForm().showClickForm(selAction.getCoordinates(), EzAutomator.getMainStage(), 0.5));
                        break;

                    case "Hover":
                        setPaneID(1);
                        selAction.turnIntoAction(new SetupWindowController().loadForm().showClickForm(selAction.getCoordinates(), EzAutomator.getMainStage(), 0.5));
                        break;

                    case "Keys":
                        setPaneID(2);
                        // Problem occurs here when editting --> FIX IS NEEDED
                        selAction.turnIntoAction(new SetupWindowController().loadForm().showKeyForm(selAction.getSendKeys(), EzAutomator.getMainStage(), 0.5));
                        break;

                    case "Confirmation":
//                    Confirmation selConfirmation = (Confirmation) selAction;
                        ArrayList<String> confirmationInfo = new ConfirmationControllerSetup().loadForm().showSetup(selAction, EzAutomator.getMainStage(), 0.5);
                        selAction.turnIntoConfirmation(new Action("", Integer.parseInt(confirmationInfo.get(0)),
                                Integer.parseInt(confirmationInfo.get(1)), confirmationInfo.get(2)));
                        break;
                }
                tempAction = null;
            } else {
                selAction.setDelay(String.valueOf(new DelayFormController().loadAlert().getDelay(EzAutomator.getMainStage())));
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
    void onItemInsertion(ActionEvent event) {
        // where would you like to insert the action?
        if (actionTable.getSelectionModel().getSelectedIndex() != -1 && !actionTable.getItems().isEmpty()) {
            isEditing = true;
            tempAction = new InsertionFormController().loadAlert().showForm(EzAutomator.getMainStage(), 0.5);
            if (tempAction != null) {
                boolean posResult = new AlertController().loadAlert().showDialog("ABOVE", "BELOW", "Would you like to insert it above or below?",
                        "exclamation", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
                selIndex = actionTable.getSelectionModel().getSelectedIndex();

//                isAbove = (posResult) ? true : false;
                if (posResult) {
                    // We insert an action above as long as the first item isn't selected
                    if (actionTable.getSelectionModel().getSelectedIndex() == 0) {
                        isAbove = true;
                    }

                } else {
                    if (actionTable.getSelectionModel().getSelectedIndex() == actionTable.getItems().size() - 1 && actionTable.getItems().size() > 1) {
                        new AlertController().loadAlert().showDialog("Ok", "Cancel", "You can only insert an action above!",
                                "warning", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
                        isEditing = false;
                        actionsBox.setDisable(false);
                        tempAction = null;
                        actionsBox.getItems().clear();
                    } else {
                        isAbove = false;
                    }
                }
            } else {
                isEditing = false;
                actionsBox.setDisable(false);
            }

        } else {
            if (actionTable.getItems().isEmpty()) {
                AlertController alertForm = new AlertController();
                AlertController currAlert = alertForm.loadAlert();
                currAlert.setFontSize(17);
                currAlert.showDialog("Ok", "Cancel", "There are no items in the table. Add an action normally.",
                        "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
            } else {
                new AlertController().loadAlert().showDialog("Ok", "Cancel", "Please select an action as the position indication!",
                        "exclamation", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
            }
        }
    }

    @FXML
    void onItemRemoveClick(ActionEvent event) {
        removeItem();
    }

    @FXML
    void onTableRemoveAll(ActionEvent event) {
        if (!actionTable.getItems().isEmpty()) {
            if (new AlertController().loadAlert().showDialog("Yes", "No", "Are you sure you want to delete all the actions?",
                    "exclamation", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5)) {
                actionTable.getItems().clear();
            }
        } else {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "There are no actions in the table to remove!",
                    "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
        }
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
            actionTable.setEditable(true);
            tempCBox = actionsBox;
            tempTable = actionTable;

            // Change Listener for choicebox
            if (!actionsBox.getItems().isEmpty()) {
                actionsBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number prevIndex, Number newIndex) {
                        String selectedAction = actionsBox.getItems().get((int) newIndex);
                        if (!isEditing) {
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
                                    StackPane subRoot = FXMLLoader.load(getClass().getResource("/ezautomator/subForms/SetupWindow.fxml"));
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

                                //String comment, int keyContinue, int keyTerminate, String message, String delay
                                tempAction = new Action("", Integer.parseInt(confirmationInfo.get(0)), Integer.parseInt(confirmationInfo.get(1)),
                                        String.valueOf(confirmationInfo.get(2)));
                            }
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

                // Attaching icons to main menu items
                mMnuNew.setGraphic(new ImageView(new Image("/ezautomator/icons/blank.png")));

                ImageView loadIcon = new ImageView(new Image("/ezautomator/icons/load.png"));
                loadIcon.setFitHeight(20);
                loadIcon.setFitWidth(20);
                mMnuLoad.setGraphic(loadIcon);

                ImageView saveIcon = new ImageView(new Image("/ezautomator/icons/save.png"));
                saveIcon.setFitHeight(20);
                saveIcon.setFitWidth(20);
                mMnuSave.setGraphic(saveIcon);

                mMnuExit.setGraphic(new ImageView(new Image("/ezautomator/icons/exit.png")));
                mMnuCT.setGraphic(new ImageView(new Image("/ezautomator/icons/eraser.png")));
                mMnuRT.setGraphic(new ImageView(new Image("/ezautomator/icons/reload.png")));

                ImageView removeIcon = new ImageView(new Image("/ezautomator/icons/remove.png"));
                removeIcon.setFitHeight(20);
                removeIcon.setFitWidth(20);
                mMnuRS.setGraphic(removeIcon);
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
        actionColumn.setMinWidth(103);
        actionColumn.setSortable(false);
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));

        // Setting up the comment column
        TableColumn<Action, String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setMinWidth(100);
        commentColumn.setSortable(false);
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        // Enabling the user to edit the comment by double clicking the field
        Callback<TableColumn<Action, String>, TableCell<Action, String>> cellFactor
                = new Callback<TableColumn<Action, String>, TableCell<Action, String>>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell();
                    }
                };

//        commentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        commentColumn.setCellFactory(cellFactor);

        commentColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Action, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Action, String> event) {
                changeComment(event);
            }
        });

        // Setting up the coordinates column
        TableColumn<Action, ArrayList> coordinatesColumn = new TableColumn<>("Coordinates");
        coordinatesColumn.setMinWidth(100);
        coordinatesColumn.setSortable(false);
//        coordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));
        coordinatesColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Action, ArrayList>, ObservableValue<ArrayList>>() {
            @Override
            public ObservableValue<ArrayList> call(TableColumn.CellDataFeatures<Action, ArrayList> param) {
                ArrayList<Integer> coordinates = param.getValue().getCoordinates();
                ArrayList<String> empty = new ArrayList<>(Arrays.asList());
                if (coordinates.size() == 1) {
                    return new ReadOnlyObjectWrapper<>(empty);
                } else {
                    return new ReadOnlyObjectWrapper<>(coordinates);
                }
            }
        });

        // Setting up the keys column
        TableColumn<Action, ArrayList> sendKeysColumn = new TableColumn<>("Keys");
        sendKeysColumn.setMinWidth(20);
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
            StackPane loginPane = FXMLLoader.load(getClass().getResource("/ezautomator/login/SplashFXML.fxml"));
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
     * Changing an action comment based on user click
     *
     * @param editedCell
     */
    private void changeComment(TableColumn.CellEditEvent editedCell) {
        Action selAction = actionTable.getSelectionModel().getSelectedItem();
        selAction.setComment(editedCell.getNewValue().toString());
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

    /**
     * Loading and converting XML files into action objects
     */
    private void onLoadXML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a Script");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("XML Document", "*.xml"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedFile = fileChooser.showOpenDialog(EzAutomator.getMainStage());

        if (selectedFile != null) {
            try {
                if (!actionTable.getItems().isEmpty()) {
                    actionTable.getItems().clear();
                }

                Actions actions = XMLController.XMLtoActions(selectedFile.toString());

                if (!actions.getActions().isEmpty()) {
                    actions.getActions().forEach(actionTable.getItems()::add);
                }

                new AlertController().loadAlert().showDialog("Ok", "Cancel", "Script was loaded successfully!", "exclamation",
                        EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);

            } catch (Exception ex) {
                new AlertController().loadAlert().showDialog("Ok", "Cancel", "The selected script is not valid!", "error",
                        EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
            }
            tabPane.getSelectionModel().select(0);
        }
    }

    /**
     * Converting action objects into serializable XML
     */
    private void onSaveXML() {
        if (!actionTable.getItems().isEmpty()) {
            new NameXMLController().loadResources().loadForm(actionTable, EzAutomator.getMainStage(), 0.5);
            tabPane.getSelectionModel().select(0);
        } else {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "There aren't any actions to save!", "error",
                    EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
            tabPane.getSelectionModel().select(0);
        }
    }
}
