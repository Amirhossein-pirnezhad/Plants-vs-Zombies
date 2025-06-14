
import Plants.Peashooter;
import Plants.Repeater;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import Zombies.*;
import Map.*;


public class Main extends Application {
    private AnimationTimer gameUpdate;
    @Override
    public void start(Stage primaryStage) {
        Zombie z = new Zombie( 4);
//        ConeheadZombie z2 = new ConeheadZombie( 0);
        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/Items/Background/Background_0.jpg")));
        background.setFitHeight(Sizes.SCREEN_HEIGHT);
        background.setFitWidth(Sizes.SCREEN_WIDTH);
        System.out.println(Sizes.SCREEN_HEIGHT);
        System.out.println(Sizes.SCREEN_WIDTH);
        Pane pane = new Pane( background , z.getZombieView()  );
        z.run();
        Peashooter p = new Peashooter(0,1);
        Repeater p2 = new Repeater(0 , 2);
        GameManager g = new GameManager(pane);
        Scene scene = new Scene(GameManager.getGamePane() , 800 , 600);
        g.spawnZombie();
        g.addPlant(p);
        g.addPlant(p2);
        System.out.println(p.getPlantView().getLayoutX());
        g.addPlant(new Peashooter(7 , 4));
        g.spawnSun();

        gameUpdate = new AnimationTimer() {//game loop
            @Override
            public void handle(long now) {
                g.updateGame();
            }
        };
        gameUpdate.start();

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