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
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class Map extends DisplayMode {
  private enum dataTypes {
    Dead, Confirmed, Active, Recovered
  }

  Canvas canvas;
  GraphicsContext gc;
  VBox sp;
  DataManager dm;
  private final int width = 900;
  private final int height = 450;
  CheckBox[] filters;
  dataTypes rType = dataTypes.Dead;
  final ToggleGroup data = new ToggleGroup();

  public Map() {
    super();
    canvas = new Canvas(width, height);
    gc = canvas.getGraphicsContext2D();
    sp = new VBox();
    title = "map";
    dm = new DataManager();
    try {
      dm.loadTries("data_files");
    } catch (Exception e) {
    }
    EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        drawShapes(gc);
      }
    };
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
    RadioButton a = new RadioButton("Active");
    c.setToggleGroup(data);
    r.setToggleGroup(data);
    a.setToggleGroup(data);
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
          if (((Labeled) data.getSelectedToggle()).getText().equals("Active"))
            rType = dataTypes.Active;
          drawShapes(gc);

        }
      }
    });

    sp.getChildren().addAll(filters);
    sp.getChildren().addAll(d, c, a, r);
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
      return Math.log(d.getConfirmed());
    } else if (rType == dataTypes.Active) {
      gc.setFill(Color.YELLOW);
      return Math.log(d.getActive());
    } else if (rType == dataTypes.Recovered) {
      gc.setFill(Color.AQUA);
      return Math.log(d.getRecovered());
    }
    gc.setFill(Color.RED);
    return Math.log(d.getDeaths());

  }


  /**
   * @param d
   * @return
   */
  private boolean filter(DataPoint d) {

    if (!d.getCity().equals(""))
      return filters[0].isSelected();
    if (!d.getState().equals(""))
      return filters[1].isSelected();
    if (!d.getCountry().equals(""))
      return filters[2].isSelected();
    return true;
  }

}
