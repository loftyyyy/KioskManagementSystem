package org.example.softfun_funsoft;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.softfun_funsoft.DaoImpl.UserDaoImpl;
import org.example.softfun_funsoft.lang.LangCheck;
import org.example.softfun_funsoft.model.User;
import org.example.softfun_funsoft.singleton.CurrentUser;
import org.example.softfun_funsoft.utils.SoundManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StartUpCont extends Application implements Initializable {

    @FXML
    private StackPane rootStackPane;

    @FXML
    private Text TITLE;

    @FXML
    private JFXButton StartButton;

    @FXML
     private MediaView mediaView;

     private MediaPlayer soundPlayer;

     @FXML
     private void onProceedButtonClick() {
      System.out.println("Proceed button clicked!");
 }

     @Override
     public void start(Stage stage) throws Exception {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("StartUp.fxml"));
          Parent root = loader.load();

          StartUpCont controller = loader.getController();
          controller.initializeMedia();

          Scene scene = new Scene(root);
          stage.setTitle("Team Hilux Fastfood Kiosk Ordering System");
          stage.setScene(scene);
          stage.setResizable(false);
          stage.show();

//          javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(Duration.seconds(1)); // 2-second delay
//          delay.setOnFinished(e -> controller.playStartSound()); // Play the sound after delay
//          delay.play();
     }

    public void initializeMedia() {

        String filePath = "src/main/resources/pic_resources/1122(1).mp4";
        File file = new File(filePath);

//        if (!file.exists()) {
//            showError("Media file not found at: " + filePath);
//            return;
//        }

        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.setAutoPlay(true);
//        mediaPlayer.setOnError(() -> showError("Error occurred while playing media: " + mediaPlayer.getError().getMessage()));
        mediaPlayer.setOnReady(() -> {
            System.out.println("Media is ready to play.");
            mediaPlayer.play();
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
    }

//    private void showError(String message) {
//          Alert alert = new Alert(Alert.AlertType.ERROR);
//          alert.setTitle("Error");
//          alert.setContentText(message);
//          alert.showAndWait();
//
//     }


     public static void main(String[] args) {
      launch(args);
 }

     @FXML
     public void handleStartButton(ActionEvent event) throws IOException {
         System.out.println("Start button pressed");

         Parent newRoot = FXMLLoader.load(getClass().getResource("DineInOrTakeout.fxml"));

         Node currentRoot = rootStackPane.getChildren().get(rootStackPane.getChildren().size() - 1);

         FadeTransition fadeOut = new FadeTransition(Duration.millis(500), currentRoot);
         fadeOut.setFromValue(1.0);
         fadeOut.setToValue(0.0);
         fadeOut.setOnFinished(e -> {

             rootStackPane.getChildren().remove(currentRoot);
             rootStackPane.getChildren().add(newRoot);

         });
         fadeOut.play();
     }
     public void generateId(){
         UserDaoImpl userDao = new UserDaoImpl();
         CurrentUser currentUser = CurrentUser.getInstance();
         User user = new User();
         user.setUserId("U" + System.currentTimeMillis());
         currentUser.setUserId(user.getUserId());
         userDao.save(user);
     }

     public void initialize(URL url, ResourceBundle resourceBundle) {
         generateId();


         javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(Duration.seconds(1)); // 2-second delay
         delay.setOnFinished(e -> SoundManager.playStartSound()); // Play the sound after delay
         delay.play();

         initializeMedia();

          System.out.println("Initializing StartUpController");

          // TITLE animation
          if (TITLE == null) {
               System.out.println("TITLE is null. Check FXML configuration.");
          } else {
               // Set TITLE to fade in and out indefinitely
               TITLE.setOpacity(0);
               FadeTransition fade = new FadeTransition(Duration.millis(2000), TITLE);
               fade.setCycleCount(FadeTransition.INDEFINITE);
               fade.setInterpolator(Interpolator.LINEAR);
               fade.setFromValue(0);
               fade.setToValue(1);
               fade.setAutoReverse(true);
               fade.play();
          }

          if (StartButton == null) {
               System.out.println("StartButton is null. Check FXML configuration.");
          } else {
               // Set StartButton to fade in and out indefinitely
               StartButton.setOpacity(0);
               FadeTransition fading = new FadeTransition(Duration.millis(2000), StartButton);
               fading.setCycleCount(FadeTransition.INDEFINITE);
               fading.setInterpolator(Interpolator.LINEAR);
               fading.setFromValue(0);
               fading.setToValue(1);
               fading.setAutoReverse(true);
               fading.play();
          }
     }
}
