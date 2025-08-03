package Map;

import Plants.NightPlant.Grave;
import Plants.NightPlant.GraveBuster;
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

    public boolean canSetPlant(Class p) {
        if (hasGrave) {
            if (p == GraveBuster.class){
                removePlant();
                return true;
            }
            return p == GraveBuster.class;
        } else {
            return plant == null && p != GraveBuster.class;
        }
    }

    public boolean hasZombie() {
        for (Zombie z : GameManager.getZombies()) {
            if (!z.isAlive()) continue;
            if (z.getCol() != this.col)   continue;
            if (z.getZombieView().getBoundsInParent()
                    .intersects( this.getBoundsInParent() )) { // if zombie on it
                return true;
            }
        }
        return false;
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
