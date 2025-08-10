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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static Pane background , panePlantVsZombie = new Pane() , panePeas = new Pane() , paneMeh = new Pane();
    private static List<Zombie> zombies = new ArrayList<>();
    private static List<Plant> plants = new ArrayList<>();
    private static List<Sun> suns = new ArrayList<>();
    public  static List<Pea> peas = new ArrayList<>();
    private GridPane gridPane;
    private static int map_row , map_col;
    private static Cell[][] cells ;

    private static Meh[][] mehGrid;

    public static int sunPoint;
    private VBox plantMenuVBox;
    private static Label sunPointLabel;
    private static Cart savedCart = null;
    private static Timeline game ;
    private Timeline  winTime , updatePlants , updatePeas , mainAttack;
    private final int timeBuildSun = 10 , timeLevel1 = 60;
    public static int timeUpdatePlants = 100;
    private int timeLevel = 1 , timerSun;
    private SaveLoad saveLoad;
    private Button save , pause , resume ,menuButton;
    private Game_Timer game_timer;
    public static boolean night , online;

    private List<Cart> selectedCards = new ArrayList<>();
    private StackPane overlayPane;
    private List<BorderPane> cartView_recharge = new ArrayList<>();
    private ArrayList<String> data , sunData;
    private Client client ;
    private Shovel shovel;
    private int howMuch = 0;


    public GameManager(Pane gamePane , SaveLoad savedGame , boolean isNight , boolean online) {
        this.online = online;
        night = isNight;
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
        suns = savedGame.getSuns();
        plants = savedGame.getPlants();
        zombies = savedGame.getZombies();
        peas = savedGame.getPeas();

        for (Sun s : suns){
            s.resume();
            addSun(s);
        }
        for(Zombie z : zombies){
            z.resume();
            addZombie(z);
        }
        for (Plant p : plants){
            p.resume();
            addPlant(p);
        }
        for (Pea p : peas){
            p.resume();
        }

        if (night){
            for (int i = 0; i < 3; i++) { //random grave in map
                int r = (int)(Math.random() * 100) % 5 + 3;
                int c = (int)(Math.random() * 100) % 4;
                addPlant(new Grave(r,c));
            }
            buildMeh();
        }
        if (online){
            client = new Client();
            data = Client.data;
            sunData = Client.dataSuns;
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
        background.getChildren().addAll(
                GameManager.getPanePeas() ,
                GameManager.getPanePlantVsZombie() ,
                GameManager.getPaneMeh()
        );
        updatePlants();
        updatePeas();
    }

    private void updatePlants(){
        updatePlants = new Timeline(new KeyFrame(Duration.millis(timeUpdatePlants) , event -> {
            List<Plant> copy = new ArrayList<>(plants);
            for (Plant p : copy)//update plants
                p.update();
            List<Sun> copy1 = new ArrayList<>(suns);
            for (Sun s : copy1) //update suns
                s.update();
            for (Cart c : selectedCards)//update carts
                c.update();
        }));
        updatePlants.setCycleCount(Animation.INDEFINITE);
        updatePlants.play();
    }

    private void updatePeas(){
        updatePeas = new Timeline(new KeyFrame(Duration.millis(10) , event -> {
            List<Pea> copy = new ArrayList<>(peas);
            for (Pea p : peas)
                p.update();
        }));
        updatePeas.setCycleCount(Animation.INDEFINITE);
        updatePeas.play();
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
        gridPane.setGridLinesVisible(false);
        panePlantVsZombie.getChildren().add(gridPane);
        gameAttack();
    }

    private void buildMeh(){
        int row = map_row / 2;

        paneMeh.setPickOnBounds(false);
        paneMeh.setMouseTransparent(true);

        mehGrid = new Meh[map_row][map_col];

        paneMeh.setPrefWidth(Sizes.SCREEN_WIDTH/2);
        paneMeh.setPrefHeight(Sizes.SCREEN_HEIGHT);
        paneMeh.setLayoutX(Sizes.SCREEN_WIDTH - (row) * Sizes.CELL_SIZE);
        paneMeh.setLayoutY(Sizes.START_Y_GRID + 10);
        GridPane grid = new GridPane();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < map_col; j++) {

                Meh mehCell = new Meh(i, j);
                mehGrid[i][j] = mehCell;

                grid.add(mehCell, i, j);
            }
        }
        paneMeh.getChildren().add(grid);
    }

    public void addZombie(Zombie z) {
        zombies.add(z);
        if(!panePlantVsZombie.getChildren().contains(z.getZombieView()))
            panePlantVsZombie.getChildren().add(z.getZombieView());
        z.run();
    }

    public static void addPlant(Plant p) {
        if (p instanceof CoffeeBean) {
            plants.add(p);
            cells[p.getRow()][p.getCol()].getChildren().add(p.getPlantView());
            return;
        }
        if(cells[p.getRow()][p.getCol()].canSetPlant(p.getClass())) {
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
        if(p.isAlive())
            p.dead();
    }

    public static void addSun(Sun sun) {
        if (!suns.contains(sun)) {
            suns.add(sun);
            ImageView view = sun.getPlantView();
            if (!background.getChildren().contains(view)) {
                background.getChildren().add(view);
            }
        }
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
                    shovel.setClicked(true);
                    savedCart = null;
                });
            }
        }
    }

    private void pauseGame(){
        if(game != null && game.getStatus() == Animation.Status.RUNNING)
            game.stop();
        if (updatePlants != null && updatePlants.getStatus() == Animation.Status.RUNNING)
            updatePlants.stop();
        if (updatePeas != null && updatePeas.getStatus() == Animation.Status.RUNNING)
            updatePeas.stop();
        if (mainAttack != null && mainAttack.getStatus() == Animation.Status.RUNNING)
            mainAttack.stop();

        for (Zombie z : zombies)
            z.pause();
        for(Plant p : plants)
            p.pause();
        for(Sun s : suns)
            s.pause();
    }

    private void resumeGame(){
        gameAttack();
        for (Zombie z : zombies)
            z.resume();
        List<Plant> copy = new ArrayList<>(plants);
        for(Plant p :copy)
            p.resume();
        List<Sun> copy1 = new ArrayList<>(suns);
        for(Sun s : copy1)
            s.resume();
        updatePeas();
        updatePlants();
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
        saveLoad.setPeas(peas);
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
            if (cells[i][j].getPlant() != null) {
                if (cells[i][j].getPlant().getClass() != Grave.class) {
                    cells[i][j].getPlant().setHP(0);
                    removePlant(cells[i][j].getPlant());
                    shovel.setClicked(false);
                }
            }
            return;
        }
        if(savedCart == null) return;
        Class plantClass;
        switch (savedCart.getPlantType()) {
            case SUNFLOWER:      plantClass = SunFlower.class;      break;
            case PEASHOOTRER:    plantClass = Peashooter.class;     break;
            case REPEATER:       plantClass = Repeater.class;       break;
            case TALLNUT:        plantClass = TallNut.class;        break;
            case WALLNUT:        plantClass = WallNut.class;        break;
            case CHERRYBOMB:     plantClass = CherryBomb.class;     break;
            case JALAPENO:       plantClass = Jalapeno.class;       break;
            case SNOWPEA:        plantClass = SnowPea.class;        break;
            case DOOMSHROOM:     plantClass = DoomShroom.class;     break;
            case HYPNOSHROOM:    plantClass = HypenoShroom.class;   break;
            case ICESHROOM:      plantClass = IceShroom.class;      break;
            case PUFFSHROOM:     plantClass = PuffShroom.class;     break;
            case SCARREDYSHROOM: plantClass = ScaredyShroom.class;  break;
            case GRAVEBUSTER:    plantClass = GraveBuster.class;    break;
            case FAN:            plantClass = Blover.class;         break;
            case FANOUS:         plantClass = Planter.class;        break;
            case COFFEEBEAN:     plantClass = CoffeeBean.class;     break;
            default:             return;
        }
        if (canBuild() && cells[i][j].canSetPlant(plantClass)) {
            Plant choose_plant;
            try {
                Constructor<? extends Plant> constructor = plantClass.getConstructor(int.class, int.class);
                choose_plant = constructor.newInstance(i, j);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            addPlant(choose_plant);
            buyPlant(savedCart);
            savedCart.setReady(false);
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
                shovel.setClicked(false);
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
        if (online){
            System.out.println("ONLINE");
            howMuch++;
            System.out.println("how much :" + howMuch);
            spawnZombie();
            return;
        }
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

    private void spawnZombie(){
        if (data == null || data.isEmpty()){
            spawnZombie(4);
            return;
        }
        String serverData = data.get(0);
        data.remove(data.get(0));
        String[] serverNum = serverData.split(",");
        int col = Integer.parseInt(serverNum[0]);
        int type = Integer.parseInt(serverNum[1]);

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
    
    private void mainAttack(int timeAttack , int type , int more){
        mainAttack = new Timeline(new KeyFrame(Duration.seconds(1) , e -> {
            for (int i = 0; i < more; i++) {
                spawnZombie(type);
            }
        }));
        mainAttack.setCycleCount(timeAttack);
        mainAttack.play();
    }

    private void gameAttack(){
        game = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
            timeLevel ++;
            System.out.println("time :" + timeLevel);
            if (timeLevel % timeBuildSun == 0) {
                spawnSun();
            }
            if (timeLevel == timeLevel1){
                checkWin();
            }
            if(timeLevel == 26){
                mainAttack(7 , 2 , 2);
            }
            if (timeLevel == 47){
                mainAttack(13 , 4 , 3);
            }
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
        if (night) return;
        if (online){
            if (sunData != null && !sunData.isEmpty()) {
                String serverData = sunData.get(0);
                sunData.remove(sunData.get(0));
                String[] serverNum = serverData.split(",");
                int row = Integer.parseInt(serverNum[0]);
                int col = Integer.parseInt(serverNum[1]);
                Sun s = new Sun(row, col, 0);
                addSun(s);
                return;
            }
        }
        int row = (int) (Math.random() * 100) % 5;
        int col = (int) (Math.random() * 100) % 9;
        Sun s = new Sun(row, col, 0);
        addSun(s);
    }

    private void showMenuOptions() {
        pauseGame();

        if (overlayPane != null && background.getChildren().contains(overlayPane)) return;

        overlayPane = new StackPane();
        overlayPane.prefWidthProperty().bind(background.widthProperty());
        overlayPane.prefHeightProperty().bind(background.heightProperty());
        overlayPane.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        overlayPane.setPickOnBounds(true);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        String buttonStyle = "-fx-background-color: #8B4513; -fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10; -fx-background-radius: 8;";

        Button saveButton = new Button("Save Game");
        Button resumeButton = new Button("Resume Game");
        Button exitButton = new Button("Exit Game");
        saveButton.setStyle(buttonStyle);
        resumeButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        saveButton.setOnAction(e -> {
            initialSaveGame();
            if (overlayPane != null && background.getChildren().contains(overlayPane)) {
                background.getChildren().remove(overlayPane);
                overlayPane = null;
            }
        });

        resumeButton.setOnAction(e -> {
            resumeGame();
            if (overlayPane != null && background.getChildren().contains(overlayPane)) {
                background.getChildren().remove(overlayPane);
                overlayPane = null;
            }
        });

        exitButton.setOnAction(e -> {
            System.exit(0);
        });

        vbox.getChildren().addAll(saveButton, resumeButton, exitButton);

        overlayPane.getChildren().add(vbox);
        StackPane.setAlignment(vbox, Pos.CENTER);
        background.getChildren().add(overlayPane);
        overlayPane.requestFocus();
    }


    public static Cell[][] getCells() {
        return cells;
    }

    public static Meh[][] getMehcell(){
        return  mehGrid;
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

    public static Pane getPaneMeh() {
        return paneMeh;
    }

    public static List<Sun> getSuns() {
        return suns;
    }
}