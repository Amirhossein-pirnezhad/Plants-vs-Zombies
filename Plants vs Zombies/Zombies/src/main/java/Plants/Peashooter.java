package Plants;

import Map.GameManager;
import Zombies.Zombie;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static Map.Cell.cell_size;
import static Map.GameManager.peas;

public class Peashooter extends Plant{
    protected transient Timeline shoot;
    protected int peaInCircle;
    protected double secondInCircle = 1.5;
    protected boolean isPauses;
    protected int frame = 0;


    public Peashooter(int row, int col) {
        super(row, col);
        isPauses = false;
        setImage("/Plants/Peashooter/Peashooter_" , 13);
        HP = 4;
        peaInCircle = 1;
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
        this.getChildren().addAll(plantView);

        shooting();
    }

    @Override
    public void dead() {
        isAlive = false;
        System.out.println("plant dead");
        if(shoot.getStatus() == Animation.Status.RUNNING)
            shoot.stop();

        GameManager.removePlant(this);

        plantView.setOnMouseClicked(null);//don't click again
    }

    protected void changeImage(Image[] images){
        if(frame >= images.length) frame = 0;
        plantView.setImage(images[frame]);
        frame = (frame + 1) % images.length;
    }

    protected void shooting(){
        if(isAlive)
            shoot = new Timeline(new KeyFrame(Duration.seconds(secondInCircle) , event -> {
                if(if_Zombie_exist()) {
                        Timeline tl =  new Timeline(new KeyFrame(Duration.millis(200) , event1 -> {
                            peas.add(new Pea(this));
                            GameManager.getPanePeas().getChildren().add(peas.get(peas.size() - 1).getPeaView());
                        }));
                        tl.setCycleCount(peaInCircle);
                        tl.play();
                }
            }));
            shoot.setCycleCount(Animation.INDEFINITE);
            shoot.play();
    }

    protected boolean if_Zombie_exist(){
        for (Zombie z : GameManager.getZombies()){
            if(z.getCol() == col)
                return true;
        }
        return false;
    }

    public void pause(){
        isPauses = true;
        if(shoot != null && shoot.getStatus() == Animation.Status.RUNNING)
            shoot.stop();
        for(Pea p : peas){
            p.pause();
        }
    }

    public void resume(){
        isPauses = false;
        GameManager.getCells()[row][col].removePlant();
        setImage("/Plants/Peashooter/Peashooter_" , 13);
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
//        this.getChildren().addAll(plantView);
        for(Pea p : peas){
            p.resume();
            GameManager.getPanePeas().getChildren().addAll(p.getPeaView());
        }
        GameManager.addPlant(this);
    }

    @Override
    public void update() {
        if(HP <= 0){
            dead();
        }
        changeImage(plantImage);
    }
}
