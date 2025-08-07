package Plants;

import Map.GameManager;

import static Map.Cell.cell_size;
import static Map.GameManager.peas;

public class Repeater extends Peashooter{
    public Repeater(int row, int col) {
        super(row, col);
        setImage("/Plants/RepeaterPea/RepeaterPea_" , 15);
        peaInCircle = 2;
    }

    @Override
    public void resume(){
        isPauses = false;
        setImage("/Plants/RepeaterPea/RepeaterPea_" , 15);
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
