package Map;

import Plants.Plant;
import Plants.Sun;
import Zombies.ConeheadZombie;
import Zombies.Zombie;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static Pane gamePane;
    private static List<Zombie> zombies = new ArrayList<>();
    private List<Plant> plants = new ArrayList<>();
    private List<Sun> suns = new ArrayList<>();
    private GridPane gridPane;
    private int map_row , map_col;
    private Cell[][] cells;
    public static int sunPoint;

    public GameManager(Pane gamePane) {
        this.gamePane = gamePane;
        this.sunPoint = 0;
        gridPane = new GridPane();
        map_row = 9;
        map_col = 5;
        cells = new Cell[map_row][map_col];
        gridPane.setTranslateX(Sizes.START_X_GRID);
        gridPane.setTranslateY(Sizes.START_Y_GRID);
        gridPane.setGridLinesVisible(true);
        buildMap();
    }

    public void buildMap(){
        for (int i = 0; i < map_row; i++) {
            for (int j = 0; j < map_col; j++) {
                cells[i][j] = new Cell(i , j);
                gridPane.add(cells[i][j] , i , j);
            }
        }
        gamePane.getChildren().add(gridPane);
    }

    public void addZombie(Zombie z) {
        zombies.add(z);
        gamePane.getChildren().add(z.getZombieView());
        z.run();
    }

    public void addPlant(Plant p, int row, int col) {
        plants.add(p);
        cells[row][col].setCellView(p.getPlantView());
    }

    public void addSun(Sun sun , int row , int col){
        suns.add(sun);
        ImageView view = sun.getPlantView();
        gamePane.getChildren().add(view);
    }

    public void updateGame() {

    }

    public void spawnZombie(){
        Timeline spawnZombies = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            int row = (int)(Math.random() * 100) % 5;
            int type = (int)(Math.random() * 100) % 2;
            Zombie z = type == 0 ? new Zombie(row) : new ConeheadZombie(row);
            addZombie(z);
        }));
        spawnZombies.setCycleCount(Timeline.INDEFINITE);
        spawnZombies.play();
    }

    public void spawnSun(){
        Timeline tlSun = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            int row = (int)(Math.random() * 100) % 5;
            int col = (int)(Math.random() * 100) % 12;
            Sun s = new Sun(row , col);
            addSun(s , row , col);
        }));
        tlSun.setCycleCount(Timeline.INDEFINITE);
        tlSun.play();
    }

    public static void addImageView(ImageView imageView){
        gamePane.getChildren().add(imageView);
    }

    public Cell[][] getCells() {
        return cells;
    }

    public static Pane getGamePane() {
        return gamePane;
    }

    public static List<Zombie> getZombies() {
        return zombies;
    }
}
