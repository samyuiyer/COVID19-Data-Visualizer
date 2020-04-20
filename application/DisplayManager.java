package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class DisplayManager extends DisplayMode{

  private DisplayMode[] displayModes;
  private Node globalSettings;
  private int currMode;
  private boolean visible;
  
  public DisplayManager() {
    visible =true;
    globalSettings = createGlobalSettingsPane();
    createDisplayModes();
    currMode =0;
    
  }
  @Override
  public Node getDiplayPane() {
    // TODO Auto-generated method stub
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
    displayModes[0]= new Table();
    
  }
  
  private Node createGlobalSettingsPane() {
    VBox leftPanel = new VBox();

    leftPanel.setStyle("-fx-background-color: grey;");

    String[] dispModes = {"Table Mode", "Map Mode", "Graph Mode"};

    ComboBox<String> dpMode = new ComboBox<String>(FXCollections.observableArrayList(dispModes));
    dpMode.setPromptText("Select Display Mode");
    leftPanel.getChildren().add(dpMode);

    // Slider
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

    sliderLabel.managedProperty().bind(sliderLabel.visibleProperty());
    sliderStart.managedProperty().bind(sliderStart.visibleProperty());
    sliderEnd.managedProperty().bind(sliderEnd.visibleProperty());
    range.managedProperty().bind(range.visibleProperty());

    sliderStart.setVisible(visible);
    sliderEnd.setVisible(visible);

    Button time = new Button("Time Range");
    time.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (visible) {
          sliderLabel.setVisible(false);
          sliderStart.setVisible(false);
          sliderEnd.setVisible(false);
          range.setVisible(false);
          visible = false;
        } else {
          sliderLabel.setVisible(true);
          sliderStart.setVisible(true);
          sliderEnd.setVisible(true);
          range.setVisible(true);
          visible = true;
        }
      }
    });

    leftPanel.getChildren().addAll(time, sliderLabel, sliderStart, sliderEnd, range);

    Button loc = new Button("Locations");
    leftPanel.getChildren().add(loc);

    ColorPicker cp = new ColorPicker();
    leftPanel.getChildren().add(cp);

    return leftPanel;
  }

  
 
}
