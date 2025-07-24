package Map;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Cart extends StackPane {
    private final String cardName;
    private final ImageView imageView;
    private int price ;
    private ScheduledExecutorService scheduler;
    private int rechargeTime;
    private boolean isReady;


    public Cart(String cardName, Image image) {
        this.cardName = cardName;
        switch (cardName){
            case "card_0" : this.price = 50;  this.rechargeTime = 11; break;
            case "card_1" : this.price = 100; this.rechargeTime = 20; break;
            case "card_2" : this.price = 250; this.rechargeTime = 50; break;
            case "card_3" : this.price = 125; this.rechargeTime = 20; break;
            case "card_4" : this.price = 50;  this.rechargeTime = 10; break;
            case "card_5" : this.price = 150; this.rechargeTime = 30; break;
            case "card_6" : this.price = 200; this.rechargeTime = 15; break;
            case "card_7" : this.price = 175; this.rechargeTime = 14; break;
            case "card_8" : this.price = 250; this.rechargeTime = 19; break;
            case "card_9" : this.price = 50;  this.rechargeTime = 10;  break;
        }
        this.imageView = new ImageView(image);
        imageView.setFitWidth(80);
        imageView.setFitHeight(100);
        getChildren().add(imageView);

        startRechargeTimer();
    }

    public void startRechargeTimer() {
        isReady = false;        // اول بازی کارت اماذه نیست
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            isReady = true;       // بعد از زمان شارژ کارت اماده میشه
            System.out.println(cardName + " is now ready to use!");
        }, rechargeTime, TimeUnit.SECONDS);
    }

    public String getCardName() {
        return cardName;
    }

    public Image getCardImage() {
        return imageView.getImage();
    }

    public int getPrice() {
        return price;
    }

    public void stopTimer() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    public boolean isReady() {
        return isReady;
    }
}
