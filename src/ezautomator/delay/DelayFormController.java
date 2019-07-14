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
        if (delay != 0) {
            FXMLDocumentController.recieveDelay(delay);
        } else {
            AlertController alert = new AlertController();
//            alert.loadAlert();
//            alert.getAlertCls().onResultFocus(getCurrStage());
//            alert.getAlertCls().setMessage("Delay must be greater than 0!");
        }
    }

    private double diffX, diffY;

    private int delay;
    
    private DelayFormController delayCls;

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
    }
    
    public void loadDelay() {
        try {
         FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/delay/DelayForm.fxml"));
            StackPane alertPane = fxmlLoader.load();
            setDelayCls(fxmlLoader.getController());
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
    
    public void setDelayCls(DelayFormController delayCls) {
        if(delayCls != null) this.delayCls = delayCls;
    }
    
    public DelayFormController getDelayCls() {
        return delayCls;
    }

}
