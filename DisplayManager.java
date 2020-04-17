package ateam_final_project;

import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DisplayManager extends Application {
  
  private DisplayMode[] displayModes;
  private SettingsPane globalSettings;
  
  private List<String> args;

  private static final int WINDOW_WIDTH = 300;
  private static final int WINDOW_HEIGHT = 200;
  private static final String APP_TITLE = "Hello World!";
  
  @Override
  public void start(Stage primaryStage) throws Exception {
      // save args example
      args = this.getParameters().getRaw();
          
      // Create a vertical box with Hello labels for each args
          VBox vbox = new VBox();
          for (String arg : args) {
              vbox.getChildren().add(new Label("hello "+arg));
          }

      // Main layout is Border Pane example (top,left,center,right,bottom)
          BorderPane root = new BorderPane();
          
          HBox hbBt = new HBox();
          Button dpMode = new Button("Display Mode");
          Button timeRange = new Button("Time Range");
          Button loc = new Button("Locations");
          hbBt.getChildren().add(dpMode);
          hbBt.getChildren().add(timeRange);
          hbBt.getChildren().add(loc);
          ColorPicker cp = new ColorPicker();

      // Add the vertical box to the center of the root pane
          root.setCenter(vbox);
          
      root.setLeft(hbBt);
      root.setRight(cp);
      Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

      // Add the stuff and set the primary stage
          primaryStage.setTitle(APP_TITLE);
          primaryStage.setScene(mainScene);
          primaryStage.show();
  }

  
  public void displaySetting(SettingsPane setting) {
    
  }
  
  public void displayPane(DisplayMode display) {
    
  }

  public static void main(String[] args) {
    launch(args);
  }

}
