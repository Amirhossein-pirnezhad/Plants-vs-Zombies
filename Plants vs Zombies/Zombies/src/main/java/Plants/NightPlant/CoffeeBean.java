package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static Map.Cell.cell_size;

public class CoffeeBean extends Plant {
    public CoffeeBean(int row, int col) {
        super(row, col);
        HP = 20;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/GraveBuster/GraveBuster.gif")));
        plantView.setFitHeight(cell_size);
        plantView.setFitWidth(cell_size);
    }

    @Override
    public void dead() {
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

    @Override
    public void update() {

    }
}
