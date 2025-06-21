package Map;

import Plants.Plant;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Cart extends StackPane {
    private ImageView imageView;
    private final CardsType plantType;
    private int price ;
    private ScheduledExecutorService scheduler;
    private int rechargeTime;
    private boolean isReady;
    private boolean isAdded;


    public Cart(CardsType cardsType, Image image) {
        isAdded = false;
        plantType = cardsType;
        switch (cardsType){
            case SUNFLOWER: this.price = 50;  this.rechargeTime = 11;   break;
            case PEASHOOTRER: this.price = 100; this.rechargeTime = 20; break;
            case REPEATER: this.price = 200; this.rechargeTime = 50;    break;
            case TALLNUT: this.price = 125; this.rechargeTime = 20;     break;
            case WALLNUT: this.price = 50;  this.rechargeTime = 10;     break;
            case CHERRYBOMB: this.price = 150; this.rechargeTime = 30;  break;
            case JALAPENO: this.price = 200; this.rechargeTime = 15;    break;
            case SNOWPEA: this.price = 175; this.rechargeTime = 14;     break;
        }

        this.imageView = new ImageView(image);
        imageView.setFitWidth(80);
        imageView.setFitHeight(100);
        this.getChildren().add(imageView);
        this.imageView.setOnMouseClicked(event -> {
            GameManager.setSavedCart(this);
            System.out.println("Yes");
        });
        startRechargeTimer();
    }

    private void startRechargeTimer() {
        isReady = false;
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            isReady = true;
        }, rechargeTime, TimeUnit.SECONDS);
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

    public void stopTimer() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
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
}
