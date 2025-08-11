package Plants;

public class SnowPea extends Peashooter{

    public SnowPea(int row, int col) {
        super(row, col);
        imgPath = "/Plants/SnowPea/SnowPea_";
        typePea = "PeaIce";
        setImage(imgPath , 15);
    }
}
