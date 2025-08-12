package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static Map.Cell.cell_size;

public class GraveBuster extends Plant {
    private int counter = 0 , more = (3 * 1000) / GameManager.timeUpdatePlants;

    public GraveBuster(int row, int col) {
        super(row, col);
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/GraveBuster/GraveBuster.gif")));
        plantView.setFitHeight(cell_size);
        plantView.setFitWidth(cell_size);
        plantView.setLayoutY(plantView.getLayoutY() - 100);
    }

    @Override
    public void dead() {
        isAlive = false;
        GameManager.removePlant(this);

        plantView.setOnMouseClicked(null);//don't click again
    }

    @Override
    public void pause() {
        Platform.runLater(() -> {
            Image frozenImage = plantView.snapshot(null, null);
            plantView.setImage(frozenImage);
        });
    }

    @Override
    public void resume() {
        if (plantView == null) {
            plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/GraveBuster/GraveBuster.gif")));
            plantView.setFitHeight(cell_size);
            plantView.setFitWidth(cell_size);
        }else plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/GraveBuster/GraveBuster.gif")));

        GameManager.getCells()[row][col].removePlant();
        GameManager.getCells()[row][col].setPlant(this);
    }

    @Override
    public void update() {
        if (counter == more)
            dead();
        counter ++;
    }
}
