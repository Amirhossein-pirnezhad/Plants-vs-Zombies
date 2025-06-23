package Map;

import Plants.Plant;
import Plants.Sun;
import Zombies.Zombie;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaveLoad extends StackPane implements Serializable {
//    private GridPane gridPane;
    private static Cell[][] cells;
    private List<Cart> selectedCards = new ArrayList<>();
    private List<Zombie> zombies;
    private List<Plant> plants;
    private List<Sun> suns;
    private int sunPoint;
    private int timeLevel;
    private Button save;

    public SaveLoad(List<Cart> selectedCards){
        this.selectedCards = selectedCards;
//        gridPane = new GridPane();
        cells = new Cell[GameManager.getMap_row()][GameManager.getMap_col()];
        zombies = new ArrayList<>();
        plants = new ArrayList<>();
        suns = new ArrayList<>();
        sunPoint = 0;
        timeLevel = 0;
        save = new Button("Save");
        this.getChildren().add(save);
        for (int i = 0; i < GameManager.getMap_row(); i++) {
            for (int j = 0; j < GameManager.getMap_col(); j++) {
                cells [i][j] = new Cell(i,j);
            }
        }
    }

//    public void setGridPane(GridPane gridPane) {
//        this.gridPane = gridPane;
//    }

    public static void setCells(Cell[][] cells) {
        SaveLoad.cells = cells;
    }

    public void setZombies(List<Zombie> zombies) {
        this.zombies = zombies;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }

    public void setSuns(List<Sun> suns) {
        this.suns = suns;
    }

    public void setSunPoint(int sunPoint) {
        this.sunPoint = sunPoint;
    }

    public void setTimeLevel(int timeLevel) {
        this.timeLevel = timeLevel;
    }

    public Button getSave() {
        return save;
    }

    public int getSunPoint() {
        return sunPoint;
    }

    public int getTimeLevel() {
        return timeLevel;
    }

    public List<Zombie> getZombies() {
        return zombies;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public List<Sun> getSuns() {
        return suns;
    }

    public List<Cart> getSelectedCards() {
        return selectedCards;
    }

    public static Cell[][] getCells() {
        return cells;
    }

//    public GridPane getGridPane() {
//        return gridPane;
//    }
}
