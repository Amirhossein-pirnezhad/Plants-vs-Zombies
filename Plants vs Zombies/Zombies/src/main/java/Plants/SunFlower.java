package Plants;

import Map.GameManager;
import Map.Sizes;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;
import static Map.Cell.cell_size;

public class SunFlower extends Plant{
    private final int timeBuild = 10;
    private int time , frame = 0 , counter = 0;
    private final int more = 1 * 1000 / GameManager.timeUpdatePlants;

    public SunFlower(int row, int col) {
        super(row, col);
        HP = 4;
        setImage("/Plants/SunFlower/SunFlower_" , 18);
    }

    protected void changeImage(Image[] images){
        if(frame >= images.length) frame = 0;
        plantView.setImage(images[frame]);
        frame = (frame + 1) % images.length;
    }

    @Override
    public void dead() {
        isAlive = false;
        System.out.println("plant dead");
        GameManager.removePlant(this);
        plantView.setOnMouseClicked(null);//don't click again
    }

    public void pause(){

    }

    public void resume(){
        GameManager.getCells()[row][col].removePlant();
        setImage("/Plants/SunFlower/SunFlower_" , 18);
        GameManager.getCells()[row][col].setPlant(this);
    }

    @Override
    public void update() {
        counter ++;
        if (counter % more == 0){
            time++;
            if(time % timeBuild == 0) {
                System.out.println("AADD SUN");
                GameManager.addSun(new Sun(col, row, cell_size * col + Sizes.START_Y_GRID), row, col);
            }
        }
        if(HP <= 0){
            dead();
        }
        changeImage(plantImage);
    }
}
