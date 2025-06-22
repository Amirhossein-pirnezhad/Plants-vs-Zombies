package Map;

import Plants.*;
import Zombies.ImpZombie;
import Zombies.ScreenDoorZombie;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Zombies.ConeheadZombie;
import Zombies.Zombie;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static Map.CardsType.*;

public class GameManager {
    private static Pane background , panePlantVsZombie = new Pane() , panePeas = new Pane();
    private static List<Zombie> zombies = new ArrayList<>();
    private static List<Plant> plants = new ArrayList<>();
    private static List<Sun> suns = new ArrayList<>();
    private GridPane gridPane;
    private static int map_row , map_col;
    private static Cell[][] cells;
    public static int sunPoint;
    private VBox plantMenuVBox;
    private static Label sunPointLabel;
    private static Cart savedCart = null;

    private List<Cart> selectedCards = new ArrayList<>();
    private List<BorderPane> cartView_recharge = new ArrayList<>();


    public GameManager(Pane gamePane , List<Cart> selectedCards) {
        this.selectedCards = selectedCards;
        this.background = gamePane;
        this.sunPoint = 0;
        gridPane = new GridPane();
        map_row = 9;
        map_col = 5;
        cells = new Cell[map_row][map_col];
        gridPane.setTranslateX(Sizes.START_X_GRID);
        gridPane.setTranslateY(Sizes.START_Y_GRID);
        gridPane.setGridLinesVisible(true);
        buildMap();
        initializePlantMenu();

    }


    private void initializePlantMenu() {
        plantMenuVBox = new VBox(8);
        plantMenuVBox.setLayoutX(0);
        plantMenuVBox.setLayoutY(90);

        plantMenuVBox.setStyle(
                "-fx-background-color: #8B4513;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 10;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4);"
        );


        for (Cart cart : selectedCards) {
            Image image = cart.getCardImageView().getImage();
            ImageView imageView = cart.getCardImageView();
            imageView.setFitWidth(180);
            imageView.setFitHeight(image.getHeight() * (180 / image.getWidth()));

            Rectangle border = new Rectangle(180, 10);
            border.setY(imageView.getFitHeight() );
            border.setFill(Color.GREEN);
            border.setStroke(Color.BLACK);
            BorderPane borderPane = cart.borderPane;
            borderPane.setCenter(imageView);

            cartView_recharge.add(borderPane);
            cart.startRechargeTimer();
        }
        plantMenuVBox.getChildren().addAll(cartView_recharge);
        panePlantVsZombie.getChildren().add(plantMenuVBox);
    }

    public static void setSunPointLabel(Label label) {
        sunPointLabel = label;
        updateSunPointLabel();
    }

    public static void updateSunPointLabel() {
        if (sunPointLabel != null) {
            sunPointLabel.setText("" + sunPoint);
        }
    }

    public void buildMap(){
        for (int i = 0; i < map_row; i++) {
            for (int j = 0; j < map_col; j++) {
                cells[i][j] = new Cell(i , j);
                gridPane.add(cells[i][j] , i , j);
            }
        }
        panePlantVsZombie.getChildren().add(gridPane);
    }

    public void addZombie(Zombie z) {
        zombies.add(z);
        panePlantVsZombie.getChildren().add(z.getZombieView());
        z.run();
    }

    public void addPlant(Plant p) {
        if(cells[p.getRow()][p.getCol()].canSetPlant()) {
            cells[p.getRow()][p.getCol()].setPlant(p);
            plants.add(p);
        }
        else System.out.println("can't");
    }

    public static void removePlant(Plant p){
        cells[p.getRow()][p.getCol()].removePlant();
        plants.remove(p);
        panePlantVsZombie.getChildren().remove(p.getPlantView());
    }

    public static void addSun(Sun sun , int row , int col){
        suns.add(sun);
        ImageView view = sun.getPlantView();
        background.getChildren().add(view);
    }

    public void updateGame() {
        handleClickOnChoice();
        for (int i = 0; i < map_row; i++) {
            for (int j = 0; j < map_col; j++) {
                int finalI = i;
                int finalJ = j;
                cells[i][j].setOnMouseClicked(event -> {
                    choice(finalI , finalJ);
                });
            }
        }
    }

    private void choice(int i , int j){
        if(savedCart == null || !canBuild()) return;
        switch (savedCart.getPlantType()){
            case SUNFLOWER:   addPlant(new SunFlower(i , j));   buyPlant(savedCart);    break;
            case PEASHOOTRER: addPlant(new Peashooter(i,j));    buyPlant(savedCart);    break;
            case REPEATER:    addPlant(new Repeater(i,j));      buyPlant(savedCart);    break;
            case TALLNUT:     addPlant(new TallNut(i,j));       buyPlant(savedCart);    break;
            case WALLNUT:     addPlant(new WallNut(i,j));       buyPlant(savedCart);    break;
            case CHERRYBOMB:  addPlant(new CherryBomb(i,j));    buyPlant(savedCart);    break;
            case JALAPENO:    addPlant(new Jalapeno(i,j));      buyPlant(savedCart);    break;
            case SNOWPEA:     addPlant(new SnowPea(i,j));       buyPlant(savedCart);    break;
            default:break;
        }
        savedCart.startRechargeTimer();
        savedCart = null;
    }

    private void buyPlant(Cart cart){
        sunPoint -= cart.getPrice();
        updateSunPointLabel();
    }

    private void handleClickOnChoice(){
        for (int i = 0; i < cartView_recharge.size(); i++) {
            BorderPane s = cartView_recharge.get(i);
            int finalI = i;
            s.setOnMouseClicked(event -> {
                savedCart = selectedCards.get(finalI);
                System.out.println(savedCart.getPlantType());
            });
        }
    }

    private boolean canBuild(){
        return savedCart.isReady() && (sunPoint >= savedCart.getPrice());
    }

    public void spawnZombie(){
        Timeline spawnZombies = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            int col = (int)(Math.random() * 100) % 5;
            int type = (int)(Math.random() * 100) % 4;
            Zombie z;
            if(type == 0)
                z = new Zombie(col);
            else if(type == 1)
                z = new ConeheadZombie(col);
            else if(type  == 2)
                z = new ScreenDoorZombie(col);
            else
                z = new ImpZombie(col);
            addZombie(z);
        }));
        spawnZombies.setCycleCount(Timeline.INDEFINITE);
        spawnZombies.play();
    }

    public void spawnSun(){
        Timeline tlSun = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            int row = (int)(Math.random() * 100) % 5;
            int col = (int)(Math.random() * 100) % 9;
            Sun s = new Sun(row , col , 0);
            addSun(s , row , col);
        }));
        tlSun.setCycleCount(Timeline.INDEFINITE);
        tlSun.play();
    }

    public static void addImageView(ImageView imageView){
        background.getChildren().add(imageView);
    }

    public static Cell[][] getCells() {
        return cells;
    }

    public static Pane getPanePlantVsZombie() {
        return panePlantVsZombie;
    }

    public static Pane getPanePeas() {
        return panePeas;
    }

    public static Pane getBackground() {
        return background;
    }

    public static List<Zombie> getZombies() {
        return zombies;
    }

    public static List<Plant> getPlants() {
        return plants;
    }

    public static int getMap_row() {
        return map_row;
    }

    public static int getMap_col() {
        return map_col;
    }
}