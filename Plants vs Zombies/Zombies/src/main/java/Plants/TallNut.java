package Plants;

import Map.GameManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static Map.Cell.cell_size;

public class TallNut extends Plant{
    private final int initialHp = 30;
    public TallNut(int row, int col) {
        super(row, col);
        HP = 30;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/TallNut/TallNut.gif")));
        plantView.setFitHeight(cell_size);
        plantView.setFitHeight(cell_size);
    }

    @Override
    public void setHP(int HP) {
        this.HP = HP;
        if(HP == initialHp * 0.6){
            plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/TallNut/TallnutCracked1.gif")));
        }
        else if (HP == initialHp * 0.3){
            plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/TallNut/TallnutCracked2.gif")));
        }
    }

    @Override
    public void dead() {
        isAlive = false;
        GameManager.removePlant(this);
        plantView.setOnMouseClicked(null);
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
            if (HP <= initialHp * 0.6) {
                plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/TallNut/TallnutCracked1.gif")));
            } else if (HP <= initialHp * 0.3) {
                plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/TallNut/TallnutCracked2.gif")));

            } else {
                plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/TallNut/TallNut.gif")));
            }
            plantView.setFitHeight(cell_size);
            plantView.setFitHeight(cell_size);
        }
        else {
            if (HP <= initialHp * 0.6) {
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/TallNut/TallnutCracked1.gif")));
            } else if (HP <= initialHp * 0.3) {
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/TallNut/TallnutCracked2.gif")));

            } else {
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/TallNut/TallNut.gif")));
            }
        }
        GameManager.getCells()[row][col].removePlant();
        GameManager.getCells()[row][col].setPlant(this);
    }

    @Override
    public void update() {
        if(HP <= 0){
            dead();
        }
    }
}
