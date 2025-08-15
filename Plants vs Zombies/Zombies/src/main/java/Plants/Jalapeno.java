package Plants;

import Map.Cell;
import Map.GameManager;
import Map.Sizes;
import Zombies.Zombie;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static Map.Sizes.CELL_SIZE;
import static Map.Sizes.START_Y_GRID;

public class Jalapeno extends CherryBomb{
    public Jalapeno(int row, int col) {
        super(row, col);
        setImage("/Plants/Jalapeno/Jalapeno/Jalapeno_" , 8);
    }

    @Override
    protected void killZombie() {
        GameManager.playOneShot("/Sounds/bomb.mp3");
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Jalapeno/JalapenoAttack.gif")));
        GameManager.getPanePeas().getChildren().add(imageView);
        imageView.setFitWidth(Sizes.SCREEN_WIDTH - Sizes.START_X_GRID);
        imageView.setLayoutX(Sizes.START_X_GRID);
        imageView.setLayoutY(START_Y_GRID + col * CELL_SIZE + 30);
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(2) , e -> {
            GameManager.getPanePeas().getChildren().remove(imageView);
        }));
        tl.setCycleCount(1);
        tl.play();
        for (Zombie z : GameManager.getZombies())
            if (z.getCol() == col) z.bomDie();
    }

    @Override
    public void resume(){
        GameManager.getCells()[row][col].removePlant();
        setImage("/Plants/Jalapeno/Jalapeno/Jalapeno_" , 8);
        bomb(frame);
        GameManager.getCells()[row][col].setPlant(this);
    }

}
