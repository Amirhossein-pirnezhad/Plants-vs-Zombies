package Plants;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Repeater extends Peashooter{
    public Repeater(int row, int col) {
        super(row, col);
        peaInSecond = 2;
    }
    @Override
    protected void setImage(){
        plantImage = new Image[14];
        for (int i = 0; i < plantImage.length; i++) {
            plantImage[i] = new Image(getClass().getResourceAsStream("/Plants/RepeaterPea/RepeaterPea_" + i + ".png"));
        }
        plantView = new ImageView(plantImage[0]);
    }
}
