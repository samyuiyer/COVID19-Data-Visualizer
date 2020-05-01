package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DataManager {
  public GeoTrie gt;
  public String[] labels;

  public DataManager() {
    gt = new GeoTrie();
  }

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
              try {
                String[] split = s.nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                DataPoint dp;
                split[0] = split[0].replace("\"", "");
                String[] data = Arrays.copyOfRange(split, 0, 6);
                Integer[] confrimed = zeros.clone();
                Integer[] deaths = zeros.clone();
                Integer[] recovered = zeros.clone();
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
                System.out.println("bad");
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

  public String[] getTimeLabels() {
    return Arrays.copyOfRange(labels, 6, labels.length);
  }

}
