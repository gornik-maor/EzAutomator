/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.alert;

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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private StackPane msgPane;

    @FXML
    private ImageView icon;

    @FXML
    void closeApp(MouseEvent event) {
        // Close and return false
//        result = false;
        hasClosed = true;
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

    private boolean result, hasClosed;

    private int btnOneID = 1;
    private int btnTwoID = 0;

    private double diffX, diffY;

    private Stage callerStage, stageToHide, tStage;

    private AlertController alertCls;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

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
    }

    /**
     * Setting the alert message
     *
     * @param message
     */
    public void setMessage(String message) {
        if (!message.isEmpty()) {
            displayLbl.setText(message);
            regainFocus();
        }
    }

    /**
     * Setting the first button text
     *
     * @param text
     */
    public void setBtnOneTxt(String text) {
        if (!text.isEmpty()) {
            btnOne.setText(text);
            regainFocus();
        }
    }

    /**
     * Setting the second button text
     *
     * @param text
     */
    public void setBtnTwoTxt(String text) {
        if (!text.isEmpty()) {
            btnTwo.setText(text);
            regainFocus();
        }
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
     * Setting the representation of each button
     *
     * @param btnOne can either represent true or false
     * @param btnTwo can either represent true or false
     */
    public void setYesNo(int btnOne, int btnTwo) {
        btnOneID = btnOne;
        btnTwoID = btnTwo;
        regainFocus();
    }

    /**
     * Setting the form that will receive the focus once the program has closed
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
        //EzAutomator.getMainStage().setIconified(false);
        if (callerStage != null) {
            callerStage.setIconified(false);
        }

        if (tStage != null) {
            tStage.setOpacity(1);
        }
        getCurrStage().close();
    }

    /**
     * Regaining focus to the alert once a component was modified
     */
    private void regainFocus() {
        if (!getCurrStage().isFocused()) {
            getCurrStage().setIconified(false);
        }
    }

    /**
     * Loading the alert and returning a reference to the current class
     *
     * @return
     */
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
            return fxmlLoader.getController();

        } catch (IOException ex) {
            Logger.getLogger(AlertController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Displaying the alert and waiting for the user to select a result
     *
     * @return
     */
    public boolean getResult() {
        hideUponLoad();
        displayLbl.requestFocus();
        getCurrStage().showAndWait();
        return result;
    }

    /**
     * Returns whether the user has closed out of the alert willingly
     *
     * @return
     */
    public boolean getStatus() {
        return hasClosed;
    }

    /**
     * Setting the form we want to minimize before showing the current form
     *
     * @param stageToHide
     * @return
     */
    public void setHideUponLoad(Stage stageToHide) {
        if (stageToHide != null) {
            this.stageToHide = stageToHide;
        }
    }

    /**
     * Hiding the selected stage on the current form load
     */
    private void hideUponLoad() {
        if (stageToHide != null) {
            stageToHide.setIconified(true);
        }
    }

    /**
     * Setting the alert icon image
     *
     * @param type
     */
    public void setIcon(String type) {
        switch (type.toLowerCase()) {
            case "exclamation":
                icon.setImage(new Image("/ezautomator/icons/exclamation.png"));
                break;
            case "warning":
                icon.setImage(new Image("/ezautomator/icons/warning.png"));
                break;

            case "error":
                icon.setImage(new Image("/ezautomator/icons/error.png"));
                break;
        }
    }

    /**
     *
     * @param btnOneTxt Text for first button
     * @param btnTwoTxt Text for second button
     * @param message Text for the alert
     * @param icon Icon for the alert
     * @param hideFrom Stage to hide from before loading alert
     * @param focusAfter Stage to focus on upon closing the alert
     * @return User's response
     */
    public boolean showDialog(String btnOneTxt, String btnTwoTxt, String message, String icon, Stage hideFrom, Stage focusAfter) {
        this.setBtnOneTxt(btnOneTxt);
        this.setBtnTwoTxt(btnTwoTxt);
        this.setMessage(message);
        this.setIcon(icon);
        stageToHide = hideFrom;
        callerStage = focusAfter;
        hideUponLoad();
        displayLbl.requestFocus();
        getCurrStage().showAndWait();
        return result;
    }

    /**
     *
     * @param btnOneTxt Text for first button
     * @param btnTwoTxt Text for second button
     * @param message Text for the alert
     * @param icon Icon for the alert
     * @param focusAfter Stage to focus on upon closing the alert
     * @return User's response
     * @param tStage Target stage to change the opacity of
     * @param opValue Opacity value to set
     * @return
     */
    public boolean showDialog(String btnOneTxt, String btnTwoTxt, String message, String icon, Stage focusAfter, Stage tStage, double opValue) {
        this.setBtnOneTxt(btnOneTxt);
        this.setBtnTwoTxt(btnTwoTxt);
        this.setMessage(message);
        this.setIcon(icon);
        this.tStage = tStage;
        setTargetOpacity(opValue);
        callerStage = focusAfter;
        hideUponLoad();
        displayLbl.requestFocus();
        getCurrStage().showAndWait();
        return result;
    }

    /**
     * Setting the alert message font size
     *
     * @param size The font size
     */
    public void setFontSize(double size) {
        if (size > 0) {
            displayLbl.setFont(new Font(size));
        }
    }

    public void setTargetOpacity(double opacity) {
        if (tStage != null) {
            tStage.setOpacity(opacity);
        }
    }

    /**
     * Setting the instance of the alert class
     *
     * @param alertCls
     */
    public void setAlertCls(AlertController alertCls) {
        if (alertCls != null) {
            this.alertCls = alertCls;
        }
    }

    /**
     * Getting an instance of the alert class
     *
     * @return
     */
    public AlertController getAlertCls() {
        return alertCls;
    }
}
