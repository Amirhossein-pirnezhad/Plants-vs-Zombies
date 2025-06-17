package Plants;

import Map.GameManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SnowPea extends Peashooter{

    public SnowPea(int row, int col) {
        super(row, col);
    }
    @Override
    protected void setImage(){
        plantImage = new Image[14];
        for (int i = 0; i < plantImage.length; i++) {
            plantImage[i] = new Image(getClass().getResourceAsStream("/Plants/SnowPea/SnowPea_" + i + ".png"));
        }
        plantView = new ImageView(plantImage[0]);
    }
    @Override
    protected void shooting(){
        if(isAlive)
            shoot = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
                if(if_Zombie_exist()) {
                    Timeline tl =  new Timeline(new KeyFrame(Duration.millis(200) , event1 -> {
                        peas.add(new PeaIce(this));
                        GameManager.getPanePeas().getChildren().add(peas.get(peas.size() - 1).getPeaView());
                    }));
                    tl.setCycleCount(peaInSecond);
                    tl.play();
                }
            }));
        shoot.setCycleCount(Animation.INDEFINITE);
        shoot.play();
    }
}
