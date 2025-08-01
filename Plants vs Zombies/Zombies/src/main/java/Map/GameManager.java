package Map;

import Plants.*;
import Plants.NightPlant.*;
import Zombies.*;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import Zombies.ConeheadZombie;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
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
    private static Timeline game ;
    private Timeline tlSunBuild , winTime;
    private final int timeBuildSun = 5 , timeLevel1 = 60;
    private int timerSun;
    private int timeLevel = 1;
    private SaveLoad saveLoad;
    private Button save , pause , resume ,menuButton;
    private Game_Timer game_timer;
    private boolean night = true;

    private List<Cart> selectedCards = new ArrayList<>();
    private List<BorderPane> cartView_recharge = new ArrayList<>();
    private Shovel shovel;


    public GameManager(Pane gamePane , SaveLoad savedGame) {
        shovel = new Shovel();
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
        gridPane.setLayoutX(Sizes.START_X_GRID);
        gridPane.setLayoutY(Sizes.START_Y_GRID);
        gridPane.setGridLinesVisible(true);
        buildMap();
        initializePlantMenu();
        for (Sun s : savedGame.getSuns()){
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
        }

        if (night){
            for (int i = 0; i < 3; i++) { //random grave in map
                int r = (int)(Math.random() * 100) % 5 + 3;
                int c = (int)(Math.random() * 100) % 4;
                addPlant(new Grave(r,c));
            }
        }

        menuButton = new Button("menu");
        menuButton.setPrefSize(170, 25);
        menuButton.setStyle(
                "-fx-background-color: #8B4513;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 10;" +
                        "-fx-text-fill: white;"+
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4);"
        );
        Font adventureFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Wonderland_3.ttf"), 40);

        menuButton.setFont(adventureFont);
        menuButton.setOnAction(e -> showMenuOptions());
        menuButton.setLayoutX(Sizes.SCREEN_WIDTH - 200);
        menuButton.setLayoutY( 10);

        panePlantVsZombie.getChildren().addAll(menuButton, shovel.getImageView());
        game_timer = new Game_Timer(timeLevel);
        StackPane timerBar = game_timer.getClip();
        timerBar.setLayoutX(100);
        timerBar.setLayoutY(50);
//        panePlantVsZombie.getChildren().add(timerBar);
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
        if(!panePlantVsZombie.getChildren().contains(z.getZombieView()))
            panePlantVsZombie.getChildren().add(z.getZombieView());
        z.run();
    }

    public static void addPlant(Plant p) {
        if(cells[p.getRow()][p.getCol()].canSetPlant()) {
            cells[p.getRow()][p.getCol()].setPlant(p);
            plants.add(p);
            System.out.println("can");
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
        if(!background.getChildren().contains(view))
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
                shovel.getImageView().setOnMouseClicked(event -> {
                    System.out.println("shovel");
                    shovel.setClicked(true);
                    savedCart = null;
                });
            }
        }
    }

