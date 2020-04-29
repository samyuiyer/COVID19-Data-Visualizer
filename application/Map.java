package application;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class Map extends DisplayMode {

  private enum dataTypes {
    Dead, Confirmed, Recovered
  }

  private VBox settingsPane;
  private DataManager dm;
  private final int width = 900;
  private final int height = 450;
  private dataTypes rType = dataTypes.Dead;
  private final ToggleGroup dataRadioBtns = new ToggleGroup();
  private Slider timeSlider;
  private String[] timeLabels;
  private CheckBox[] filters;
  private Canvas canvas;
  private GraphicsContext graphicsContext;

  public Map(DataManager dm) {
    super();
    canvas = new Canvas(width, height);
    graphicsContext = canvas.getGraphicsContext2D();
    settingsPane = new VBox();
    title = "map";
    this.dm = dm;

    EventHandler<ActionEvent> redraw = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        drawShapes(graphicsContext);
      }
    };

    // setup time slider
    Label sliderLabel = new Label("Choose Time:");
    timeSlider = new Slider(0, 94, 94);
    timeLabels = dm.getTimeLabels();
    Label timeLabel = new Label("" + timeLabels[(int) timeSlider.getValue()]);
    timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue<? extends Number> observable, Number oldValue,
          Number newValue) {
        timeLabel.setText("" + timeLabels[(int) timeSlider.getValue()]);
        drawShapes(graphicsContext);
      }
    });

    filters = new CheckBox[3];
    filters[0] = new CheckBox("Cities");
    filters[0].setIndeterminate(false);
    filters[0].setOnAction(redraw);
    filters[1] = new CheckBox("States");
    filters[1].setIndeterminate(false);
    filters[1].setOnAction(redraw);
    filters[2] = new CheckBox("Countries");
    filters[2].setIndeterminate(false);
    filters[2].setOnAction(redraw);
    filters[1].fire();
    filters[2].fire();

    RadioButton cRadio = new RadioButton("Confirmed");
    RadioButton dRadio = new RadioButton("Dead");
    RadioButton rRadio = new RadioButton("Recovered");

    cRadio.setToggleGroup(dataRadioBtns);
    rRadio.setToggleGroup(dataRadioBtns);

    dRadio.setSelected(true);
    dRadio.setToggleGroup(dataRadioBtns);
    dataRadioBtns.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle,
          Toggle new_toggle) {
        if (dataRadioBtns.getSelectedToggle() != null) {
          if (((Labeled) dataRadioBtns.getSelectedToggle()).getText().equals("Confirmed"))
            rType = dataTypes.Confirmed;
          if (((Labeled) dataRadioBtns.getSelectedToggle()).getText().equals("Dead"))
            rType = dataTypes.Dead;
          if (((Labeled) dataRadioBtns.getSelectedToggle()).getText().equals("Recovered"))
            rType = dataTypes.Recovered;
          drawShapes(graphicsContext);
        }
      }
    });

    settingsPane.getChildren().addAll(sliderLabel, timeSlider, timeLabel);
    settingsPane.getChildren().addAll(filters);
    settingsPane.getChildren().addAll(dRadio, cRadio, rRadio);
  }

  @Override
  public Node getDisplayPane() {
    drawShapes(graphicsContext);
    return canvas;
  }

  @Override
  public Node getSettingsPane() {
    return settingsPane;
  }

  private void drawShapes(GraphicsContext gc) {

    List<DataPoint> list = dm.gt.getAll();
    Image image = new Image("map.jpg");
    gc.drawImage(image, 0, 0, width, height);
    gc.setLineWidth(5);

    for (DataPoint d : list) {
      if (filter(d)) {
        double x = ((d.getLon()) + 180) * width / 360;
        double y = (d.getLat() + 90.0) * height / 180;
        double factor = getFactor(d);
        gc.fillOval(x - factor / 2.0, height - (y + factor / 2.0), factor, factor);
      }
    }
  }


  /**
   * @param d
   * @return
   */
  private double getFactor(DataPoint d) {
    if (rType == dataTypes.Confirmed) {
      graphicsContext.setFill(Color.ORANGE);
      return Math.log(d.confirmedList.get((int) timeSlider.getValue()));
    } else if (rType == dataTypes.Recovered) {
      graphicsContext.setFill(Color.AQUA);
      return Math.log(d.recoveredList.get((int) timeSlider.getValue()));
    }
    graphicsContext.setFill(Color.RED);

    return Math.log(d.deathsList.get((int) timeSlider.getValue()));
  }


  /**
   * @param d
   * @return
   */
  private boolean filter(DataPoint d) {
    if (d.getLon() != 0 || d.getLat() != 0)
      return d.filter(filters[0].isSelected(), filters[1].isSelected(), filters[2].isSelected());
    return false;

  }

}
