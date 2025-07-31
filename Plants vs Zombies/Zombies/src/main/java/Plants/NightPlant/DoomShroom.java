package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import Zombies.Zombie;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class DoomShroom extends Plant {
    private Timeline boom;
    public DoomShroom(int row, int col) {
        super(row, col);
        HP = Integer.MAX_VALUE;
        setImage("/Plants/DoomShroom/DoomShroom_" ,1);
        bomb();
    }

    protected void bomb(){
        boom = new Timeline(new KeyFrame(Duration.seconds(3) , event -> kill()));
        boom.setCycleCount(1);
        boom.play();
    }

    protected void kill(){
        for(Zombie z : GameManager.getZombies()){
            if(!z.isHypnosis())
                z.bomDie();
        }
        dead();
    }

    @Override
    public void dead() {
        plantView.setImage(new Image(getClass().getResourceAsStream("/Screen/Boom.gif")));
        Timeline dead = new Timeline(new KeyFrame(Duration.seconds(1.2) , event -> {
            if(boom != null && boom.getStatus() == Animation.Status.RUNNING)
                boom.stop();
            isAlive = false;
            GameManager.removePlant(this);
            plantView.setOnMouseClicked(null);
        }));
        dead.setCycleCount(1);
        dead.play();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
