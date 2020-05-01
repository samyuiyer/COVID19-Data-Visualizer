///////////////////////////////////////////////////////////////////////////////
//
// Title: ateam_final_project
// Author: Ankur Garg, Eric Ertl, Justin Chan, Samyu Iyer, Sudeep Reddy
//
// Course: CS400
// Semester: Spring 2020
// Lecture Number: 001
//
// Date: 4/29/2020
//
// Description: A project that displays statistics relating to COVID-19 in a
// variety of ways.
//
///////////////////////////////////////////////////////////////////////////////

package application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DataPoint represents various aspects of COVID-19 data at a particular location. Among these are
 * death counts, confirmed cases counts, and recovered counts.
 */
public class DataPoint {
  /**
   * @param dataRadioBtns
   */

  public String key;
  public String[] dataArray;
  public List<Integer> confirmedList;
  public List<Integer> deathsList;
  public List<Integer> recoveredList;
  public static String[] labels;
  public static int time = 94;

  /**
   * Constructor for DataPoint with all possible args
   * 
   * @param key
   * @param testNum
   * @param confirmedList
   * @param deathsList
   * @param recoveredList
   */
  public DataPoint(String key, String[] testNum, Integer[] confirmedList, Integer[] deathsList,
      Integer[] recoveredList) {
    this.key = key;
    this.dataArray = testNum.clone();
    this.confirmedList = new ArrayList<>(Arrays.asList(confirmedList));
    this.deathsList = new ArrayList<>(Arrays.asList(deathsList));
    this.recoveredList = new ArrayList<>(Arrays.asList(recoveredList));
    this.parseData();
  }

  /**
   * Alternate constructor for DataPoint with data
   * 
   * @param data
   */
  public DataPoint(DataPoint data) {

    this.key = data.key;
    this.dataArray = data.dataArray.clone();
    this.confirmedList = new ArrayList<>(data.confirmedList);
    this.deathsList = new ArrayList<>(data.deathsList);
    this.recoveredList = new ArrayList<>(data.recoveredList);
    this.parseData();
  }

  /**
   * Alternate constructor for DataPoint with key and data
   * 
   * @param key
   * @param data
   */
  public DataPoint(String key, DataPoint data) {

    this.key = key;
    this.dataArray = data.dataArray.clone();
    this.confirmedList = new ArrayList<>(data.confirmedList);
    this.deathsList = new ArrayList<>(data.deathsList);
    this.recoveredList = new ArrayList<>(data.recoveredList);
    this.parseData();
  }

  /**
   * gets city
   * 
   * @return city name
   */
  public String getCity() {
    return dataArray[1];
  }

  /**
   * gets state
   * 
   * @return state name
   */
  public String getState() {
    return dataArray[2];
  }

  /**
   * gets country
   * 
   * @return country name
   */
  public String getCountry() {
    return dataArray[3];
  }

  /**
   * gets latitude
   * 
   * @return latitude
   */
  public Double getLat() {
    try {
      return new BigDecimal(Double.valueOf(dataArray[4])).setScale(8, RoundingMode.HALF_UP)
          .doubleValue();
    } catch (Exception e) {
      return 0.0;
    }

  }

  /**
   * gets longitude
   * 
   * @return longitude
   */
  public Double getLon() {
    try {
      return new BigDecimal(Double.valueOf(dataArray[5])).setScale(8, RoundingMode.HALF_UP)
          .doubleValue();
    } catch (Exception e) {
      return 0.0;
    }
  }

  /**
   * gets confirmed
   * 
   * @return confirmed cases
   */
  public int getConfirmed() {
    return confirmedList.get(time);
  }

  /**
   * gets deaths
   * 
   * @return number of deaths
   */
  public int getDeaths() {
    return deathsList.get(time);
  }

  /**
   * gets recovered
   * 
   * @return number of recovered cases
   */
  public int getRecovered() {
    return recoveredList.get(time);
  }

  /**
   * Parses the data in dataArray
   */
  private void parseData() {
    dataArray[0] = key;
    String[] keySplit = key.replace(", ", ",").split(",");
    if (keySplit.length == 1) {
      dataArray[3] = keySplit[0];
      dataArray[2] = "";
      dataArray[1] = "";
    } else if (keySplit.length == 2) {
      dataArray[1] = "";
      dataArray[2] = keySplit[0];
      dataArray[3] = keySplit[1];
    } else {
      dataArray[1] = keySplit[0];
      dataArray[2] = keySplit[1];
      dataArray[3] = keySplit[2];
    }
  }

  /**
   * Increments all aspects of location's data appropriately based on new data
   * 
   * @param data
   */
  public void increment(DataPoint data) {

    for (int i = 0; confirmedList != null && i < confirmedList.size(); i++) {
      confirmedList.set(i, confirmedList.get(i) + data.confirmedList.get(i));
    }
    for (int i = 0; deathsList != null && i < deathsList.size(); i++) {
      deathsList.set(i, deathsList.get(i) + data.deathsList.get(i));
    }
    for (int i = 0; recoveredList != null && i < recoveredList.size(); i++) {
      recoveredList.set(i, recoveredList.get(i) + data.recoveredList.get(i));
    }
  }

  /**
   * Removes empty city, state, or country values
   * 
   * @param city
   * @param state
   * @param country
   * @return
   */
  public boolean filter(boolean city, boolean state, boolean country) {
    if (!getCity().equals(""))
      return city;
    if (!getState().equals(""))
      return state;
    if (!getCountry().equals(""))
      return country;
    return true;
  }

  /**
   * toString method returns key
   */
  @Override
  public String toString() {
    return key;
  }
}
