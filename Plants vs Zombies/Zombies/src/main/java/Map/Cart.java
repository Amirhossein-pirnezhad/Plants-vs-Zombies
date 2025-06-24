package Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Cart{
    private ImageView imageView;
    private final CardsType plantType;
    private int price ;
    private Timeline scheduler;
    private Timeline recharge;
    private int rechargeTime;
    private int timer;
    private boolean isReady;
    private boolean isAdded;
    public BorderPane borderPane;
    private Rectangle border;


    public Cart(CardsType cardsType, Image image) {
        isAdded = false;
        plantType = cardsType;
        switch (cardsType){
            case SUNFLOWER: this.price = 50;  this.rechargeTime = 11;   break;
            case PEASHOOTRER: this.price = 100; this.rechargeTime = 10; break;
            case REPEATER: this.price = 200; this.rechargeTime = 10;    break;
            case TALLNUT: this.price = 125; this.rechargeTime = 10;     break;
            case WALLNUT: this.price = 50;  this.rechargeTime = 10;     break;
            case CHERRYBOMB: this.price = 150; this.rechargeTime = 10;  break;
            case JALAPENO: this.price = 200; this.rechargeTime = 11;    break;
            case SNOWPEA: this.price = 175; this.rechargeTime = 10;     break;
        }

        this.imageView = new ImageView(image);
        imageView.setFitWidth(160);
        imageView.setFitHeight(image.getHeight() * (160 / image.getWidth()));
        imageView.setPreserveRatio(true);

        border= new Rectangle(0, 10);
        border.setY(imageView.getFitHeight() );
        border.setFill(Color.RED);
        border.setStroke(Color.BLACK);

        borderPane = new BorderPane(imageView);
        this.borderPane.setBottom(border);
    }

    public void startRechargeTimer() {
        isReady = false;
        scheduler = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
            timer ++;
            if(timer == rechargeTime) {
                isReady = true;
                border.setFill(Color.GREEN);
                System.out.println("isReady" + plantType);
                scheduler.stop();
            }
        }));
        scheduler.setCycleCount(rechargeTime);
        scheduler.play();
        animCharging();
    }

    public void animCharging() {
        border.setWidth(0);
        border.setFill(Color.RED);
        int timeForEachFrame = 100;
        int cycle = (int)rechargeTime * 1000 / timeForEachFrame;
        double valueForIncrease = imageView.getFitWidth() / cycle;
        recharge = new Timeline(new KeyFrame(Duration.millis(timeForEachFrame) , event -> {
            border.setWidth(border.getWidth() + valueForIncrease);
        }));
        recharge.setCycleCount(cycle);
        recharge.play();
    }


    public ImageView getCardImageView() {
        return imageView;
    }

    public int getPrice() {
        return price;
    }

    public CardsType getPlantType() {
        return plantType;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public boolean isReady() {
        return isReady;
    }

    public Rectangle getBorder() {
        return border;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
