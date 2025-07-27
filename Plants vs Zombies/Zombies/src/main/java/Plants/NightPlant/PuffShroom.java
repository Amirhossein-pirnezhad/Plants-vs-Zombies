package Plants.NightPlant;

import Map.GameManager;
import Map.Sizes;
import Plants.Pea;
import Plants.Peashooter;
import Zombies.Zombie;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static Map.Cell.cell_size;

public class PuffShroom extends Peashooter {

    public PuffShroom(int row, int col) {
        super(row, col);
        setImage("/Plants/PuffShroom/PuffShroom/PuffShroom_" , 14);
        plantView.setTranslateY(-30);

    }
    @Override
    protected void setImage(String path , int len){
        plantImage = new Image[len];
        for (int i = 0; i < len; i++) {
            plantImage[i] = new Image(getClass().getResourceAsStream(path + i + ".png"));
        }
        plantView = new ImageView(plantImage[0]);
        plantView.setFitHeight(cell_size * 0.8);
        plantView.setFitWidth(cell_size * 0.8);
    }
    @Override
    protected boolean if_Zombie_exist(){//only 4 house after
        for (Zombie z : GameManager.getZombies()){
            if(z.getCol() == col && ((z.getZombieView().getLayoutX() - (row * Sizes.CELL_SIZE + Sizes.START_X_GRID)) < 4 * Sizes.CELL_SIZE))
                return true;
        }
        return false;
    }

    @Override
    public void resume(){
        isPauses = false;
        GameManager.getCells()[row][col].removePlant();
        setImage("Plants/PuffShroom/PuffShroom_" , 14);
        plantView.setFitHeight(cell_size * 0.75);
        plantView.setFitWidth(cell_size * 0.75);
//        this.getChildren().addAll(plantView);
        animPeashooter();
        for(Pea p : peas){
            p.resume();
            GameManager.getPanePeas().getChildren().addAll(p.getPeaView());
        }
        GameManager.addPlant(this);
    }
}
