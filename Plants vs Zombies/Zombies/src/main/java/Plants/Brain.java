package Plants;

import Map.GameManager;
import Map.Sizes;
import Zombies.Zombie;
import javafx.scene.layout.Pane;

import static Map.Cell.cell_size;

public class Brain extends Sun{
    public Brain(int row, int col, double sty) {
        super(row, col, sty);
        plantView.setOnMouseClicked(null);
        lifeTime = Integer.MAX_VALUE;
        setImage("/Plants/Sun/brain_", 1);
    }
    @Override
    public void update() {
        if (isAlive) {
            if (plantView.getLayoutY() < row * cell_size + Sizes.START_Y_GRID + 30) { // anim
                plantView.setLayoutY(plantView.getLayoutY() + 10);
            }else {
                for (Zombie z : GameManager.getZombies()) {
                    if (intersectsAccurately(z) && !z.getBonus && !z.isHypnosis()) {
                        System.out.println("EATING");
                        z.bonus();
                        GameManager.playOneShot("/Sounds/groan5.wav");
                        dead();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void dead() {
        isAlive = false;
        if (this.plantView.getParent() instanceof Pane) {
            ((Pane) this.plantView.getParent()).getChildren().removeAll(plantView , this);// remove image sun
        }
        GameManager.getPlants().remove(this);
        plantView.setOnMouseClicked(null);//don't click again
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

}
