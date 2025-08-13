package Plants;

import Map.GameManager;
import Map.Sizes;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import static Map.Cell.cell_size;
import static Map.GameManager.sunPoint;

public class Sun extends Plant{
    private final int point = 50;
    protected int lifeTime = 10 , counter = 0 , more = 1000 / GameManager.timeUpdatePlants;
    private double x , y;

    public Sun(int row, int col , double sty) {
        super(row, col);
        setImage("/Plants/Sun/Sun_" , 22);

        this.getChildren().add(plantView);

        plantView.setOnMouseClicked(e -> {
            sunPoint += point;
            GameManager.updateSunPointLabel();
            dead();
        });
        plantView.setLayoutX(cell_size * col + Sizes.START_X_GRID);
        plantView.setLayoutY(sty);
    }

    @Override
    public void dead() {
        isAlive = false;
        if (this.plantView.getParent() instanceof Pane) {
            ((Pane) this.plantView.getParent()).getChildren().removeAll(plantView , this);// remove image sun
        }
        GameManager.getSuns().remove(this);
        plantView.setOnMouseClicked(null);//don't click again
    }

    public void pause(){
        x = plantView.getLayoutX();
        y = plantView.getLayoutY();
    }

    public void resume(){
        if(plantView == null || plantImage == null) {
            setImage("/Plants/Sun/Sun_", 22);
            this.getChildren().add(plantView);
            plantView.setLayoutX(x);
            plantView.setLayoutY(y);
        }
        if (!GameManager.getSuns().contains(this)){
            GameManager.getSuns().add(this);
        }
    }

    @Override
    public void update() {
        if(plantView.getLayoutY() < row * cell_size + Sizes.START_Y_GRID + 30) { // anim sun
            plantView.setLayoutY(plantView.getLayoutY() + 10);
        }
        changeImage(plantImage);

        counter ++;
        if (counter  %  more == 0){
            lifeTime --;
            if (lifeTime == 0){
                dead();
            }
        }
    }

}
