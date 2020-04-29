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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static application.Constants.*;

/**
 * @author ankurgarg
 *
 */
public class Main extends Application {

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

		// adds styles
		mainScene.getStylesheets().add(getClass().getResource("application.css").toString());

		ChangeListener<Number> stageSizeListener = new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				dm.eventThrower(oldValue.intValue(), newValue.intValue());
			}
		};

		primaryStage.widthProperty().addListener(stageSizeListener);
		primaryStage.heightProperty().addListener(stageSizeListener);

		primaryStage.setTitle(APP_TITLE);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
