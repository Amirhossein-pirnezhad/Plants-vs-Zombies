package Zombies;

import Map.GameManager;
import Map.Sizes;
import Map.ZombieType;
import Plants.Plant;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import static Map.Cell.cell_size;
import static Map.ZombieType.*;

import Map.ZombieType.*;

import java.io.Serializable;

public class Zombie implements Serializable {
    protected int HP;
    protected double speed;
    protected int col;
    protected double x;
    protected transient Image[] zombieImages;
    protected transient Image[] zombieAttack;
    protected transient Image[] zombieDei;
    protected transient ImageView zombieView;
    protected transient Timeline deadZombie;
    protected transient Timeline runZombie;
    protected transient Timeline boomDieZombie;
    protected transient Timeline getDamage;
    protected boolean isAttacking;
    protected boolean isAlive;
    protected Plant targetPlant;
    protected boolean isSpeedHalf;
    protected double distance;
    protected ZombieType mode;

    public Zombie(int col){
        mode = RUN;
        HP = 5;
        this.speed = cell_size/4;
        distance = 1;
        this.col = col;
        isAttacking = false;
        isSpeedHalf = false;
        isAlive = true;
        zombieView = new ImageView();
        zombieImages = setZombieImages("/Zombies/NormalZombie/Zombie/Zombie_" , 22);
        zombieView.setLayoutX(1500);
        zombieView.setLayoutY(col * cell_size + 30);
    }

    protected Image[] setZombieImages(String path , int len){
        Image[] images = new Image[len];
        for (int i = 0; i < len; i++) {
            images[i] = new Image(getClass().getResourceAsStream(path + i + ".png"));
        }
        zombieView.setFitWidth(cell_size * 1.5);
        zombieView.setFitHeight(cell_size * 1.5);
        return images;
    }

    public void run(){
        getDamage = null;
        zombieAttack = null;
        runZombie = new Timeline();
        runZombie.setCycleCount(Timeline.INDEFINITE);
        final int[] frameIndex = new int[1];

        int[] fps = new int[]{zombieImages.length};
        double[] frameIntervalMs = new double[]{((1000000)/(fps[0] * speed * 30))};
        double[] dxPerFrame = new double[]{speed / fps[0]};

        runZombie.getKeyFrames().add(
                new KeyFrame(Duration.millis(frameIntervalMs[0]), e -> {
                    if(isAlive && mode == RUN) {
                        frameIntervalMs[0] = ((1000000) / (fps[0] * speed * 30));
                        dxPerFrame[0] = speed / fps[0];

                        if (HP <= 0) {
                            mode = DEAD;
                            isAlive = false;
                            runZombie.stop();
                            deadZombie();
                        }
                        if ((targetPlant = if_touch_plant()) == null) {//Run
                            frameIndex[0] = (frameIndex[0] + 1) % zombieImages.length;
                            zombieView.setImage(zombieImages[frameIndex[0]]);
                            zombieView.setLayoutX(zombieView.getLayoutX() - dxPerFrame[0]);
                        } else {
                            mode = EATING;
                            attackZombie();
                        }
                    }
                })

        );
        runZombie.play();
    }

    public void speedIsHalf(){
        isSpeedHalf = true;
        speed = speed/2;
        zombieImages = null;
        zombieImages = setZombieImages("/Zombies/NormalZombie/ZombieIce/Zombie_" , 22);
    }

    protected void attackZombie(){
        if (getDamage != null && getDamage.getStatus() == Animation.Status.RUNNING) return;
        isAttacking = true;
        zombieImages = null;
        runZombie.stop();
        runZombie = null;
        zombieAttack = setZombieImages("/Zombies/NormalZombie/ZombieAttack/ZombieAttack_" , 21);

        final int[] frameIndex = new int[1];
        final double[] time = {0};
        getDamage = new Timeline(new KeyFrame(Duration.millis(70), e -> {//get damage plant
            time[0] += 0.07;
                if (targetPlant != null && targetPlant.isAlive()) {
                    if(isAlive && mode == EATING) {
                        if (HP <= 0) {
                            mode = DEAD;
                            zombieAttack = null;
                            getDamage.stop();
                            deadZombie();
                        }
                        zombieView.setImage(zombieAttack[frameIndex[0]]);
                        frameIndex[0] = (frameIndex[0] + 1) % zombieAttack.length;
                        if((int)(time[0] + 0.07) > (int) time[0]) {
                            targetPlant.setHP(targetPlant.getHP() - 1);
                            System.out.println("eating :" + mode);
                        }
                    }
                }
            else{
                    System.out.println("finish eating");
                    mode = RUN;
                    isAttacking = false;
                    targetPlant = null;
                    getDamage.stop();
                    getDamage  = null;
                    zombieImages = setZombieImages("/Zombies/NormalZombie/Zombie/Zombie_" , 22);
                    run();
                }
        }));
        getDamage.setCycleCount(Animation.INDEFINITE);
        getDamage.play();
    }

