package application;

import java.io.FileWriter;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
  public Node getDisplayPane() {
    return displayNode;
  }

  @Override
  public Node getSettingsPane() {
    if (load) {
      return globalSettings;
    } else {
      return new Label("");
    }
  }

  public Node getMenuBar() {
    return bar;
  }

  private void createDisplayModes() {
    displayModes = new DisplayMode[3];
    displayModes[0] = new Table(dm);
    displayModes[1] = new Map(dm);
    displayModes[2] = new Graph(dm);
    setMode(0);
  }

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

    // Add to MenuBar

    menu.getItems().addAll(toggle, view1, view2, view3, exit);
    bar.getMenus().addAll(menu, help);

    // Event Handlers

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
      confirmLoad.showAndWait().filter(response -> response == ButtonType.OK)
          .ifPresent(response -> {
            load = dm.loadTries(fileTextField.getText());
            setMode(0);
          });
    });
    saveFileBtn.setOnAction(e -> {
      String fileName = fileTextField.getText();
      if (fileName.equals("File Name"))
        fileName = "default.csv";
      if (!(fileName.endsWith(".csv")))
        fileName += ".csv";
      final String finalName = fileName;
      Alert confirmSave = new Alert(AlertType.CONFIRMATION,
          "Are you sure you want to save in the contents to " + fileTextField.getText() + "?");
      confirmSave.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
          saveToFile(finalName, saveLabel);
        }
      });
    });
  }

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

  private void setMode(int modeNum) {
    displayModes[modeNum].refresh();
    displayNode.setCenter(displayModes[modeNum].getDisplayPane());
    settingsNode.setCenter(displayModes[modeNum].getSettingsPane());
    if (!load)
      displayNode.setCenter(new Label("Bad Input File(s)"));
  }

  private void exitProgram() {
    Platform.exit();
    System.exit(0);

  }

  @Override
  public void refresh() {
  }

}
