package Plants;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.Serializable;

import static Map.Cell.cell_size;

public abstract class Plant extends StackPane implements Serializable {
    protected int HP;
    protected transient Image[] plantImage;
    protected transient ImageView plantView;
    protected int row , col;
    protected boolean isAlive;

    public Plant(int row , int col){
        this.row = row;
        this.col = col;
        isAlive = true;
    }
    protected void setImage(String path , int len){
        plantImage = new Image[len];
        for (int i = 0; i < len; i++) {
            plantImage[i] = new Image(getClass().getResourceAsStream(path + i + ".png"));
        }
        plantView = new ImageView(plantImage[0]);
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
    }
    public ImageView getPlantView() {
        return plantView;
    }

    public abstract void dead();

    public abstract void pause();

    public abstract void resume();

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
