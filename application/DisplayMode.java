package application;

import java.util.List;
import javafx.scene.Node;

public abstract class DisplayMode {
  
  public String title;
  
  public abstract Node getDisplayPane();
  public abstract Node getSettingsPane();
  public abstract List getFilteredList();
  abstract void reset();

}
