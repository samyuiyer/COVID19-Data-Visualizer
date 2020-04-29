package application;

import java.io.FileWriter;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
    load = false;
    dm = new DataManager();
    try {
      if (dm.loadTries()) {
        load = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    settingsPanel = new VBox();
    settingsPanel.managedProperty().bind(settingsPanel.visibleProperty());
    settingsVisible = true;
    displayNode = new BorderPane();
    settingsNode = new BorderPane();
    createDisplayModes();
    createMenuBar();
    createGlobalSettingsPane();
  }

  @Override
  public Node getDisplayPane() {
    if (load) {
      return displayNode;
    } else {
      return new Label("Bad Input File(s)");
    }
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
    displayNode.setCenter(displayModes[0].getDisplayPane());
    settingsNode.setCenter(displayModes[0].getSettingsPane());
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
    
    exit.setOnAction(e -> Platform.exit());

    toggle.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        if (settingsVisible) {
          settingsPanel.setVisible(false);
          settingsVisible = false;
        } else {
          settingsPanel.setVisible(true);
          settingsVisible = true;
        }
      }
    });

    view1.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        displayNode.setCenter(displayModes[0].getDisplayPane());
        settingsNode.setCenter(displayModes[0].getSettingsPane());
        dspModeComboBox.setPromptText("Table Mode");
      }
    });

    view2.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        displayNode.setCenter(displayModes[1].getDisplayPane());
        settingsNode.setCenter(displayModes[1].getSettingsPane());
        dspModeComboBox.setPromptText("Map Mode");
      }
    });

    view3.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        displayNode.setCenter(displayModes[2].getDisplayPane());
        settingsNode.setCenter(displayModes[2].getSettingsPane());
        dspModeComboBox.setPromptText("Graph Mode");
      }
    });
  }

  private void createGlobalSettingsPane() {

    // setup objects

    Label saveLabel = new Label(); // TODO rename this stuff
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

    colorPicker.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        settingsPanel
            .setStyle("-fx-background-color: #" + colorPicker.getValue().toString().substring(2));
      }
    });

    dspModeComboBox.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<String>() {
          public void changed(ObservableValue<? extends String> observable, String oldValue,
              String newValue) {
            if (newValue.equals("Table Mode")) {
              displayNode.setCenter(displayModes[0].getDisplayPane());
              settingsNode.setCenter(displayModes[0].getSettingsPane());
            } else if (newValue.equals("Map Mode")) {
              displayNode.setCenter(displayModes[1].getDisplayPane());
              settingsNode.setCenter(displayModes[1].getSettingsPane());
            } else if (newValue.equals("Graph Mode")) {
              displayNode.setCenter(displayModes[2].getDisplayPane());
              settingsNode.setCenter(displayModes[2].getSettingsPane());
            }
          }
        });

    fileTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
          Boolean newValue) {
        if (newValue) {
          fileTextField.clear();
        }
        if (oldValue) {
          if (fileTextField.getText().isBlank()) {
            fileTextField.setText("File Name");
          }
        }
      }
    });

    dspModeComboBox.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<String>() {
          public void changed(ObservableValue<? extends String> observable, String oldValue,
              String newValue) {
            if (newValue.equals("Table Mode")) {
              displayNode.setCenter(displayModes[0].getDisplayPane());
              settingsNode.setCenter(displayModes[0].getSettingsPane());
            } else if (newValue.equals("Map Mode")) {
              displayNode.setCenter(displayModes[1].getDisplayPane());
              settingsNode.setCenter(displayModes[1].getSettingsPane());
            } else if (newValue.equals("Graph Mode")) {
              displayNode.setCenter(displayModes[2].getDisplayPane());
              settingsNode.setCenter(displayModes[2].getSettingsPane());
            }
          }
        });

    // Add all Nodes to VBox

    settingsPanel.getChildren().addAll(fileTextField, saveFileBtn, saveLabel, dspModeComboBox,
        colorPicker, settingsNode);

    globalSettings = settingsPanel;

    saveFileBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        String fileName = fileTextField.getText();
        if (fileName.equals("File Name"))
          fileName = "default.csv";
        if (!(fileName.endsWith(".csv")))
          fileName += ".csv";
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
    });
  }

}
