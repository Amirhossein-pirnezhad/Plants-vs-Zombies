package Plants;

import Map.GameManager;
import Map.Sizes;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import static Map.Cell.cell_size;

public class SunFlower extends Plant{
    protected Timeline buildSun;
    protected Timeline anim;
    private int timeBuild = 10;

    public SunFlower(int row, int col) {
        super(row, col);
        HP = 4;
        setImage("/Plants/SunFlower/SunFlower_" , 17);
        animSunFlower();
        buildSun();
    }

    private void animSunFlower(){
        final int[] frame = new int[1];

        anim = new Timeline(new KeyFrame(Duration.millis(100) , event -> {
            if(HP <= 0){
                dead();
            }
            plantView.setImage(plantImage[frame[0]]);
            frame[0] = (frame[0] + 1) % plantImage.length;
        }));
        anim.setCycleCount(Animation.INDEFINITE);
        anim.play();
    }

    private void buildSun(){
        buildSun = new Timeline(new KeyFrame(Duration.seconds(timeBuild) , event -> {
            GameManager.addSun(new Sun(row, col , cell_size * row + Sizes.START_Y_GRID ) , row , col);
        }));
        buildSun.setCycleCount(Animation.INDEFINITE);
        buildSun.play();
    }

    @Override
    public void dead() {
        isAlive = false;
        System.out.println("plant dead");
        if (this.plantView.getParent() instanceof Pane) {
            ((Pane) this.plantView.getParent()).getChildren().removeAll(plantView , this);// remove image
            GameManager.getPlants().remove(this);
        }
        plantView.setOnMouseClicked(null);//don't click again
    }
}
