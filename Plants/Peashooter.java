package Plants;

import Map.GameManager;
import Zombies.Zombie;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

import static Map.Cell.cell_size;

public class Peashooter extends Plant{
    private ArrayList<Pea> peas = new ArrayList<>();
    private Timeline shoot;
    protected int peaInSecond;

    public Peashooter(int row, int col) {
        super(row, col);
        setImage();
        HP = 4;
        peaInSecond = 1;
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
        this.getChildren().addAll(plantView );
        animPeashooter();
    }

    protected void setImage(){
        plantImage = new Image[12];
        for (int i = 0; i < 12; i++) {
            plantImage[i] = new Image(getClass().getResourceAsStream("/Plants/Peashooter/Peashooter_" + i + ".png"));
        }
        plantView = new ImageView(plantImage[0]);
    }

    @Override
    public void dead() {
        isAlive = false;
        if (this.plantView.getParent() instanceof Pane) {
            ((Pane) this.plantView.getParent()).getChildren().remove(plantView);// remove image
        }
        plantView.setOnMouseClicked(null);//don't click again
    }

    protected void animPeashooter(){
        if(isAlive)
            shooting();
            Timeline tl = new Timeline();
            tl.setCycleCount(Timeline.INDEFINITE);
            final int[] frameIndex = new int[1];

            tl.getKeyFrames().add(
                    new KeyFrame(Duration.millis(100), e -> {
                        plantView.setImage(plantImage[frameIndex[0]]);
                        if(HP <= 0){
                            dead();
                        }
                        frameIndex[0] = (frameIndex[0] + 1) % plantImage.length;
                    })
            );
            tl.play();
    }

    protected void shooting(){
        if(isAlive)
            shoot = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
                if(if_Zombie_exist()) {
                        Timeline tl =  new Timeline(new KeyFrame(Duration.millis(200) , event1 -> {
                            peas.add(new Pea(this));
                            GameManager.getGamePane().getChildren().add(peas.get(peas.size() - 1).getPeaView());
                        }));
                        tl.setCycleCount(peaInSecond);
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

    public ArrayList<Pea> getPeas() {
        return peas;
    }
}
