/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.main.script;

import ezautomator.confirmation.ConfirmationController;
import ezautomator.main.Action;
import ezautomator.main.EzAutomator;
import ezautomator.main.FXMLDocumentController;
import ezautomator.subForms.SetupWindowController;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author gornicma
 */
public class ScriptExecutor implements Runnable {

    private static volatile boolean canceled;
    private boolean isListening;
    private TableView<Action> actionTable;
    private Thread scriptThread;
    private Runnable runnable;
    private Robot sRobot;
    private static int numExcs = 1;

    /**
     * Constructor for instances of type ScriptExecutor
     *
     * @param actionTable
     */
    public ScriptExecutor(TableView<Action> actionTable) {
        try {
            sRobot = new Robot();

            if (!actionTable.getItems().isEmpty()) {
                this.actionTable = actionTable;
            } else {
                // Action table is empty! {THIS MUST BE HANDLED}
            }

            scriptThread = new Thread(runnable, "Script Thread");
        } catch (AWTException ex) {
            Logger.getLogger(ScriptExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This is the first method that needs to be called
     *
     * @param actionTable
     */
    public void load(TableView<Action> actionTable) {
        if (!actionTable.getItems().isEmpty()) {
            this.actionTable = actionTable;
        } else {
            // Action table is empty! {THIS MUST BE HANDLED}
        }
    }

    public static void stop() {
        System.out.println("Stopping..");
        canceled = true;
    }

    public void waitForResponse() {

    }

    private void mousePress(ArrayList<Integer> coordinates, int numOfClicks, int delay) {
        sRobot.mouseMove(coordinates.get(0), coordinates.get(1));

        for (int i = 0; i < numOfClicks; i++) {
            sRobot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            synchronized (scriptThread) {
                try {
                    scriptThread.wait(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ScriptExecutor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            sRobot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        }

        setDelay(delay);
    }

    private void sendKeys(int keyOne, int keyTwo, int delay) {
        keyOne = (keyOne == 162) ? 17 : keyOne;
        keyTwo = (keyTwo == 162) ? 17 : keyTwo;

        // Holding the selected keys
        sRobot.keyPress(keyOne);
        sRobot.keyPress(keyTwo);

        // Releasing the selected keys
        sRobot.keyRelease(keyOne);
        sRobot.keyRelease(keyTwo);

        setDelay(delay);
    }

    private void sendKeys(int keyOne, int delay) {
        // Performing small adjustions to match the keycode given by
        // the native keyboard listener and the KeyEvent class
        // to satisfy the robot class requirements
        keyOne = (keyOne == 162) ? 17 : keyOne;
        keyOne = (keyOne == 13) ? 10 : keyOne;

        // Holding and then releasing the selected key 
        sRobot.keyPress(keyOne);
        sRobot.keyRelease(keyOne);

        setDelay(delay);
    }

    public void setDelay(int delay) {
        // Adding a delay if the user chooses to do so
        if (delay > 0) {
            synchronized (scriptThread) {
                try {
                    scriptThread.wait(delay);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ScriptExecutor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * The number of executions of the script
     *
     * @param numExecsIn
     */
    public static void setExecs(int numExecsIn) {
        numExcs = numExecsIn;
    }

    private void dispNotification(int choice) {
        Image nImg = new Image("/ezautomator/icons/checkmark.png");
        String mTxt = "", title = "";

        switch (choice) {
            case 0:
                nImg = new Image("/ezautomator/icons/canceled.png");
                mTxt = "All actions were stopped.";
                title = "Execution Terminated";
                break;

            case 1:
                String numExecsStr = (numExcs > 1) ? " " + numExcs + " times." : ".";

                mTxt = "(" + actionTable.getItems().size() + ") Actions were executed successfully" + numExecsStr;
                title = "Execution Complete";
                break;
        }

        Notifications messageBuilder = Notifications.create()
                .title(title)
                .text(mTxt)
                .graphic(new ImageView(nImg))
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);

        messageBuilder.darkStyle();
        messageBuilder.show();
    }

    @Override
    public void run() {
        canceled = false;
        if(!isListening) enableListener();
        System.out.println("running...");
        int executions = 0;
        actionTable.getSelectionModel().select(0);
        while (executions < numExcs) {
            for (int i = 0; i < actionTable.getItems().size(); i++) {
                if (!canceled) {
                    // Parsing the assigned action delay and storing in a variable
                    Action tempAction = actionTable.getItems().get(i);
                    int delay = Integer.parseInt(tempAction.getDelay().replace(" m/s", ""));

                    // Selecting the current action that is currently being executed
                    actionTable.getSelectionModel().select(i);

                    switch (tempAction.getAction()) {
                        case "Click":
                            mousePress(tempAction.getCoordinates(), 1, delay);
                            break;

                        case "Click x2":
                            mousePress(tempAction.getCoordinates(), 2, delay);
                            break;

                        case "Hover":
                            mousePress(tempAction.getCoordinates(), 0, delay);
                            break;

                        case "Keys":
                            if (tempAction.getSendKeys().size() == 2) {
                                sendKeys(tempAction.getSendKeys().get(0), tempAction.getSendKeys().get(1), delay);
                            } else {
                                sendKeys(tempAction.getSendKeys().get(0), delay);
                            }

                            break;

                        case "Confirmation":
                            new ConfirmationController().loadAlert().showConfirmation(tempAction.getSendKeys().get(0), tempAction.getSendKeys().get(1),
                                    tempAction.getMessage(), EzAutomator.getMainStage(), EzAutomator.getMainStage(), 0.5);
                            setDelay(delay);
                            break;

                        default:
                            System.out.println("Ignoring!");
                            break;
                    }
                } else {
                    // Breaking out of while loop 
                    executions = numExcs;
                    // Displaying script status notification
                    dispNotification(0);
                    break;
                }
            }

            // Displaying script status notification
            if (!canceled) {
                // All actions were successfully executed
                executions++;
            }
        }

        if ((executions >= numExcs) && !canceled) {
            dispNotification(1);
        }

        // Display message in the top right corner of the screen informing the user the script has been successfully fnished.
        // Handling all processes and closing them
        EzAutomator.getMainStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                scriptThread.interrupt();
            }
        });
    }

    public void enableListener() {
        NativeKeyListener keyListener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent nke) {
                switch (nke.getRawCode()) {
                    case KeyEvent.VK_F5:
                        canceled = true;
                        break;
                        
                    case KeyEvent.VK_F8:
                        System.exit(1);
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
        isListening = true;
    }
}
