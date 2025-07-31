package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static Map.Cell.cell_size;

public class GraveBuster extends Plant {
    private Timeline eating;

    public GraveBuster(int row, int col) {
        super(row, col);
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/GraveBuster/DoomShroom.gif")));
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);

        eating = new Timeline(new KeyFrame(Duration.seconds(3) , e->{
            dead();
        }));
        eating.setCycleCount(1);
        eating.play();
    }

    @Override
    public void dead() {
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
