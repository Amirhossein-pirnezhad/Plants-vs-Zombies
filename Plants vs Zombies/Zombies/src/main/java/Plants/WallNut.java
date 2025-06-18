package Plants;

import Map.GameManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import static Map.Cell.cell_size;

public class WallNut extends Plant{
    protected final int initialHp = 15;
    protected Timeline hpGood;

    public WallNut(int row, int col) {
        super(row, col);
        setImage("/Plants/WallNut/WallNut/WallNut_" , 15);
        HP = 12;
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
        this.getChildren().addAll(plantView);
        animWallNut();
    }

//    protected void setImage(String path , int len){
//        plantImage = new Image[len];
//        for (int i = 0; i < len; i++) {
//            plantImage[i] = new Image(getClass().getResourceAsStream(path + i + ".png"));
//        }
//        plantView = new ImageView(plantImage[0]);
//    }

    protected void animWallNut(){
        final int[] frame = new int[1];
        final boolean[] sw = new boolean[]{true};
        hpGood = new Timeline(new KeyFrame(Duration.millis(200) , event -> {
            if(HP <= 0){
                dead();
            }
//            else if(HP < 9 && sw[0] ){
//                hpGood.stop();
//                setImage("/Plants/WallNut/WallNut_cracked1/WallNut_cracked1_" , 10);
//                frame[0] = 0;
//                sw[0] = false;
//                hpGood.play();
//            }
            plantView.setImage(plantImage[frame[0]]);
            frame[0] = (frame[0] + 1) % plantImage.length;
        }));
        hpGood.setCycleCount(Animation.INDEFINITE);
        hpGood.play();
    }

    @Override
    public void dead() {
        if(hpGood.getStatus() == Animation.Status.RUNNING){
            hpGood.stop();
        }
        isAlive = false;
        System.out.println("plant dead");
        if (this.plantView.getParent() instanceof Pane) {
            ((Pane) this.plantView.getParent()).getChildren().removeAll(plantView , this);// remove image
            GameManager.getPlants().remove(this);
        }
        plantView.setOnMouseClicked(null);//don't click again
    }
}
