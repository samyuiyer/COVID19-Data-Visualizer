/**
 *
 * Author: Ankur Garg Email: Agarg34@wisc.edu
 * 
 * Course: CS400 Semester: Spring2020 Lecture: 001 Date: Apr 19, 2020
 *
 * Files: Main.java
 *
 */
package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author ankurgarg
 *
 */
public class Main extends Application {

  private static final int WINDOW_WIDTH = 800;
  private static final int WINDOW_HEIGHT = 600;
  private static final String APP_TITLE = "hey its corona";
  DisplayManager dm;
  Boolean visible = true;

  @Override
  public void start(Stage primaryStage) throws Exception {
    // Main layout is Border Pane (top,left,center,right,bottom)
    BorderPane root = new BorderPane();
    dm = new DisplayManager();
    // add to pane
    root.setLeft(dm.getSettingsPane());
    root.setCenter(dm.getDiplayPane());

    Button toggleSettingsBtn = new Button("Toggle Settings");
    root.getChildren().addAll(toggleSettingsBtn);


    // Set Scene
    Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    
    // longrunning operation runs on different thread
    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Runnable updater = new Runnable() {

                @Override
                public void run() {
                  root.setCenter(dm.getDiplayPane());
                }
            };

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }

                // UI update is run on the Application thread
                Platform.runLater(updater);
            }
        }

    });
    // don't let thread prevent JVM shutdown
    thread.setDaemon(true);
    thread.start();
    // try to add css
    // mainScene.getStylesheets().add("stylesheet.css");
    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(mainScene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
