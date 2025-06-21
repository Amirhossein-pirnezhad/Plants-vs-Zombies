
import Plants.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import Zombies.*;
import Map.*;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    private AnimationTimer gameUpdate;
    private VBox selectedCardsBox;
    private List<Cart> selectedCards = new ArrayList<>();
    private Pane cardSelectionPane;
    @Override
    public void start(Stage primaryStage) {
        startGame(primaryStage);
    }

    private void startGame(Stage stage){
        stage.setTitle("Plants vs Zombies");
        stage.setFullScreen(true);
        ImageView imageView= new ImageView(new Image(getClass().getResourceAsStream("/Screen/MainMenu.png")));
        Button button = new Button("Start");
        imageView.setFitWidth(Sizes.SCREEN_WIDTH);//set background
        imageView.setFitHeight(Sizes.SCREEN_HEIGHT);
        Pane pane = new Pane(button);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        button.setOnAction(event -> {
            initializeCardSelection();
            stage.close();
        });
        stage.show();
    }

    public void initializeCardSelection() {
        Stage stage = new Stage();
        Button button = new Button("Start Game");
        cardSelectionPane = new Pane();
        Scene scene = new Scene(cardSelectionPane);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        ImageView imageView= new ImageView(new Image(getClass().getResourceAsStream("/Items/Background/background_5.jpg")));
        imageView.setFitWidth(Sizes.SCREEN_WIDTH);//set background
        imageView.setFitHeight(Sizes.SCREEN_HEIGHT);
        cardSelectionPane.getChildren().add(imageView);

        selectedCardsBox = new VBox(10);
        selectedCardsBox.setPrefWidth(150);
        selectedCardsBox.setLayoutX(Sizes.SCREEN_WIDTH / 2);
        selectedCardsBox.setLayoutY(100);
        selectedCardsBox.setStyle("-fx-background-color: rgba(200,200,200,0.7); -fx-padding: 10; -fx-border-color: black;");

        cardSelectionPane.getChildren().addAll(selectedCardsBox);

        ImageView frame = new ImageView(new Image(getClass().getResourceAsStream("/Screen/PanelBackground.png")));
        frame.setScaleX(1.5);
        frame.setScaleY(1.5);
        cardSelectionPane.getChildren().addAll(frame , button);

        double[][] positions = {
                {50, 50}, {150, 50}, {250, 50}, {350, 50}, {450, 50},
                {50, 200}, {150, 200}, {250, 200}, {350, 200}, {450, 200}
        };
        int k = -1;
        for (CardsType c : CardsType.values()) {
            Image img = new Image(getClass().getResourceAsStream("/Cards/" + c.toString() + ".png"));
            Cart card = new Cart(c, img);

            ImageView cardView = new ImageView(img);
            cardView.setFitWidth(80);
            cardView.setFitHeight(100);
            cardView.setLayoutX(positions[++k][0]);
            cardView.setLayoutY(positions[k][1]);


            cardView.setOnMouseClicked(e -> {
                if (selectedCards.size() < 7 && !selectedCards.contains(card) && !card.isAdded()) {
                    selectedCards.add(card);
                    card.setAdded(true);
                    System.out.println("price cart " + card.getPrice());

                    ImageView selectedView = card.getCardImageView();
                    selectedView.setFitWidth(80);
                    selectedView.setFitHeight(100);
                    selectedCardsBox.getChildren().add(selectedView);

//                    if (selectedCards.size() == 6) {
//                        background.getChildren().remove(cardSelectionPane);
//                        background.getChildren().remove(selectedCardsBox);
//                        if (onCardsSelected != null) onCardsSelected.run();
//                    }
                }
//                else if(card.isAdded()){
//                    card.setAdded(false);
//                    selectedCards.remove(card);
//                    selectedCardsBox.getChildren().remove(cardView);
//                    System.out.println("deleting");
//                }
            });

            cardSelectionPane.getChildren().add(cardView);
        }
        button.setOnAction(event -> {
            if(selectedCards.size() == 6) {
                Game(selectedCards);
                stage.close();
            }

        });
    }

    private void Game(List<Cart> selectedCards){
        Stage primaryStage = new Stage();
        ConeheadZombie z = new ConeheadZombie( 4);

        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/Items/Background/Background_0.jpg")));
        background.setFitHeight(Sizes.SCREEN_HEIGHT);
        background.setFitWidth(Sizes.SCREEN_WIDTH);
        ImageView sunCounter = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Sun/sunCounter.png")));
        sunCounter.setFitHeight(90);
        sunCounter.setFitWidth(250);

        Pane pane = new Pane( background , z.getZombieView()  );
        pane.getChildren().add(sunCounter);
        GameManager g = new GameManager(pane , selectedCards);

        Label sunLabel = new Label("SunPoints: 0");
        sunLabel.setFont(new Font("Arial", 60));
        sunLabel.setTextFill(Color.BLACK);
        sunLabel.setLayoutX(110);
        sunLabel.setLayoutY(7);
        pane.getChildren().add(sunLabel);



        z.run();
        Peashooter p = new Peashooter(0,1);
        Repeater p2 = new Repeater(0 , 2);

        pane.getChildren().addAll(
            GameManager.getPanePeas() ,
                GameManager.getPanePlantVsZombie()
        );

        Scene scene = new Scene(pane);

        GameManager.setSunPointLabel(sunLabel);

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
        g.addPlant(new TallNut(4 , 0));
        g.spawnSun();

        gameUpdate = new AnimationTimer() {//game loop
            @Override
            public void handle(long now) {
                g.updateGame();
            }
        };
        gameUpdate.start();
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}