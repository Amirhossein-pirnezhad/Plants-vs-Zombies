
import Plants.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import Zombies.*;
import Map.*;


public class Main extends Application {
    private AnimationTimer gameUpdate;
    @Override
    public void start(Stage primaryStage) {
        ConeheadZombie z = new ConeheadZombie( 4);
//        ConeheadZombie z2 = new ConeheadZombie( 0);
        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/Items/Background/Background_0.jpg")));
        background.setFitHeight(Sizes.SCREEN_HEIGHT);
        background.setFitWidth(Sizes.SCREEN_WIDTH);
        ImageView sunCounter = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Sun/sunCounter.png")));
        sunCounter.setFitHeight(90);
        sunCounter.setFitWidth(250);

        System.out.println(Sizes.SCREEN_HEIGHT);
        System.out.println(Sizes.SCREEN_WIDTH);
        Pane pane = new Pane( background , z.getZombieView()  );
        pane.getChildren().add(sunCounter);

        Label sunLabel = new Label("SunPoints: 0");
        sunLabel.setFont(new Font("Arial", 60));
        sunLabel.setTextFill(Color.BLACK);
        sunLabel.setLayoutX(110);
        sunLabel.setLayoutY(7);
        pane.getChildren().add(sunLabel);

        z.run();
        Peashooter p = new Peashooter(0,1);
        Repeater p2 = new Repeater(0 , 2);
        GameManager g = new GameManager(pane);

        pane.getChildren().addAll(
            GameManager.getPanePeas() ,
                GameManager.getPanePlantVsZombie()
        );

        GameManager.setSunPointLabel(sunLabel);

        Scene scene = new Scene(GameManager.getBackground() , 800 , 600);
        g.spawnZombie();
        g.addPlant(p);
        g.addPlant(p2);
        System.out.println(p.getPlantView().getLayoutX());
        g.addZombie(new ImpZombie(3));
        g.addPlant(new Peashooter(7 , 4));
        g.addPlant(new Peashooter(6,3));
        g.addPlant(new SnowPea(1 , 4));
        g.addPlant(new WallNut(4 , 4));
        g.addPlant(new WallNut(7 , 0));
        g.addPlant(new SunFlower(0 , 0));
        g.addPlant(new SunFlower(4 , 1));
        g.addPlant(new CherryBomb(7 , 2));
        g.addPlant(new Jalapeno(1,0));
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