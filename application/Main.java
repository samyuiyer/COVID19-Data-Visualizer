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
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author ankurgarg
 *
 */
public class Main extends Application {

  private static final int WINDOW_WIDTH = 1170;
  private static final int WINDOW_HEIGHT = 800;
  private static final String APP_TITLE = "COVID-19 Data Visualizer";
  DisplayManager dm;
  Boolean visible = true;

  @Override
  public void start(Stage primaryStage) throws Exception {
    // Main layout is Border Pane (top, left, center, right, bottom)
    BorderPane root = new BorderPane();
    dm = new DisplayManager();
    // add to pane
    root.setLeft(dm.getSettingsPane());
    root.setCenter(dm.getDisplayPane());
    root.setTop(dm.getMenuBar()); 

    // Set Scene
    Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    
    //adds styles
    mainScene.getStylesheets().add(getClass().getResource("application.css").toString());
    
    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(mainScene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
