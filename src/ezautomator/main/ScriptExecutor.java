/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.stage.WindowEvent;

/**
 *
 * @author gornicma
 */
public class ScriptExecutor implements Runnable {

    TableView<Action> actionTable;
    Thread scriptThread;
    Timer sTimer;
    Runnable runnable;
    Robot sRobot;

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

    public void stop() {

    }

    public void delay() {

    }

    public void waitForResponse() {

    }

    private void mousePress(ArrayList<Integer> coordinates, int numOfClicks, int delay) {
        sRobot.mouseMove(coordinates.get(0), coordinates.get(1));
        for (int i = 1; i <= numOfClicks; i++) {
            sRobot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        }

        sTimer = new Timer();
        sTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("timer is running...");
                sRobot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
        }, 100);

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

    private void sendKeys(String keyOne, String keyTwo) {
       //sRobot.keyPress(KeyEvent.);
    }
    
    private void sendKeys(String keyOne) {

    }

    @Override
    public void run() {
        System.out.println("running...");
        actionTable.getItems().forEach((Action tempAction) -> {
            switch (tempAction.getAction()) {
                case "Click":
                    mousePress(tempAction.getCoordinates(), 1, Integer.parseInt(tempAction.getDelay().replace(" m/s", "")));
                    break;

                case "Click x2":
                    mousePress(tempAction.getCoordinates(), 2, Integer.parseInt(tempAction.getDelay().replace(" m/s", "")));
                    break;

                case "Hover":
                    mousePress(tempAction.getCoordinates(), 0, Integer.parseInt(tempAction.getDelay().replace(" m/s", "")));
                    break;

                case "Keys":

                    break;

                case "Confirmation":

                    break;
            }
        });

        // Handling all processes and closing them
        EzAutomator.getMainStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                sTimer.cancel();
                scriptThread.stop();
            }
        });
    }
}
