/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.subForms;

import com.jfoenix.controls.JFXTextField;
import ezautomator.alert.AlertController;
import ezautomator.confirmation.ConfirmationControllerSetup;
import ezautomator.main.Action;
import ezautomator.main.EzAutomator;
import ezautomator.main.FXMLDocumentController;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * FXML Controller class
 *
 * @author gornicma
 */
public class SetupWindowController implements Initializable {

    @FXML
    private Button captureBtn;

    @FXML
    private Button btnProceed;

    @FXML
    private ImageView closeBtn;

    @FXML
    private JFXTextField xValueTxt;

    @FXML
    private Button capKeysBtn;

    @FXML
    private JFXTextField fKeyTxt;

    @FXML
    private JFXTextField sKeyTxt;

    @FXML
    private JFXTextField yValueTxt;

    @FXML
    private Pane paneClick;

    @FXML
    private StackPane root;

    @FXML
    private Pane paneKeys;

    private boolean isCapPoint;

    private boolean isCapKeys;

    private double diffX;

    private double diffY;

    private int keyOne, keyTwo;

    private Action tempAction;

    private Stage tStage;

    private final ArrayList<Timer> timers = new ArrayList<>();

    private SetupWindowController setupCls;

    private static final ArrayList<Integer> coordinates = new ArrayList<>();
    private static final ArrayList<Integer> keys = new ArrayList<>();

