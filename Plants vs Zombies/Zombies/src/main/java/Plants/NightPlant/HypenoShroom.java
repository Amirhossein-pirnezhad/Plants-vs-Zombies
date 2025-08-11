package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import Zombies.Zombie;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static Map.Cell.cell_size;

public class HypenoShroom extends Plant {
    private boolean started = false;
    public HypenoShroom(int row, int col) {
        super(row, col);
        coffee = GameManager.night;
        HP = 4;
        if (coffee)
            plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/HypnoShroom/HypnoShroom/HypnoShroom.gif")));
        if (!coffee){
            plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/HypnoShroom/HypnoShroom/HypnoShroomSleep.gif")));
        }
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
    }


    @Override
    public void dead() {
        if (!isAlive) return;
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
        if (plantView == null){
            if (coffee)
                plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/HypnoShroom/HypnoShroom/HypnoShroom.gif")));
            if (!coffee){
                plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/HypnoShroom/HypnoShroom/HypnoShroomSleep.gif")));
            }
            plantView.setFitHeight(cell_size * 0.75);
            plantView.setFitWidth(cell_size * 0.75);
        }
        else{
            if (coffee)
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/HypnoShroom/HypnoShroom/HypnoShroom.gif")));
            if (!coffee){
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/HypnoShroom/HypnoShroom/HypnoShroomSleep.gif")));
            }
        }
    }

    @Override
    public void update() {
        if (coffee && !started) {
            plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/HypnoShroom/HypnoShroom/HypnoShroom.gif")));
            started = true;
        }
        if(HP <= 0){
            dead();
        }
    }

    public boolean isCoffee() {
        return coffee;
    }
}
