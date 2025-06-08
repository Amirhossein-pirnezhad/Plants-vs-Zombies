package Map;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



public class Cell extends StackPane {
    private int row , col;
    private Rectangle border;
    private ImageView cellView;
    public static int cell_size = 140;

    public Cell(int r ,int c){
        row = r;
        col = c;
        border = new Rectangle(cell_size , cell_size);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        this.getChildren().add(border);
    }

    public void setCellView(ImageView cellView) {
        this.cellView = cellView;
    }
}
