package Plants.NightPlant;

import Map.GameManager;
import Map.Sizes;
import Plants.Peashooter;
import Zombies.Zombie;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class PuffShroom extends Peashooter {
    protected String sleepPath = "/Plants/PuffShroom/PuffShroomSleep/PuffShroomSleep_";
    protected String awakePath = "/Plants/PuffShroom/PuffShroom/PuffShroom_";
    protected int imgLenSleep = 16, imgLenAwake = 14;
    protected boolean isSleep = true;
    private Timeline sleepCheckTimeline;

    public PuffShroom(int row, int col) {
        super(row, col);
        setSleepImage();
        plantView.setTranslateY(-30);
        setupSleepCheck();
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

    private void setupSleepCheck() {
        sleepCheckTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> checkWakeCondition())
        );
        sleepCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        sleepCheckTimeline.play();
    }

    private void checkWakeCondition() {
        boolean zombieNearby = if_Zombie_exist();
        if (isSleep && zombieNearby) {
            wakeUp();
        } else if (!isSleep && !zombieNearby) {
            sleep();
        }
    }

    private void wakeUp() {
        isSleep = false;
        setAwakeImage();
//        if (animPeashooter != null) {
//            animPeashooter.stop();
//        }
//        if (shoot != null)
//            shoot.stop();
//        animPeashooter();
    }

    private void sleep() {
        isSleep = true;
//        if (shoot != null) {
//            shoot.stop();
//        }
//        if(animPeashooter != null)
//            animPeashooter.stop();
        setSleepImage();
//        animPeashooter();
    }

    @Override
    protected boolean if_Zombie_exist() {
        for (Zombie z : GameManager.getZombies()) {
            if (z.getCol() == col && ((z.getZombieView().getLayoutX() - (row * Sizes.CELL_SIZE + Sizes.START_X_GRID)) < 4 * Sizes.CELL_SIZE)) {
                return true;
            }
        }
        return false;
    }

//    @Override
//    protected void animPeashooter() {
//        if (isSleep) return;
//        if (isAlive) shooting();
//
//        animPeashooter = new Timeline();
//        animPeashooter.setCycleCount(Timeline.INDEFINITE);
//        final int[] frameIndex = new int[1];
//
//        animPeashooter.getKeyFrames().add(
//                new KeyFrame(Duration.millis(100), e -> {
//                    plantView.setImage(plantImage[frameIndex[0]]);
//                    if (HP <= 0) {
//                        dead();
//                    }
//                    frameIndex[0] = (frameIndex[0] + 1) % plantImage.length;
//                })
//        );
//        animPeashooter.play();
//    }

    @Override
    public void pause() {
        super.pause();
        if (sleepCheckTimeline != null) {
            sleepCheckTimeline.pause();
        }
    }

    @Override
    public void resume() {
        super.resume();
        if (sleepCheckTimeline != null) {
            sleepCheckTimeline.play();
        }
    }
}