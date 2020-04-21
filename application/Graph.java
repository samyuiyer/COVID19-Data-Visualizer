package application;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;

public class Graph extends DisplayMode {

  DataManager dm;

  Graph() {
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
    chart.setTitle("Corona Time");
    return chart;
  }

  @Override
  public Node getSettingsPane() {
    return new Label("Graph Settings");
  }

}
