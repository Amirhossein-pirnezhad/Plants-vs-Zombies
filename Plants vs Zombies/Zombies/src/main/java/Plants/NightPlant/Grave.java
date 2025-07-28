package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;

public class Grave extends Plant {
    private String[] shapeGrave;
    private  int shape;
    public Grave(int row, int col) {
        super(row, col);
        shape = (int) (Math.random() % 2);
        setImage(shapeGrave[shape] , 1);
    }

    @Override
    public void dead() {
        isAlive = false;
        GameManager.removePlant(this);

        plantView.setOnMouseClicked(null);//don't click again
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        setImage(shapeGrave[shape] , 1);
    }
}
