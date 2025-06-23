package Plants;

public class Repeater extends Peashooter{
    public Repeater(int row, int col) {
        super(row, col);
        setImage("/Plants/RepeaterPea/RepeaterPea_" , 15);
        this.plantView.setOnMouseClicked(event -> {
            if(!isPauses)
                pause();
            else resume();
        });
        peaInCircle = 2;
    }
}
