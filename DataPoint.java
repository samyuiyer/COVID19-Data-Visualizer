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
package ateam_final_project;

/**
 * @author ankurgarg
 *
 */
public class DataPoint {
    /**
   * @param data
   */
  
  public String key;
  public int testNum;
  
  public DataPoint(String key,int testNum) {
    this.key = key;
    this.testNum=testNum;
   }
  public DataPoint(DataPoint data) {
   this.key = data.key;
   this.testNum=data.testNum;
  }
  public DataPoint(int testNum) {
    this.key = "";
    this.testNum=testNum;
   }
  public DataPoint(String key, DataPoint data) {
    this.key = key;
    this.testNum=data.testNum;
   }
  public void increment(DataPoint data) {
    testNum+=data.testNum;
  }
  @Override
  public String toString() {
    return key + ": " + testNum;
  }
    
}
