package Map;

import Plants.Plant;
import Plants.Sun;
import Zombies.Zombie;
import javafx.scene.layout.StackPane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaveLoad extends StackPane implements Serializable {
    private List<Cart> selectedCards = new ArrayList<>();
    private List<Zombie> zombies;
    private List<Plant> plants;
    private List<Sun> suns;
    private int sunPoint;
    private int timeLevel;

    public SaveLoad(List<Cart> selectedCards){
        this.selectedCards = selectedCards;
        zombies = new ArrayList<>();
        plants = new ArrayList<>();
        suns = new ArrayList<>();
        sunPoint = 0;
        timeLevel = 0;
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

}
