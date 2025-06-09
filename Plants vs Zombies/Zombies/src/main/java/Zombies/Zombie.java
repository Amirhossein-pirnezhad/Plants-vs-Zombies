package Zombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import static Map.Cell.cell_size;

public class Zombie {
    protected int HP;
    protected double speed;
    protected int col;
    protected Image[] zombieImages;
    protected ImageView zombieView;

    public Zombie(int hp, int speed , int col){
        HP = hp;
        this.speed = speed;
        this.col = col;
        setZombieImages();
    }

    protected void setZombieImages(){
        zombieImages = new Image[21];
        for (int i = 0; i < 21; i++) {
            zombieImages[i] = new Image(getClass().getResourceAsStream("/Zombies/NormalZombie/Zombie/Zombie_" + i + ".png"));
        }
        zombieView = new ImageView(zombieImages[0]);
        zombieView.setFitWidth(cell_size * 1.5);
        zombieView.setFitHeight(cell_size * 1.5);
        zombieView.setX(1500);
        zombieView.setY(col * cell_size + 30);
    }

    public void run(){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        final int[] frameIndex = new int[1];

        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(100), e -> {
                    zombieView.setImage(zombieImages[frameIndex[0]]);
                    zombieView.setX(zombieView.getX() - 1.5);
                    frameIndex[0] = (frameIndex[0] + 1) % zombieImages.length;
                })

        );
        timeline.play();
    }

    public ImageView getZombieView() {
        return zombieView;
    }
}
