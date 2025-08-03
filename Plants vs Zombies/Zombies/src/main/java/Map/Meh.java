package Map;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;

public class Meh extends StackPane {
    private int row, col;
    public Rectangle border;
    public static double cell_size = Sizes.CELL_SIZE;
    private final ImageView fogLayer1, fogLayer2,fogLayer3;

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

        fogLayer1 = createFogLayer("/Screen/meh/Fog1.png");
        fogLayer2 = createFogLayer("/Screen/meh/Fog2.png");
        fogLayer3 = createFogLayer("/Screen/meh/Fog3.png");
        this.getChildren().addAll(fogLayer1, fogLayer2, fogLayer3);
    }
    private ImageView createFogLayer(String resourcePath) {
        double imgSize = cell_size * 1.5;
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(resourcePath)));
        imageView.setFitWidth(imgSize);
        imageView.setFitHeight(imgSize);
        return imageView;
    }

//    private void setCellView() {
//        double imgSize = cell_size * 1.5;
//
//        ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/Screen/meh/Fog1.png")));
//        imageView1.setFitWidth(imgSize);
//        imageView1.setFitHeight(imgSize);
//
//        ImageView imageView2 = new ImageView(new Image(getClass().getResourceAsStream("/Screen/meh/Fog2.png")));
//        imageView2.setFitWidth(imgSize);
//        imageView2.setFitHeight(imgSize);
//
//        ImageView imageView3 = new ImageView(new Image(getClass().getResourceAsStream("/Screen/meh/Fog3.png")));
//        imageView3.setFitWidth(imgSize);
//        imageView3.setFitHeight(imgSize);
//
//        this.getChildren().addAll(imageView1, imageView2, imageView3);
//    }


    public void fadeOutFog() {
        ParallelTransition pt = new ParallelTransition();
        for (ImageView fogLayer : List.of(fogLayer1, fogLayer2, fogLayer3)) {
            if (!fogLayer.isVisible()) continue;
            FadeTransition ft = new FadeTransition(Duration.seconds(1), fogLayer);
            ft.setFromValue(fogLayer.getOpacity());
            ft.setToValue(0.0);
            ft.setOnFinished(e -> {
                fogLayer.setVisible(false);
                fogLayer.setOpacity(1.0);
            });
            pt.getChildren().add(ft);
        }
        pt.play();
    }

    public void fadeInFog() {
        ParallelTransition pt = new ParallelTransition();
        for (ImageView fogLayer : List.of(fogLayer1, fogLayer2, fogLayer3)) {
            fogLayer.setVisible(true);
            fogLayer.setOpacity(0.0);
            FadeTransition ft = new FadeTransition(Duration.seconds(1), fogLayer);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            pt.getChildren().add(ft);
        }
        pt.play();
    }

}