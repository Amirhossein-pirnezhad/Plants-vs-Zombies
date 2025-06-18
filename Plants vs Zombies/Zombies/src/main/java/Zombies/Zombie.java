package Zombies;

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
    protected Image[] zombieDei;
    protected ImageView zombieView;
    protected Timeline deadZombie;
    protected Timeline runZombie;
    protected Timeline eating;
    protected boolean isAttacking;
    protected Plant targetPlant;
    protected boolean isSpeedHalf;

    public Zombie(int col){
        HP = 5;
        this.speed = cell_size/4;
        this.col = col;
        isAttacking = false;
        isSpeedHalf = false;
        setZombieImages();
        zombieView.setLayoutX(1500);
        zombieView.setLayoutY(col * cell_size + 30);
    }

    protected void setZombieImages(){
        zombieImages = new Image[21];
        for (int i = 0; i < zombieImages.length; i++) {
            zombieImages[i] = new Image(getClass().getResourceAsStream("/Zombies/NormalZombie/Zombie/Zombie_" + i + ".png"));
        }
        zombieView = new ImageView(zombieImages[0]);
        zombieView.setFitWidth(cell_size * 1.5);
        zombieView.setFitHeight(cell_size * 1.5);
    }

    protected void setZombieImageAttack(){
        zombieAttack = new Image[20];
        for (int i = 0; i < zombieAttack.length; i++) {
            zombieAttack[i] = new Image(getClass().getResourceAsStream("/Zombies/NormalZombie/ZombieAttack/ZombieAttack_" + i + ".png"));
        }
    }

    public void run(){
        runZombie = new Timeline();
        runZombie.setCycleCount(Timeline.INDEFINITE);
        final int[] frameIndex = new int[1];

        int[] fps = new int[]{zombieImages.length};
        double[] frameIntervalMs = new double[]{(speed*30/fps[0])};
        double[] dxPerFrame = new double[]{speed / fps[0]};

        runZombie.getKeyFrames().add(
                new KeyFrame(Duration.millis(frameIntervalMs[0]), e -> {
                    frameIntervalMs[0] = speed*30/fps[0];
                    dxPerFrame[0] = speed/fps[0];
                    if (HP <= 0) {
                        runZombie.stop();
                        deadZombie();
                    }
                    if((targetPlant = if_touch_plant()) == null) {

                        zombieView.setImage(zombieImages[frameIndex[0]]);
                        zombieView.setLayoutX(zombieView.getLayoutX() - dxPerFrame[0]);
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

        setZombieImageAttack();

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
            if (targetPlant != null && targetPlant.isAlive() ) {
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
        if(runZombie != null || (runZombie.getStatus() == Animation.Status.RUNNING)){
            runZombie.stop();
        }
        zombieDei = new Image[9];
        for (int i = 0; i < zombieDei.length; i++) {
            zombieDei[i] = new Image(getClass().getResourceAsStream("/Zombies/NormalZombie/ZombieDie/ZombieDie_" + i + ".png"));
        }
        final int[] frame = new int[1];
        deadZombie = new Timeline(new KeyFrame(Duration.millis(200) , event -> {
            zombieView.setImage(zombieDei[frame[0]]);
            frame[0] = (frame[0] + 1) % zombieDei.length;
        }));
        deadZombie.setCycleCount(zombieDei.length);
        deadZombie.play();
        deadZombie = new Timeline(new KeyFrame(Duration.millis(1000) , event -> {
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

    public boolean isSpeedHalf() {
        return isSpeedHalf;
    }

    public void setSpeedHalf(boolean speedHalf) {
        isSpeedHalf = speedHalf;
    }
}
