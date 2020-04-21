package application;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Map extends DisplayMode {
  
  ImageView iv1 = new ImageView();
  
  Map(){
    Image image = new Image("map.jpg");
    iv1.setImage(image);
  }

  @Override
  void reset() {
    
  }

  @Override
  public Node getDisplayPane() {
    return iv1;
  }

  @Override
  public Node getSettingsPane() {
    // TODO Auto-generated method stub
    return null;
  }

}
