package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;

public class GraveBuster extends Plant {
    private Timeline eating;

    public GraveBuster(int row, int col) {
        super(row, col);
        plantView = new ImageView();
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
