package application;

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
import javafx.scene.control.Slider;
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
  private boolean slidersVisible;
  private boolean settingsVisible;
  private ComboBox<String> dpMode;
  VBox settingsPanel;

  public DisplayManager() {
    settingsPanel = new VBox();
    settingsPanel.managedProperty().bind(settingsPanel.visibleProperty());
    settingsVisible = true;
    displayNode = new BorderPane();
    settingsNode = new BorderPane();
    createDisplayModes();
    globalSettings = createGlobalSettingsPane();
  }

  @Override
  public Node getDisplayPane() {
    return displayNode;
  }

  @Override
  public Node getSettingsPane() {
    return globalSettings;
  }

  public Node getMenuBar() {
    MenuBar bar = new MenuBar();
    final Menu menu = new Menu("Menu");
    final MenuItem toggle = new MenuItem("Toggle Settings");
    final MenuItem view1 = new MenuItem("Table");
    final MenuItem view2 = new MenuItem("Map");
    final MenuItem view3 = new MenuItem("Graph");
    final Menu help = new Menu("Help");

    menu.getItems().addAll(toggle, view1, view2, view3);

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
        dpMode.setPromptText("Table Mode");
      }
    });

    view2.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        displayNode.setCenter(displayModes[1].getDisplayPane());
        settingsNode.setCenter(displayModes[1].getSettingsPane());
        dpMode.setPromptText("Map Mode");
      }
    });

    view3.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        displayNode.setCenter(displayModes[2].getDisplayPane());
        settingsNode.setCenter(displayModes[2].getSettingsPane());
        dpMode.setPromptText("Graph Mode");
      }
    });

    bar.getMenus().addAll(menu, help);
    return bar;
  }

  private void createDisplayModes() {
    displayModes = new DisplayMode[3];
    displayModes[0] = new Table();
    displayModes[1] = new Map();
    displayModes[2] = new Graph();
    displayNode.setCenter(displayModes[0].getDisplayPane());
    settingsNode.setCenter(displayModes[0].getSettingsPane());
  }

  private Node createGlobalSettingsPane() {
    // setup VBOX
    settingsPanel.setStyle("-fx-background-color: grey;");
    settingsPanel.setSpacing(5);
    // Insets(double top, double right, double bottom, double left) // TODO remove this comment
    settingsPanel.setPadding(new Insets(0, 10, 0, 10));

    // setup load file textfield and button
    HBox loadSave = new HBox();
    TextField fileTextField = new TextField("File name");
    fileTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			if (newValue) {
				fileTextField.clear();
			}
			if (oldValue) {
				if(fileTextField.getText().isBlank()) {
					fileTextField.setText("File Name");
				}
			}
		}

	});

    Button loadFileBtn = new Button("Load File");
    Button saveFileBtn = new Button("Save File");
    loadSave.setSpacing(3);
    loadSave.getChildren().addAll(loadFileBtn, saveFileBtn);

    // setup combobox
    String[] dispModes = {"Table Mode", "Map Mode", "Graph Mode"};
    dpMode = new ComboBox<String>(FXCollections.observableArrayList(dispModes));
    dpMode.setPromptText("Select Display Mode");
    dpMode.getSelectionModel().selectedItemProperty()
    .addListener(new ChangeListener<String>() {
        public void changed(ObservableValue<? extends String> observable,
                            String oldValue, String newValue) {
            if(newValue.equals("Table Mode")) {
              displayNode.setCenter(displayModes[0].getDisplayPane());
              settingsNode.setCenter(displayModes[0].getSettingsPane());
            }else if(newValue.equals("Map Mode")) {
              displayNode.setCenter(displayModes[1].getDisplayPane());
              settingsNode.setCenter(displayModes[1].getSettingsPane());
            }else if(newValue.equals("Graph Mode")) {
              displayNode.setCenter(displayModes[2].getDisplayPane());
              settingsNode.setCenter(displayModes[2].getSettingsPane());
            }
        }
});

    // setup time range slider and label
    Label sliderLabel = new Label("Choose Time Range:");
    Slider sliderStart = new Slider(0, 99, 10);
    Slider sliderEnd = new Slider(1, 100, 90);
    Label range = new Label(
        "Time Range: " + (int) sliderStart.getValue() + " to " + (int) sliderEnd.getValue());

    sliderStart.setShowTickLabels(true);
    sliderStart.setShowTickMarks(true);
    sliderStart.setBlockIncrement(10);
    sliderStart.setSnapToTicks(true);
    sliderEnd.setShowTickLabels(true);
    sliderEnd.setShowTickMarks(true);
    sliderEnd.setBlockIncrement(10);
    sliderEnd.setSnapToTicks(true);

    // create Event Handlers for sliders to change their values when necessary
    final ChangeListener<Number> startListener = new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
        if (sliderStart.getValue() >= sliderEnd.getValue()) {
          sliderEnd.setValue(sliderStart.getValue() + 1);
        } else if (sliderEnd.getValue() <= sliderStart.getValue()) {
          sliderEnd.setValue(sliderStart.getValue() + 1);
        }
        range.setText(
            "Time Range: " + (int) sliderStart.getValue() + " to " + (int) sliderEnd.getValue());
      }
    };
    final ChangeListener<Number> endListener = new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
        if (sliderStart.getValue() >= sliderEnd.getValue()) {
          sliderStart.setValue(sliderEnd.getValue() - 1);
        } else if (sliderEnd.getValue() <= sliderStart.getValue()) {
          sliderStart.setValue(sliderEnd.getValue() - 1);
        }
        range.setText(
            "Time Range: " + (int) sliderStart.getValue() + " to " + (int) sliderEnd.getValue());
      }
    };
    sliderStart.valueProperty().addListener(startListener);
    sliderEnd.valueProperty().addListener(endListener);

    // bind properties so buttons shift when hidden
    sliderLabel.managedProperty().bind(sliderLabel.visibleProperty());
    sliderStart.managedProperty().bind(sliderStart.visibleProperty());
    sliderEnd.managedProperty().bind(sliderEnd.visibleProperty());
    range.managedProperty().bind(range.visibleProperty());

    sliderStart.setVisible(slidersVisible);
    sliderEnd.setVisible(slidersVisible);

    // setup time range button
    Button time = new Button("Time Range");
    time.setOnAction(new EventHandler<ActionEvent>() { // button should hide time sliders and labels
      @Override
      public void handle(ActionEvent event) {
        if (slidersVisible) {
          sliderLabel.setVisible(false);
          sliderStart.setVisible(false);
          sliderEnd.setVisible(false);
          range.setVisible(false);
          slidersVisible = false;
        } else {
          sliderLabel.setVisible(true);
          sliderStart.setVisible(true);
          sliderEnd.setVisible(true);
          range.setVisible(true);
          slidersVisible = true;
        }
      }
    });

    // setup button
    settingsPanel.setStyle("-fx-background-color: #70C1B3");

    // setup color picker
    ColorPicker colorPicker = new ColorPicker(Color.web("#70C1B3"));
    colorPicker.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent arg0) {
        settingsPanel
            .setStyle("-fx-background-color: #" + colorPicker.getValue().toString().substring(2));
      }

    });

    // add Nodes to VBox
    settingsPanel.getChildren().addAll(fileTextField, loadSave, dpMode, colorPicker, time,
        sliderLabel, sliderStart, sliderEnd, range, settingsNode);


    return settingsPanel;
  }

  @Override
  void reset() {
  
  }

}
