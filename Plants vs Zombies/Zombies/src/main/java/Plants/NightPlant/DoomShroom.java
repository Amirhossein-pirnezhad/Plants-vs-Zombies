package Plants.NightPlant;

import Map.GameManager;
import Map.Sizes;
import Plants.Plant;
import Zombies.Zombie;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static Map.Cell.cell_size;

public class DoomShroom extends Plant {
    private Timeline boom;
    public DoomShroom(int row, int col) {
        super(row, col);
        HP = Integer.MAX_VALUE;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/DoomShroom/DoomShroom.gif")));
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
        bomb();
    }

    protected void bomb(){
        boom = new Timeline(new KeyFrame(Duration.seconds(0.75) , event -> kill()));
        boom.setCycleCount(1);
        boom.play();
    }

    protected boolean isKilled(Zombie z) {
        double x1 = Sizes.START_X_GRID + (row - 4) * Sizes.CELL_SIZE;
        double x2 = x1 + 5 * Sizes.CELL_SIZE;
        double zombieX = z.getZombieView().getLayoutX();
        System.out.println("x1 " + x1 + "x2 " + x2 + "zombie : "+ zombieX);

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
            if(boom != null && boom.getStatus() == Animation.Status.RUNNING)
                boom.stop();
            isAlive = false;
            GameManager.removePlant(this);
            plantView.setOnMouseClicked(null);
        }));
        dead.setCycleCount(1);
        dead.play();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
