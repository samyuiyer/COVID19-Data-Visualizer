package application;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    tv.setItems(getInitialTableData());
    TableColumn<DataPoint,String> location = new TableColumn<>("Location");
    TableColumn<DataPoint,String> city = new TableColumn<>("City");
    TableColumn<DataPoint,String> state = new TableColumn<>("Province_State");
    TableColumn<DataPoint,String> country = new TableColumn<>("Country_Region");
    TableColumn<DataPoint,String> lat = new TableColumn<>("Lat");
    TableColumn<DataPoint,String> lon = new TableColumn<>("Long");
    
    TableColumn<DataPoint,String> stats = new TableColumn<>("Stats");
    TableColumn<DataPoint,String> cases = new TableColumn<>("Cases");
    TableColumn<DataPoint,String> dead = new TableColumn<>("Dead");
    TableColumn<DataPoint,String>recover = new TableColumn<>("Recover");
    TableColumn<DataPoint,String> active = new TableColumn<>("Active");
    
    
 
    location.getColumns().addAll(city, state,country,lat,lon);
    stats.getColumns().addAll(cases, dead,recover,active);
    tv.getColumns().setAll(location, stats);
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
    // TODO Auto-generated method stub
    return null;
  }

}
