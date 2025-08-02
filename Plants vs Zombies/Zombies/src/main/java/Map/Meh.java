package Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Meh extends StackPane {
    private int row, col;
    public Rectangle border;
    public static double cell_size = Sizes.CELL_SIZE;

    public Meh(int r, int c) {
        row = r;
        col = c;
        this.setPrefSize(cell_size, cell_size);
        this.setMinSize(cell_size, cell_size);
        this.setMaxSize(cell_size, cell_size);
        this.setPickOnBounds(false);
        border = new Rectangle(cell_size, cell_size);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.TRANSPARENT);
        this.getChildren().add(border);
        setCellView();
    }

    private void setCellView() {
        double imgSize = cell_size * 1.5;

        ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/Screen/meh/Fog1.png")));
        imageView1.setFitWidth(imgSize);
        imageView1.setFitHeight(imgSize);

        ImageView imageView2 = new ImageView(new Image(getClass().getResourceAsStream("/Screen/meh/Fog2.png")));
        imageView2.setFitWidth(imgSize);
        imageView2.setFitHeight(imgSize);

        ImageView imageView3 = new ImageView(new Image(getClass().getResourceAsStream("/Screen/meh/Fog3.png")));
        imageView3.setFitWidth(imgSize);
        imageView3.setFitHeight(imgSize);

        this.getChildren().addAll(imageView1, imageView2, imageView3);
    }
}