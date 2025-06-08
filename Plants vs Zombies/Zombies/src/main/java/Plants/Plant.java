package Plants;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Plant {
    protected int HP;
    protected Image[] plantImage;
    protected ImageView plantView;
    protected int row , col;

    public Plant(int row , int col){
        this.row = row;
        this.col = col;
    }

}
