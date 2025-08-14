package Plants;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import static Map.GameManager.peas;

public class SnowPea extends Peashooter{

    public SnowPea(int row, int col) {
        super(row, col);
        imgPath = "/Plants/SnowPea/SnowPea_";
        typePea = "PeaIce";
        setImage(imgPath , 15);
    }

    @Override
    protected void shooting(){
        if(isAlive)
            shoot = new Timeline(new KeyFrame(Duration.seconds(secondInCircle) , event -> {
                if(if_Zombie_exist()) {
                    Timeline tl =  new Timeline(new KeyFrame(Duration.millis(200) , event1 -> {
                        peas.add(new PeaIce(this));
                    }));
                    tl.setCycleCount(peaInCircle);
                    tl.play();
                }
            }));
        shoot.setCycleCount(Animation.INDEFINITE);
        shoot.play();
    }
}
