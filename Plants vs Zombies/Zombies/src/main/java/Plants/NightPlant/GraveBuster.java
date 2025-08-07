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
    private int counter = 0 , more = (3 * 1000) / GameManager.timeUpdatePlants;

    public GraveBuster(int row, int col) {
        super(row, col);
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/GraveBuster/GraveBuster.gif")));
        plantView.setFitHeight(cell_size);
        plantView.setFitWidth(cell_size);
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
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/GraveBuster/GraveBuster.gif")));
        plantView.setFitHeight(cell_size);
        plantView.setFitWidth(cell_size);
    }

    @Override
    public void update() {
        if (counter == more)
            dead();
        counter ++;
    }
}
