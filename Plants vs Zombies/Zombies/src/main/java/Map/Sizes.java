package Map;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class Sizes {
    public static final double SCREEN_WIDTH , SCREEN_HEIGHT , START_X_GRID , START_Y_GRID , CELL_SIZE;

    static {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        SCREEN_HEIGHT = screenBounds.getHeight();
        SCREEN_WIDTH = screenBounds.getWidth();
        START_X_GRID = SCREEN_WIDTH/4.4;
        START_Y_GRID = SCREEN_HEIGHT/8.52;
        CELL_SIZE = (SCREEN_WIDTH - START_X_GRID)/9;
    }
}
