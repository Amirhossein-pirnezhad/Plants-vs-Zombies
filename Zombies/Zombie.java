package Zombies;

import Map.Cell;
import Map.GameManager;
import Map.Sizes;
import Plants.Plant;
import javafx.animation.Animation;
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
    protected Timeline deadZombie;
    protected Timeline runZombie;
    protected Timeline eating;

    public Zombie(int col){
        HP = 5;
        this.speed = cell_size/4;
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
        runZombie = new Timeline();
        runZombie.setCycleCount(Timeline.INDEFINITE);
        final int[] frameIndex = new int[1];

        runZombie.getKeyFrames().add(
                new KeyFrame(Duration.millis(100), e -> {
                    zombieView.setImage(zombieImages[frameIndex[0]]);
                    zombieView.setLayoutX(zombieView.getLayoutX() - 1.5);
                    frameIndex[0] = (frameIndex[0] + 1) % zombieImages.length;

                    if_touch_plant();

                    if(HP <= 0){
                        runZombie.stop();
                        deadZombie();
                    }
                })

        );
        runZombie.play();
    }

    protected void attackZombie(){
        zombieImages = new Image[20];
        for (int i = 0; i < zombieImages.length; i++) {
            zombieImages[i] = new Image(getClass().getResourceAsStream("/Zombies/NormalZombie/ZombieAttack/ZombieAttack_" + i + ".png"));
        }

        final int[] frameIndex = new int[1];

        eating = new Timeline(new KeyFrame(Duration.millis(500) , event -> {
            zombieView.setImage(zombieImages[frameIndex[0]]);
            frameIndex[0] = (frameIndex[0] + 1) % zombieImages.length;
        }));

        eating.setCycleCount(4);
        eating.play();
    }

    protected void if_touch_plant(){
        for (Plant p : GameManager.getPlants()){
            if(this.col == p.getCol()){
                if(Math.abs((p.getRow() * cell_size + Sizes.START_X_GRID) - this.zombieView.getLayoutX()) < 1){
                    p.setHP(p.getHP() - 1);
                    attackZombie();
                }
            }
        }
    }

    protected void deadZombie(){
        zombieImages = new Image[9];
        for (int i = 0; i < zombieImages.length; i++) {
            zombieImages[i] = new Image(getClass().getResourceAsStream("/Zombies/NormalZombie/ZombieDie/ZombieDie_" + i + ".png"));
        }
        final int[] frame = new int[1];
        deadZombie = new Timeline(new KeyFrame(Duration.millis(200) , event -> {
            zombieView.setImage(zombieImages[frame[0]]);
            frame[0] = (frame[0] + 1) % zombieImages.length;
        }));
        deadZombie.setCycleCount(zombieImages.length);
        deadZombie.play();
        deadZombie = new Timeline(new KeyFrame(Duration.millis(500) , event -> {
            GameManager.getZombies().remove(this);
            zombieView.setVisible(false);
        }));
        deadZombie.setCycleCount(1);
        deadZombie.play();
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
