package ateam_final_project;

import javafx.scene.layout.Pane;

public abstract class DisplayMode {
  
  public String title;
  
  private Pane centerDiplayPane;
  private Pane settingsPane;
  abstract void reset();

}
