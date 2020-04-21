package application;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;

public class Graph extends DisplayMode {

  @Override
  void reset() {

  }

  @Override
  public Node getDisplayPane() {
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
    return lineChart;
  }

  @Override
  public Node getSettingsPane() {
    return new Label("Graph Settings");
  }

}
