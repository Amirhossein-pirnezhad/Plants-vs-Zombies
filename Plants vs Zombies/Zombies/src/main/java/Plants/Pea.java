package Plants;

import Map.GameManager;
import Zombies.Zombie;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import static Map.Sizes.*;

public class Pea {
    protected Image peaImage;
    protected ImageView peaView;
    protected double speed;
    protected Timeline shot;
    protected Peashooter peashooter;
    protected boolean isAlive;

    public Pea(Peashooter peashooter){
        peaImage = new Image(getClass().getResourceAsStream("/Bullets/PeaNormal/PeaNormal_0.png"));
        peaView = new ImageView(peaImage);
        isAlive = true;
        this.peashooter = peashooter;
        animPea();
    }

    public void animPea(){
        peaView.setLayoutX(peashooter.row * CELL_SIZE + START_X_GRID );
        peaView.setLayoutY((peashooter.col + 1) * CELL_SIZE - 10);
        shot = new Timeline(new KeyFrame(Duration.millis(10) , event -> {
            if_touch_Zombie();
            peaView.setLayoutX(peaView.getLayoutX() + 2);
        }));
        shot.setCycleCount(Animation.INDEFINITE);
        shot.play();
    }

    protected void if_touch_Zombie(){
        if(isAlive)
            for (Zombie z : GameManager.getZombies()){
                if(z.getCol() == peashooter.col && z.isAlive()){
                    if(Math.abs(peaView.getLayoutX() - z.getZombieView().getLayoutX()) < 5) {
//                        System.out.println(peaView.getLayoutX());
                        z.setHP(z.getHP() - 1); //zombie health
                        dead();
                        break;
                    }
                }
            }
    }

    protected void dead(){
        isAlive = false;
        shot = new Timeline(new KeyFrame(Duration.millis(500), event -> {//boom!
            peaView.setImage(new Image(getClass().getResourceAsStream("/Bullets/PeaNormalExplode/PeaNormalExplode_0.png")));
        }));
        shot.setCycleCount(1);
        shot.play();
        shot = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {//for delete pea
            peashooter.getPeas().remove(this);
            GameManager.getPanePeas().getChildren().remove(peaView);
        }));
        shot.setCycleCount(1);
        shot.play();
    }

    public ImageView getPeaView() {
        return peaView;
    }
}
