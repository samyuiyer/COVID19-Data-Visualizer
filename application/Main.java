///////////////////////////////////////////////////////////////////////////////
//
// 	Title: ateam_final_project
// 	Author: Ankur Garg, Eric Ertl, Justin Chan, Samyu Lyer, Sudeep Reddy, 
//
// 	Course: CS400
//	Semester: Spring 2020
//	Lecture Number: 001
//
//	Date: 4/29/2020
//
// 	Description: 	A project that displays statistics relating to COVID-19 in a
//					variety of ways. 
//
///////////////////////////////////////////////////////////////////////////////

package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The main driver class
 */
public class Main extends Application {

	private static final int WINDOW_WIDTH = 1170;
	private static final int WINDOW_HEIGHT = 879;
	private static final String APP_TITLE = "COVID-19 Data Visualizer";
	private DisplayManager dm;

	/**
	 * Starts the program
	 * 
	 * @param primaryStage - the main stage
	 */
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

		// add stylesheet

		mainScene.getStylesheets().add(getClass().getResource("application.css").toString());

		primaryStage.setTitle(APP_TITLE);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	/**
	 * Main method
	 * 
	 * @param args - program arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
