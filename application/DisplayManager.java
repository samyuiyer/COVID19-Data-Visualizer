///////////////////////////////////////////////////////////////////////////////
//
// Title: ateam_final_project
// Author: Ankur Garg, Eric Ertl, Justin Chan, Samyu Iyer, Sudeep Reddy
//
// Course: CS400
// Semester: Spring 2020
// Lecture Number: 001
//
// Date: 4/29/2020
//
// Description: A project that displays statistics relating to COVID-19 in a
// variety of ways.
//
///////////////////////////////////////////////////////////////////////////////

package application;

import java.io.FileWriter;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class manages the overall display by setting up the display modes and settings panes
 */
public class DisplayManager extends DisplayMode {

  private DisplayMode[] displayModes;
  private BorderPane displayNode;
  private BorderPane settingsNode;
  private Node globalSettings;
  private MenuBar bar;
  private boolean settingsVisible;
  private DataManager dm;
  private ComboBox<String> dspModeComboBox;
  private VBox settingsPanel;
  private boolean load;

  /**
   * Constructs a display manager with the settings node as a vertical box and a display node
   */
  public DisplayManager() {

    dm = new DataManager();
    load = dm.loadTries("time_data");
    settingsPanel = new VBox();
    settingsPanel.managedProperty().bind(settingsPanel.visibleProperty());
    settingsVisible = true;
    displayNode = new BorderPane();
    settingsNode = new BorderPane();
    createGlobalSettingsPane();
    createDisplayModes();
    createMenuBar();

  }

  @Override
  /**
   * gets the current display mode
   */
  public Node getDisplayPane() {
    return displayNode;
  }

  @Override
  /**
   * gets the settings mode
   */
  public Node getSettingsPane() {
    if (load) {
      return globalSettings;
    } else {
      return new Label("");
    }
  }

  /**
   * Returns a menu bar node for the top of the display
   * 
   * @return a menu bar node
   */
  public Node getMenuBar() {
    return bar;
  }

  /**
   * Initializes each of the display modes as objects
   */
  private void createDisplayModes() {
    displayModes = new DisplayMode[3];
    displayModes[0] = new Table(dm);
    displayModes[1] = new Map(dm);
    displayModes[2] = new Graph(dm);
    setMode(0);
  }

  /**
   * Helper method to create the menu bar UI Elements
   */
  private void createMenuBar() {

    // Setup

    bar = new MenuBar();
    final Menu menu = new Menu("Menu");
    final MenuItem toggle = new MenuItem("Toggle Settings");
    final MenuItem view1 = new MenuItem("Table");
    final MenuItem view2 = new MenuItem("Map");
    final MenuItem view3 = new MenuItem("Graph");
    final MenuItem exit = new MenuItem("Exit");
    final Menu help = new Menu("Help");
    final MenuItem help1 = new MenuItem("Tutorial");
    final MenuItem help2 = new MenuItem("Load and Save");

    // Add to MenuBar

    menu.getItems().addAll(toggle, view1, view2, view3, exit);
    help.getItems().addAll(help1, help2);
    bar.getMenus().addAll(menu, help);

    // Event Handlers

    exit.setOnAction(e -> exitProgram());

    help1.setOnAction(e -> {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Help");
      alert.setHeaderText("Tutorial");
      alert.setContentText(
          "You can choose between various modes to display the data (map, table, graph), as well as select which portions of data are shown with the ability to filter by timeframe and location.\n\n"
              + "Table Mode:\n"
              + "In this mode, the number of COVID-19 deaths and recovered COVID-19 patients are displayed for all locations. Clicking on a column name (\"City\", \"Province/State\", \"Region\", etc.) rearranges the table such that the values in that column are sorted. Clicking once sorts from A-Z order, clicking once more sorts from Z-A order, and clicking a third time clears that column altogether. Additionally, users can filter by names of specific cities, states, or countries in the Filter fields in the left pane. Selecting \"Set Filter\" after typing in the desired filter will apply selected filters to visible data. \n\n"
              + "Map Mode:\n"
              + "In this mode, the various aspects of numerical COVID-19 data are displayed as dots, with the size of the dots pertaining to the size of the number they represent. The dots are placed in the location which they reference. The various checkboxes and radio buttons in the left panel allow for further customization, and the \"Choose Time\" slider enables users to select the end point in time for which to display data. \n\n"
              + "Graph Mode:\n"
              + "In this mode, the various aspects of numerical COVID-19 data are displayed in a line graph. The sliders in the left settings bar allow users to set the start date and end date for which to display data. The radio buttons enable users to filter by particular locations, as well as by which aspect of the numerical data they desire to plot.");
      alert.showAndWait();
    });
    help2.setOnAction(e -> {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Help");
      alert.setHeaderText("How to Save and Load");
      alert.setContentText("How to load a file:\n"
          + "The user will input a folder name that contains all the CSV files. By default, files will be loaded from a folder labeled time_data.\n\n"
          + "How to save a file:\n"
          + "The user will write a file name and the current filters will be applied and stored into a CSV file.\n\n");
      alert.showAndWait();
    });

    exit.setOnAction(e -> exitProgram());

    toggle.setOnAction(e -> {
      if (settingsVisible) {
        settingsPanel.setVisible(false);
        settingsVisible = false;
      } else {
        settingsPanel.setVisible(true);
        settingsVisible = true;
      }
    });

    view1.setOnAction(e -> {
      setMode(0);
    });

    view2.setOnAction(e -> {
      setMode(1);
    });

    view3.setOnAction(e -> {
      setMode(2);
    });
  }

