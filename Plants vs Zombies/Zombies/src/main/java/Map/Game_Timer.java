package Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Game_Timer {
    private int time;
    private Timeline t;
    private StackPane clip;
    private Rectangle rectangleTime;
    private Rectangle remainTime;
    private double width = Sizes.CELL_SIZE * 2 ,height = Sizes.CELL_SIZE / 2;

    public Game_Timer(int timer){
        time = timer;
        remainTime = new Rectangle(width , height);
        remainTime.setFill(Color.BLUE);
        clip = new StackPane();
        rectangleTime = new Rectangle(0 , height);
        rectangleTime.setFill(Color.RED);
        clip.getChildren().addAll(remainTime ,rectangleTime);
        showTime();
    }

    private void showTime(){
        int timeFrame = 100;
        int cycle = time * 1000 / timeFrame;
        double increaseForEachFrame = width / cycle;

        t = new Timeline(new KeyFrame(Duration.millis(timeFrame) , event -> {
            rectangleTime.setWidth(rectangleTime.getWidth() + increaseForEachFrame);
            remainTime.setWidth(remainTime.getWidth() - increaseForEachFrame);
        }));

        t.setCycleCount(cycle);
        t.play();
    }

    public StackPane getClip() {
        return clip;
    }
}
