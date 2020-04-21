package application;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class Map extends DisplayMode {

  @Override
  void reset() {
    
  }

  @Override
  public Node getDiplayPane() {
    return new Label("Map");
  }

  @Override
  public Node getSettingsPane() {
    return new Label("Map Settings");
  }

}