//    private void if_game_over(){
//        for (Zombie z : zombies){
//            if (z.getZombieView().getLayoutX() < Sizes.START_X_GRID)
//                lose();
//        }
//    }

    private void pauseGame(){
        if(game != null && game.getStatus() == Animation.Status.RUNNING)
            game.stop();
        if (tlSunBuild != null && tlSunBuild.getStatus() == Animation.Status.RUNNING)
            tlSunBuild.stop();

        for (Zombie z : zombies)
            z.pause();
        for(Plant p : plants)
            p.pause();
        for(Sun s : suns)
            s.pause();
    }

    private void resumeGame(){
        gameAttack();
        spawnSun();
        for (Zombie z : zombies)
            z.resume();
        for(Plant p : plants)
            p.resume();
        for(Sun s : suns)
            s.resume();
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

    private void choice(int i, int j) {
        if (savedCart == null && shovel.isClicked()){
            cells[i][j].getPlant().setHP(0);
            removePlant(cells[i][j].getPlant());
            return;
        }
        else if(savedCart == null && !shovel.isClicked()) return;
        else if (savedCart != null && canBuild() && cells[i][j].canSetPlant()) {
            switch (savedCart.getPlantType()) {
                case SUNFLOWER:      addPlant(new SunFlower(i, j));      break;
                case PEASHOOTRER:    addPlant(new Peashooter(i, j));     break;
                case REPEATER:       addPlant(new Repeater(i, j));       break;
                case TALLNUT:        addPlant(new TallNut(i, j));        break;
                case WALLNUT:        addPlant(new WallNut(i, j));        break;
                case CHERRYBOMB:     addPlant(new CherryBomb(i, j));     break;
                case JALAPENO:       addPlant(new Jalapeno(i, j));       break;
                case SNOWPEA:        addPlant(new SnowPea(i, j));        break;
                case DOOMSHROOM:     addPlant(new DoomShroom(i, j));     break;
                case HYPNOSHROOM:    addPlant(new HypenoShroom(i, j));   break;
                case ICESHROOM:      addPlant(new IceShroom(i, j));      break;
                case PUFFSHROOM:     addPlant(new PuffShroom(i, j));     break;
                case SCARREDYSHROOM: addPlant(new ScaredyShroom(i, j));  break;
                case GRAVEBUSTER:    addPlant(new GraveBuster(i, j));    break;
                default: return;
            }
            buyPlant(savedCart);
            savedCart.startRechargeTimer();
            savedCart = null;
        }
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

    //change random spawn zombie
    private int[] Count = new int[5];
    public void spawnZombie(int model){
        int col = balanceRandom();
        int type = (int)(Math.random() * model) ;
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
        Count[col]++;

    }

    private int balanceRandom(){
        int min = Integer.MAX_VALUE;
        for (int count : Count)
            if(count < min)
                min = count;
        List<Integer> choose = new ArrayList<>();
        for (int i = 0; i < Count.length; i++){
            if(Count[i] <= min)
                choose.add(i);
        }
        return choose.get((int)(Math.random() * choose.size()));
    }

    private void gameAttack(){
        game = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
            timeLevel ++;
            if (timeLevel == timeLevel1){
                checkWin();
            }
            System.out.println(timeLevel);
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
        game.setCycleCount(timeLevel1);
        game.play();
    }

    private void checkWin(){
        winTime = new Timeline(new KeyFrame(Duration.seconds(1) , actionEvent -> {
            System.out.println("zmbi Aive" + zombies.size());
            if (zombies.isEmpty()){
                win();
                winTime.stop();
            }
        }));
        winTime.setCycleCount(Animation.INDEFINITE);
        winTime.play();
    }

    private void win(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You Win!");
        Platform.runLater(() -> alert.showAndWait());
    }

    public static void lose(){
        game.stop();
        for (Zombie z : zombies)
            z.pause();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("You lost!");
        Platform.runLater(() -> alert.showAndWait());
    }

    public void spawnSun(){
        tlSunBuild = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if(timerSun == timeBuildSun) {
                int row = (int) (Math.random() * 100) % 5;
                int col = (int) (Math.random() * 100) % 9;
                Sun s = new Sun(row, col, 0);
                addSun(s, row, col);
                timerSun = -1;
            }
            timerSun ++;
        }));
        tlSunBuild.setCycleCount(timeLevel1);
        tlSunBuild.play();
    }

    private void showMenuOptions() {
        pauseGame();

        Stage menuStage = new Stage();
        menuStage.setTitle("Game Menu");

        Button saveButton = new Button("Save Game");
        Button resumeButton = new Button("Resume Game");
        Button exitButton = new Button("Exit Game");

        String buttonStyle = "-fx-background-color: #8B4513; -fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10;";
        saveButton.setStyle(buttonStyle);
        resumeButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        saveButton.setOnAction(e -> {
            initialSaveGame();
            menuStage.close();
        });

        resumeButton.setOnAction(e -> {
            resumeGame();
            menuStage.close();
        });

        exitButton.setOnAction(e -> {
            menuStage.close();
            System.exit(0);
        });

        VBox vbox = new VBox(10, saveButton, resumeButton, exitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #f5f5dc;");

        Scene scene = new Scene(vbox, 300, 200);
        menuStage.setScene(scene);
        menuStage.showAndWait();
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