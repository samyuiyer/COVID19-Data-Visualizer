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

  Canvas canvas;
  GraphicsContext gc;
  VBox sp;
  DataManager dm;
  private final int width = 900;
  private final int height = 450;
  Slider time;
  String[] timeLabels;
  CheckBox[] filters;
  dataTypes rType = dataTypes.Dead;
  final ToggleGroup data = new ToggleGroup();

  public Map(DataManager dm) {
    super();
    canvas = new Canvas(width, height);
    gc = canvas.getGraphicsContext2D();
    sp = new VBox();
    title = "map";
    this.dm = dm;
    EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        drawShapes(gc);
      }
    };
    Label sliderLabel = new Label("Choose Time:");
    time = new Slider(0, 94, 94);
    timeLabels = dm.getTimeLabels();
    Label timeLabel = new Label("" + timeLabels[(int) time.getValue()]);
    time.valueProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue<? extends Number> observable, Number oldValue,
          Number newValue) {
        timeLabel.setText("" + timeLabels[(int) time.getValue()]);
        drawShapes(gc);
      }
    });

    filters = new CheckBox[3];
    filters[0] = new CheckBox("Cities");
    filters[0].setIndeterminate(false);
    filters[0].setOnAction(eh);
    filters[1] = new CheckBox("States");
    filters[1].setIndeterminate(false);
    filters[1].setOnAction(eh);
    filters[2] = new CheckBox("Countries");
    filters[2].setIndeterminate(false);
    filters[2].setOnAction(eh);
    filters[1].fire();
    filters[2].fire();

    RadioButton c = new RadioButton("Confirmed");
    RadioButton d = new RadioButton("Dead");
    RadioButton r = new RadioButton("Recovered");

    c.setToggleGroup(data);
    r.setToggleGroup(data);

    d.setSelected(true);
    d.setToggleGroup(data);
    data.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle,
          Toggle new_toggle) {
        if (data.getSelectedToggle() != null) {
          if (((Labeled) data.getSelectedToggle()).getText().equals("Confirmed"))
            rType = dataTypes.Confirmed;
          if (((Labeled) data.getSelectedToggle()).getText().equals("Dead"))
            rType = dataTypes.Dead;
          if (((Labeled) data.getSelectedToggle()).getText().equals("Recovered"))
            rType = dataTypes.Recovered;
          drawShapes(gc);
        }
      }
    });


    sp.getChildren().addAll(sliderLabel, time, timeLabel);
    sp.getChildren().addAll(filters);
    sp.getChildren().addAll(d, c, r);
  }

  @Override
  void reset() {

  }

  @Override
  public Node getDisplayPane() {


    drawShapes(gc);

    return canvas;
  }

  @Override
  public Node getSettingsPane() {
    return sp;
  }

  private void drawShapes(GraphicsContext gc) {

    List<DataPoint> list = dm.gt.getAll();
    Image image = new Image("map.jpg");
    gc.drawImage(image, 0, 0, width, height);
    gc.setLineWidth(5);

    for (DataPoint d : list) {
      if (filter(d)) {
        // get x value
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
      gc.setFill(Color.ORANGE);
      return Math.log(d.confirmedList.get((int) time.getValue()));
    } else if (rType == dataTypes.Recovered) {
      gc.setFill(Color.AQUA);
      return Math.log(d.recoveredList.get((int) time.getValue()));
    }
    gc.setFill(Color.RED);

    return Math.log(d.deathsList.get((int) time.getValue()));
  }


  /**
   * @param d
   * @return
   */
  private boolean filter(DataPoint d) {
    if(d.getLon()==0&&d.getLat()==0)
      return false;
    if (!d.getCity().equals(""))
      return filters[0].isSelected();
    if (!d.getState().equals(""))
      return filters[1].isSelected();
    if (!d.getCountry().equals(""))
      return filters[2].isSelected();
    return true;
  }

}
