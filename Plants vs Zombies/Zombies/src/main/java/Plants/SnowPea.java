package Plants;

import Map.GameManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SnowPea extends Peashooter{

    public SnowPea(int row, int col) {
        super(row, col);
        setImage("/Plants/SnowPea/SnowPea_" , 14);
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
