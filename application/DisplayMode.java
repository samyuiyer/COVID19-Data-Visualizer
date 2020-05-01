///////////////////////////////////////////////////////////////////////////////
//
// 	Title: ateam_final_project
// 	Author: Ankur Garg, Eric Ertl, Justin Chan, Samyu Iyer, Sudeep Reddy, 
//
// 	Course: CS400
//	Semester: Spring 2020
//	Lecture Number: 001
//
//	Date: 4/29/2020
//
// 	Description: 	A project that displays statistics relating to COVID-19 in a
//					variety of ways. 
//
///////////////////////////////////////////////////////////////////////////////

package application;

import javafx.scene.Node;

/**
 * An abstract class for the various DisplayModes (Map, Table, Graph)
 */
public abstract class DisplayMode {

	public String title;

	/**
	 * @return the displayPane
	 */
	public abstract Node getDisplayPane();

	/**
	 * @return the settingsPane
	 */
	public abstract Node getSettingsPane();

}