    @FXML
    void closeApp(MouseEvent event) {
        EzAutomator.getMainStage().setIconified(false);
        CancelTimers();

        // Ensuring the user truly wants to add a confirmation window to the script
        FXMLDocumentController.getActionsBox().getItems().clear();

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
    void captureCoordinates(MouseEvent event) {
        if (!isCapPoint) {
            // [ADD] Create a getter for the current stage for this class
            Stage currStage = (Stage) btnProceed.getScene().getWindow();
            currStage.setIconified(true);
            startCapCoordinates();
        } else {
            closeBtn.requestFocus();
            xValueTxt.setText("");
            yValueTxt.setText("");
            CancelTimers();
        }
        isCapPoint = !isCapPoint;
    }

    @FXML
    void captureKeys(MouseEvent event) {
        if (fKeyTxt.getText().isEmpty()) {
            fKeyTxt.requestFocus();
        }
        isCapKeys = !isCapKeys;

        if (!isCapKeys) {
            capKeysBtn.setText("Capture Keys");
            root.requestFocus();
        } else {
            capKeysBtn.setText("Capturing...");
        }
    }

    @FXML
    void proceedOnClick(MouseEvent event) {
        // Checking which pane is enabled
        switch (FXMLDocumentController.getPaneID()) {
            case 1:
                System.out.println("PANE 1 is SELECTED");
                // Working with the first pane
                if (!xValueTxt.getText().isEmpty() && !yValueTxt.getText().isEmpty()) {
                    char actionType = 'C';
                    String action = "Click";

                    // Checking what type the action is
                    boolean result = new AlertController().loadAlert().showDialog("Click", "Hover", "What click action would you like to add?",
                            "exclamation", getSubStage(), getSubStage(), 0.5);
                    if (!result) {
                        actionType = 'H';
                        action = "Hover";
                    } else {
                        // Checking how many times to click
                        boolean tChoice = new AlertController().loadAlert().showDialog("Once", "Twice", "How many times would you like to click?",
                                "exclamation", getSubStage(), getSubStage(), 0.5);
                        if (!tChoice) {
                            action += " x2";
                        }
                    }

                    int tempX = (int) Integer.parseInt(xValueTxt.getText());
                    int tempY = (int) Integer.parseInt(yValueTxt.getText());

//                    ArrayList<Integer> tempCoordinates = new ArrayList(Arrays.asList(tempX, tempY));
//                    ArrayList<String> tempKeys = new ArrayList(Arrays.asList());
                    tempAction = new Action(action, "", new ArrayList<>(Arrays.asList(tempX, tempY)), new ArrayList<>(Arrays.asList()), "", actionType);
                    FXMLDocumentController.recieveActionType(tempAction);

                    // Close this stage
                    EzAutomator.getMainStage().setIconified(false);

                    closeForm();
                }
                break;
            case 2:
                System.out.println("PANE 2 is SELECTED");
                // Working with the second pane
                if (!fKeyTxt.getText().isEmpty() || !sKeyTxt.getText().isEmpty()) {
                    ArrayList<Integer> keyList = new ArrayList<>(Arrays.asList());

                    if (keyOne > 0) {
                        keyList.add(keyOne);
                    }

                    if (keyTwo > 0) {
                        keyList.add(keyTwo);
                    }

                    tempAction = new Action("Keys", "", new ArrayList<>(Arrays.asList("")), keyList, "", 'E');
                    FXMLDocumentController.recieveActionType(tempAction);

                    // Close this stage
                    EzAutomator.getMainStage().setIconified(false);
                    closeForm();
                }
                break;
            case 3:
                System.out.println("PANE 3 is SELECTED");
                // Working with the third pane
                closeForm();
                break;
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (FXMLDocumentController.getPaneID() == 1) {
            paneKeys.setDisable(true);
        } else {
            paneClick.setDisable(true);
        }

        /**
         * Form movement implementation
         */
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) root.getScene().getWindow();
                // Calculating the difference between the cursor and the stage to later add it to allow the cursor to stay at the same
                // position without teleporting it to a different place
                diffX = stage.getX() - event.getScreenX();
                diffY = stage.getY() - event.getScreenY();
            }
        });

        // Ensuring that the stage moves a fixed number (that we calculated when the user first clicked the stage) of spaces
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) root.getScene().getWindow();
                // Helps my cursor stay at the same position when dragging the window instead of teleporting to the top most-left corner
                stage.setX(event.getScreenX() + diffX);
                stage.setY(event.getScreenY() + diffY);
            }
        });

        // Enabling key listening upon form load
        enableListener();
    }

    public void startCapCoordinates() {
        // Would you like to capture specific coordinates? (Y/N)
        EzAutomator.getMainStage().setIconified(true);

        Timer ptTimer = new Timer();
        timers.add(ptTimer);
        ptTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                PointerInfo mouseCursor = MouseInfo.getPointerInfo();
                Point screenPoint = mouseCursor.getLocation();
                xValueTxt.setText(String.valueOf(screenPoint.getX()).replace(".0", ""));
                yValueTxt.setText(String.valueOf(screenPoint.getY()).replace(".0", ""));
            }
        }, 0, 250);
    }

    /**
     *
     * @param timers
     */
    private void CancelTimers() {
        timers.forEach((Timer timer) -> {
            timer.cancel();
        });

        timers.clear();
    }

    /**
     *
     */
    private void closeForm() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            Logger.getLogger(SetupWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (tStage != null) {
            tStage.setOpacity(1);
        }

        getSubStage().close();
    }

    public static ArrayList<Integer> getClickPoint() {
        if (coordinates.size() > 0) {
            return coordinates;
        } else {
            return null;
        }
    }

    public static ArrayList<Integer> getKeys() {
        if (keys.size() > 0) {
            return keys;
        } else {
            return null;
        }
    }

    public void enableListener() {
        NativeKeyListener keyListener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent nke) {

                if (FXMLDocumentController.getPaneID() == 1) {
                    switch (nke.getRawCode()) {
                        case KeyEvent.VK_F1: {
                            // Allowing the user to continue capturing coordinates
                            // on screen
                            try {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Minimizing this form
                                        getSubStage().setIconified(true);
                                        startCapCoordinates();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                        case KeyEvent.VK_F2:
                            // Disabling coordinates screen capture when F2 is pressed
                            CancelTimers();

                            // Bringing back-up the form (Running on a seperated thread)
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    getSubStage().setIconified(false);
                                }
                            });
                            break;
                    }
                } else if (FXMLDocumentController.getPaneID() == 2 && isCapKeys) {
                    String keyPressed = EzAutomator.getKeyTextRep(nke.getRawCode());

                    if (fKeyTxt.isFocused()) {
                        fKeyTxt.setText(keyPressed);
                        keyOne = nke.getRawCode();
                    } else {
                        sKeyTxt.setText(keyPressed);
                        keyTwo = nke.getRawCode();
                    }
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

    public Stage getSubStage() {
        Stage subStage = (Stage) btnProceed.getScene().getWindow();
        return subStage;
    }

    /**
     * Loading the alert and returning a reference to the current class
     *
     * @return
     */
    public SetupWindowController loadForm() {
        try {
            FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/subForms/SetupWindow.FXML"));
            StackPane confirmPane = fxmlLoader.load();
            setSetupWindowCls(fxmlLoader.getController());
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

    public Action showClickForm(ArrayList<Integer> coordinates, Stage tStage, double opValue) {
        autoFill(coordinates);
        this.tStage = tStage;
        hideUponLoad(tStage, opValue);
        closeBtn.requestFocus();
        getSubStage().showAndWait();
        return tempAction;
    }

    public Action showKeyForm(ArrayList<Integer> keyList, Stage tStage, double opValue) {
        autoFill(keyList);
        this.tStage = tStage;
        hideUponLoad(tStage, opValue);
        closeBtn.requestFocus();
        getSubStage().showAndWait();
        return tempAction;
    }

    public Action showForm(Stage tStage, double opValue) {
        this.tStage = tStage;
        hideUponLoad(tStage, opValue);
        closeBtn.requestFocus();
        getSubStage().showAndWait();
        return tempAction;
    }

    /**
     *
     * @param setupCls
     */
    public void setSetupWindowCls(SetupWindowController setupCls) {
        /**
         * INCOMPLETE: VALIDATE DELAY WHEN CONTINUE BUTTON IS CLICKED OTHERWISE
         * DELAY WILL ALWAYS REMAIN 0
         */
        if (setupCls != null) {
            this.setupCls = setupCls;
        }
    }

    private void autoFill(ArrayList givenList) {
        switch (FXMLDocumentController.getPaneID()) {
            // Click
            case 1:
                xValueTxt.setText(String.valueOf(givenList.get(0)));
                yValueTxt.setText(String.valueOf(givenList.get(1)));

                break;

            // Send
            case 2:
                if (givenList.size() == 2) {
                    fKeyTxt.setText(EzAutomator.getKeyTextRep((int) givenList.get(0)));
                    sKeyTxt.setText(EzAutomator.getKeyTextRep((int) givenList.get(1)));
                } else {
                    fKeyTxt.setText(EzAutomator.getKeyTextRep((int) givenList.get(0)));
                }

                break;

            case 3:

                break;
        }
    }

    /**
     *
     * @param tStage
     * @param opValue
     */
    private void hideUponLoad(Stage tStage, double opValue) {
        if (tStage != null) {
            tStage.setOpacity(opValue);
        }
    }
}
