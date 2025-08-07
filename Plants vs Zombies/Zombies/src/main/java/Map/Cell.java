package Map;

import Plants.NightPlant.*;
import Plants.Plant;
import Zombies.Zombie;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Cell extends StackPane {
    private int row , col;
    public  Rectangle border;
    private transient ImageView cellView;
    public static double cell_size = Sizes.CELL_SIZE;
    private Plant plant;
    private boolean hasGrave = false;

    public Cell(int r ,int c){
        row = r;
        col = c;
        this.setPrefSize(cell_size, cell_size);
        this.setMinSize(cell_size, cell_size);
        this.setMaxSize(cell_size, cell_size);
        this.setPickOnBounds(false);
        border = new Rectangle(cell_size , cell_size);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.TRANSPARENT);
        this.getChildren().add(border);
    }

    public void setPlant(Plant plant) {
        if (canSetPlant(plant.getClass())) {
            this.plant = plant;
            setCellView(plant.getPlantView());
            if (plant instanceof Grave) {
                hasGrave = true;
            }
        }
    }

    public void removePlant(){
        if(plant != null) {
            getChildren().remove(plant.getPlantView());
            plant = null;
        }
    }

    public void setCellView(ImageView cellView) {
        this.cellView = cellView;
        this.getChildren().add(cellView);
    }

    public boolean canSetPlant(Class<?> newPlantClass) {
        if (newPlantClass == CoffeeBean.class) {
            if (plant != null &&
                    (plant instanceof PuffShroom || plant instanceof ScaredyShroom || plant instanceof DoomShroom ||
                     plant instanceof HypenoShroom || plant instanceof IceShroom)) {
                return true;
            }
            return false;
        }

        if (hasGrave) {
            if (newPlantClass == GraveBuster.class){
                removePlant();
                return true;
            }
            return false;
        } else {
            return plant == null && newPlantClass != GraveBuster.class && newPlantClass != CoffeeBean.class;
        }
    }




    public Plant getPlant() {
        return plant;
    }

    public void setHasGrave(boolean hasGrave) {
        this.hasGrave = hasGrave;
    }

    public boolean isHasGrave() {
        return hasGrave;
    }
}
