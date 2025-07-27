package Plants;

import Map.GameManager;
import Zombies.Zombie;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.Serializable;

import static Map.Sizes.*;

public class Pea implements Serializable {
    protected transient Image peaImage;
    protected transient ImageView peaView;
    protected double x;
    protected transient Timeline shoot;
    protected Peashooter peashooter;
    protected boolean isAlive;

    public Pea(Peashooter peashooter){
        peaImage = new Image(getClass().getResourceAsStream("/Bullets/PeaNormal/PeaNormal_0.png"));
        peaView = new ImageView(peaImage);
        peaView.setLayoutX(peashooter.row * CELL_SIZE + START_X_GRID );
        peaView.setLayoutY((peashooter.col + 1) * CELL_SIZE - 10);
        isAlive = true;
        this.peashooter = peashooter;
        animPea();
        peaView.setOnMouseClicked(event -> {
            System.out.println("pea view x :" + peaView.getLayoutX() + "event: " + event.getX());
        });
    }

    public void animPea(){
        shoot = new Timeline(new KeyFrame(Duration.millis(10) , event -> {
            if_touch_Zombie();
            peaView.setLayoutX(peaView.getLayoutX() + 2);
        }));
        shoot.setCycleCount(Animation.INDEFINITE);
        shoot.play();
    }

    protected void if_touch_Zombie(){
        if(isAlive)
            for (Zombie z : GameManager.getZombies()){
                if(z.getCol() == peashooter.col && z.isAlive()){
                    if(Math.abs(peaView.getLayoutX() - z.getZombieView().getLayoutX()) < 5) {
                        System.out.println(peaView.getLayoutX());
                        z.setHP(z.getHP() - 1); //zombie health
                        dead();
                        break;
                    }
                }
            }
    }

    protected void dead(){
        isAlive = false;
        shoot = new Timeline(new KeyFrame(Duration.millis(500), event -> {//boom!
            peaView.setImage(new Image(getClass().getResourceAsStream("/Bullets/PeaNormalExplode/PeaNormalExplode_0.png")));
        }));
        shoot.setCycleCount(1);
        shoot.play();
        shoot = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {//for delete pea
            peashooter.getPeas().remove(this);
            GameManager.getPanePeas().getChildren().remove(peaView);
        }));
        shoot.setCycleCount(1);
        shoot.play();
    }

    public void pause(){
        if(shoot != null && shoot.getStatus() == Animation.Status.RUNNING)
            shoot.stop();
        x = peaView.getLayoutX();
    }

    public void resume(){
        if(peaView != null)
            GameManager.getPanePeas().getChildren().remove(peaView);
        peaImage = new Image(getClass().getResourceAsStream("/Bullets/PeaNormal/PeaNormal_0.png"));
        peaView = new ImageView(peaImage);
        peaView.setLayoutX(x);
        peaView.setLayoutY((peashooter.col + 1) * CELL_SIZE - 10);
        animPea();
    }

    public ImageView getPeaView() {
        return peaView;
    }
}
