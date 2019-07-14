/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.alert;

import ezautomator.main.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Abwatts
 */
public class AlertController implements Initializable {

    @FXML
    private Button btnTwo;

    @FXML
    private ImageView closeBtn;

    @FXML
    private Text displayLbl;

    @FXML
    private Button btnOne;

    @FXML
    private StackPane root;

    @FXML
    void closeApp(MouseEvent event) {
        // Close and return false
        result = false;
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
    void onBtnOneMousePressed(MouseEvent event) {
        switch (btnOneID) {
            case 0:
                result = false;
                break;
            case 1:
                result = true;
                break;
        }

        closeForm();
    }

    @FXML
    void onBtnTwoMousePressed(MouseEvent event) {
        switch (btnTwoID) {
            case 0:
                result = false;
                break;
            case 1:
                result = true;
                break;
        }

        closeForm();
    }

    private boolean result;

    private int btnOneID, btnTwoID;

    private double diffX, diffY;

    private Stage callerStage;

    private Stage currStage;

    private AlertController alertCls;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

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

    public void setMessage(String message) {
        if (!message.isEmpty()) {
            displayLbl.setText(message);
            regainFocus();
        }
    }

    public void setBtnOneTxt(String text) {
        if (!text.isEmpty()) {
            btnOne.setText(text);
            regainFocus();
        }
    }

    public void setBtnTwoTxt(String text) {
        if (!text.isEmpty()) {
            btnTwo.setText(text);
            regainFocus();
        }
    }

    private Stage getCurrStage() {
        Stage currStage = (Stage) root.getScene().getWindow();
        return currStage;
    }

    public void setYesNo(int btnOne, int btnTwo) {
            btnOneID = btnOne;
            btnTwoID = btnTwo;
            regainFocus();
    }

    public void onResultFocus(Stage focusedStage) {
        if (focusedStage != null) {
            callerStage = focusedStage;
        } else {
            // throw exception!
        }
    }

    private void closeForm() {
        //FXMLDocumentController.getPrimaryStage().setIconified(false);
        if (callerStage != null) {
            callerStage.setIconified(false);
        }
        getCurrStage().close();
    }

    private void regainFocus() {
        if (!getCurrStage().isFocused()) {
            getCurrStage().setIconified(false);
        }
    }

    public AlertController loadAlert() {
        try {
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
//            alertStage.show();
            currStage = alertStage;
            return fxmlLoader.getController();

        } catch (IOException ex) {
            Logger.getLogger(AlertController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * return on window closed
     *
     * @return
     */
    public boolean getResult() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.showAndWait();
        return result;
    }

    public void setAlertCls(AlertController alertCls) {
        if (alertCls != null) {
            this.alertCls = alertCls;
        }
    }

    public AlertController getAlertCls() {
        return alertCls;
    }
}
