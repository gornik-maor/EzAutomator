/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.delay;

import com.jfoenix.controls.JFXTextField;
import ezautomator.alert.AlertController;
import ezautomator.main.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
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

/**
 * FXML Controller class
 *
 * @author Abwatts
 */
public class DelayFormController implements Initializable {

    @FXML
    private ImageView closeBtn;

    @FXML
    private StackPane root;

    @FXML
    private JFXTextField delayTxt;

    @FXML
    private ChoiceBox<String> delayBox;

    @FXML
    private Button btnConfirm;

    @FXML
    void closeApp(MouseEvent event) {
        FXMLDocumentController.getPrimaryStage().setIconified(false);
        getCurrStage().close();
    }

    @FXML
    void closeBtnChangeHover(MouseEvent event) {
        closeBtn.setImage(new Image("/ezautomator/icons/close-hover.png"));
    }

    @FXML
    void closeBtnChangeLeave(ActionEvent event) {
        closeBtn.setImage(new Image("/ezautomator/icons/close.png"));
    }

    @FXML
    void onBtnConfirmPress(MouseEvent event) {
        if (!delayTxt.getText().isEmpty() && !delayBox.getItems().isEmpty()) {
            switch(delayBox.getSelectionModel().getSelectedIndex()) {
                case 0:
                    // Seconds (Convering to miliseconds)
                    delay = (Integer.parseInt(delayTxt.getText())) * 1000;
                    System.out.println(delay);
                    break;
                case 1:
                    // Minutes (Convering to miliseconds)
                    delay = (Integer.parseInt(delayTxt.getText())) * 1000 * 60;
                    System.out.println(delay);
                    break;
                    
                default:
                    System.out.println("Please select mins/secs");
            }
            
//            delay = Integer.parseInt(delayTxt.getText());
            closeForm();
        } else {
            System.out.println("No delay was set!");
//            AlertController alert = new AlertController();
//            Are you sure you don't need a delay?
//            alert.loadAlert();
//            alert.getAlertCls().onResulgtFocus(getCurrStage());
//            alert.getAlertCls().setMessage("Delay must be greater than 0!");
        }
    }

    private double diffX, diffY;

    private int delay;

    private DelayFormController delayCls;

    private Stage callerStage;
    
    private Stage stageToHide;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDelayBox();

        /**
         * Events for main window Click first, then drag.
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
    }

    private Stage getCurrStage() {
        Stage currStage = (Stage) delayTxt.getScene().getWindow();
        return currStage;
    }

    private void loadDelayBox() {
        delayBox.getItems().addAll("Seconds", "Minutes");
        delayBox.getSelectionModel().selectFirst();
    }

    /**
     * Setting the form that will recieve the focus once the program has closed
     *
     * @param focusedStage
     */
    public void onResultFocus(Stage focusedStage) {
        if (focusedStage != null) {
            callerStage = focusedStage;
        } else {
            // throw exception!
            callerStage = getCurrStage();
        }
    }

    /**
     * Bringing the focus to the desired form
     */
    private void closeForm() {
        //FXMLDocumentController.getPrimaryStage().setIconified(false);
        if (callerStage != null) {
            callerStage.setIconified(false);
        }
        getCurrStage().close();
    }

    /**
     * Loading the alert and returning a reference to the current class
     *
     * @return
     */
    public DelayFormController loadAlert() {
        try {
            FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/delay/DelayForm.fxml"));
            StackPane alertPane = fxmlLoader.load();
            setDelayCls(fxmlLoader.getController());
            Stage delayStage = new Stage();
            delayStage.initStyle(StageStyle.UNDECORATED);
            delayStage.initModality(Modality.APPLICATION_MODAL);
            delayStage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
            delayStage.setTitle("EzAutomator");
            delayStage.setScene(new Scene(alertPane));
            return fxmlLoader.getController();

        } catch (IOException ex) {
            Logger.getLogger(AlertController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Returning the amount of delay the user has chosen
     *
     * @return
     */
    public int getDelay() {
        hideUponLoad();
        getCurrStage().showAndWait();
        return delay;
    }
    
    /**
     * Setting the form we want to minimize before showing the current form
     * @param stageToHide 
     * @return 
     */
    public void setHideUponLoad(Stage stageToHide) {
        if(stageToHide != null) this.stageToHide = stageToHide;
    }
    
    /**
     * Hiding the selected stage on the current form load
     */
    private void hideUponLoad() {
        if(stageToHide != null) stageToHide.setIconified(true);
    }

    /**
     *
     * @param delayCls
     */
    public void setDelayCls(DelayFormController delayCls) {
        /**
         * INCOMPLETE: VALIDATE DELAY WHEN CONTINUE BUTTON IS CLICKED OTHERWISE
         * DELAY WILL ALWAYS REMAIN 0
         */

        if (delayCls != null) {
            this.delayCls = delayCls;
        }
    }

    /**
     * Getting an instance of the alert class
     *
     * @return
     */
    public DelayFormController getDelayCls() {
        return delayCls;
    }

}
