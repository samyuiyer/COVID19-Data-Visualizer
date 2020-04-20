package application;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Table extends DisplayMode {
  TableView<DataPoint> tv;
  DataManager dm;

  @SuppressWarnings("unchecked")
  public Table() {
    super();
    title = "table";
    tv = new TableView<>();
    dm = new DataManager();
    try {
      dm.loadTries("data_test.txt");
    } catch (Exception e) {
    }

    TableColumn<DataPoint, String> location = new TableColumn<>("Location");
    TableColumn<DataPoint, String> city = new TableColumn<>("City");
    city.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("city"));
    TableColumn<DataPoint, String> state = new TableColumn<>("Province_State");
    state.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("state"));
    TableColumn<DataPoint, String> country = new TableColumn<>("Country_Region");
    country.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("country"));
    TableColumn<DataPoint, String> lat = new TableColumn<>("Lat");
    lat.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("lat"));
    TableColumn<DataPoint, String> lon = new TableColumn<>("Long");
    lon.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("lon"));


    TableColumn<DataPoint, String> stats = new TableColumn<>("Stats");
    TableColumn<DataPoint, String> confirmed = new TableColumn<>("confirmed");
    confirmed.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("confirmed"));
    TableColumn<DataPoint, String> deaths = new TableColumn<>("Deaths");
    deaths.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("deaths"));
    TableColumn<DataPoint, String> recovered = new TableColumn<>("Recovered");
    recovered.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("recovered"));
    TableColumn<DataPoint, String> active = new TableColumn<>("Active");
    active.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("active"));



    location.getColumns().addAll(city, state, country, lat, lon);
    stats.getColumns().addAll(confirmed, deaths, recovered, active);
    tv.getColumns().setAll(location, stats);
    tv.setItems(getInitialTableData());
    tv.setPlaceholder(new Label("No rows to display"));
  }

  private ObservableList<DataPoint> getInitialTableData() {

    List<DataPoint> list = dm.gt.getAll();
    ObservableList<DataPoint> data = FXCollections.observableList(list);

    return data;
  }

  @Override
  void reset() {

  }

  @Override
  public Node getDiplayPane() {
    return tv;
  }

  @Override
  public Node getSettingsPane() {
    return null;
  }

}
