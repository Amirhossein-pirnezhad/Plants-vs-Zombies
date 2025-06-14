package Zombies;

import Map.GameManager;
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

    public Zombie(int col){
        HP = 7;
        this.speed = 15;
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
        zombieView.setLayoutX(1500);
        zombieView.setLayoutY(col * cell_size + 30);
    }

    public void run(){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        final int[] frameIndex = new int[1];

        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(100), e -> {
                    zombieView.setImage(zombieImages[frameIndex[0]]);
                    zombieView.setLayoutX(zombieView.getLayoutX() - 1.5);
                    frameIndex[0] = (frameIndex[0] + 1) % zombieImages.length;
                    if(HP < 0){
                        timeline.stop();
                        GameManager.getZombies().remove(this);
                        zombieView.setVisible(false);
                    }
                })

        );
        timeline.play();
    }

    public ImageView getZombieView() {
        return zombieView;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getHP() {
        return HP;
    }

    public double getSpeed() {
        return speed;
    }

    public int getCol() {
        return col;
    }
}
