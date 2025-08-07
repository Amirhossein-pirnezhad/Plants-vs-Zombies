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
    private transient Timeline lifeTimeline;
    private transient Timeline animTimeline;
    private int point = 50;
    private int lifeTime = 10;
    private double x , y;

    public Sun(int row, int col , double sty) {
        super(row, col);
        setImage("/Plants/Sun/Sun_" , 22);

        this.getChildren().add(plantView);

        plantView.setOnMouseClicked(e -> {
            sunPoint += point;
            GameManager.updateSunPointLabel();
            lifeTimeline.stop();
            dead();
        });
        plantView.setLayoutX(cell_size * col + Sizes.START_X_GRID);
        plantView.setLayoutY(sty);

        animSun();
        startLifeTime();
    }

    @Override
    public void dead() {
        isAlive = false;
        if (animTimeline != null && animTimeline.getStatus() == Animation.Status.RUNNING)
            animTimeline.stop();
        if (this.plantView.getParent() instanceof Pane) {
            ((Pane) this.plantView.getParent()).getChildren().removeAll(plantView , this);// remove image sun
        }
        plantView.setOnMouseClicked(null);//don't click again
    }

    private void startLifeTime(){
        lifeTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1) , e->{
                    lifeTime --;
                    if(lifeTime == 0)
                        dead();
                })
        );
        lifeTimeline.setCycleCount(lifeTime);
        lifeTimeline.play();
    }

    private void animSun(){
        animTimeline = new Timeline();
        animTimeline.setCycleCount(Timeline.INDEFINITE);
        final int[] frameIndex = new int[1];

        animTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(100), e -> {
                    plantView.setImage(plantImage[frameIndex[0]]);
                    if(plantView.getLayoutY() < row * cell_size + Sizes.START_Y_GRID + 30)
                        plantView.setLayoutY(plantView.getLayoutY() + 10);
                    frameIndex[0] = (frameIndex[0] + 1) % plantImage.length;
                })
        );
        animTimeline.play();
    }

    public void pause(){
        if (animTimeline != null && animTimeline.getStatus() == Animation.Status.RUNNING)
            animTimeline.stop();
        if (lifeTimeline != null && lifeTimeline.getStatus() == Animation.Status.RUNNING)
            lifeTimeline.stop();
        x = plantView.getLayoutX();
        y = plantView.getLayoutY();
    }

    public void resume(){
        if(plantView != null)
            GameManager.getBackground().getChildren().remove(plantView);
        setImage("/Plants/Sun/Sun_" , 22);
        this.getChildren().add(plantView);
        plantView.setLayoutX(x);
        plantView.setLayoutY(y);
        if(!GameManager.getBackground().getChildren().contains(plantView))
            GameManager.getBackground().getChildren().add(plantView);
        animSun();
        startLifeTime();
    }

    @Override
    public void update() {

    }

}
