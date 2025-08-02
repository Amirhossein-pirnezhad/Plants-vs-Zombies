package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Blover extends Plant {
    public Blover(int row, int col) {
        super(row, col);
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Blover/Blover.gif")));

        //animation by chat gpt :)
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), GameManager.getPaneMeh());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> GameManager.getPaneMeh().setVisible(false));

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), GameManager.getPaneMeh());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setOnFinished(e -> dead());

        fadeOut.play();
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            GameManager.getPaneMeh().setVisible(true);
            fadeIn.play();
        }));
        tl.setCycleCount(1);
        tl.play();
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
