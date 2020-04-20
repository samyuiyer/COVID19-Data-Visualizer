/**
 *
 * Author: Ankur Garg Email: Agarg34@wisc.edu
 * 
 * Course: CS400 Semester: Spring2020 Lecture: 001 Date: Apr 16, 2020
 *
 * Files: DataPoint.java
 *
 */
package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author ankurgarg
 *
 */
public class DataPoint {
  /**
   * @param data
   */

  public String key;
  public String[] dataArray;
  public static String[] labels;

  private SimpleStringProperty city;

  public String getCity() {
    return city.get();
  }

  public void setCity(String city) {
    this.city.set(city);
  }

  private final SimpleStringProperty state;

  public String getState() {
    return state.get();
  }

  public void setState(String state) {
    this.state.set(state);
  }

  private final SimpleStringProperty country;

  public String getCountry() {
    return country.get();
  }

  public void setCountry(String country) {
    this.country.set(country);
  }

  private final SimpleStringProperty update;

  public String getUpdate() {
    return update.get();
  }

  public void setUpdate(String update) {
    this.update.set(update);
  }

  private final SimpleStringProperty lat;

  public String getLat() {
    return lat.get();
  }

  public void setLat(String lat) {
    this.lat.set(lat);
  }

  private final SimpleStringProperty lon;

  public String getLon() {
    return lon.get();
  }

  public void setLon(String lon) {
    this.lon.set(lon);
  }

  private final SimpleIntegerProperty confirmed;

  public int getConfirmed() {
    return confirmed.get();
  }

  public void setConfirmed(int confirmed) {
    this.confirmed.set(confirmed);
  }

  private final SimpleIntegerProperty deaths;

  public int getDeaths() {
    return deaths.get();
  }

  public void setDeaths(int deaths) {
  }

  private final SimpleIntegerProperty recovered;

  public int getRecovered() {
    return recovered.get();
  }

  public void setRecovered(int recovered) {
    this.recovered.set(recovered);
  }

  private final SimpleIntegerProperty active;

  public int getActive() {
    return active.get();
  }

  public void setActive(int active) {
    this.active.set(active);
  }

  public DataPoint() {
    city = new SimpleStringProperty();
    state = new SimpleStringProperty();
    country = new SimpleStringProperty();
    update = new SimpleStringProperty();
    lat = new SimpleStringProperty();
    lon = new SimpleStringProperty();
    confirmed = new SimpleIntegerProperty();
    deaths = new SimpleIntegerProperty();
    recovered = new SimpleIntegerProperty();
    active = new SimpleIntegerProperty();
  }

  public DataPoint(String key, String[] testNum) {
    this();
    this.key = key;
    this.dataArray = testNum;
    this.parseData();
  }

  public DataPoint(DataPoint data) {
    this();
    this.key = data.key;
    this.dataArray = data.dataArray;
    this.parseData();
  }

  public DataPoint(String key, DataPoint data) {
    this();
    this.key = key;
    this.dataArray = data.dataArray;
    this.parseData();
  }

  private void parseData() {
    String[] keySplit = key.replace(", ", ",").split(",");
    if (keySplit.length == 1) {
      country.set(keySplit[0]);
      state.set("");
      city.set("");
    } else if (keySplit.length == 2) {
      city.set("");
      state.set(keySplit[0]);
      country.set(keySplit[1]);
    } else {
      city.set(keySplit[0]);
      state.set(keySplit[1]);
      country.set(keySplit[2]);
    }
    update.set(dataArray[4]);
    lat.set(dataArray[5]);
    lon.set(dataArray[6]);
    if (dataArray[7] != null) {
      confirmed.set(Integer.parseInt(dataArray[7]));
      deaths.set(Integer.parseInt(dataArray[8]));
      recovered.set(Integer.parseInt(dataArray[9]));
      active.set(Integer.parseInt(dataArray[10]));
    }
    updateData();
  }

  private void updateData() {
    dataArray[1] = city.get();
    dataArray[2] = state.get();
    dataArray[3] = country.get();
    dataArray[4] = update.get();
    dataArray[5] = lat.get();
    dataArray[6] = lon.get();
    if (dataArray[7] != null) {
      dataArray[7] = Integer.toString(confirmed.get());
      dataArray[8] = Integer.toString(deaths.get());
      dataArray[9] = Integer.toString(recovered.get());
      dataArray[10] = Integer.toString(active.get());
    }
    dataArray[11] = key;
  }

  public void increment(DataPoint data) {
    this.confirmed.set(this.confirmed.get() + data.confirmed.get());
    this.deaths.set(this.deaths.get() + data.deaths.get());
    this.recovered.set(this.recovered.get() + data.recovered.get());
    this.active.set(this.active.get() + data.active.get());
    updateData();

  }

  @Override
  public String toString() {
    String rtn = key + ": ";
    for (int i = 0; i < labels.length; i++) {
      rtn += labels[i] + ":" + dataArray[i] + " ";
    }
    return rtn;
  }

}
