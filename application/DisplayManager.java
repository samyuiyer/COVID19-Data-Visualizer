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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class DisplayManager extends DisplayMode {

  private DisplayMode[] displayModes;
  private Node globalSettings;
  private int currMode;
  private boolean slidersVisible;
  private boolean settingsVisible;
  VBox settingsPanel;

  public DisplayManager() {

    settingsPanel = new VBox();
    settingsPanel.managedProperty().bind(settingsPanel.visibleProperty());
    settingsVisible = true;
    slidersVisible = true;
    globalSettings = createGlobalSettingsPane();
    createDisplayModes();
    globalSettings = createGlobalSettingsPane();
    currMode = 0;
  }

  @Override
  public Node getDiplayPane() {
    return displayModes[currMode].getDiplayPane();
  }

  @Override
  public Node getSettingsPane() {
    return globalSettings;
  }

  @Override
  void reset() {

  }

  private void createDisplayModes() {
    displayModes = new DisplayMode[1];
    displayModes[0] = new Table();
  }

  public Node getMenuBar() {
    MenuBar bar = new MenuBar();
    final Menu menu = new Menu("Menu");
    final MenuItem toggle = new MenuItem("Toggle Settings");
    final Menu help = new Menu("Help");
    
    menu.getItems().addAll(toggle);

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

    bar.getMenus().addAll(menu, help);
    return bar;
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

    Button loadFileBtn = new Button("Load File");
    Button saveFileBtn = new Button("Save File");
    loadSave.setSpacing(3);
    loadSave.getChildren().addAll(loadFileBtn, saveFileBtn);

    // setup combobox
    String[] dispModes = {"Table Mode", "Map Mode", "Graph Mode"};
    ComboBox<String> dpMode = new ComboBox<String>(FXCollections.observableArrayList(dispModes));
    dpMode.setPromptText("Select Display Mode");

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

    // setup color picker
    ColorPicker colorPicker = new ColorPicker(Color.LIGHTGRAY);
    colorPicker.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent arg0) {
        settingsPanel
            .setStyle("-fx-background-color: #" + colorPicker.getValue().toString().substring(2));
      }

    });

    // add Nodes to VBox
    settingsPanel.getChildren().addAll(fileTextField, loadSave, dpMode, colorPicker, time, sliderLabel,
        sliderStart, sliderEnd, range, displayModes[currMode].getSettingsPane());


    return settingsPanel;
  }
}
