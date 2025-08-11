package Plants.NightPlant;

import Map.GameManager;
import Map.Sizes;
import Plants.Pea;
import Plants.Peashooter;
import Zombies.Zombie;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static Map.GameManager.peas;

public class PuffShroom extends Peashooter {
    protected String sleepPath = "/Plants/PuffShroom/PuffShroomSleep/PuffShroomSleep_";
    protected String awakePath = "/Plants/PuffShroom/PuffShroom/PuffShroom_";
    protected int imgLenSleep = 16, imgLenAwake = 14;
    protected boolean isSleep = true;

    public PuffShroom(int row, int col) {
        super(row, col);
        coffee = GameManager.night;
        setSleepImage();
        plantView.setTranslateY(-30);
    }

    protected void setSleepImage() {
        plantImage = new Image[imgLenSleep];
        for (int i = 0; i < imgLenSleep; i++) {
            plantImage[i] = new Image(getClass().getResourceAsStream(sleepPath + i + ".png"));
        }
        plantView.setImage(plantImage[0]);
    }

    private void setAwakeImage() {
        plantImage = new Image[imgLenAwake];
        for (int i = 0; i < imgLenAwake; i++) {
            plantImage[i] = new Image(getClass().getResourceAsStream(awakePath + i + ".png"));
        }
        plantView.setImage(plantImage[0]);
    }

    private void wakeUp() {
        isSleep = false;
        setAwakeImage();
    }

    private void sleep() {
        isSleep = true;
        setSleepImage();
    }

    @Override
    protected void shooting(){
        if(isAlive)
            shoot = new Timeline(new KeyFrame(Duration.seconds(secondInCircle) , event -> {
                if(if_Zombie_exist() && coffee) {
                    Timeline tl =  new Timeline(new KeyFrame(Duration.millis(200) , event1 -> {
                        peas.add(new Pea(this));
                    }));
                    tl.setCycleCount(peaInCircle);
                    tl.play();
                }
            }));
        shoot.setCycleCount(Animation.INDEFINITE);
        shoot.play();
    }

    @Override
    protected boolean if_Zombie_exist() {
        for (Zombie z : GameManager.getZombies()) {
            if (z.getCol() == col &&
                    (z.getZombieView().getLayoutX() - (row * Sizes.CELL_SIZE + Sizes.START_X_GRID)) < 4 * Sizes.CELL_SIZE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update() {
        if(HP <= 0){
            dead();
        }
        changeImage(plantImage);

        if (!coffee) {
            if (!isSleep) {
                isSleep = true;
                setSleepImage();
            }
            return;
        }

        boolean zombieNearby = if_Zombie_exist();

        if (isSleep && zombieNearby) {
            wakeUp();
        } else if (!isSleep && !zombieNearby) {
            sleep();
        }
    }

    @Override
    public void resume(){
        isPauses = false;
        if (coffee){
            setAwakeImage();
        }
        else setSleepImage();

        plantView.setTranslateY(-30);
        this.getChildren().addAll(plantView);

        GameManager.getCells()[row][col].removePlant();
        GameManager.getCells()[row][col].setPlant(this);
        shooting();
    }
}
