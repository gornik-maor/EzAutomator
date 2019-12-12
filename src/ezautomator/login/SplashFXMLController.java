/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * FXML Controller class
 *
 * @author Maor Gornic
 */
public class SplashFXMLController implements Initializable {

    @FXML
    private ImageView splashPic;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Getting annoncement's title
            Document docTitle = Jsoup.connect("http://www.mediafire.com/file/nbi9vnxw3uqagki/SplashStatus.txt").timeout(0).get();
            Elements metaTagsTitle = docTitle.getElementsByTag("meta");
            String event = metaTagsTitle.get(metaTagsTitle.size() - 1).attr("content");

            String splashImage = "/ezautomator/icons/splash/splash-screen" + event + ".png";
            Image image = new Image(splashImage);
            splashPic.setImage(image);
        } catch (IOException ex) {
            // Ignore
            Logger.getLogger(SplashFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
