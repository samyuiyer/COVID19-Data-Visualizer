package application;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class Graph extends DisplayMode {

  DataManager dm;

  Graph() {
    super();
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
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    final LineChart<Number, Number> chart = new LineChart<Number, Number>(xAxis, yAxis);
    XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
    
    chart.setTitle("Corona Time");
    List<DataPoint> list = dm.gt.getAll();
    int i = 0;
    for (DataPoint d : list) {
      series.getData().add(new XYChart.Data<Number, Number>(i, d.getDeaths()));
      i++;
      if (i == 100)
        break;
    }

    chart.getData().add(series);


    return chart;
  }

  @Override
  public Node getSettingsPane() {
    return new Label("Graph Settings");
  }

}
