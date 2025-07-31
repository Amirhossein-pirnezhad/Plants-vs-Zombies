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

public class HypenoShroom extends Plant {
    public HypenoShroom(int row, int col) {
        super(row, col);
        HP = 4;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/HypnoShroom/HypnoShroom/HypnoShroom.gif")));
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
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
