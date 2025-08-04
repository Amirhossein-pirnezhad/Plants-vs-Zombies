package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static Map.Cell.cell_size;

public class Crater extends Plant {
    private Timeline lifeTime;
    private final int timeDead = 20;
    private int t = timeDead;

    public Crater(int row, int col) {
        super(row, col);
        HP = Integer.MAX_VALUE;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater1.png")));
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
        startLife();
    }

    private void startLife(){
        lifeTime = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
            t--;
            if (t == timeDead * 0.75){
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater2.png")));
            }if (t == timeDead * 0.5){
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater4.png")));
            }if (t == timeDead * 0.25){
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater3.png")));
            }
            if (t == 0){
                dead();
            }
        }));
        lifeTime.setCycleCount(timeDead);
        lifeTime.play();
    }

    @Override
    public void dead() {
        if (!isAlive) return;
        isAlive = false;
        GameManager.removePlant(this);

        plantView.setOnMouseClicked(null);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
