package application;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class Table extends DisplayMode {



  TableView<DataPoint> tv;
  VBox sp;
  DataManager dm;
  FilteredList<DataPoint> filteredList;

  public Table() {
    super();
    title = "table";
    tv = new TableView<>();
    dm = new DataManager();
    sp = new VBox();
    try {
      dm.loadTries();
    } catch (Exception e) {
    }
    initTv();
    initSp();
  }

  @SuppressWarnings("unchecked")
  private void initTv() {
    tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    TableColumn<DataPoint, String> location = new TableColumn<>("Location");
    TableColumn<DataPoint, String> city = new TableColumn<>("City");
    TableColumn<DataPoint, String> state = new TableColumn<>("Province/State");
    TableColumn<DataPoint, String> country = new TableColumn<>("Country/Region");
    TableColumn<DataPoint, String> lat = new TableColumn<>("Lat");
    TableColumn<DataPoint, String> lon = new TableColumn<>("Long");
    TableColumn<DataPoint, String> stats = new TableColumn<>("Stats");
    TableColumn<DataPoint, String> confirmed = new TableColumn<>("Confirmed");
    TableColumn<DataPoint, String> deaths = new TableColumn<>("Deaths");
    TableColumn<DataPoint, String> recovered = new TableColumn<>("Recovered");
    TableColumn<DataPoint, String> active = new TableColumn<>("Active");

    location.setId("column_header_location");
    city.setId("column_city");
    state.setId("column_state");
    country.setId("column_country");
    lat.setId("column_lat");
    lon.setId("column_lon");
    stats.setId("column_header_stats");
    confirmed.setId("column_confirmed");
    deaths.setId("column_deaths");
    recovered.setId("column_recovered");
    active.setId("column_active");

    city.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("city"));
    city.setComparator(getComp(city));
    state.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("state"));
    state.setComparator(getComp(state));
    country.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("country"));
    country.setComparator(getComp(country));
    lat.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("lat"));
    lon.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("lon"));
    confirmed.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("confirmed"));
    deaths.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("deaths"));
    recovered.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("recovered"));
    active.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("active"));

    location.getColumns().addAll(city, state, country, lat, lon);
    stats.getColumns().addAll(confirmed, deaths, recovered, active);
    tv.getColumns().setAll(location, stats);
    tv.setItems(getInitialTableData());

    tv.setPlaceholder(new Label("No rows to display"));
  }

  private SortedList<DataPoint> getInitialTableData() {
    List<DataPoint> list = dm.gt.getAll();
    ObservableList<DataPoint> data = FXCollections.observableList(list);
    filteredList = new FilteredList<>(data);

    // to filter
    filteredList.setPredicate(new Predicate<DataPoint>() {
      public boolean test(DataPoint t) {
        return true; // or true
      }
    });
    SortedList<DataPoint> sortableData = new SortedList<>(this.filteredList);
    sortableData.comparatorProperty().bind(tv.comparatorProperty());
    return sortableData;
  }

  @Override
  void reset() {

  }

  @Override
  public Node getDisplayPane() {
    return tv;
  }

  private void initSp() {
    TextField cityFilter = new TextField("Filter City");
    TextField stateFilter = new TextField("Filter State");
    TextField countryFilter = new TextField("Filter Country");

    cityFilter.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
          Boolean newValue) {
        if (newValue) {
          cityFilter.clear();
        }
        if (oldValue) {
          if (cityFilter.getText().isBlank()) {
            cityFilter.setText("Filter City");
          }
        }
      }

    });

    stateFilter.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
          Boolean newValue) {
        if (newValue) {
          stateFilter.clear();
        }
        if (oldValue) {
          if (stateFilter.getText().isBlank()) {
            stateFilter.setText("Filter State");
          }
        }
      }

    });

    countryFilter.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
          Boolean newValue) {
        if (newValue) {
          countryFilter.clear();
        }
        if (oldValue) {
          if (countryFilter.getText().isBlank()) {
            countryFilter.setText("Filter Country");
          }
        }
      }

    });

    Button setFilter = new Button("Set Filter");
    setFilter.setId("set-filter-btn");
    setFilter.setOnAction(new EventHandler<ActionEvent>() { // button should hide time sliders and
                                                            // labels
      @Override
      public void handle(ActionEvent event) {
        filteredList.setPredicate(new Predicate<DataPoint>() {
          public boolean test(DataPoint t) {
            boolean checkCountry = t.getCountry().equals(countryFilter.getText())
                || countryFilter.getText().equals("Filter Country");
            boolean checkState = t.getState().equals(stateFilter.getText())
                || stateFilter.getText().equals("Filter State");
            boolean checkCity = t.getCity().equals(cityFilter.getText())
                || cityFilter.getText().equals("Filter City");
            return checkCountry && checkCity && checkState;
          }
        });
      }
    });

    Button resetFilter = new Button("Reset Filter");
    resetFilter.setId("reset-filter-btn");

    resetFilter.setOnAction(new EventHandler<ActionEvent>() { // button should hide time sliders and
                                                              // // labels
      @Override
      public void handle(ActionEvent event) {
        cityFilter.setText("Filter City");
        stateFilter.setText("Filter State");
        countryFilter.setText("Filter Country");
        filteredList.setPredicate(new Predicate<DataPoint>() {
          public boolean test(DataPoint t) {
            boolean checkCountry = t.getCountry().equals(countryFilter.getText())
                || countryFilter.getText().equals("Filter Country");
            boolean checkState = t.getState().equals(stateFilter.getText())
                || stateFilter.getText().equals("Filter State");
            boolean checkCity = t.getCity().equals(cityFilter.getText())
                || cityFilter.getText().equals("Filter City");
            return checkCountry && checkCity && checkState;
          }
        });
      }
    });
    sp.getChildren().addAll(cityFilter, stateFilter, countryFilter, setFilter, resetFilter);
  }

  @Override
  public Node getSettingsPane() {
    return sp;
  }

  private Comparator<String> getComp(TableColumn<DataPoint, String> tc) {
    Comparator<String> comparator = (o1, o2) -> {
      final boolean isDesc = tc.getSortType() == SortType.DESCENDING;
      if (o1.equals("") && o2.equals(""))
        return 0;
      else if (o1.equals("") && !o2.equals(""))
        return isDesc ? -1 : 1;
      else if (!o1.equals("") && o2.equals(""))
        return isDesc ? 1 : -1;
      else
        return o1.compareTo(o2);
    };
    return comparator;
  }

}
