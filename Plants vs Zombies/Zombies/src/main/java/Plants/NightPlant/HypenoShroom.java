package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class HypenoShroom extends Plant {
    private Timeline anim;
    public HypenoShroom(int row, int col) {
        super(row, col);
        HP = 4;
        setImage("/Plants/HypnoShroom/HypnoShroom/HypnoShroom_" , 14);
        animHypno();
    }

    private void animHypno(){
        final int[] frame = new int[1];
        anim = new Timeline(new KeyFrame(Duration.millis(100) , event -> {
            if(HP <= 0){
                dead();
            }
            plantView.setImage(plantImage[frame[0]]);
            frame[0] = (frame[0] + 1) % plantImage.length;
        }));
        anim.setCycleCount(Animation.INDEFINITE);
        anim.play();
    }

    @Override
    public void dead() {
        if (anim != null && anim.getStatus() == Animation.Status.RUNNING)
            anim.stop();
        isAlive = false;
        GameManager.removePlant(this);

        plantView.setOnMouseClicked(null);//don't click again
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
