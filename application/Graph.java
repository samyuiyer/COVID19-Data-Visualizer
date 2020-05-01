package application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
  private ToggleGroup scopeToggleGroup;

  Graph(DataManager dm) {
    super();
    this.dm = dm;
    timeLabels = dm.getTimeLabels();
    slidersVisible = true;
    scopeName = SCOPE_NAMES[0];
    dataName = DATA_NAMES[0];
    setupChart();

    setupSettings();
    updateChart();

  }

  @Override
  public Node getDisplayPane() {
    return chart;
  }

  @Override
  public Node getSettingsPane() {
    return settings;
  }

  private void setupChart() {
    xAxis = new CategoryAxis();
    yAxis = new NumberAxis();
    xAxis.setAnimated(false);
    yAxis.setAnimated(false);
    chart = new LineChart<String, Number>(xAxis, yAxis);
    chart.setAnimated(false);
    series = new XYChart.Series<String, Number>();
    chart.getData().add(series);
  }

  private void updateChart() {
    xAxis.setLabel("Date");
    yAxis.setLabel("Number of " + dataName);

    List<DataPoint> list = dm.gt.getAll();

    Collection<XYChart.Data<String, Number>> col = new ArrayList<>();

    DataPoint d = list.get(0);
    if (scopeName.equals(SCOPE_NAMES[0])) {
      d = list.get(0);
    } else if (scopeName.equals(SCOPE_NAMES[1])) {
      d = countryBox.getValue() == null ? d : countryBox.getValue();
    } else if (scopeName.equals(SCOPE_NAMES[2])) {
      d = stateBox.getValue() == null ? d : stateBox.getValue();
    } else if (scopeName.equals(SCOPE_NAMES[3])) {
      d = cityBox.getValue() == null ? d : cityBox.getValue();
    }
    chart.setTitle(dataName + " cases, " + d.key);

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

  private void setupSettings() {
    settings = new VBox();
    Button timeSlider = new Button("Time Range");

    // setup time range slider and label
    Label sliderLabel = new Label("Choose Time Range:");
    sliderStart = new Slider(0, 94, 0);
    sliderEnd = new Slider(0, 94, 94);
    Label rangeLabel = new Label("Time Range: " + timeLabels[(int) sliderStart.getValue()] + " to "
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
    rangeLabel.managedProperty().bind(rangeLabel.visibleProperty());
    sliderStart.setVisible(slidersVisible);
    sliderEnd.setVisible(slidersVisible);

    timeSlider.setOnAction(e -> {
        if (slidersVisible) {
          sliderLabel.setVisible(false);
          sliderStart.setVisible(false);
          sliderEnd.setVisible(false);
          rangeLabel.setVisible(false);
          slidersVisible = false;
        } else {
          sliderLabel.setVisible(true);
          sliderStart.setVisible(true);
          sliderEnd.setVisible(true);
          rangeLabel.setVisible(true);
          slidersVisible = true;
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
        rangeLabel.setText("Time Range: " + timeLabels[(int) sliderStart.getValue()] + " to "
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
        rangeLabel.setText("Time Range: " + timeLabels[(int) sliderStart.getValue()] + " to "
            + timeLabels[(int) sliderEnd.getValue()]);
        updateChart();
      }
    };
    sliderStart.valueProperty().addListener(startListener);
    sliderEnd.valueProperty().addListener(endListener);

    Label scopeLabel = new Label("Scope:");
    scopeToggleGroup = new ToggleGroup();
    RadioButton globalRadio = new RadioButton("Global");
    RadioButton countryRadio = new RadioButton("Country");
    RadioButton stateRadio = new RadioButton("State");
    RadioButton cityRadio = new RadioButton("City");
    globalRadio.setToggleGroup(scopeToggleGroup);
    countryRadio.setToggleGroup(scopeToggleGroup);
    stateRadio.setToggleGroup(scopeToggleGroup);
    cityRadio.setToggleGroup(scopeToggleGroup);
    globalRadio.setSelected(true);

    scopeToggleGroup.selectedToggleProperty().addListener((ov, ot, nt) -> {
      for (int i = 0; i < 4; i++)
        if (((Labeled) scopeToggleGroup.getSelectedToggle()).getText().equals(SCOPE_NAMES[i]))
          scopeName = SCOPE_NAMES[i];
      updateChart();
    });

    final ToggleGroup dataToggleGroup = new ToggleGroup();
    Label dataLabel = new Label("Data:");
    RadioButton confRadio = new RadioButton("Confirmed");
    RadioButton deadRadio = new RadioButton("Dead");
    RadioButton recovRadio = new RadioButton("Recovered");
    confRadio.setToggleGroup(dataToggleGroup);
    deadRadio.setToggleGroup(dataToggleGroup);
    recovRadio.setToggleGroup(dataToggleGroup);
    confRadio.setSelected(true);

    dataToggleGroup.selectedToggleProperty().addListener((ov, ot, nt) -> {
      for (int i = 0; i < 3; i++)
        if (((Labeled) dataToggleGroup.getSelectedToggle()).getText().equals(DATA_NAMES[i]))
          dataName = DATA_NAMES[i];
      updateChart();
    });
    countryBox = new ComboBox<>();
    stateBox = new ComboBox<>();
    cityBox = new ComboBox<>();


    countryBox.valueProperty().addListener((ov, t, t1) -> {
      updateChart();
    });
    stateBox.valueProperty().addListener((ov, t, t1) -> {
      updateChart();
    });
    cityBox.valueProperty().addListener((ov, t, t1) -> {
      updateChart();
    });

    countryBox.setItems(FXCollections.observableList(filter(dm.gt.getAll(), false, false, true)));
    stateBox.setItems(FXCollections.observableList(filter(dm.gt.getAll(), false, true, false)));
    cityBox.setItems(FXCollections.observableList(filter(dm.gt.getAll(), true, false, false)));
    countryBox.managedProperty().bind(countryBox.visibleProperty());
    countryBox.visibleProperty().bind(countryRadio.selectedProperty());
    stateBox.managedProperty().bind(stateBox.visibleProperty());
    stateBox.visibleProperty().bind(stateRadio.selectedProperty());
    cityBox.managedProperty().bind(cityBox.visibleProperty());
    cityBox.visibleProperty().bind(cityRadio.selectedProperty());

    settings.getChildren().addAll(timeSlider, sliderLabel, sliderStart, sliderEnd, rangeLabel,
        scopeLabel, globalRadio, countryRadio, countryBox, stateRadio, stateBox, cityRadio, cityBox,
        dataLabel, confRadio, deadRadio, recovRadio);
  }

  private List<DataPoint> filter(List<DataPoint> dataList, boolean city, boolean state,
      boolean country) {
    Iterator<DataPoint> itr = dataList.iterator();
    while (itr.hasNext()) {
      DataPoint d = itr.next();
      if (d == null || !d.filter(city, state, country)) {
        itr.remove();
      }
    }

    return dataList.stream().sorted((o1, o2) -> {
      return o1.toString().compareTo(o2.toString());
    }).collect(Collectors.toList());
  }

  @Override
  public void refresh() {
    updateChart();
  }
}
