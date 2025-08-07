package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Blover extends Plant {
    private int counter = 0 , more = (5 * 1000) / GameManager.timeUpdatePlants;
    private transient FadeTransition fadeIn , fadeOut;
    public Blover(int row, int col) {
        super(row, col);
        System.out.println("more" + more);
        HP = 4;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Blover/Blover.gif")));

        //animation by chat gpt :)
        fadeOut = new FadeTransition(Duration.seconds(1), GameManager.getPaneMeh());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> GameManager.getPaneMeh().setVisible(false));

        fadeIn = new FadeTransition(Duration.seconds(1), GameManager.getPaneMeh());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setOnFinished(e -> dead());

        fadeOut.play();
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
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Blover/Blover.gif")));

        //animation by chat gpt :)
        fadeOut = new FadeTransition(Duration.seconds(1), GameManager.getPaneMeh());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> GameManager.getPaneMeh().setVisible(false));

        fadeIn = new FadeTransition(Duration.seconds(1), GameManager.getPaneMeh());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setOnFinished(e -> dead());
        fadeOut.play();
    }

    @Override
    public void update() {
       if (counter == more) {
           GameManager.getPaneMeh().setVisible(true);
           fadeIn.play();
       }
       counter++;
       if (HP <= 0)
           dead();
    }
}
