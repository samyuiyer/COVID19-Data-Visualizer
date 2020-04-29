package application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class Graph extends DisplayMode {

  private DataManager dm;
  private String[] timeLabels;
  private boolean slidersVisible;
  private VBox settings;
  private Slider sliderStart;
  private Slider sliderEnd;
  private LineChart<String, Number> chart;
  private CategoryAxis xAxis;
  private NumberAxis yAxis;
  private XYChart.Series<String, Number> series;
  private String scopeName, dataName;
  private final String[] SCOPE_NAMES = {"Global", "Country", "State", "City"};
  private final String[] DATA_NAMES = {"Confirmed", "Dead", "Recovered"};
  private ComboBox<DataPoint> countryBox;
  private ComboBox<DataPoint> stateBox;
  private ComboBox<DataPoint> cityBox;
  private ToggleGroup scope;

  Graph(DataManager dm) {
    super();
    this.dm = dm;

    timeLabels = dm.getTimeLabels();
    slidersVisible = true;
    setupSettings();

    scopeName = SCOPE_NAMES[0];
    dataName = DATA_NAMES[0];
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
  public Node getDisplayPane() {
    return chart;
  }

  private void updateChart() {
    xAxis.setLabel("Date");
    yAxis.setLabel("Number of " + dataName);
    chart.setTitle(dataName + " cases, " + scopeName);

    List<DataPoint> list = dm.gt.getAll();

    Collection<XYChart.Data<String, Number>> col = new ArrayList<>();

    DataPoint d = list.get(0);
    
    if (scopeName != null) {
      if (scopeName.equals(SCOPE_NAMES[0])) {
        d = list.get(0);
      } else if (scopeName.equals(SCOPE_NAMES[1])) {
        System.out.println(countryBox.getValue());
        d = countryBox.getValue();
      } else if (scopeName.equals(SCOPE_NAMES[2])) {
        System.out.println(stateBox.getValue());
        d = stateBox.getValue();
      } else if (scopeName.equals(SCOPE_NAMES[3])) {
        System.out.println(cityBox.getValue());
        d = cityBox.getValue();
      } else {
        // use default value if Global
      }
    } else {
   // use default value if Global
    }

    if (dataName.equals(DATA_NAMES[0])) {
      for (int time = (int) sliderStart.getValue(); time < (int) sliderEnd.getValue(); time++) {
        col.add(new XYChart.Data<String, Number>(timeLabels[time], d.confirmedList.get(time)));
        time++;
      }
    }

    if (dataName.equals(DATA_NAMES[1])) {
      for (int time = (int) sliderStart.getValue(); time < (int) sliderEnd.getValue(); time++) {
        col.add(new XYChart.Data<String, Number>(timeLabels[time], d.deathsList.get(time)));
        time++;
      }
    }

    if (dataName.equals(DATA_NAMES[2])) {
      for (int time = (int) sliderStart.getValue(); time < (int) sliderEnd.getValue(); time++) {
        col.add(new XYChart.Data<String, Number>(timeLabels[time], d.recoveredList.get(time)));
        time++;
      }
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
    Label range = new Label("Time Range: " + timeLabels[(int) sliderStart.getValue()] + " to "
        + timeLabels[(int) sliderEnd.getValue()]);

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
        range.setText("Time Range: " + timeLabels[(int) sliderStart.getValue()] + " to "
            + timeLabels[(int) sliderEnd.getValue()]);
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
        range.setText("Time Range: " + timeLabels[(int) sliderStart.getValue()] + " to "
            + timeLabels[(int) sliderEnd.getValue()]);
        updateChart();
      }
    };
    sliderStart.valueProperty().addListener(startListener);
    sliderEnd.valueProperty().addListener(endListener);

    Label scopeLabel = new Label("Scope:");
    scope = new ToggleGroup();
    RadioButton gl = new RadioButton("Global");
    RadioButton cn = new RadioButton("Country");
    RadioButton st = new RadioButton("State");
    RadioButton ct = new RadioButton("City");
    gl.setToggleGroup(scope);
    cn.setToggleGroup(scope);
    st.setToggleGroup(scope);
    ct.setToggleGroup(scope);
    gl.setSelected(true);

    scope.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle,
          Toggle new_toggle) {
        if (scope.getSelectedToggle() != null) {
          if (((Labeled) scope.getSelectedToggle()).getText().equals(SCOPE_NAMES[0])) {
            scopeName = SCOPE_NAMES[0];
          }
          if (((Labeled) scope.getSelectedToggle()).getText().equals(SCOPE_NAMES[1])) {
            scopeName = SCOPE_NAMES[1];
          }
          if (((Labeled) scope.getSelectedToggle()).getText().equals(SCOPE_NAMES[2])) {
            scopeName = SCOPE_NAMES[2];
          }
          if (((Labeled) scope.getSelectedToggle()).getText().equals(SCOPE_NAMES[3])) {
            scopeName = SCOPE_NAMES[3];
          }
        }
        updateChart();
      }
    });

    final ToggleGroup data = new ToggleGroup();
    Label dataLabel = new Label("Data:");
    RadioButton c = new RadioButton("Confirmed");
    RadioButton d = new RadioButton("Dead");
    RadioButton r = new RadioButton("Recovered");
    c.setToggleGroup(data);
    d.setToggleGroup(data);
    r.setToggleGroup(data);
    c.setSelected(true);

    data.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle,
          Toggle new_toggle) {
        if (data.getSelectedToggle() != null) {
          if (((Labeled) data.getSelectedToggle()).getText().equals(DATA_NAMES[0]))
            dataName = DATA_NAMES[0];
          if (((Labeled) data.getSelectedToggle()).getText().equals(DATA_NAMES[1]))
            dataName = DATA_NAMES[1];
          if (((Labeled) data.getSelectedToggle()).getText().equals(DATA_NAMES[2]))
            dataName = DATA_NAMES[2];
        }
        updateChart();
      }
    });
    countryBox = new ComboBox<>();
    stateBox = new ComboBox<>();
    cityBox = new ComboBox<>();
    try {
      cityBox.setItems(FXCollections.observableList(
          filter(dm.at.suggest(cityBox.getEditor().getText()), true, false, false)));
      cityBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
        try {
          cityBox.setItems(FXCollections.observableList(
              filter(dm.at.suggest(cityBox.getEditor().getText()), true, false, false)));
        } catch (IllegalNullKeyException e) {
        }
      });

      stateBox.setItems(FXCollections.observableList(
          filter(dm.at.suggest(stateBox.getEditor().getText()), false, true, false)));
      stateBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
        try {
          stateBox.setItems(FXCollections.observableList(
              filter(dm.at.suggest(stateBox.getEditor().getText()), false, true, false)));
        } catch (IllegalNullKeyException e) {
        }
      });

      countryBox.setItems(FXCollections.observableList(
          filter(dm.at.suggest(countryBox.getEditor().getText()), false, false, true)));
      countryBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
        try {
          countryBox.setItems(FXCollections.observableList(
              filter(dm.at.suggest(countryBox.getEditor().getText()), false, false, true)));
        } catch (IllegalNullKeyException e) {
        }
      });

    } catch (IllegalNullKeyException e) {

      e.printStackTrace();
    }
    countryBox.setEditable(true);
    stateBox.setEditable(true);
    cityBox.setEditable(true);
    countryBox.managedProperty().bind(countryBox.visibleProperty());
    countryBox.visibleProperty().bind(cn.selectedProperty());
    stateBox.managedProperty().bind(stateBox.visibleProperty());
    stateBox.visibleProperty().bind(st.selectedProperty());
    cityBox.managedProperty().bind(cityBox.visibleProperty());
    cityBox.visibleProperty().bind(ct.selectedProperty());

    settings.getChildren().addAll(time, sliderLabel, sliderStart, sliderEnd, range, scopeLabel, gl,
        cn, countryBox, st, stateBox, ct, cityBox, dataLabel, c, d, r);
  }

  private List<DataPoint> filter(List<DataPoint> dataList, boolean city, boolean state,
      boolean country) {
    Iterator<DataPoint> itr = dataList.iterator();
    while (itr.hasNext()) {
      DataPoint d = itr.next();
      if (!d.filter(city, state, country)) {
        itr.remove();
      }
    }
    return dataList;
  }

}
