package application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class Graph extends DisplayMode {

  DataManager dm;
  String[] timeLabels;
  private boolean slidersVisible;
  VBox settings;
  Slider sliderStart;
  Slider sliderEnd;
  LineChart<String, Number> chart;
  CategoryAxis xAxis;
  NumberAxis yAxis;
  XYChart.Series<String, Number> series;

  Graph() {
    super();
    dm = new DataManager();
    try {
      dm.loadTries();
    } catch (Exception e) {
      e.printStackTrace();
    }
    timeLabels = dm.getTimeLabels();
    slidersVisible = true;
    setupSettings();
    xAxis = new CategoryAxis();
    yAxis = new NumberAxis();
    xAxis.setAnimated(false);
    yAxis.setAnimated(false);
    chart = new LineChart<String, Number>(xAxis, yAxis);
    chart.setAnimated(false);
    series = new XYChart.Series<String, Number>();
    updateChart();
    chart.getData().add(series);
  }

  @Override
  void reset() {

  }

  @Override
  public Node getDisplayPane() {
    return chart;
  }

  private void updateChart() {
    xAxis.setLabel("Date");
    yAxis.setLabel("Number of X(confrimed)");
    chart.setTitle("X Data(confrimed)");

    List<DataPoint> list = dm.gt.getAll();
    DataPoint d = list.get(0);

    // // clear
    // List<XYChart.Data<String, Number>> toRemove = new ArrayList<>();
    // for (XYChart.Data<String, Number> s : series.getData()) {
    // toRemove.add(s);
    // }
    // series.getData().removeAll(toRemove);

    Collection<XYChart.Data<String, Number>> col = new ArrayList<>();

    for (int time = (int) sliderStart.getValue(); time < (int) sliderEnd.getValue(); time++) {
      col.add(new XYChart.Data<String, Number>(timeLabels[time], d.confirmedList.get(time)));
      // series.getData()
      // .add(new XYChart.Data<String, Number>(timeLabels[time], d.confirmedList.get(time)));
      time++;
    }
    series.getData().setAll(col);
  }

  @Override
  public Node getSettingsPane() {
    return settings;
  }

  private void setupSettings() {
    settings = new VBox();
    Button time = new Button("Time Range");

    // setup time range slider and label
    Label sliderLabel = new Label("Choose Time Range:");
    sliderStart = new Slider(0, 94, 0);
    sliderEnd = new Slider(0, 94, 94);
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
    sliderLabel.managedProperty().bind(sliderLabel.visibleProperty());
    sliderStart.managedProperty().bind(sliderStart.visibleProperty());
    sliderEnd.managedProperty().bind(sliderEnd.visibleProperty());
    range.managedProperty().bind(range.visibleProperty());
    sliderStart.setVisible(slidersVisible);
    sliderEnd.setVisible(slidersVisible);

    time.setOnAction(new EventHandler<ActionEvent>() {
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
        updateChart();
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
        updateChart();
      }
    };
    sliderStart.valueProperty().addListener(startListener);
    sliderEnd.valueProperty().addListener(endListener);

    settings.getChildren().addAll(time, sliderLabel, sliderStart, sliderEnd, range);
  }

}
