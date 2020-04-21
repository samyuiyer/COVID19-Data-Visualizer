package application;

import java.util.List;
import java.util.function.Predicate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
			dm.loadTries("data_test.txt");
		} catch (Exception e) {
		}
		initTv();
		initSp();
	}

	@SuppressWarnings("unchecked")
	private void initTv() {

		TableColumn<DataPoint, String> location = new TableColumn<>("Location");
		location.setStyle("-fx-background-color:#C69DB8");
		TableColumn<DataPoint, String> city = new TableColumn<>("City");
		city.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("city"));
		city.setStyle("-fx-background-color:#E8D1E3");

		TableColumn<DataPoint, String> state = new TableColumn<>("Province/State");
		state.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("state"));
		state.setStyle("-fx-background-color:#F6E8F4");

		TableColumn<DataPoint, String> country = new TableColumn<>("Country/Region");
		country.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("country"));
		country.setStyle("-fx-background-color:#E8D1E3");

		TableColumn<DataPoint, String> lat = new TableColumn<>("Lat");
		lat.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("lat"));
		lat.setStyle("-fx-background-color:#F6E8F4");

		TableColumn<DataPoint, String> lon = new TableColumn<>("Long");
		lon.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("lon"));
		lon.setStyle("-fx-background-color:#E8D1E3");

		TableColumn<DataPoint, String> stats = new TableColumn<>("Stats");
		stats.setStyle("-fx-background-color:#DD7373");

		TableColumn<DataPoint, String> confirmed = new TableColumn<>("Confirmed");
		confirmed.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("confirmed"));
		confirmed.setStyle("-fx-background-color:#F5BFBD");

		TableColumn<DataPoint, String> deaths = new TableColumn<>("Deaths");
		deaths.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("deaths"));
		deaths.setStyle("-fx-background-color:#EEAAA8");

		TableColumn<DataPoint, String> recovered = new TableColumn<>("Recovered");
		recovered.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("recovered"));
		recovered.setStyle("-fx-background-color:#F5BFBD");

		TableColumn<DataPoint, String> active = new TableColumn<>("Active");
		active.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("active"));
		active.setStyle("-fx-background-color:#EEAAA8");

		location.getColumns().addAll(city, state, country, lat, lon);
		stats.getColumns().addAll(confirmed, deaths, recovered, active);
		tv.getColumns().setAll(location, stats);
		tv.setItems(getInitialTableData());
		tv.setPlaceholder(new Label("No rows to display"));
	}

	private FilteredList<DataPoint> getInitialTableData() {

		List<DataPoint> list = dm.at.getAll();
		ObservableList<DataPoint> data = FXCollections.observableList(list);
		filteredList = new FilteredList<>(data);

		// to filter
		filteredList.setPredicate(new Predicate<DataPoint>() {
			public boolean test(DataPoint t) {
				return true; // or true
			}
		});
		return this.filteredList;
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
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					cityFilter.clear();
				}
				if (oldValue) {
					if(cityFilter.getText().isBlank()) {
						cityFilter.setText("Filter City");
					}
				}
			}

		});

		Button setFilter = new Button("Set Filter");
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

}
