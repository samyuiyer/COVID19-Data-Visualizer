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
    labels = fileIn.get(0).nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    DataPoint.labels = labels;
    System.out.println(labels.length);
    while (fileIn.get(0).hasNext()) {
      String[][] split = new String[fileIn.size()][];
      DataPoint dp;
      try {

        for (int i = 0; i < fileIn.size(); i++) {
          split[i] = fileIn.get(i).nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
          split[i][0] = split[i][0].replace("\"", "");
        }
        String[] data = Arrays.copyOfRange(split[0], 0, 6);
        Integer[] confrimed = new Integer[split[0].length - 6];
        Integer[] deaths = new Integer[split[0].length - 6];
        Integer[] recovered = new Integer[split[0].length - 6];
        for (int i = 0; i < confrimed.length; i++) {
          confrimed[i] = Integer.parseInt(split[0][i + 6]);
          deaths[i] = Integer.parseInt(split[1][i + 6]);
          recovered[i] = Integer.parseInt(split[2][i + 6]);
        }
        dp = new DataPoint(split[0][0], data, confrimed, deaths, recovered);
        at.insert(split[0][0], dp);
        gt.insert(split[0][0], dp);

      } catch (

      Exception e) {

      }


    }

  }



  public static void main(String[] args) throws FileNotFoundException, IllegalNullKeyException {
    DataManager dm = new DataManager();
    dm.loadTries();
    dm.gt.print();
    dm.at.print();
    System.out.println(dm.at.numKeys());
    System.out.println(dm.gt.numKeys());
  }
}
