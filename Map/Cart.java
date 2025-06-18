package Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Cart extends StackPane {
    private final String cardName;
    private final ImageView imageView;

    public Cart(String cardName, Image image) {
        this.cardName = cardName;
        this.imageView = new ImageView(image);
        imageView.setFitWidth(80);
        imageView.setFitHeight(100);
        getChildren().add(imageView);
    }

    public String getCardName() {
        return cardName;
    }

    public Image getCardImage() {
        return imageView.getImage();
    }
}
