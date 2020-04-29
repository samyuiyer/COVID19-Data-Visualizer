package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DataManager {
  public GeoTrie gt;
  public AlphaTrie at;
  public String[] labels;
  private final List<String> DATA_TYPES;

  public DataManager() {
    at = new AlphaTrie();
    gt = new GeoTrie();
    DATA_TYPES = Arrays.asList(new String[] {"confirmed", "deaths", "recovered"});
  }

  public void loadTries() throws FileNotFoundException, IllegalNullKeyException {
    ArrayList<Scanner> fileIn = new ArrayList<>();
    for (String s : DATA_TYPES)
      fileIn.add(new Scanner(new File("time_data/" + s + ".csv")));

    Integer[] zeros = new Integer[95];
    Arrays.fill(zeros, 0);

    for (Scanner s : fileIn) {
      labels = s.nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
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
          // System.out.println(labels[0]);
          for (int i = 0; i < confrimed.length; i++) {
            if (labels[0].equals("confirmed"))
              confrimed[i] = Integer.parseInt(split[i + 6]);
            else if (labels[0].equals("deaths"))
              deaths[i] = Integer.parseInt(split[i + 6]);
            else if (labels[0].equals("recovered"))
              recovered[i] = Integer.parseInt(split[i + 6]);
          }
          dp = new DataPoint(split[0], data, confrimed, deaths, recovered);
           at.insert(split[0], dp);
          gt.insert(split[0], dp);
        } catch (Exception e) {
          System.out.println("bad");
        }
      }
      s.close();
    }
  }

  public String[] getTimeLabels() {
    return Arrays.copyOfRange(labels, 6, labels.length);
  }

  // TODO rememebr to remove
  public static void main(String[] args) throws FileNotFoundException, IllegalNullKeyException {
    DataManager dm = new DataManager();
    dm.loadTries();
    dm.gt.print();
     dm.at.print();
     System.out.println(dm.at.numKeys());
    System.out.println(dm.gt.numKeys());
  }
}
