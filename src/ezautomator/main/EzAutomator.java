package ezautomator.main;

import ezautomator.alert.AlertController;
import ezautomator.help.update.UpdateFormController;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Abwatts
 */
public class EzAutomator extends Application {

    private final static String version = "1.0.0.2";
    public static Boolean isSplashLoaded = false;
    private static Stage mainStage;

    double diffX, diffY;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ezautomator/main/FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("EzAutomator");
        stage.show();

        mainStage = stage;

        // Ensuring the program stops listening to key events even though the user has closed the program through a different way
        // other than simply clickling the exit button
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(1);
            }
        });

        // Catching all excpetions thrown in JavaFX's Application Thread and filtering
        Thread.setDefaultUncaughtExceptionHandler(EzAutomator::showError);

        /**
         * Events for main window Click first, then drag.
         */
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
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
                // Helps my cursor stay at the same position when dragging the window instead of teleporting to the top most-left corner
                stage.setX(event.getScreenX() + diffX);
                stage.setY(event.getScreenY() + diffY);
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private static void showError(Thread t, Throwable e) {
        if (e instanceof ArrayIndexOutOfBoundsException) {
            System.err.println("***Ignorable exception was thrown*** (" + e.toString() + ")");
        } else {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage instance
     *
     * @return main application stage
     */
    public static Stage getMainStage() {
        return mainStage;
    }

    /**
     * Getter for the software's current version
     * in a #.#.#.# format
     *
     * @return Program version
     */
    public static String getVersion() {
        return version;
    }

    /**
     * Converting key code to its string representation
     *
     * @param keyCode
     * @return
     */
    public static String getKeyTextRep(int keyCode) {
        String keyPressed = KeyEvent.getKeyText(keyCode);
        if (keyPressed.startsWith("Unknown keyCode:")) {
            keyPressed = keyPressed.replace("Unknown keyCode: ", "");
        }

        keyPressed = (keyPressed.startsWith("Right B")) ? "CTRL" : keyPressed;
        keyPressed = (keyPressed.equals("0xa4")) ? "ALT" : keyPressed;
        keyPressed = (keyPressed.equals("0xd")) ? "ENTER" : keyPressed;

        return keyPressed;
    }

    /**
     * Checks for software updates 5 seconds after the method is invoked
     */
    public static void checkForUpdates() {
        Timer upTimer = new Timer("Update Timer");
        upTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Document doc = Jsoup.connect("http://www.mediafire.com/file/80vqhg0bulcgjc3/EzAutomator.zip").timeout(0).get();
                            Element filename = doc.select("div.filename").first();
                            String pubVersion = filename.text();
                            pubVersion = pubVersion.replace("EzAutomator v", "");
                            pubVersion = pubVersion.replace(".zip", "");
                            if (!getVersion().equals(pubVersion)) {
                                if (!version.trim().equals("EzAutomator")) {
                                    if (new AlertController().loadAlert().showDialog("Yes", "No", "A new update is available! Download?",
                                            "exclamation", getMainStage(), getMainStage(), 0.5, true)) {
                                        try {
                                            java.awt.Desktop.getDesktop().browse(new URI("http://www.mediafire.com/file/80vqhg0bulcgjc3/EzAutomator.zip"));
                                        } catch (URISyntaxException ex) {
                                            Logger.getLogger(UpdateFormController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(UpdateFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        }, 5000);
    }
}
