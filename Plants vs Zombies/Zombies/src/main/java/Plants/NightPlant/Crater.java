package Plants.NightPlant;

import Map.GameManager;
import Plants.Plant;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static Map.Cell.cell_size;

public class Crater extends Plant {
    private final int timeDead = (20 * 1000) / GameManager.timeUpdatePlants;
    private int t = timeDead;

    public Crater(int row, int col) {
        super(row, col);
        HP = Integer.MAX_VALUE;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater1.png")));
        plantView.setFitHeight(cell_size * 0.9);
        plantView.setFitWidth(cell_size * 0.9);
    }

    @Override
    public void dead() {
        if (!isAlive) return;
        isAlive = false;
        GameManager.removePlant(this);

        plantView.setOnMouseClicked(null);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        if (t > timeDead * 0.75) {
            if (plantView == null) {
                plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater1.png")));
            } else
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater1.png")));
        }
        else if (t <= timeDead * 0.75){
            if (plantView == null){
                plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater2.png")));
            }else
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater2.png")));
        }else if (t <= timeDead * 0.5){
            if (plantView == null){
                plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater4.png")));
            }else
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater4.png")));
        }else if (t <= timeDead * 0.25){
            if (plantView == null){
                plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater3.png")));
            }else
                plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/Crafter/crate3.png")));
        }
        plantView.setFitHeight(cell_size * 0.9);
        plantView.setFitWidth(cell_size * 0.9);

        GameManager.getCells()[row][col].removePlant();
        GameManager.getCells()[row][col].setPlant(this);
    }

    @Override
    public void update() {
        t--;
        if (t == timeDead * 0.75){
            plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater2.png")));
        }if (t == timeDead * 0.5){
            plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater4.png")));
        }if (t == timeDead * 0.25){
            plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/Crafter/crater3.png")));
        }
        if (t <= 0){
            dead();
        }
    }
}
