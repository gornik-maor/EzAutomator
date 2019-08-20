/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.help.update;

import com.jfoenix.controls.JFXSpinner;
import ezautomator.alert.AlertController;
import ezautomator.main.EzAutomator;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * FXML Controller class
 *
 * @author gornicma
 */
public class UpdateFormController implements Initializable {
    
    @FXML
    private ImageView closeBtn;
    
    @FXML
    private Text versionLbl;
    
    @FXML
    private StackPane root;
    
    @FXML
    private Button btnCheck;
    
    @FXML
    private JFXSpinner prgBar;
    
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
    void onBtnCheckForUpdates(MouseEvent event) {
        
        btnCheck.setMaxHeight(39);
        btnCheck.setLayoutY(167);
        prgBar.setVisible(true);

//        try {
//            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(UpdateFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        Timer upTimer = new Timer();
        upTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        checkUpdates();
                        upTimer.cancel();
                    }
                });
                
            }
        }, 10000, 500000000);
    }
    
    private Stage tStage;
    private double diffX, diffY;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        
        versionLbl.setText("v" + EzAutomator.getVersion());
    }
    
    private void closeForm() {
        tStage.setOpacity(1);
        getCurrStage().close();
    }
    
    private Stage getCurrStage() {
        return (Stage) closeBtn.getScene().getWindow();
    }
    
    public UpdateFormController loadResources() {
        try {
            
            FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/help/update/UpdateForm.fxml"));
            StackPane updatePane = fxmlLoader.load();
            Stage updateStage = new Stage();
            updateStage.initStyle(StageStyle.UNDECORATED);
            updateStage.initModality(Modality.APPLICATION_MODAL);
            updateStage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
            updateStage.setTitle("EzAutomator");
            updateStage.setScene(new Scene(updatePane));
            return fxmlLoader.getController();
            
        } catch (IOException ex) {
            Logger.getLogger(UpdateFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void loadForm(Stage tStage, double opValue) {
        if (tStage != null) {
            this.tStage = tStage;
            tStage.setOpacity(opValue);
        }
        
        getCurrStage().show();
    }

    /**
     * Comparing the version of this software and scrapping the filename of the
     * software from the download link and comparing their versions
     */
    private void checkUpdates() {
        try {
            Document doc = Jsoup.connect("http://www.mediafire.com/file/80vqhg0bulcgjc3/EzAutomator.zip").timeout(0).get();
            Element filename = doc.select("div.filename").first();
            String version = filename.text();
            version = version.replace("EzAutomator v", "");
            version = version.replace(".zip", "");
            if (!versionLbl.getText().replace("v", "").equals(version)) {
                if (version.trim().equals("EzAutomator")) {
                    new AlertController().loadAlert().showDialog("Ok", "Cancel", "Update Manager is currently unavailable!",
                            "error", getCurrStage(), getCurrStage(), 0.5, true);
                } else {
                    if (new AlertController().loadAlert().showDialog("Ok", "Cancel", "A new update was found! Download?",
                            "exclamation", getCurrStage(), getCurrStage(), 0.5, true)) {
                        try {
                            java.awt.Desktop.getDesktop().browse(new URI("http://www.mediafire.com/file/80vqhg0bulcgjc3/EzAutomator.zip"));
                            closeForm();
                        } catch (URISyntaxException ex) {
                            new AlertController().loadAlert().showDialog("Ok", "Cancel", "Could not open download! Please try again later.",
                                    "error", getCurrStage(), getCurrStage(), 0.5, true);
                            Logger.getLogger(UpdateFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
                new AlertController().loadAlert().showDialog("Ok", "Cancel", "Congratulations! You are up to date.",
                        "exclamation", getCurrStage(), getCurrStage(), 0.5, true);
                closeForm();
            }
        } catch (IOException ex) {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "Could not check for updates! Please try again later.",
                    "error", getCurrStage(), getCurrStage(), 0.5, true);
            Logger.getLogger(UpdateFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeForm();
    }
    
}
