package Plants.NightPlant;

import Map.GameManager;
import Map.Sizes;
import Plants.CherryBomb;
import Zombies.Zombie;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class IceShroom extends DoomShroom {
    public IceShroom(int row, int col) {
        super(row, col);
        img = "/Plants/IceShroom/IceShroom/IceShroom.gif";
        if (coffee)
            plantView.setImage(new Image(getClass().getResourceAsStream(img)));
        else
            plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/IceShroom/IceShroom/IceShroomSleep.gif")));
    }

    @Override
    protected boolean isKilled(Zombie z) {
        return true;
    }

    @Override
    protected void kill(){
        for (Zombie z : GameManager.getZombies()){
            z.ice();
        }
        dead();
    }

    @Override
    public void dead() {
        plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/IceShroom/IceShroom/icetrap.gif")));
        Timeline dead = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
            if(boom != null && boom.getStatus() == Animation.Status.RUNNING)
                boom.stop();
            isAlive = false;
            GameManager.removePlant(this);
            plantView.setOnMouseClicked(null);
        }));
        dead.setCycleCount(1);
        dead.play();
    }
}
