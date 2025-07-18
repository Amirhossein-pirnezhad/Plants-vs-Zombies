package Map;

import Plants.*;
import Zombies.ImpZombie;
import Zombies.ScreenDoorZombie;
import javafx.animation.Animation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import Zombies.ConeheadZombie;
import Zombies.Zombie;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    private Timeline game;
    private int timeLevel = 0;
    private SaveLoad saveLoad;
    private Button save , pause , resume;
    private Game_Timer game_timer;

    private List<Cart> selectedCards = new ArrayList<>();
    private List<BorderPane> cartView_recharge = new ArrayList<>();


    public GameManager(Pane gamePane , SaveLoad savedGame) {
        saveLoad = savedGame;
        for(Cart cart : savedGame.getSelectedCards()){
            cart.repair();
        }
        selectedCards = savedGame.getSelectedCards();
        background = gamePane;
        gridPane = new GridPane();
        timeLevel = savedGame.getTimeLevel();
        sunPoint = savedGame.getSunPoint();
        map_row = 9;
        map_col = 5;
        cells = new Cell[map_row][map_col];
        gridPane.setTranslateX(Sizes.START_X_GRID);
        gridPane.setTranslateY(Sizes.START_Y_GRID);
        gridPane.setGridLinesVisible(true);
        buildMap();
        initializePlantMenu();
        for (Sun s : suns){
            s.resume();
            addSun(s , s.getRow() , s.getCol());
        }
        for(Zombie z : savedGame.getZombies()){
            z.resume();
            addZombie(z);
        }
        for (Plant p : savedGame.getPlants()){
            p.resume();
            addPlant(p);
            cells[p.getRow()][p.getCol()].setPlant(p);
        }
        save = new Button("Save");
        pause = new Button("Pause");
        resume = new Button("resume");
        VBox vBox = new VBox(save , pause , resume);
        panePlantVsZombie.getChildren().add(vBox);
        game_timer = new Game_Timer(timeLevel);
        StackPane timerBar = game_timer.getClip();
        timerBar.setLayoutX(100);
        timerBar.setLayoutY(50);
        panePlantVsZombie.getChildren().add(timerBar);
        sunPoint = 1000;
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
            imageView.setFitWidth(160);
            imageView.setFitHeight(image.getHeight() * (160 / image.getWidth()));

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
                    gridPane.add(cells[i][j], i, j);
                }
            }
        panePlantVsZombie.getChildren().add(gridPane);
        gameAttack();

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
        for (Zombie z : zombies){
            if (z.getZombieView().getLayoutX() < Sizes.START_X_GRID)
                lose();
        }
        save.setOnAction(event -> {
            game.stop();
            initialSaveGame();
        });
        pause.setOnAction(event -> {
            for (Zombie z : zombies)
                z.pause();
            for(Plant p : plants)
                p.pause();
            for(Sun s : suns)
                s.pause();
        });
        resume.setOnAction(event -> {
            for (Zombie z : zombies)
                z.resume();
            for(Plant p : plants)
                p.resume();
            for(Sun s : suns)
                s.resume();
        });
    }

    private void SaveGame(SaveLoad save, String fileName) {
        try {
            File dir = new File("saves");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName + ".txt");

            try (FileOutputStream fileOut = new FileOutputStream(file);
                 ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

                objectOut.writeObject(save);
                objectOut.flush();
                System.out.println("save" + file.getAbsolutePath());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initialSaveGame(){
        for(Zombie z : zombies)
            z.pause();
        for (Plant p : plants)
            p.pause();
        for (Sun s : suns){
            s.pause();
        }

        saveLoad = new SaveLoad(selectedCards);
        saveLoad.setTimeLevel(timeLevel);
        saveLoad.setZombies(zombies);
        saveLoad.setPlants(plants);
        saveLoad.setSuns(suns);
        saveLoad.setSunPoint(sunPoint);
        getSaveName(saveLoad);
    }

    private void getSaveName(SaveLoad saveLoad){
        Stage stage = new Stage();
        stage.setTitle("Save");
        Button enter = new Button("Enter");
        Text text = new Text("Please enter your save's name\nthen press enter");
        TextField textField = new TextField();
        textField.setPromptText("Please enter your save's name:");
        BorderPane borderPane = new BorderPane(textField);
        borderPane.setTop(text);
        borderPane.setBottom(enter);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
        enter.setOnAction(event -> {
            if(textField.getText() != ""){
                SaveGame(saveLoad , textField.getText());
                stage.close();
                System.exit(0);
            }
        });
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
            Cart c = selectedCards.get(i);
            if(c.getPrice() > sunPoint){
                c.getBorder().setFill(Color.RED);
            }
            else if(c.isReady()){
                c.getBorder().setFill(Color.GREEN);
            }
            s.setOnMouseClicked(event -> {
                savedCart = c;
                System.out.println(savedCart.getPlantType());
            });
        }
    }

    private boolean canBuild(){
        return savedCart.isReady() && (sunPoint >= savedCart.getPrice());
    }

    public void spawnZombie(int model){
            int col = (int)(Math.random() * 100) % 5;
            int type = (int)(Math.random() * 100) % model;
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
    }

    private void gameAttack(){
        game = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
            timeLevel ++;
            if(timeLevel > 3 && timeLevel <=15){
                if(timeLevel % 3 == 0){
                    spawnZombie(1);
                }
            }
            else if(timeLevel > 15 && timeLevel <= 30){
                if(timeLevel % 2 == 0)
                    spawnZombie(2);
            }
            else if(timeLevel > 30 && timeLevel <= 45) {
                if (timeLevel % 2 == 0)
                    spawnZombie(3);
            }
            else if(timeLevel > 45){
                if (timeLevel % 2 == 0) {
                    spawnZombie(4);
                    spawnZombie(4);
                }
            }
        }));
        game.setCycleCount(Animation.INDEFINITE);
        game.play();
    }

    private void lose(){
        game.stop();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("You lost!");

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