package Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;


public class Cart implements Serializable {
    private transient ImageView imageView;
    private final CardsType plantType;
    private int price ;
    private int rechargeTime;
    private int timer = 0;
    private boolean isReady , isStart;
    private boolean isAdded;
    public transient BorderPane borderPane;
    private transient Rectangle border;
    private double currentWidth , valueForIncrease;
    private double x;
    private double width = 160;
    private int cycle;
    private int counter = 0 , more = 1000/GameManager.timeUpdatePlants;


    public Cart(CardsType cardsType) {
        Image image = new Image(getClass().getResourceAsStream("/Cards/" + cardsType.toString() + ".png"));
        isAdded = false;
        isStart = false;
        plantType = cardsType;
        switch (cardsType){
            case SUNFLOWER: this.price = 50;  this.rechargeTime = 5;   break;
            case PEASHOOTRER: this.price = 100; this.rechargeTime = 10; break;
            case REPEATER: this.price = 200; this.rechargeTime = 5;    break;
            case TALLNUT: this.price = 125; this.rechargeTime = 5;     break;
            case WALLNUT: this.price = 50;  this.rechargeTime = 5;     break;
            case CHERRYBOMB: this.price = 150; this.rechargeTime = 5;  break;
            case JALAPENO: this.price = 200; this.rechargeTime = 5;    break;
            case SNOWPEA: this.price = 175; this.rechargeTime = 5;     break;
            case DOOMSHROOM: this.price = 125; this.rechargeTime = 5;  break;
            case HYPNOSHROOM: this.price = 75; this.rechargeTime = 5;  break;
            case ICESHROOM: this.price = 75; this.rechargeTime = 5;    break;
            case PUFFSHROOM: this.price = 0; this.rechargeTime = 5;     break;
            case SCARREDYSHROOM: this.price = 25; this.rechargeTime = 5;break;
            case GRAVEBUSTER: this.price = 75; this.rechargeTime = 5;  break;
            case FANOUS: this.price = 25 ; this.rechargeTime = 5;      break;
            case FAN:   this.price = 100;  this.rechargeTime = 5;      break;
            case COFFEEBEAN: this.price =75; this.rechargeTime= 5;     break;
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

    public void update() {
        counter++;
        if (counter % more == 0) {
            if (!isReady) {
                timer++;
                if (timer == rechargeTime) {
                    timer = 0;
                    isReady = true;
                    border.setWidth(imageView.getFitWidth());
                }
            }
        }
        if (!isReady) {
            if (!isStart) {
                currentWidth = (timer / (double) rechargeTime) * imageView.getFitWidth();
                border.setWidth(currentWidth);
                int remainTime = rechargeTime - timer;
                cycle = remainTime * 1000 / GameManager.timeUpdatePlants;
                valueForIncrease = (imageView.getFitWidth() - currentWidth) / (double) cycle;
                isStart = true;
            }
            border.setWidth(Math.min(border.getWidth() + valueForIncrease, imageView.getFitWidth()));
        }



        if (isReady && GameManager.sunPoint >= price){
            border.setFill(Color.GREEN);
        }else border.setFill(Color.RED);

    }

    public void repair(){
        Image image = new Image(getClass().getResourceAsStream("/Cards/" + plantType + ".png"));
        imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(image.getHeight() * (width / image.getWidth()));
        imageView.setPreserveRatio(true);

        if (isReady){
            border = new Rectangle(width , 10);
        }else {
            border = new Rectangle(width * ((double) timer / rechargeTime), 10);
        }
        border.setFill(Color.RED);
        border.setStroke(Color.BLACK);

        isStart = false;

        borderPane = new BorderPane(imageView);
        this.borderPane.setBottom(border);
    }

//    public void animCharging() {
//        border.setWidth(0);
//        border.setFill(Color.RED);
//        int cycle = (int)rechargeTime * 1000 / timeForEachFrame;
//        double valueForIncrease = imageView.getFitWidth() / cycle;
//        recharge = new Timeline(new KeyFrame(Duration.millis(timeForEachFrame) , event -> {
//            border.setWidth(border.getWidth() + valueForIncrease);
//        }));
//        recharge.setCycleCount(cycle);
//        recharge.play();
//    }


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

    public void setReady(boolean ready) {
        isReady = ready;
        isStart = ready;
    }
}
