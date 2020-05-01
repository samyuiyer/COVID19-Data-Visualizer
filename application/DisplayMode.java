///////////////////////////////////////////////////////////////////////////////
//
//  Title: ateam_final_project
//  Author: Ankur Garg, Eric Ertl, Justin Chan, Samyu Iyer, Sudeep Reddy
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

import javafx.scene.Node;

/**
 * Class to represent a Pane to be displayed
 */
public abstract class DisplayMode {

  /**
   * Title of given DisplayMode
   */
  public String title;

  /**
   * @return the pane to display given DisplayMode
   */
  public abstract Node getDisplayPane();

  /**
   * @return the pane to display settings
   */
  public abstract Node getSettingsPane();

  /**
   * Updates view
   */
  public abstract void refresh();

}
