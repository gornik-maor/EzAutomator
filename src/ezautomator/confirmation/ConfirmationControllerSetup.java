/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.confirmation;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import ezautomator.main.FXMLDocumentController;
import ezautomator.subForms.SetupWindowController;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * FXML Controller class
 *
 * @author gornicma
 */
public class ConfirmationControllerSetup implements Initializable {

    @FXML
    private JFXTextField confirmationTxt;

    @FXML
    private ImageView closeBtn;

    @FXML
    private StackPane root;

    @FXML
    private Button btnConfirm;

    @FXML
    private JFXToggleButton toggleInfo;

    @FXML
    private Button btnKContinue;

    @FXML
    private Button btnKTerminate;

    @FXML
    private JFXTextField continueTxt;

    @FXML
    private JFXTextField terminateTxt;

    @FXML
    void closeApp(MouseEvent event) {
        try {
            GlobalScreen.unregisterNativeHook();
            closeForm();
            getCurrStage().close();
        } catch (NativeHookException ex) {
            Logger.getLogger(ConfirmationControllerSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        if (!keyContinue.isEmpty() && !keyTerminate.isEmpty() && !confirmationTxt.getText().isEmpty() && !keyContinue.equals(keyTerminate)) {
            confirmationInfo = new ArrayList(Arrays.asList(keyContinue, keyTerminate, confirmationTxt.getText()));
            closeForm();
        }
    }

    @FXML
    void onBtnCapOne(MouseEvent event) {
        isCapConfirm = !isCapConfirm;

        if (isCapConfirm) {
            btnKContinue.setText("Capturing...");
            btnKTerminate.setText("Capture");
            isCapTerminate = false;
        } else {
            btnKContinue.setText("Capture");
        }
    }

    @FXML
    void onBtnCapTwo(MouseEvent event) {
        isCapTerminate = !isCapTerminate;
        if (isCapTerminate) {
            btnKTerminate.setText("Capturing...");
            btnKContinue.setText("Capture");
            isCapConfirm = false;
        } else {
            btnKTerminate.setText("Capture");
        }
    }

    private double diffX, diffY;

    private ConfirmationControllerSetup confirmationCls;

    private Stage callerStage, stageToHide;

    private String cMessage;

    private ArrayList<String> confirmationInfo;

    private String keyContinue, keyTerminate;

    private boolean isCapConfirm, isCapTerminate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        enableListener();
    }

    public void enableListener() {
        NativeKeyListener keyListener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent nke) {
                if (isCapConfirm) {
                    keyContinue = KeyEvent.getKeyText(nke.getRawCode());
                    continueTxt.setText(keyContinue);

                } else if (isCapTerminate) {
                    keyTerminate = KeyEvent.getKeyText(nke.getRawCode());
                    terminateTxt.setText(keyTerminate);
                }
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent nke) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void nativeKeyTyped(NativeKeyEvent nke) {

            }
        };

        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(keyListener);
        } catch (NativeHookException ex) {
            Logger.getLogger(SetupWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Stage getCurrStage() {
        Stage currStage = (Stage) closeBtn.getScene().getWindow();
        return currStage;
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
        try {
            if (callerStage != null) {
                callerStage.setIconified(false);
            }
            GlobalScreen.unregisterNativeHook();
            getCurrStage().close();
        } catch (NativeHookException ex) {
            Logger.getLogger(ConfirmationControllerSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Loading the alert and returning a reference to the current class
     *
     * @return
     */
    public ConfirmationControllerSetup loadForm() {
        try {
            FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/confirmation/ConfirmationSetup.FXML"));
            StackPane confirmPane = fxmlLoader.load();
            setConfirmationCls(fxmlLoader.getController());
            Stage confirmStage = new Stage();
            confirmStage.initStyle(StageStyle.UNDECORATED);
            confirmStage.initModality(Modality.APPLICATION_MODAL);
            confirmStage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
            confirmStage.setTitle("EzAutomator");
            confirmStage.setScene(new Scene(confirmPane));
            return fxmlLoader.getController();

        } catch (IOException ex) {
            Logger.getLogger(ConfirmationControllerSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Returning the amount of delay the user has chosen
     *
     * @return
     */
    public ArrayList getConfirmationInfo() {
        hideUponLoad();
        getCurrStage().showAndWait();
        System.out.println(confirmationInfo);
        return confirmationInfo;
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
     *
     * @param confirmationCls
     */
    public void setConfirmationCls(ConfirmationControllerSetup confirmationCls) {
        /**
         * INCOMPLETE: VALIDATE DELAY WHEN CONTINUE BUTTON IS CLICKED OTHERWISE
         * DELAY WILL ALWAYS REMAIN 0
         */

        if (confirmationCls != null) {
            this.confirmationCls = confirmationCls;
        }
    }

    /**
     * Getting an instance of the alert class
     *
     * @return
     */
    public ConfirmationControllerSetup getConfirmationCls() {
        return confirmationCls;
    }
}
