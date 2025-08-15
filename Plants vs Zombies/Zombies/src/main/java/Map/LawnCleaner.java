package Map;

import Zombies.Zombie;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static Map.Sizes.*;

public class LawnCleaner {
    private int col;
    private ImageView plantView;
    private boolean move = false , start = false;

    public LawnCleaner(int c){
        col = c;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Screen/LawnCleaner.png")));
        plantView.setLayoutX(START_X_GRID - 50);
        plantView.setLayoutY(START_Y_GRID + col * CELL_SIZE + 30);
    }

    private void if_touch_zombie(){
        for (Zombie z : GameManager.getZombies()){
            if ( intersectsAccurately(z) && z.getCol() == col){
                move = true;
                z.setHP(0);
            }
        }
    }

    public void update(){
        if_touch_zombie();
        if (move){
            if (!start){
                start = true;
                GameManager.playOneShot("/Sounds/lawnmower.wav");
            }
            plantView.setLayoutX(plantView.getLayoutX() + 2);
        }
    }

    private boolean intersectsAccurately(Zombie z) {
        double shrink = 30;
        return plantView.getBoundsInParent().intersects(
                z.getZombieView().getBoundsInParent().getMinX() + shrink,
                z.getZombieView().getBoundsInParent().getMinY() + shrink,
                z.getZombieView().getBoundsInParent().getWidth() - 2 * shrink,
                z.getZombieView().getBoundsInParent().getHeight() - 2 * shrink
        );
    }

    public ImageView getPlantView() {
        return plantView;
    }
}
