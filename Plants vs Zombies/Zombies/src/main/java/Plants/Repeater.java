package Plants;

public class Repeater extends Peashooter{
    public Repeater(int row, int col) {
        super(row, col);
        imgPath = "/Plants/RepeaterPea/RepeaterPea_";
        setImage(imgPath , 15);
        peaInCircle = 2;
    }

}
