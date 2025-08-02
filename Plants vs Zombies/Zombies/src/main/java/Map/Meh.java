package Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Meh extends StackPane {
    private int row, col;
    public Rectangle border;
    private transient ImageView cellView;
    public static double cell_size = Sizes.CELL_SIZE;

    public Meh(int r, int c) {
        row = r;
        col = c;
        border = new Rectangle(cell_size, cell_size);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.TRANSPARENT);
        this.getChildren().add(border);
        setCellView();
        this.getChildren().add(cellView);
    }

    private void setCellView(){
        cellView = new ImageView(new Image(getClass().getResourceAsStream("/Screen/meh/Fog1.png")));
    }
}