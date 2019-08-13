/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.insertion;

import ezautomator.alert.AlertController;
import ezautomator.confirmation.ConfirmationControllerSetup;
import ezautomator.main.Action;
import ezautomator.main.EzAutomator;
import ezautomator.main.FXMLDocumentController;
import static ezautomator.main.FXMLDocumentController.setPaneID;
import ezautomator.subForms.SetupWindowController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author gornicma
 */
public class InsertionFormController implements Initializable {

    @FXML
    private ChoiceBox<String> actionCmb;

    @FXML
    private Button btnContinue;

    @FXML
    private ImageView closeBtn;

    @FXML
    private StackPane root;

    @FXML
    void closeApp(MouseEvent event) {
        closeForm();
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
    void onBtnContinuePress(MouseEvent event) {
        if (actionCmb.getSelectionModel().getSelectedIndex() != -1) {
            closeForm();
        } else {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "No action was selected!",
                    "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
            tStage.setOpacity(0.5);
        }
    }

    private Action insAction;
    private double diffX, diffY;
    private InsertionFormController insertionCls;
    private Stage tStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        populateActionsBox();

        // Change Listener for choicebox
        if (!actionCmb.getItems().isEmpty()) {
            actionCmb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number prevIndex, Number newIndex) {
                    String selectedAction = actionCmb.getItems().get((int) newIndex);
                    EzAutomator.getMainStage().setIconified(true);

                    if (selectedAction.startsWith("Click")) {
                        setPaneID(1);
                        insAction = new SetupWindowController().loadForm().showForm(getCurrStage(), 0.5);
                        if (insAction == null) {
                            actionCmb.getItems().clear();
                            populateActionsBox();
                        }
                        FXMLDocumentController.getActionsBox().getSelectionModel().select(0); // <-- NOT SURE IF THAT'D WORK
                        FXMLDocumentController.getActionsBox().setDisable(true);
                        
                    } else if (selectedAction.startsWith("Send")) {
                        setPaneID(2);
                        insAction = new SetupWindowController().loadForm().showForm(getCurrStage(), 0.5);
                        if (insAction == null) {
                            actionCmb.getItems().clear();
                            populateActionsBox();
                        }
                        FXMLDocumentController.getActionsBox().getSelectionModel().select(1); // <-- NOT SURE IF THAT'D WORK
                        FXMLDocumentController.getActionsBox().setDisable(true);
                        
                    } else if (selectedAction.equals("Confirmation")) {
                        setPaneID(3);
                        try {
                            ArrayList<String> confirmationInfo = new ConfirmationControllerSetup().loadForm().showSetup(getCurrStage(), 0.5);
                            insAction = new Action("", Integer.parseInt(confirmationInfo.get(0)),
                                    Integer.parseInt(confirmationInfo.get(1)), confirmationInfo.get(2));
                            FXMLDocumentController.getActionsBox().getSelectionModel().select(2);
                            FXMLDocumentController.getActionsBox().setDisable(true);
                        } catch (NullPointerException e) {
                            actionCmb.getItems().clear();
                            populateActionsBox();
                        }
                    }
                }
            });

            /**
             * Allowing the user to drag the form
             */
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // Calculating the difference between the cursor and the stage to later add it to allow the cursor to stay at the same
                    // position without teleporting it to a different place
                    diffX = getCurrStage().getX() - event.getScreenX();
                    diffY = getCurrStage().getY() - event.getScreenY();
                }
            });

            // Ensuring that the stage moves a fixed number (that we calculated when the user first clicked the stage) of spaces
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // Helps my cursor stay at the same position when dragging the window instead of teleporting to the top most-left corner
                    getCurrStage().setX(event.getScreenX() + diffX);
                    getCurrStage().setY(event.getScreenY() + diffY);
                }
            });

            EzAutomator.getMainStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    closeForm();
                }
            });
        }
    }

    /**
     * Populating the choice box
     */
    private void populateActionsBox() {
        actionCmb.getItems().addAll("Click on specified coordinates", "Send Keys", "Confirmation");
    }

    /**
     * Retrieving a stage instance
     *
     * @return
     */
    private Stage getCurrStage() {
        Stage currStage = (Stage) root.getScene().getWindow();
        return currStage;
    }

    /**
     * Loading the alert and returning a reference to the current class
     *
     * @return
     */
    public InsertionFormController loadAlert() {
        try {
            FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/insertion/InsertionForm.fxml"));
            StackPane insertionPane = fxmlLoader.load();
            setInsertionCls(fxmlLoader.getController());
            Stage alertStage = new Stage();
            alertStage.initStyle(StageStyle.UNDECORATED);
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
            alertStage.setTitle("EzAutomator");
            alertStage.setScene(new Scene(insertionPane));
            return fxmlLoader.getController();

        } catch (IOException ex) {
            Logger.getLogger(AlertController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Action showForm(Stage tStage, double opValue) {
        this.tStage = tStage;
        setTargetOpacity(opValue);
        closeBtn.requestFocus();
        getCurrStage().showAndWait();
        return insAction;
    }

    /**
     * Setting the instance of this class
     *
     * @param insertionCls
     */
    public void setInsertionCls(InsertionFormController insertionCls) {
        if (insertionCls != null) {
            this.insertionCls = insertionCls;
        }
    }

    /**
     * Returning a reference to this specific instance of the class
     *
     * @return
     */
    public InsertionFormController getInsertionCls() {
        return insertionCls;
    }

    private void setTargetOpacity(double opacity) {
        if (tStage != null) {
            tStage.setOpacity(opacity);
        }
    }

    private void closeForm() {
        tStage.setOpacity(1);
        EzAutomator.getMainStage().setIconified(false);
        getCurrStage().close();
    }

}
