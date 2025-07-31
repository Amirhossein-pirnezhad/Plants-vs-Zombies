package Map;

import Plants.NightPlant.Grave;
import Plants.NightPlant.GraveBuster;
import Plants.Plant;
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
        border = new Rectangle(cell_size , cell_size);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.TRANSPARENT);
        this.getChildren().add(border);
    }

    public void setPlant(Plant plant) {
        if(canSetPlant(plant)) {
            this.plant = plant;
            setCellView(plant.getPlantView());
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

    public boolean canSetPlant(Plant p){
        if (hasGrave) {
            return (p instanceof GraveBuster);
        } else {
            return (plant == null) && !(p instanceof GraveBuster);
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
