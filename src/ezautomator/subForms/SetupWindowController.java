/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.subForms;

import com.jfoenix.controls.JFXTextField;
import ezautomator.main.Action;
import ezautomator.main.FXMLDocumentController;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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

    private boolean isCapturing;

    private double diffX;

    private double diffY;

    private final ArrayList<Timer> timers = new ArrayList<>();

    private static final ArrayList<Integer> coordinates = new ArrayList<>();
    private static final ArrayList<String> keys = new ArrayList<>();

    @FXML
    void closeApp(MouseEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        FXMLDocumentController.getPrimaryStage().setIconified(false);
        CancelTimers();
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            Logger.getLogger(SetupWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Ensuring the user has captured the desired point on the screen
        if (xValueTxt.getText().isEmpty() && yValueTxt.getText().isEmpty()) {
            FXMLDocumentController.getActionsBox().getItems().clear();
        }

        stage.close();
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
        if (!isCapturing) {
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
        isCapturing = !isCapturing;
    }

    @FXML
    void proceedOnClick(MouseEvent event) {
        // Checking which pane is enabled
        switch (FXMLDocumentController.getPaneID()) {
            case 1:
                System.out.println("PANE 1 is SELECTED");
                // Working with the first pane
                if (!xValueTxt.getText().isEmpty() && !yValueTxt.getText().isEmpty()) {

//                    double tempX = Double.parseDouble(xValueTxt.getText());
//                    double tempY = Double.parseDouble(yValueTxt.getText());
                    int tempX = (int) Integer.parseInt(xValueTxt.getText());
                    int tempY = (int) Integer.parseInt(yValueTxt.getText());

                    ArrayList<Integer> tempCoordinates = new ArrayList(Arrays.asList(tempX, tempY));
                    ArrayList<String> tempKeys = new ArrayList(Arrays.asList());
                    Action tempAction = new Action("Click", "", tempCoordinates, tempKeys, "");
                    FXMLDocumentController.recieveActionType(tempAction);

                    try {
                        GlobalScreen.unregisterNativeHook();
                    } catch (NativeHookException ex) {
                        Logger.getLogger(SetupWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // Close this stage
                    Stage currStage = (Stage) btnProceed.getScene().getWindow();
                    FXMLDocumentController.getPrimaryStage().setIconified(false);
                    currStage.close();
                }
                break;
            case 2:
                System.out.println("PANE 2 is SELECTED");
                // Working with the second pane
                // add more
                break;
            case 3:
                System.out.println("PANE 2 is SELECTED");
                // Working with the third pane
                // add more
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

        // Ensuring the timer has stopped even though the user has closed the program through a different way
        // other than simply clickling the exit button
        FXMLDocumentController.getPrimaryStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    CancelTimers();
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException ex) {
                    Logger.getLogger(SetupWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        enableListener();

    }

    public void startCapCoordinates() {
        // Would you like to capture specific coordinates? (Y/N)
        FXMLDocumentController.getPrimaryStage().setIconified(true);

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

    public static ArrayList<Integer> getClickPoint() {
        if (coordinates.size() > 0) {
            return coordinates;
        } else {
            return null;
        }
    }

    public static ArrayList<String> getKeys() {
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
        Stage currStage = (Stage) btnProceed.getScene().getWindow();
        return currStage;
    }
}
