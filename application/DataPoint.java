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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
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
  public List<Integer> confirmedList;
  public List<Integer> deathsList;
  public List<Integer> recoveredList;
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


  private final SimpleDoubleProperty lat;

  public Double getLat() {
    return lat.get();
  }

  public void setLat(Double lat) {
    this.lat.set(lat);
  }

  private final SimpleDoubleProperty lon;

  public Double getLon() {
    return lon.get();
  }

  public void setLon(Double lon) {
    this.lon.set(lon);
  }


  public DataPoint() {
    city = new SimpleStringProperty();
    state = new SimpleStringProperty();
    country = new SimpleStringProperty();
    lat = new SimpleDoubleProperty();
    lon = new SimpleDoubleProperty();

  }

  public DataPoint(String key, String[] testNum, Integer[] confirmedList, Integer[] deathsList,
      Integer[] recoveredList) {
    this();
    this.key = key;
    this.dataArray = testNum.clone();
    this.confirmedList = new ArrayList<>(Arrays.asList(confirmedList));
    this.deathsList = new ArrayList<>(Arrays.asList(deathsList));
    this.recoveredList = new ArrayList<>(Arrays.asList(recoveredList));
    this.parseData();
  }

  public DataPoint(DataPoint data) {
    this();
    this.key = data.key;
    this.dataArray = data.dataArray.clone();
    this.confirmedList = new ArrayList<>(data.confirmedList);
    this.deathsList = new ArrayList<>(data.deathsList);
    this.recoveredList = new ArrayList<>(data.recoveredList);
    this.parseData();
  }

  public DataPoint(String key, DataPoint data) {
    this();
    this.key = key;
    this.dataArray = data.dataArray.clone();
    this.confirmedList = new ArrayList<>(data.confirmedList);
    this.deathsList = new ArrayList<>(data.deathsList);
    this.recoveredList = new ArrayList<>(data.recoveredList);
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
    double latTrim = 0;
    double lonTrim = 0;
    if (!dataArray[4].isEmpty()) {
      latTrim = new BigDecimal(Double.valueOf(dataArray[4])).setScale(8, RoundingMode.HALF_UP)
          .doubleValue();
    }
    if (!dataArray[5].isEmpty()) {
      lonTrim = new BigDecimal(Double.valueOf(dataArray[5])).setScale(8, RoundingMode.HALF_UP)
          .doubleValue();
    }

    lat.set(latTrim);
    lon.set(lonTrim);
    updateData();
  }

  private void updateData() {
    dataArray[1] = city.get();
    dataArray[2] = state.get();
    dataArray[3] = country.get();
    dataArray[4] = Double.toString(lat.get());
    dataArray[5] = Double.toString(lon.get());
    dataArray[0] = key;
  }

  public void increment(DataPoint data) {

    for (int i = 0; i < confirmedList.size(); i++) {
      confirmedList.set(i, confirmedList.get(i) + data.confirmedList.get(i));
    }
    for (int i = 0; i < deathsList.size(); i++) {
      deathsList.set(i, deathsList.get(i) + data.deathsList.get(i));
    }
    for (int i = 0; i < recoveredList.size(); i++) {
      recoveredList.set(i, recoveredList.get(i) + data.recoveredList.get(i));
    }
    updateData();

  }

  @Override
  public String toString() {
    String rtn = key + ": ";
    for (int i = 0; i < 6; i++) {
    
      rtn += labels[i] + ":" + dataArray[i] + " ";
      
    }
    return rtn;
  }

}
