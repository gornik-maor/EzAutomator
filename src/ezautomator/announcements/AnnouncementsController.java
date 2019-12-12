/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.announcements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import ezautomator.help.update.UpdateFormController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * FXML Controller class
 *
 * @author Maor Gornic
 */
public class AnnouncementsController implements Initializable {

    @FXML
    private Text txtAnnouncement;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXSpinner spinner;

    @FXML
    private ImageView closeBtn;

    @FXML
    private StackPane root;

    @FXML
    private Text txtTitle;

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
    void onRefreshAnnouncements(MouseEvent event) {
        System.out.println("Clicked!");
        spinner.setVisible(true);
        fetchAnnouncement(2500);
    }

    private Stage tStage;
    private double diffX, diffY;
    private boolean terminateUponExit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Fetching announcements on load
        fetchAnnouncement(1);
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
    }

    private void closeForm() {
        if (!terminateUponExit) {
            tStage.setOpacity(1);
            getCurrStage().close();
        } else {
            System.exit(1);
        }
    }

    private Stage getCurrStage() {
        return (Stage) closeBtn.getScene().getWindow();
    }

    public AnnouncementsController loadResources() {
        try {
            FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/announcements/Announcements.fxml"));
            StackPane announcementPane = fxmlLoader.load();
            Stage announcementStage = new Stage();
            announcementStage.initStyle(StageStyle.UNDECORATED);
            announcementStage.initModality(Modality.APPLICATION_MODAL);
            announcementStage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
            announcementStage.setTitle("EzAutomator");
            announcementStage.setScene(new Scene(announcementPane));
            return fxmlLoader.getController();

        } catch (IOException ex) {
            Logger.getLogger(UpdateFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void loadForm(Stage tStage, boolean terminateUponExit, double opValue) {
        if (tStage != null) {
            this.tStage = tStage;
            tStage.setOpacity(opValue);
        }
        this.terminateUponExit = terminateUponExit;

        getCurrStage().show();
    }

    /*
     Fetching the Announcement based on 3 files.
     */
    private void fetchAnnouncement(int delay) {
        Timer upTimer = new Timer("Blockage Timer");
        upTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // Getting annoncement's title
                    Document docTitle = Jsoup.connect("http://www.mediafire.com/file/250cszqlzsy4hw5/Title.txt").timeout(0).get();
                    Elements metaTagsTitle = docTitle.getElementsByTag("meta");
                    String title = metaTagsTitle.get(metaTagsTitle.size() - 1).attr("content");

                    // Getting text from first file
                    Document docOne = Jsoup.connect("http://www.mediafire.com/file/dlty0csdrdhw3xl/PartOne.txt").timeout(0).get();
                    Elements metaTagsOne = docOne.getElementsByTag("meta");
                    String descriptionOne = metaTagsOne.get(metaTagsOne.size() - 1).attr("content");

                    // Getting text from second file
                    Document docTwo = Jsoup.connect("http://www.mediafire.com/file/z49v65rh6emekyj/PartTwo.txt").timeout(0).get();
                    Elements metaTagsTwo = docTwo.getElementsByTag("meta");
                    String descriptionTwo = metaTagsTwo.get(metaTagsTwo.size() - 1).attr("content");

                    if (!title.isEmpty()) {
                        txtTitle.setText(title);
                        String announcement = descriptionOne + " " + descriptionTwo;
                        if (!announcement.isEmpty()) {
                            txtAnnouncement.setText(announcement);
                            spinner.setVisible(false);
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(UpdateFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, delay);
    }

}
