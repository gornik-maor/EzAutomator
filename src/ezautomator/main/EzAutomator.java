package ezautomator.main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Abwatts
 */
public class EzAutomator extends Application {
    
    public static Boolean isSplashLoaded = false;
    public static Boolean isMaximized = false;
    
    double diffX;
    double diffY;
    
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
    
}
