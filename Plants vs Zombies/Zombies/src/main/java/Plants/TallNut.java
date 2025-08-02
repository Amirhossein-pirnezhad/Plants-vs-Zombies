package Plants;

import Map.GameManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static Map.Cell.cell_size;

public class TallNut extends Plant{
    public TallNut(int row, int col) {
        super(row, col);
        HP = 30;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/TallNut/TallNut.gif")));
        plantView.setFitHeight(cell_size);
        plantView.setFitHeight(cell_size);


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
}
