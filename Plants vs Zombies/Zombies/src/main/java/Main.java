
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import Zombies.*;
import Map.*;


public class Main extends Application {
    public static double screenHeight;
    public static double screenWidth;
    @Override
    public void start(Stage primaryStage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        screenHeight = screenBounds.getHeight();
        screenWidth = screenBounds.getWidth();

        Zombie z = new Zombie(1 , 2 , 4);
        ConeheadZombie z2 = new ConeheadZombie(1 , 2 , 0);
        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/Items/Background/Background_0.jpg")));
        background.setFitHeight(screenHeight);
        background.setFitWidth(screenWidth);
        Map m = new Map();
        m.buildMap();
        Pane pane = new Pane( background , z.getZombieView() , z2.getZombieView(), m.getGridPane());
        z.run();
        z2.run();
        m.update();
        Scene scene = new Scene(pane , 800 , 600);
        scene.setOnMouseClicked(e -> {
            double x = e.getSceneX();
            double y = e.getSceneY();
            System.out.println("Clicked at: (" + x + ", " + y + ")" + e);
        });
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}