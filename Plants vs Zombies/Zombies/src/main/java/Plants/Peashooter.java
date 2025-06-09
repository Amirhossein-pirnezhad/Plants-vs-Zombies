package Plants;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import static Map.Cell.cell_size;

public class Peashooter extends Plant{

    public Peashooter(int row, int col) {
        super(row, col);
        plantImage = new Image[12];
        for (int i = 0; i < 12; i++) {
            plantImage[i] = new Image(getClass().getResourceAsStream("/Plants/Peashooter/Peashooter_" + i + ".png"));
        }
        plantView = new ImageView(plantImage[0]);
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
        this.getChildren().add(plantView);
        animPeashooter();
    }

    @Override
    public void dead() {
        isAlive = false;
        if (this.plantView.getParent() instanceof Pane) {
            ((Pane) this.plantView.getParent()).getChildren().remove(plantView);// remove image sun
        }
        plantView.setOnMouseClicked(null);//don't click again
    }

    private void animPeashooter(){
        Timeline tl = new Timeline();
        tl.setCycleCount(Timeline.INDEFINITE);
        final int[] frameIndex = new int[1];

        tl.getKeyFrames().add(
                new KeyFrame(Duration.millis(100), e -> {
                    plantView.setImage(plantImage[frameIndex[0]]);
                    plantView.setX(plantView.getX());
                    frameIndex[0] = (frameIndex[0] + 1) % plantImage.length;
                })
        );
        tl.play();
    }

}