    protected Plant if_touch_plant(){
        for (Plant p : GameManager.getPlants()){
            if(this.col == p.getCol()){
                if(Math.abs((p.getRow() * cell_size + Sizes.START_X_GRID) - this.zombieView.getLayoutX()) < distance){
                    return p;
                }
            }
        }
        return null;
    }

    protected void deadZombie(){
        isAlive = false;
        if(runZombie != null && (runZombie.getStatus() == Animation.Status.RUNNING)){
            runZombie.stop();
        }
        if(getDamage != null && (getDamage.getStatus() == Animation.Status.RUNNING)){
           getDamage.stop();
        }
        runZombie = null;
        getDamage = null;
        zombieAttack = null;
        zombieImages = null;

        zombieDei = setZombieImages("/Zombies/NormalZombie/ZombieDie/ZombieDie_" , 10);

        Image[] zombieLostHead = setZombieImages("/Zombies/NormalZombie/ZombieHead/ZombieHead_" , 11);//anim lost head
        ImageView lostHead = new ImageView(zombieLostHead[0]);
        lostHead.setLayoutX(zombieView.getLayoutX() + 30);
        lostHead.setLayoutY(zombieView.getLayoutY() + 20);
        lostHead.setFitWidth(cell_size * 1.5);
        lostHead.setFitWidth(cell_size * 1.5);
        GameManager.getPanePlantVsZombie().getChildren().add(lostHead);

        final int[] frame = new int[]{0};
        final int[] frame1 = new int[]{0};

        deadZombie = new Timeline(new KeyFrame(Duration.millis(200) , event -> {
            zombieView.setImage(zombieDei[frame[0]]);
            lostHead.setImage(zombieLostHead[frame1[0]]);
            frame[0] = (frame[0] + 1) % zombieDei.length;
            frame1[0] = (frame1[0] + 1) % zombieLostHead.length;
        }));

        deadZombie.setCycleCount(zombieDei.length);
        deadZombie.play();

        Timeline dead = new Timeline(new KeyFrame(Duration.millis(200 * zombieDei.length) , event -> {
            GameManager.getZombies().remove(this);
            GameManager.getPanePlantVsZombie().getChildren().removeAll(zombieView, lostHead);
            zombieImages = null;
            zombieDei = null;
        }));
        dead.setCycleCount(1);
        dead.play();
    }

    public void bomDie(){
        isAlive = false;
        if(runZombie != null && (runZombie.getStatus() == Animation.Status.RUNNING)){
            runZombie.stop();
            runZombie = null;
        }
        if(deadZombie  != null && deadZombie.getStatus() == Animation.Status.RUNNING) {
            deadZombie.stop();
            deadZombie = null;
        }
        if(getDamage.getStatus() == Animation.Status.RUNNING) {
            getDamage.stop();
            getDamage = null;
        }

        zombieAttack = null;
        zombieImages = null;
        zombieDei = setZombieImages("/Zombies/NormalZombie/BoomDie/BoomDie_" , 19);

        final int[] frame = new int[]{0};

        boomDieZombie = new Timeline(new KeyFrame(Duration.millis(200) , event -> {
            zombieView.setImage(zombieDei[frame[0]]);
            frame[0] = (frame[0] + 1) % zombieDei.length;
        }));
        boomDieZombie.setCycleCount(zombieDei.length);
        boomDieZombie.play();

        Timeline dead = new Timeline(new KeyFrame(Duration.millis(200 * zombieDei.length) , event -> {
            GameManager.getZombies().remove(this);
            GameManager.getPanePlantVsZombie().getChildren().removeAll(zombieView);
            zombieImages = null;
            zombieDei = null;
        }));
        dead.setCycleCount(1);
        dead.play();
    }

    public void pause(){
        if(runZombie != null && (runZombie.getStatus() == Animation.Status.RUNNING)){
            runZombie.stop();
        }
        if(deadZombie != null ){
            deadZombie.stop();
        }
        if(boomDieZombie != null ){
            boomDieZombie.stop();
        }
        if(getDamage != null ){
            getDamage.stop();
        }
        x = zombieView.getLayoutX();

    }

    public void resume(){
        zombieView = new ImageView();
        zombieImages = setZombieImages("/Zombies/NormalZombie/Zombie/Zombie_" , 22);
        zombieView.setLayoutX(x);
        zombieView.setLayoutY(col * cell_size + 30);
        run();
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

    public boolean isAlive() {
        return isAlive;
    }
}
