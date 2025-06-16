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
    protected Image[] zombieAttack;
    protected ImageView zombieView;
    protected Timeline deadZombie;
    protected Timeline runZombie;
    protected Timeline eating;
    protected boolean isAttacking;
    protected Plant targetPlant;

    public Zombie(int col){
        HP = 5;
        this.speed = cell_size/4;
        this.col = col;
        isAttacking = false;
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
                    if (HP <= 0) {
                        runZombie.stop();
                        deadZombie();
                    }
                    if((targetPlant = if_touch_plant()) == null) {
                        zombieView.setImage(zombieImages[frameIndex[0]]);
                        zombieView.setLayoutX(zombieView.getLayoutX() - 1.5);

                        frameIndex[0] = (frameIndex[0] + 1) % zombieImages.length;

                    }
                    else {
                        attackZombie();
                    }
                })

        );
        runZombie.play();
    }

    protected void attackZombie(){
        if (eating != null && eating.getStatus() == Animation.Status.RUNNING) return;
        isAttacking = true;
        zombieAttack = new Image[20];
        for (int i = 0; i < zombieAttack.length; i++) {
            zombieAttack[i] = new Image(getClass().getResourceAsStream("/Zombies/NormalZombie/ZombieAttack/ZombieAttack_" + i + ".png"));
        }

        final int[] frameIndex = new int[1];

        eating = new Timeline(new KeyFrame(Duration.millis(70) , event -> {//animation eating
                System.out.println(isAttacking);
                zombieView.setImage(zombieAttack[frameIndex[0]]);
                frameIndex[0] = (frameIndex[0] + 1) % zombieAttack.length;
        }));
        eating.setCycleCount(Animation.INDEFINITE);
        eating.play();

        Timeline[] damage = new Timeline[1];

        damage[0] = new Timeline(new KeyFrame(Duration.seconds(1), e -> {//get damage plant
            if (targetPlant != null && targetPlant.isAlive()) {
                targetPlant.setHP(targetPlant.getHP() - 1);
            } else {
                isAttacking = false;
                targetPlant = null;
                eating.stop();
                damage[0].stop();
            }
        }));
        damage[0].setCycleCount(Animation.INDEFINITE);
        damage[0].play();
    }

    protected Plant if_touch_plant(){
        for (Plant p : GameManager.getPlants()){
            if(this.col == p.getCol()){
                if(Math.abs((p.getRow() * cell_size + Sizes.START_X_GRID) - this.zombieView.getLayoutX()) < 1){
                    return p;
                }
            }
        }
        return null;
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
