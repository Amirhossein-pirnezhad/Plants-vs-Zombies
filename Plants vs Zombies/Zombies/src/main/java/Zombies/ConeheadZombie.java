package Zombies;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static Map.Cell.cell_size;

public class ConeheadZombie extends Zombie{

    public ConeheadZombie(int col) {
        super(col);
        HP = 7;
        speed = 20;
    }

    @Override
    protected void setZombieImages(){
        zombieImages = new Image[21];
        for (int i = 0; i < 21; i++) {
            zombieImages[i] = new Image(getClass().getResourceAsStream("/Zombies/ConeheadZombie/ConeheadZombie/ConeheadZombie_" + i + ".png"));
        }
        zombieView = new ImageView(zombieImages[0]);
        zombieView.setFitWidth(cell_size * 1.5);
        zombieView.setFitHeight(cell_size * 1.5);
        zombieView.setLayoutX(1500);
        zombieView.setLayoutY(col * 140 + 20);
    }
}
