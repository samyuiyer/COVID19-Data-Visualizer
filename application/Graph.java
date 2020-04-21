package application;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class Graph extends DisplayMode {

  @Override
  void reset() {
    
  }

  @Override
  public Node getDiplayPane() {
    return new Label("Graph");
  }

  @Override
  public Node getSettingsPane() {
    return new Label("Graph Settings");
  }

}