  /**
   * Creates the global settings UI elements that will appear with each display mode
   */
  private void createGlobalSettingsPane() {

    // setup objects

    Label saveLabel = new Label(); // TODO rename this stuff
    Button loadFileBtn = new Button("Load File");
    Button saveFileBtn = new Button("Save File");
    TextField fileTextField = new TextField("File name");
    ColorPicker colorPicker = new ColorPicker(Color.web("#70C1B3"));
    String[] dispModes = {"Table Mode", "Map Mode", "Graph Mode"};
    dspModeComboBox = new ComboBox<String>(FXCollections.observableArrayList(dispModes));

    // Setup IDs for css styling

    settingsPanel.setId("settings_panel");
    saveLabel.setId("load_btn");
    saveFileBtn.setId("save_btn");
    fileTextField.setId("file_text_field");
    colorPicker.setId("color_picker");
    dspModeComboBox.setId("dsp_combo_box");

    // non-css styling

    settingsPanel.setPadding(new Insets(0, 10, 0, 10));

    // Setup values and properties

    dspModeComboBox.setPromptText("Select Display Mode");

    // Setup Listeners and EventHandlers

    colorPicker.setOnAction(e -> {
      settingsPanel
          .setStyle("-fx-background-color: #" + colorPicker.getValue().toString().substring(2));
    });

    fileTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        fileTextField.clear();
      }
      if (oldValue) {
        if (fileTextField.getText().isBlank()) {
          fileTextField.setText("File Name");
        }
      }
    });

    dspModeComboBox.getSelectionModel().selectedItemProperty()
        .addListener((o, oldValue, newValue) -> {
          if (newValue.equals("Table Mode")) {
            setMode(0);
          } else if (newValue.equals("Map Mode")) {
            setMode(1);
          } else if (newValue.equals("Graph Mode")) {
            setMode(2);
          }
        });

    // Add all Nodes to VBox
    HBox fileBtns = new HBox();
    fileBtns.getChildren().addAll(loadFileBtn, saveFileBtn);
    settingsPanel.getChildren().addAll(fileTextField, fileBtns, dspModeComboBox, colorPicker,
        settingsNode);

    globalSettings = settingsPanel;
    loadFileBtn.setOnAction(e -> {
      Alert confirmLoad = new Alert(AlertType.CONFIRMATION,
          "Are you sure you want to load in the contents of " + fileTextField.getText() + "?");
      confirmLoad.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
          load = dm.loadTries(fileTextField.getText());
          setMode(0);
        }
      });
    });
    saveFileBtn.setOnAction(e -> {
      final String finalName = getSaveFileName(fileTextField.getText());
      Alert confirmSave = new Alert(AlertType.CONFIRMATION,
          "Are you sure you want to save in the contents to " + finalName + "?");
      confirmSave.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
          saveToFile(finalName, saveLabel);
        }
      });
    });
  }

  /**
   * Gets the file name entered by the user and adds csv if the ending isnt already there
   * 
   * @param fileName the current file name the user has entered
   * @return the final file name with the proper ending
   */
  private String getSaveFileName(String fileName) {
    if (fileName.equals("File Name")) // changes to default file name
      fileName = "default.csv";
    if (!(fileName.endsWith(".csv")))
      fileName += ".csv";
    return fileName;
  }

  /**
   * Saves file to local storage with the given file name
   * 
   * @param fileName  the name of the file being saved
   * @param saveLabel the label to be changed once save has finished
   */
  private void saveToFile(String fileName, Label saveLabel) {
    List<DataPoint> filtered = ((Table) displayModes[0]).getFilteredList();
    try {
      FileWriter txtFile = new FileWriter(fileName);
      txtFile.write("City,State,Country,Confirmed,Dead,Recovered\n");
      for (DataPoint dp : filtered) {
        txtFile.write(dp.getCity() + "," + dp.getState() + "," + dp.getCountry() + ","
            + dp.getConfirmed() + "," + dp.getDeaths() + "," + dp.getRecovered() + "\n");
      }
      txtFile.close();
      saveLabel.setText("File " + fileName + " successfully saved");
    } catch (Exception ex) {

    }

  }

  /**
   * Changes the display mode with the given int
   * 
   * @param modeNum - the int for which display mode to change to
   */
  private void setMode(int modeNum) {
    displayModes[modeNum].refresh();
    displayNode.setCenter(displayModes[modeNum].getDisplayPane());
    settingsNode.setCenter(displayModes[modeNum].getSettingsPane());
    if (!load)
      displayNode.setCenter(new Label("Bad Input File(s)"));
  }

  /**
   * Closes the program with an alert
   */
  private void exitProgram() {
    Alert confirmExit = new Alert(AlertType.CONFIRMATION, "Are you sure you want to exit?");
    confirmExit.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
      Platform.exit();
      System.exit(0);
    });
  }

  @Override
  /**
   * Not used by this class
   */
  public void refresh() {}

}
