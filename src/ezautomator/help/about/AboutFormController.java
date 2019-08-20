/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.help.about;

import ezautomator.alert.AlertController;
import ezautomator.help.update.UpdateFormController;
import ezautomator.main.EzAutomator;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Abwatts
 */
public class AboutFormController implements Initializable {

    @FXML
    private ImageView closeBtn;

    @FXML
    private Text versionLbl;

    @FXML
    private StackPane root;

    @FXML
    private Text emailLbl;

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
    void onEmailAddressPressed(MouseEvent event) {
        StringSelection stringSelection = new StringSelection(emailLbl.getText());
        java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        new AlertController().loadAlert().showDialog("Ok", "Cancel", "Email address was copied successfully!", "exclamation",
                getCurrStage(), getCurrStage(), 0.5, true);
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

    public AboutFormController loadResources() {
        try {
            FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/help/about/AboutForm.fxml"));
            StackPane aboutPane = fxmlLoader.load();
            Stage aboutStage = new Stage();
            aboutStage.initStyle(StageStyle.UNDECORATED);
            aboutStage.initModality(Modality.APPLICATION_MODAL);
            aboutStage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
            aboutStage.setTitle("EzAutomator");
            aboutStage.setScene(new Scene(aboutPane));
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

}
