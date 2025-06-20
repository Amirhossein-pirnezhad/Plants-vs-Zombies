package Plants;

import Map.GameManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import static Map.Cell.cell_size;


public class WallNut extends Plant{
    protected final int initialHp = 15;
    protected Timeline hpGood;
    protected int frame = 0;
    protected Image[] wallCracked1;
    protected Image[] wallCracked2;

    public WallNut(int row, int col) {
        super(row, col);
        setImage("/Plants/WallNut/WallNut/WallNut_" , 15);
        HP = 12;
        this.getChildren().addAll(plantView);
        wallCracked1 = setImageCracked("/Plants/WallNut/WallNut_cracked1/WallNut_cracked1_" , 10);
        wallCracked2 = setImageCracked("/Plants/WallNut/WallNut_cracked2/WallNut_cracked2_" , 14);
        animWallNut();
    }

    protected Image[] setImageCracked(String path , int len){
        Image[] images = new Image[len];
        for (int i = 0; i < len; i++) {
            images[i] = new Image(getClass().getResourceAsStream(path + i + ".png"));
        }
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
        return images;
    }

    protected void animWallNut(){
        hpGood = new Timeline(new KeyFrame(Duration.millis(200) , event -> {
            if(HP <= 0){
                dead();
            }
            else if(HP > 9){
                changeImage(plantImage);
            }
            else if(HP > 5){
                changeImage(wallCracked1);
            }
            else{
                changeImage(wallCracked2);
            }
        }));
        hpGood.setCycleCount(Animation.INDEFINITE);
        hpGood.play();
    }

    protected void changeImage(Image [] images){
        if(frame >= images.length) frame = 0;
        plantView.setImage(images[frame]);
        frame = (frame + 1) % images.length;
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
