package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CoffeeBean extends Plant {
    private int count = 0 , time1 = 1000 / GameManager.timeUpdatePlants , time2 = time1 * 2;
    private Plant p ;
    public CoffeeBean(int row, int col) {
        super(row, col);
        HP = 20;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/CoffeeBean/CoffeeBean.gif")));
        findSleepPlant();
    }

    private void findSleepPlant(){
        p = GameManager.getCells()[row][col].getPlant();
    }

    private void wakeUpNightPlant(){
        p.setCoffee(true);
    }

    @Override
    public void dead() {
        isAlive = false;
        GameManager.getCells()[row][col].getChildren().remove(plantView);

        plantView.setOnMouseClicked(null);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void update() {
        if (count == time1){
            plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/CoffeeBean/CoffeeBeanEat.gif")));
        }
        if (count == time2) {
            wakeUpNightPlant();
            dead();
        }
        count ++;
    }
}
