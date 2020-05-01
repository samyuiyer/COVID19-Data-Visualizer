///////////////////////////////////////////////////////////////////////////////
//
//  Title: ateam_final_project
//  Author: Ankur Garg, Eric Ertl, Justin Chan, Samyu Iyer, Sudeep Reddy, 
//
//  Course: CS400
//  Semester: Spring 2020
//  Lecture Number: 001
//
//  Date: 4/29/2020
//
//  Description:    A project that displays statistics relating to COVID-19 in a
//                  variety of ways. 
//
///////////////////////////////////////////////////////////////////////////////
package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Manages the data for the different display modes
 */
public class DataManager {
  public GeoTrie gt;
  public String[] labels;

  /**
   * Constructs a DataManger with a GeoTrie object
   */
  public DataManager() {
    gt = new GeoTrie();
  }
  /**
   * Checks if file being loaded meets the requirements
   * @param folderName location of the file to be checked
   * @return True if no file errors are detected otherwise false
   */
  public boolean loadTries(String folderName) {
    gt.clear();
    try {
      ArrayList<Scanner> fileIn = new ArrayList<>();
      for (File s : new File(folderName + "/").listFiles())
        fileIn.add(new Scanner(s));
      Integer[] zeros = new Integer[95];
      Arrays.fill(zeros, 0);

      for (Scanner s : fileIn) {

        if (s.hasNext()) {
          labels = s.nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
          if (labels.length == 101) {
            DataPoint.labels = labels;
            while (s.hasNext()) {
              try { // formatting file
                String[] split = s.nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                DataPoint dp;
                split[0] = split[0].replace("\"", "");
                String[] data = Arrays.copyOfRange(split, 0, 6);
                Integer[] confrimed = zeros.clone();
                Integer[] deaths = zeros.clone();
                Integer[] recovered = zeros.clone();
                // Check for required data
                for (int i = 0; i < confrimed.length; i++) { 
                  if (labels[0].equals("confirmed"))
                    confrimed[i] = Integer.parseInt(split[i + 6]);
                  else if (labels[0].equals("deaths"))
                    deaths[i] = Integer.parseInt(split[i + 6]);
                  else if (labels[0].equals("recovered"))
                    recovered[i] = Integer.parseInt(split[i + 6]);
                }
                dp = new DataPoint(split[0], data, confrimed, deaths, recovered);
                gt.insert(split[0], dp);
              } catch (Exception e) {
              }
            }
            s.close();
          }
        }
      }
    } catch (Exception e) {
      labels = new String[101];
      return false;
    }
    return true;
  }
  /**
   * Returns a copied array of time labels
   * @return array of time labels
   */
  public String[] getTimeLabels() {
    return Arrays.copyOfRange(labels, 6, labels.length);
  }

}
