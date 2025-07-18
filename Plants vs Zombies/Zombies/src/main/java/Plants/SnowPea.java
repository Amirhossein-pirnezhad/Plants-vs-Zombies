package Plants;

import Map.GameManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import static Map.Cell.cell_size;

public class SnowPea extends Peashooter{

    public SnowPea(int row, int col) {
        super(row, col);
        setImage("/Plants/SnowPea/SnowPea_" , 15);
    }

    @Override
    protected void shooting(){
        if(isAlive)
            shoot = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
                if(if_Zombie_exist()) {
                    Timeline tl =  new Timeline(new KeyFrame(Duration.millis(200) , event1 -> {
                        peas.add(new PeaIce(this));
                        GameManager.getPanePeas().getChildren().add(peas.get(peas.size() - 1).getPeaView());
                    }));
                    tl.setCycleCount(peaInCircle);
                    tl.play();
                }
            }));
        shoot.setCycleCount(Animation.INDEFINITE);
        shoot.play();
    }

    @Override
    public void resume(){
        isPauses = false;
        setImage("/Plants/SnowPea/SnowPea_" , 15);
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
        this.getChildren().addAll(plantView);
        animPeashooter();
        for(Pea p : peas){
            p.resume();
            GameManager.getPanePeas().getChildren().addAll(p.getPeaView());
        }
        GameManager.getCells()[row][col].removePlant();
        GameManager.getCells()[row][col].setPlant(this);
    }
}
