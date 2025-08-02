package Plants.NightPlant;

import Map.Cell;
import Map.GameManager;
import Plants.Plant;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static Map.Cell.cell_size;

public class Grave extends Plant {
    private String[] shapeGrave;
    private int shape;

    public Grave(int row, int col) {
        super(row, col);
        HP = Integer.MAX_VALUE;
        shape = (int) (Math.random() * 2);
        shapeGrave = new String[]{"Grave.png" , "Grave2.png"};
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Screen/Grave/" + shapeGrave[shape])));
        plantView.setFitHeight(cell_size * 1.5);
        plantView.setFitWidth(cell_size );
    }

    @Override
    public void dead() {
        isAlive = false;
        GameManager.removePlant(this);

        plantView.setOnMouseClicked(null);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        setImage(shapeGrave[shape] , 1);
    }
}
