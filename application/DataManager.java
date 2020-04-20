package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataManager {
  public GeoTrie gt;
  public AlphaTrie at;
  public String[] labels;

  public DataManager() {
    at = new AlphaTrie();
    gt = new GeoTrie();
  }

  public void loadTries(String loadFile) throws FileNotFoundException, IllegalNullKeyException {
    Scanner fileIn = new Scanner(new File(loadFile));
    labels = fileIn.nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    DataPoint.labels = labels;
    int i = 0;
    while (fileIn.hasNext()) {
      i++;
      String[] split = null;
      DataPoint dp;
      try {
        split = fileIn.nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        dp = new DataPoint(split[11], split);
        String[] geoSplit = split[11].split(",");
        at.insert(split[11], dp);
        gt.insert(geoSplit, dp);
      } catch (Exception e) {

      }

    }
    fileIn.close();

  }

  public static void main(String[] args) throws FileNotFoundException, IllegalNullKeyException {
    DataManager dm = new DataManager();
    dm.loadTries("data_test.txt");
    dm.gt.print();
    dm.at.print();
  }
}
