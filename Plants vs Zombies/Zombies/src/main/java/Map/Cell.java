package Map;

import Plants.Plant;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;


public class Cell extends StackPane implements Serializable {
    private int row , col;
    public Rectangle border;
    private ImageView cellView;
    public static double cell_size = Sizes.CELL_SIZE;
    private Plant plant;

    public Cell(int r ,int c){
        row = r;
        col = c;
        border = new Rectangle(cell_size , cell_size);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.TRANSPARENT);
        this.getChildren().add(border);
    }

    public void setPlant(Plant plant) {
        if(canSetPlant()) {
            this.plant = plant;
            setCellView(plant.getPlantView());
        }
    }

    public void removePlant(){
        getChildren().remove(plant.getPlantView());
        plant = null;
    }

    private void setCellView(ImageView cellView) {
        this.cellView = cellView;
        this.getChildren().add(cellView);
    }

    public boolean canSetPlant(){
        return (plant == null);
    }
}
