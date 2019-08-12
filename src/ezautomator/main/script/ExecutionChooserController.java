/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.main.script;

import com.jfoenix.controls.JFXTextField;
import ezautomator.alert.AlertController;
import ezautomator.main.EzAutomator;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Abwatts
 */
public class ExecutionChooserController implements Initializable {

    @FXML
    private ImageView closeBtn;

    @FXML
    private StackPane root;

    @FXML
    private JFXTextField numTxt;

    @FXML
    private Button btnConfirm;

    @FXML
    private ImageView icon;

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
    void onBtnConfirmPressed(MouseEvent event) {
        try {
            numExecs = Integer.parseInt(numTxt.getText());
            if (numExecs > 0) {
                closeForm();
            } else {
                new AlertController().loadAlert().showDialog("Ok", "Cancel", "Number can not be negative or 0!",
                        "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
                numTxt.setText("");
            }
        } catch (NumberFormatException e) {
            if (!numTxt.getText().isEmpty()) {
                AlertController alert = new AlertController().loadAlert();
                alert.setFontSize(17.5);
                alert.showDialog("Ok", "Cancel", "Input must be a number and smaller than " + Integer.MAX_VALUE + "!",
                        "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
            } else {
                new AlertController().loadAlert().showDialog("Ok", "Cancel", "Textfield can not be left blank!",
                        "error", EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
            }
            tStage.setOpacity(0.5);
            numTxt.setText("");
        }
    }

    private double diffX, diffY;
    private int numExecs;
    private Stage tStage;
    private ExecutionChooserController executionCls;

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
     * Retrieving the displayed stage instance
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
    public ExecutionChooserController LoadForm() {
        try {
            FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/main/script/ExecutionChooser.fxml"));
            StackPane alertPane = fxmlLoader.load();
            setExecutionCls(fxmlLoader.getController());
            Stage executionStage = new Stage();
            executionStage.initStyle(StageStyle.UNDECORATED);
            executionStage.initModality(Modality.APPLICATION_MODAL);
            executionStage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
            executionStage.setTitle("EzAutomator");
            executionStage.setScene(new Scene(alertPane));
            return fxmlLoader.getController();

        } catch (IOException ex) {
            Logger.getLogger(ExecutionChooserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Setting the instance of the alert class
     *
     * @param executionCls
     */
    public void setExecutionCls(ExecutionChooserController executionCls) {
        if (executionCls != null) {
            this.executionCls = executionCls;
        }
    }

    /**
     * Getting an instance of the alert class
     *
     * @return
     */
    public ExecutionChooserController getExecutionCls() {
        return executionCls;
    }

    /**
     * Displaying the form and waiting for the user to enter a number
     *
     * @return
     */
    public int showDialog(Stage tStage) {
        closeBtn.requestFocus();
        blurUponLoad(tStage);
        getCurrStage().showAndWait();
        return numExecs;
    }

    /**
     * Blurs the form before it loads up
     */
    private void blurUponLoad(Stage tStage) {
        if (tStage != null) {
            this.tStage = tStage;
            tStage.setOpacity(0.5);
        }
    }

    private void closeForm() {
        tStage.setOpacity(1);
        getCurrStage().close();
    }

}
