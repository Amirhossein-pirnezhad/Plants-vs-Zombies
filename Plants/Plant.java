package Plants;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public abstract class Plant extends StackPane {
    protected int HP;
    protected Image[] plantImage;
    protected ImageView plantView;
    protected int row , col;
    protected boolean isAlive;

    public Plant(int row , int col){
        this.row = row;
        this.col = col;
        isAlive = true;
    }

    public ImageView getPlantView() {
        return plantView;
    }

    public abstract void dead();

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getHP() {
        return HP;
    }

    public Image[] getPlantImage() {
        return plantImage;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
