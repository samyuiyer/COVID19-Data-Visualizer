package application;

import javafx.scene.Node;

public abstract class DisplayMode {
  
  public String title;
  
  public abstract Node getDiplayPane();
  public abstract Node getSettingsPane();
  abstract void reset();

}
