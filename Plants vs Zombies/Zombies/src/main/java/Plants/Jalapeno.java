package Plants;

import Map.Cell;
import Map.GameManager;
import Zombies.Zombie;

public class Jalapeno extends CherryBomb{
    public Jalapeno(int row, int col) {
        super(row, col);
        setImage("/Plants/Jalapeno/Jalapeno/Jalapeno_" , 8);
    }

    @Override
    protected void killZombie() {
        for (Zombie z : GameManager.getZombies())
            if (z.getCol() == col) z.bomDie();
    }

    @Override
    public void resume(){
        GameManager.getCells()[row][col].removePlant();
        setImage("/Plants/Jalapeno/Jalapeno/Jalapeno_" , 8);
        bomb(frame);
        GameManager.getCells()[row][col].setPlant(this);
    }

//    @Override
//    protected void setAnimDie(){
//        GameManager.getBackground().getChildren().add(plantView);
//        plantView.setLayoutX(Sizes.START_X_GRID + row * Sizes.CELL_SIZE);
//        plantView.setLayoutY(Sizes.START_Y_GRID + col * Sizes.CELL_SIZE);
//        plantView.setFitWidth(GameManager.getMap_row() * Sizes.CELL_SIZE);
//        setImage("/Plants/Jalapeno/JalapenoExplode/JalapenoExplode_" , 8);
//    }

//    @Override
//    public void dead() {
//        dead = new Timeline(new KeyFrame(Duration.millis(500) , event -> {
//            if(timerBomb.getStatus() != Animation.Status.RUNNING) {
//                setImage("/Plants/Jalapeno/JalapenoExplode/JalapenoExplode_" , 8);
//                Timeline tl = new Timeline(new KeyFrame(Duration.millis(200) , event1 -> {
//
//                }));
//                tl.setCycleCount(plantImage.length);
//                tl.play();
//
//                killZombie();
//                isAlive = false;
//                if (this.plantView.getParent() instanceof Pane) {
//                    ((Pane) this.plantView.getParent()).getChildren().removeAll(plantView , this);// remove image
//                    GameManager.getPlants().remove(this);
//                }
//                dead.stop();
//            }
//        }));
//        dead.setCycleCount(Animation.INDEFINITE);
//        dead.play();
//    }
}
