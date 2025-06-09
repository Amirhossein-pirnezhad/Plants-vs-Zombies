package Map;

import Plants.Plant;
import Plants.Sun;
import Zombies.Zombie;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private Map map;
    private Pane gamePane;
    private List<Zombie> zombies = new ArrayList<>();
    private List<Plant> plants = new ArrayList<>();
    private List<Sun> suns = new ArrayList<>();

    public static int sunPoint;

    public GameManager(Map map, Pane gamePane) {
        this.map = map;
        this.gamePane = gamePane;
        this.sunPoint = 0;
    }

    public void addZombie(Zombie z) {
        zombies.add(z);
        gamePane.getChildren().add(z.getZombieView());
        z.run();
    }

    public void addPlant(Plant p, int row, int col) {
        plants.add(p);
        map.getCells()[row][col].setCellView(p.getPlantView());
        map.getCells()[row][col].getChildren().add(p.getPlantView());
    }

    public void addSun(Sun sun , int row , int col){
        suns.add(sun);
        ImageView view = sun.getPlantView();
        gamePane.getChildren().add(view);
    }

    public void updateGame() {

    }

    public void spawnZombie(){
        Timeline spawnZombies = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            int row = (int)(Math.random() * 100) % 5;
            Zombie z = new Zombie(1, 2, row);
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
}
