package Plants;

import Map.GameManager;
import Zombies.Zombie;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;

import static Map.Cell.cell_size;

public class Peashooter extends Plant{
    protected ArrayList<Pea> peas = new ArrayList<>();
    protected transient Timeline shoot;
    protected transient Timeline animPeashooter;
    protected int peaInCircle;
    protected double secondInCircle = 1.5;
    protected boolean isPauses;


    public Peashooter(int row, int col) {
        super(row, col);
        isPauses = false;
        setImage("/Plants/Peashooter/Peashooter_" , 13);
        HP = 4;
        peaInCircle = 1;
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
        this.getChildren().addAll(plantView);
        animPeashooter();
    }

    @Override
    public void dead() {
        isAlive = false;
        System.out.println("plant dead");
        if(shoot.getStatus() == Animation.Status.RUNNING)
            shoot.stop();
        if(animPeashooter.getStatus() == Animation.Status.RUNNING)
            animPeashooter.stop();

        GameManager.removePlant(this);

        plantView.setOnMouseClicked(null);//don't click again
    }

    protected void animPeashooter(){
        if(isAlive)
            shooting();
            animPeashooter = new Timeline();
            animPeashooter.setCycleCount(Timeline.INDEFINITE);
            final int[] frameIndex = new int[1];

            animPeashooter.getKeyFrames().add(
                    new KeyFrame(Duration.millis(100), e -> {
                        plantView.setImage(plantImage[frameIndex[0]]);
                        if(HP <= 0){
                            dead();
                        }
                        frameIndex[0] = (frameIndex[0] + 1) % plantImage.length;
                    })
            );
            animPeashooter.play();
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
        if(animPeashooter != null && animPeashooter.getStatus() == Animation.Status.RUNNING)
            animPeashooter.stop();
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
        animPeashooter();
        for(Pea p : peas){
            p.resume();
            GameManager.getPanePeas().getChildren().addAll(p.getPeaView());
        }
        GameManager.getCells()[row][col].setPlant(this);
    }

    public ArrayList<Pea> getPeas() {
        return peas;
    }
}
