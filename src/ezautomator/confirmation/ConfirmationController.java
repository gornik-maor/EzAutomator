/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.confirmation;

import ezautomator.main.EzAutomator;
import ezautomator.main.ScriptExecutor;
import ezautomator.subForms.SetupWindowController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author gornicma
 */
public class ConfirmationController implements Initializable {

    @FXML
    private Button btnStop;

    @FXML
    private Button btnContinue;

    @FXML
    private ImageView closeBtn;

    @FXML
    private StackPane root;

    @FXML
    private Text displayLbl;

    @FXML
    private StackPane msgPane;

    @FXML
    void closeApp(MouseEvent event) {
        // Close and return false
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
        keyResult = true;
        closeForm();
    }

    @FXML
    void onBtnStopPress(MouseEvent event) {
        keyResult = false;
        ScriptExecutor.stop();
        closeForm();
    }

    private double diffX, diffY;

    private boolean keyResult;

    private Stage callerStage, stageToHide, tStage;

    private int keyConfirmation, keyStop;

    private ConfirmationController confirmCls;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("running in the background :SSS");
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

        enableListener();
    }

    public void enableListener() {
        NativeKeyListener keyListener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent nke) {

            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent nke) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if (nke.getRawCode() == keyConfirmation && getCurrStage().isShowing()) {
                    keyResult = true;

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            closeForm();
                        }
                    });

                } else if (nke.getRawCode() == keyStop && getCurrStage().isShowing()) {
                    keyResult = false;
                    ScriptExecutor.stop();
                    System.out.println("have exectued!");

                    // problem here --> this line gets executed more than once
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            closeForm();
                        }
                    });
                }
            }

            @Override
            public void nativeKeyTyped(NativeKeyEvent nke) {

            }
        };

            try {
                GlobalScreen.registerNativeHook();
                GlobalScreen.addNativeKeyListener(keyListener);

            } catch (NativeHookException ex) {
                Logger.getLogger(SetupWindowController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
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
     * Setting the first button execution key
     *
     * @param keyText
     */
    public void setBtnOneKey(String keyText) {
        if (!keyText.isEmpty()) {
            btnContinue.setText("Continue (" + keyText + ")");
            regainFocus();
        }
    }

    /**
     *
     * @param keyText
     */
    public void setBtnTwoKey(String keyText) {
        if (!keyText.isEmpty()) {
            btnStop.setText("Stop (" + keyText + ")");
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

        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            Logger.getLogger(ConfirmationController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        getCurrStage().close();
    }

    public boolean showConfirmation(int keyConfirmation, int keyStop, String message, Stage hideFrom, Stage focusAfter) {
        if (keyConfirmation > 0) {
            this.keyConfirmation = keyConfirmation;
            setBtnOneKey(EzAutomator.getKeyTextRep(this.keyConfirmation));
        }
        if (keyStop > 0) {
            this.keyStop = keyStop;
            setBtnTwoKey(EzAutomator.getKeyTextRep(this.keyStop));
        }
        setMessage(message);
        stageToHide = hideFrom;
        callerStage = focusAfter;
        hideUponLoad();
        displayLbl.requestFocus();
        return getResult();
    }

    public boolean showConfirmation(int keyConfirmation, int keyStop, String message, Stage focusAfter, Stage tStage, double opValue) {
        if (keyConfirmation > 0) {
            this.keyConfirmation = keyConfirmation;
            setBtnOneKey(EzAutomator.getKeyTextRep(this.keyConfirmation));
        }
        if (keyStop > 0) {
            this.keyStop = keyStop;
            setBtnTwoKey(EzAutomator.getKeyTextRep(this.keyStop));
        }
        setMessage(message);
        this.tStage = tStage;
        setTargetOpacity(opValue);
        callerStage = focusAfter;
        hideUponLoad();
        displayLbl.requestFocus();
        return getResult();
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
    public ConfirmationController loadAlert() {
        try {
            FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/confirmation/ConfirmationForm.fxml"));
            StackPane confirmPane = fxmlLoader.load();
            setConfirmCls(fxmlLoader.getController());
            Stage alertStage = new Stage();
            alertStage.initStyle(StageStyle.UNDECORATED);
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
            alertStage.setTitle("EzAutomator");
            alertStage.setScene(new Scene(confirmPane));
            return fxmlLoader.getController();

        } catch (IOException ex) {
            Logger.getLogger(ConfirmationController.class
                    .getName()).log(Level.SEVERE, null, ex);
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
        getCurrStage().requestFocus();
        getCurrStage().showAndWait();
        return keyResult;
    }

    /**
     *
     * @param stageToHide
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
     *
     * @param confirmCls
     */
    public void setConfirmCls(ConfirmationController confirmCls) {
        if (confirmCls != null) {
            this.confirmCls = confirmCls;
        }
    }

    /**
     * Getting an instance of the alert class
     *
     * @return
     */
    public ConfirmationController getClss() {
        return confirmCls;
    }
}
