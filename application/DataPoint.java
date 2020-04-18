/**
 *
 * Author: 		Ankur Garg
 * Email:  		Agarg34@wisc.edu
 * 
 * Course:		CS400
 * Semester:	Spring2020
 * Lecture:		001
 * Date:		Apr 16, 2020
 *
 * Files:		DataPoint.java
 *
 */
package application;

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
  
  public DataPoint(String key,String[] testNum) {
    this.key = key;
    this.dataArray=testNum;
   }
  public DataPoint(DataPoint data) {
   this.key = data.key;
   this.dataArray=data.dataArray;
  }
  public DataPoint(String[] testNum) {
    this.key = "";
    this.dataArray=testNum;
   }
  public DataPoint(String key, DataPoint data) {
    this.key = key;
    this.dataArray=data.dataArray;
   }
  public void increment(DataPoint data) {
    //not yet
  }
  @Override
  public String toString() {
    String rtn = key +": ";
    for(int i =0; i<labels.length;i++) {
      rtn+= labels[i] + ":" + dataArray[i]+" ";
    }
    return rtn;
  }
    
}
