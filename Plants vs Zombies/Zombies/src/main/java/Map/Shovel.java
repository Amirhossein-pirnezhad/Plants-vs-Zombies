package Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Shovel {
    private ImageView imageView;
    private boolean clicked = false;
    public Shovel(){
        imageView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/shovel/shovel.png")));
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
