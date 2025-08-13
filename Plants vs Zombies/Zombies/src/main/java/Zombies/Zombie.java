package Zombies;

import Map.GameManager;
import Map.Sizes;
import Plants.NightPlant.Crater;
import Plants.NightPlant.Grave;
import Plants.NightPlant.HypenoShroom;
import Plants.Plant;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import static Map.Cell.cell_size;
import static Zombies.ZombieType.*;

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
    protected boolean isAlive;
    protected Plant targetPlant;
    protected boolean isSpeedHalf;
    protected double distance;
    protected ZombieType mode;
    protected String imgPath = "/Zombies/NormalZombie/Zombie/Zombie_" ,
                     imgPathAttack = "/Zombies/NormalZombie/ZombieAttack/ZombieAttack_" ,
                     imgPathDead = "/Zombies/NormalZombie/ZombieDie/ZombieDie_" ,
                     imgPathBoomDie = "/Zombies/NormalZombie/BoomDie/BoomDie_" ,
                     imgFreezedZombie = "/Zombies/FreezedZombie/freezNormalZombie.png";
    protected int imgLen = 22 , imgAttackLen = 21 , imgDieLen = 10;
    protected ZombieState state;
    protected boolean hypnosis;
    public boolean getBonus = false;

    public Zombie(int col){
        mode = RUN;
        HP = 5;
        this.speed = cell_size/4;
        distance = 1;
        this.col = col;
        isSpeedHalf = false;
        isAlive = true;
        hypnosis = false;
        zombieView = new ImageView();
        zombieImages = setZombieImages(imgPath , imgLen);
//        zombieAttack = setZombieImages(imgPathAttack , imgAttackLen);
//        zombieDei = setZombieImages(imgPathDead , imgDieLen);
        zombieView.setLayoutX(1500);
        zombieView.setLayoutY(col * cell_size + 30);
        imgFreezedZombie = "/Zombies/FreezedZombie/freezNormalZombie.png";
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
        runZombie = new Timeline();
        runZombie.setCycleCount(Timeline.INDEFINITE);
        final int[] frameIndex = new int[1];

        int[] fps = new int[]{zombieImages.length};
        double[] frameIntervalMs = new double[]{((1000000)/(fps[0] * speed * 30))};
        double[] dxPerFrame = new double[]{speed / fps[0]};

        runZombie.getKeyFrames().add(
                new KeyFrame(Duration.millis(Math.abs(frameIntervalMs[0])), e -> {
                    if(isAlive && mode == RUN) {
                        frameIntervalMs[0] = ((1000000) / (fps[0] * speed * 30));
                        dxPerFrame[0] = speed / fps[0];

                        if (zombieView.getLayoutX() < Sizes.START_X_GRID - Sizes.CELL_SIZE){//Win
                            GameManager.lose();
                        }

                        if (HP <= 0) {//Dead
                            mode = DEAD;
                            isAlive = false;
                            runZombie.stop();
                            deadZombie();
                        }
                        if(!hypnosis) {
                            Zombie z = if_touch_zombie();
                            if ((targetPlant = if_touch_plant()) == null && (z == null || !z.hypnosis)) {//Run
                                frameIndex[0] = (frameIndex[0] + 1) % zombieImages.length;
                                zombieView.setImage(zombieImages[frameIndex[0]]);
                                zombieView.setLayoutX(zombieView.getLayoutX() - dxPerFrame[0]);
                            } else if (z != null && z.isHypnosis()){//attack to hypnosis zombie
                                mode = EATING;
                                attackZombie(z);
                            }else if (targetPlant != null){//eating plant
                                mode = EATING;
                                attackZombie();
                            }
                        }
                        else {
                            Zombie z = if_touch_zombie();
                            if (z == null) {//Run
                                frameIndex[0] = (frameIndex[0] + 1) % zombieImages.length;
                                zombieView.setImage(zombieImages[frameIndex[0]]);
                                zombieView.setLayoutX(zombieView.getLayoutX() - dxPerFrame[0]);
                            } else {//eating
                                mode = EATING;
                                attackZombie(z);
                            }
                        }
                    }
                })

        );
        runZombie.play();
    }

    public void speedIsHalf(){
        if(isSpeedHalf)
            return;
        isSpeedHalf = true;
        if (runZombie != null && runZombie.getStatus() == Animation.Status.RUNNING){
            runZombie.stop();
        }
        speed = speed/2;
        imgPath = "/Zombies/NormalZombie/ZombieIce/Zombie_";
        zombieImages = setZombieImages(imgPath , 22);
        run();
    }

    public void bonus(){
        if (getBonus) return;
        getBonus = true;
        System.out.println("BRAIN EAT");
        int bonus = (int) (Math.random() * 100);
        if (bonus < 70){
            if (runZombie != null && runZombie.getStatus() == Animation.Status.RUNNING){
                runZombie.stop();
            }
            speed = speed * 2;
            run();
        }else{
            HP = HP * 2;
        }
    }

    protected void attackZombie(){
        zombieAttack = setZombieImages(imgPathAttack , imgAttackLen);
        if (getDamage != null && getDamage.getStatus() == Animation.Status.RUNNING) return;
//        zombieImages = null;
        if(runZombie != null)
            runZombie.stop();
        runZombie = null;

        final int[] frameIndex = new int[1];
        final double[] time = {0};
        getDamage = new Timeline(new KeyFrame(Duration.millis(70), e -> {//get damage plant
            time[0] += 0.07;
                if (targetPlant != null && targetPlant.isAlive()) {
                    if(isAlive && mode == EATING) {
                        if (HP <= 0) {
                            mode = DEAD;
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
                    targetPlant = null;
                    getDamage.stop();
                    getDamage  = null;
//                    zombieImages = setZombieImages("/Zombies/NormalZombie/Zombie/Zombie_" , 22);
                    run();
                }
        }));
        getDamage.setCycleCount(Animation.INDEFINITE);
        getDamage.play();
    }

    protected void attackZombie(Zombie z){//for hypnosis mode
        zombieAttack = setZombieImages(imgPathAttack , imgAttackLen);
        if (getDamage != null && getDamage.getStatus() == Animation.Status.RUNNING) return;

        if(runZombie != null)
            runZombie.stop();
        runZombie = null;

        final int[] frameIndex = new int[1];
        final double[] time = {0};
        getDamage = new Timeline(new KeyFrame(Duration.millis(70), e -> {//get damage plant
            time[0] += 0.07;
            if (z != null && z.isAlive()) {
                if(isAlive && mode == EATING) {
                    if (HP <= 0) {
                        mode = DEAD;
                        getDamage.stop();
                        deadZombie();
                    }
                    zombieView.setImage(zombieAttack[frameIndex[0]]);
                    frameIndex[0] = (frameIndex[0] + 1) % zombieAttack.length;
                    if((int)(time[0] + 0.07) > (int) time[0]) {
                        z.setHP(z.getHP() - 1);
                        System.out.println("eating :" + mode);
                    }
                }
            }
            else{
                System.out.println("finish eating");
                mode = RUN;
                getDamage.stop();
                getDamage  = null;
                run();
            }
        }));
        getDamage.setCycleCount(Animation.INDEFINITE);
        getDamage.play();
    }

    protected Plant if_touch_plant(){
        if(hypnosis)
            return null;
        for (Plant p : GameManager.getPlants()){
            if(this.col == p.getCol()){
                if (p.getClass() == Grave.class || p.getClass() == Crater.class)
                    continue;
                if(Math.abs((p.getRow() * cell_size + Sizes.START_X_GRID) - this.zombieView.getLayoutX()) < distance){
                    if(p.getClass() == HypenoShroom.class) {
                        if(((HypenoShroom) p).isCoffee()) {
                            hypnosis();
                            p.dead();
                            return null;
                        }
                    }
                    return p;
                }
            }
        }
        return null;
    }

    protected Zombie if_touch_zombie(){
        for (Zombie z : GameManager.getZombies()){
            if(z == this) continue;
            boolean attack = z.isHypnosis() ^ hypnosis;
            if (Math.abs((z.getZombieView().getLayoutX() - this.zombieView.getLayoutX())) < distance
                && attack && z.getCol() == col && z.isAlive())
                return z;
        }
        return null;
    }



    private void hypnosis(){
        hypnosis = true;
        mode = RUN;
        speed *= -1;
        zombieView.setScaleX(-1);
    }

    private void stopTimeLines(){
        if(runZombie != null && (runZombie.getStatus() == Animation.Status.RUNNING)){
            runZombie.stop();
        }
        if(deadZombie != null && (deadZombie.getStatus() == Animation.Status.RUNNING)){
            deadZombie.stop();
        }
        if(boomDieZombie != null && (boomDieZombie.getStatus() == Animation.Status.RUNNING)){
            boomDieZombie.stop();
        }
        if(getDamage != null && getDamage.getStatus() == Animation.Status.RUNNING){
            getDamage.stop();
        }
    }

    public void ice(){
        stopTimeLines();
        try {
            Image frozenImage = new Image(getClass().getResourceAsStream(imgFreezedZombie));
            zombieView.setImage(frozenImage);
        }catch (Exception e){
            e.printStackTrace();
        }
        Timeline t = new Timeline(new KeyFrame(Duration.seconds(4) , event -> {
            zombieImages = setZombieImages(imgPath , imgLen);
            zombieAttack = setZombieImages(imgPathAttack , imgAttackLen);
            zombieDei = setZombieImages(imgPathDead , imgDieLen);
            switch (mode) {
                case EATING: attackZombie();break;
                case DEAD: deadZombie();    break;
                default: run();        break;
            }
        }));
        t.setCycleCount(1);
        t.play();
    }

    protected void deadZombie(){
        isAlive = false;
        if((deadZombie != null && deadZombie.getStatus() == Animation.Status.RUNNING)) {
            return;
        }
        if(runZombie != null && (runZombie.getStatus() == Animation.Status.RUNNING)){
            runZombie.stop();
        }
        if(getDamage != null && (getDamage.getStatus() == Animation.Status.RUNNING)){
           getDamage.stop();
        }

        zombieDei = setZombieImages(imgPathDead , imgDieLen);
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
//            zombieDei = null;
            runZombie = null;
            getDamage = null;
            zombieAttack = null;
            zombieImages = null;
        }));
        dead.setCycleCount(1);
        dead.play();
    }

    public void bomDie(){
        isAlive = false;
        stopTimeLines();

        zombieDei = setZombieImages(imgPathBoomDie , 20);

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
            zombieDei = null;
            runZombie = null;
            getDamage = null;
            zombieAttack = null;
            zombieImages = null;
        }));
        dead.setCycleCount(1);
        dead.play();
    }

    public void pause(){
        if(runZombie != null && (runZombie.getStatus() == Animation.Status.RUNNING)){
            runZombie.stop();
        }
        if(deadZombie != null && (deadZombie.getStatus() == Animation.Status.RUNNING)){
            deadZombie.stop();
        }
        if(boomDieZombie != null && (boomDieZombie.getStatus() == Animation.Status.RUNNING)){
            boomDieZombie.stop();
        }
        if(getDamage != null && getDamage.getStatus() == Animation.Status.RUNNING){
            getDamage.stop();
        }
        x = zombieView.getLayoutX();
        state = new ZombieState(this);
    }

    public void resume(){
        if(state != null) {
            if(zombieView!=null)
                GameManager.getPanePlantVsZombie().getChildren().remove(zombieView);
            mode = state.getZombieType();
            HP = state.getHP();
            speed = state.getSpeed();
            distance = 1;
            col = state.getCol();
            isSpeedHalf = state.isSpeedHalf();
            isAlive = state.isAlive();
            imgPath = state.getImgPath();
            imgPathAttack = state.getImgPathAttack();
            imgPathDead = state.getImgPathDead();
            imgPathBoomDie = state.getImgPathBoomDie();
            imgLen = state.getImgLen();
            imgAttackLen = state.getImgAttackLen();
            imgDieLen = state.getImgDieLen();
            x = state.getX();

            zombieView = new ImageView();
            GameManager.getPanePlantVsZombie().getChildren().add(zombieView);
            zombieImages = setZombieImages(imgPath, imgLen);
            zombieAttack = setZombieImages(imgPathAttack, imgAttackLen);
            zombieDei = setZombieImages(imgPathDead, imgDieLen);
            zombieView.setLayoutX(x);
            zombieView.setLayoutY(col * cell_size + 30);

            switch (mode) {
                case EATING: attackZombie();break;
                case DEAD: deadZombie();    break;
                default: run();        break;
            }
        }
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


    public double getX() {
        return x;
    }

    public Plant getTargetPlant() {
        return targetPlant;
    }

    public double getDistance() {
        return distance;
    }

    public ZombieType getMode() {
        return mode;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getImgPathAttack() {
        return imgPathAttack;
    }

    public String getImgPathDead() {
        return imgPathDead;
    }

    public String getImgPathBoomDie() {
        return imgPathBoomDie;
    }

    public int getImgLen() {
        return imgLen;
    }

    public int getImgAttackLen() {
        return imgAttackLen;
    }

    public int getImgDieLen() {
        return imgDieLen;
    }

    public void setState(ZombieState state) {
        this.state = state;
    }

    public boolean isHypnosis() {
        return hypnosis;
    }
}
