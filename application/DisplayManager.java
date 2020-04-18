package application;

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

	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;
	private static final String APP_TITLE = "hey its corona";

	@Override
	public void start(Stage primaryStage) throws Exception {

		args = this.getParameters().getRaw();

		// Main layout is Border Pane example (top,left,center,right,bottom)
		BorderPane root = new BorderPane();

		VBox leftPanel = new VBox();
		leftPanel.setStyle("-fx-background-color: grey;");
		
		String[] dispModes = {"Table Mode", "Map Mode", "Graph Mode"};
		
		ComboBox<String> dpMode = new ComboBox<String>(FXCollections.observableArrayList(dispModes));
		dpMode.setPromptText("Select Display Mode");
		leftPanel.getChildren().add(dpMode);
		
		Button time = new Button("Time Range");
		leftPanel.getChildren().add(time);
		
		Button loc = new Button("Locations");
		leftPanel.getChildren().add(loc);
		
		ColorPicker cp = new ColorPicker();
		leftPanel.getChildren().add(cp);

		root.setLeft(leftPanel);
		
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
