package application;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class Graph extends DisplayMode {

  DataManager dm;
  String[] timeLabels;

  Graph() {
    super();
    dm = new DataManager();
    try {
      dm.loadTries();
    } catch (Exception e) {
      e.printStackTrace();
    }
    timeLabels = dm.getTimeLabels();
  }

  @Override
  void reset() {

  }

  @Override
  public Node getDisplayPane() {
    final CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel("Date");
    final NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Number of X(confrimed)");
    final LineChart<String, Number> chart = new LineChart<String, Number>(xAxis, yAxis);
    XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

    chart.setTitle("X Data(confrimed)");
    List<DataPoint> list = dm.gt.getAll();
    DataPoint d = list.get(0);
    for (int time = 0; time < 95; time++) {
      series.getData()
          .add(new XYChart.Data<String, Number>(timeLabels[time], d.confirmedList.get(time)));
      time++;
    }

    chart.getData().add(series);

    return chart;
  }

  @Override
  public Node getSettingsPane() {
    return new Label("Graph Settings");
  }

}
