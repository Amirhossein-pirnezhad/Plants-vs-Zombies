package Plants;

import Map.GameManager;
import Map.Sizes;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import static Map.Cell.cell_size;
import static Map.GameManager.sunPoint;

public class Sun extends Plant{
    private Timeline lifeTimeline;
    private Timeline animTimeline;
    private int point = 50;
    private int lifeTime = 10;

    public Sun(int row, int col) {
        super(row, col);
        plantImage = new Image[21];
        for (int i = 0; i < 21; i++) {
            plantImage[i] = new Image(getClass().getResourceAsStream("/Plants/Sun/Sun_" + i + ".png"));
        }
        plantView = new ImageView(plantImage[0]);
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
        this.getChildren().add(plantView);

        plantView.setOnMouseClicked(e -> {
            sunPoint += point;
            GameManager.updateSunPointLabel();
            lifeTimeline.stop();
            dead();
        });

        animSun();
        startLifeTime();
    }

    @Override
    public void dead() {
        isAlive = false;
        animTimeline.stop();
        if (this.plantView.getParent() instanceof Pane) {
            ((Pane) this.plantView.getParent()).getChildren().removeAll(plantView , this);// remove image sun
        }
        plantView.setOnMouseClicked(null);//don't click again
    }

    private void startLifeTime(){
        lifeTimeline = new Timeline(
                new KeyFrame(Duration.seconds(lifeTime) , e->{
                    dead();
                })
        );
        lifeTimeline.setCycleCount(1);
        lifeTimeline.play();
    }

//    public int clickHandle(){
//        plantView.setOnMouseClicked(event -> {
//            dead();
//            lifeTimeline.stop();
//        });
//        return 50;
//    }

    private void animSun(){
        animTimeline = new Timeline();
        animTimeline.setCycleCount(Timeline.INDEFINITE);
        final int[] frameIndex = new int[1];

        plantView.setX(cell_size * col + Sizes.START_X_GRID);
        plantView.setY(0);

        animTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(100), e -> {
                    plantView.setImage(plantImage[frameIndex[0]]);
                    if(plantView.getY() < row * cell_size + Sizes.START_Y_GRID)
                        plantView.setY(plantView.getY() + 10);
                    frameIndex[0] = (frameIndex[0] + 1) % plantImage.length;
                })
        );
        animTimeline.play();
    }

}
