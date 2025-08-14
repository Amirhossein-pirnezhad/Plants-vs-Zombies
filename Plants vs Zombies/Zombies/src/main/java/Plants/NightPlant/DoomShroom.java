package Plants.NightPlant;

import Map.GameManager;
import Map.Sizes;
import Plants.Plant;
import Zombies.Zombie;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static Map.Cell.cell_size;

public class DoomShroom extends Plant {
    protected boolean started = false;
    protected int counter = 0 , bombTime = 800 / GameManager.timeUpdatePlants;

    protected String img = "/Plants/DoomShroom/DoomShroom.gif";
    public DoomShroom(int row, int col) {
        super(row, col);
        coffee = GameManager.night;
        HP = 10;
        if(coffee)
            plantView = new ImageView(new Image(getClass().getResourceAsStream(img)));
        if (!coffee){
            plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/DoomShroom/Sleep.gif")));
        }
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
    }


    protected boolean isKilled(Zombie z) {
        double x1 = Sizes.START_X_GRID + (row - 3) * Sizes.CELL_SIZE;
        double x2 = x1 + 5 * Sizes.CELL_SIZE;
        double zombieX = z.getZombieView().getLayoutX();

        return ((z.getCol() == col) || (z.getCol() == col - 1) || (z.getCol() == col + 1)
                || (z.getCol() == col - 2) || (z.getCol() == col + 2))
                && (zombieX > x1 && zombieX < x2);
    }

    protected void kill(){
        for(Zombie z : GameManager.getZombies()){
            if(!z.isHypnosis() && isKilled(z))
                z.bomDie();
        }
        dead();
    }

    @Override
    public void dead() {
        plantView.setImage(new Image(getClass().getResourceAsStream("/Screen/Boom.gif")));
        Timeline dead = new Timeline(new KeyFrame(Duration.seconds(1.2) , event -> {
            isAlive = false;
            GameManager.removePlant(this);
            if(coffee)
                GameManager.addPlant(new Crater(row , col));
            plantView.setOnMouseClicked(null);
        }));
        dead.setCycleCount(1);
        dead.play();
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
        if(coffee)
            if (plantView == null) {
                plantView = new ImageView(new Image(getClass().getResourceAsStream(img)));
            }else plantView.setImage(new Image(getClass().getResourceAsStream(img)));
        if (!coffee){
            if (plantView == null)
                plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/DoomShroom/Sleep.gif")));
            else plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/DoomShroom/Sleep.gif")));
        }
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);

        GameManager.getCells()[row][col].removePlant();
        GameManager.getCells()[row][col].setPlant(this);
    }

    @Override
    public void update() {
        if (coffee && !started){ // if bomb not started , start!
            started = true;
            plantView.setImage(new Image(getClass().getResourceAsStream(img)));
        }
        if (HP <= 0)
            dead();
        if (started) {
            counter++;
            if (counter == bombTime) {
                kill();
            }
        }
    }
}
