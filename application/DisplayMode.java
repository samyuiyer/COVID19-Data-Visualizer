package application;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public abstract class DisplayMode {
  
  public String title;
  

  protected Pane settingsPane;
  public abstract Node getDiplayPane();
  abstract void reset();

}
