package application;

import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.util.StringConverter;

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
    xAxis = new CategoryAxis();
    yAxis = new NumberAxis();
    xAxis.setAnimated(false);
    yAxis.setAnimated(false);
    chart = new LineChart<String, Number>(xAxis, yAxis);
    chart.setAnimated(false);
    series = new XYChart.Series<String, Number>();
    setupSettings();
    updateChart();
    chart.getData().add(series);
  }

  @Override
  public Node getDisplayPane() {
    return chart;
  }

  @Override
  public Node getSettingsPane() {
    return settings;
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
        d = countryBox.getValue() == null || countryBox.getValue().dataArray[5].equals("INVALID")
            ? d
            : countryBox.getValue();
      } else if (scopeName.equals(SCOPE_NAMES[2])) {
        d = stateBox.getValue() == null || stateBox.getValue().dataArray[5].equals("INVALID") ? d
            : stateBox.getValue();
      } else if (scopeName.equals(SCOPE_NAMES[3])) {
        d = cityBox.getValue() == null || cityBox.getValue().dataArray[5].equals("INVALID") ? d
            : cityBox.getValue();
      } else {
        // use default value if Global
      }
    } else {
      // use default value if Global
    }
    System.out.println(d);

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

    timeSlider.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
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

    scopeToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle,
          Toggle new_toggle) {
        if (scopeToggleGroup.getSelectedToggle() != null) {
          if (((Labeled) scopeToggleGroup.getSelectedToggle()).getText().equals(SCOPE_NAMES[0])) {
            scopeName = SCOPE_NAMES[0];
          }
          if (((Labeled) scopeToggleGroup.getSelectedToggle()).getText().equals(SCOPE_NAMES[1])) {
            scopeName = SCOPE_NAMES[1];
          }
          if (((Labeled) scopeToggleGroup.getSelectedToggle()).getText().equals(SCOPE_NAMES[2])) {
            scopeName = SCOPE_NAMES[2];
          }
          if (((Labeled) scopeToggleGroup.getSelectedToggle()).getText().equals(SCOPE_NAMES[3])) {
            scopeName = SCOPE_NAMES[3];
          }
        }
        updateChart();
      }
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

    dataToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle,
          Toggle new_toggle) {
        if (dataToggleGroup.getSelectedToggle() != null) {
          if (((Labeled) dataToggleGroup.getSelectedToggle()).getText().equals(DATA_NAMES[0]))
            dataName = DATA_NAMES[0];
          if (((Labeled) dataToggleGroup.getSelectedToggle()).getText().equals(DATA_NAMES[1]))
            dataName = DATA_NAMES[1];
          if (((Labeled) dataToggleGroup.getSelectedToggle()).getText().equals(DATA_NAMES[2]))
            dataName = DATA_NAMES[2];
        }
        updateChart();
      }
    });

    countryBox = new ComboBox<>();
    stateBox = new ComboBox<>();
    cityBox = new ComboBox<>();


    ChangeListener<DataPoint> boxListener = new ChangeListener<DataPoint>() {
      @Override
      public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, DataPoint t,
          DataPoint t1) {
        updateChart();
      }
    };


    StringConverter<DataPoint> dataToString = new StringConverter<DataPoint>() {
      @Override
      public String toString(DataPoint object) {
        if (object == null)
          return null;
        return object.toString();
      }

      @Override
      public DataPoint fromString(String string) {
        // replace this with approquiate implementation of parsing function
        // or lookup function


        DataPoint invalid =
            new DataPoint(string, new String[] {"INVALID", "", "", "", "0", "INVALID"},
                new Integer[95], new Integer[95], new Integer[95]);

        try {
          int index = dm.at.suggest(string).indexOf(invalid);
          return index != -1 ? dm.at.suggest(string).get(index) : invalid;
        } catch (IllegalNullKeyException e) {
          return invalid;
        }

      }
    };


    countryBox.setConverter(dataToString);
    stateBox.setConverter(dataToString);
    cityBox.setConverter(dataToString);

    countryBox.getSelectionModel().selectFirst();
    stateBox.getSelectionModel().selectFirst();
    cityBox.getSelectionModel().selectFirst();
    countryBox.valueProperty().addListener(boxListener);
    stateBox.valueProperty().addListener(boxListener);
    cityBox.valueProperty().addListener(boxListener);

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
    return dataList;
  }

}
