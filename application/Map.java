package application;

import java.util.List;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;


public class Map extends DisplayMode {

  ImageView iv1 = new ImageView();
  DataManager dm;
  int scale = 5;

  public Map() {
    super();
    title = "map";
    dm = new DataManager();
    try {
      dm.loadTries("data_test.txt");
    } catch (Exception e) {
    }
  }


  @Override
  void reset() {

  }

  @Override
  public Node getDisplayPane() {

    Canvas canvas = new Canvas(360 * scale, 180 * scale);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    drawShapes(gc);
    return canvas;
  }

  @Override
  public Node getSettingsPane() {
    // TODO Auto-generated method stub
    return null;
  }



  private void drawShapes(GraphicsContext gc) {
    List<DataPoint> list = dm.gt.getAll();

    gc.setLineWidth(5);

    for (DataPoint d : list) {
       double dead = Math.log(d.getDeaths())/3;
      gc.setFill(new Color(Math.max(0,Math.min(dead,1)), 0, 1 - Math.max(0,Math.min(dead,1)), .5));
      double factor = 2.0;
      gc.fillOval((d.getLon() + 180.0) * scale - factor / 2,
          (d.getLat() + 90.0) * scale - factor / 2, factor, factor);

    }
  }

}
