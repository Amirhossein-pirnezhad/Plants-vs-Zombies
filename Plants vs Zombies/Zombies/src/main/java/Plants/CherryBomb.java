package Plants;

import Map.GameManager;
import Map.Sizes;
import Zombies.Zombie;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class CherryBomb extends Plant{
    protected Timeline timerBomb;
    protected Timeline dead;
    protected boolean isBoom = false;
    protected double timer = 20;
    protected int frame = 0;
    protected boolean isPauses;

    public CherryBomb(int row, int col) {
        super(row, col);
        isPauses = false;
        HP = Integer.MAX_VALUE;
        setImage("/Plants/CherryBomb/CherryBomb_" , 7);
        bomb();
        plantView.setOnMouseClicked(event -> {
            if(!isPauses)
                pause();
            else resume();
        });
    }

    private void bomb(){
            timerBomb = new Timeline(new KeyFrame(Duration.millis(200) , e ->{
                changeImage(plantImage);
            }));
            timerBomb.setCycleCount(plantImage.length);
            timerBomb.play();
            dead();
    }

    protected boolean isKilled(Zombie z){
        double x1 = Sizes.START_X_GRID + (row - 2) * Sizes.CELL_SIZE;
        double x2 = x1 + 1 * row * Sizes.CELL_SIZE;
        return ((z.getCol() == col) || (z.getCol() == col - 1) || (z.getCol() == col + 1))
                && (z.getZombieView().getLayoutX() < x2) && (z.getZombieView().getLayoutX() > x1);
    }

    protected void killZombie(){
        for (Zombie z : GameManager.getZombies()){
            if(isKilled(z)){
                z.bomDie();
            }
        }
    }

    protected void changeImage(Image[] images){
        if(frame >= images.length) frame = 0;
        plantView.setImage(images[frame]);
        frame = (frame + 1) % images.length;
    }

    protected void setAnimDie(){
        plantView.setImage(new Image(getClass().getResourceAsStream("/Screen/Boom.png")));
    }

    @Override
    public void dead() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            if(timerBomb.getStatus() != Animation.Status.RUNNING) {
                isAlive = false;
                GameManager.removePlant(this);
            }
        }));
        timeline.setCycleCount(1);

        dead = new Timeline(new KeyFrame(Duration.millis(500) , event -> {
            if(timerBomb.getStatus() != Animation.Status.RUNNING) {
                setAnimDie();
                killZombie();
                dead.stop();
                timeline.play();
            }
        }));
        dead.setCycleCount(Animation.INDEFINITE);
        dead.play();
    }

    public void pause(){
        isPauses = true;
        if(timerBomb != null && timerBomb.getStatus() == Animation.Status.RUNNING)
            timerBomb.stop();
        if(dead != null && dead.getStatus() == Animation.Status.RUNNING)
            dead.stop();
    }

    public void resume(){
        bomb();
    }
}
