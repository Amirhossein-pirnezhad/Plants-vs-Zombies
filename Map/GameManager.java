package Map;

import Plants.Plant;
import Plants.Sun;
import javafx.scene.shape.Rectangle;
import Zombies.ConeheadZombie;
import Zombies.Zombie;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static Pane gamePane;
    private static List<Zombie> zombies = new ArrayList<>();
    private static List<Plant> plants = new ArrayList<>();
    private List<Sun> suns = new ArrayList<>();
    private GridPane gridPane;
    private int map_row , map_col;
    private Cell[][] cells;
    public static int sunPoint;
    private VBox plantMenuVBox;
    private static Label sunPointLabel;

    private GridPane cardSelectionGrid;
    private VBox selectedCardsBox;
    private List<Cart> selectedCards = new ArrayList<>();
    private Runnable onCardsSelectedCallback;
    private Pane cardSelectionPane;

    public void initializeCardSelection(Runnable onCardsSelected) {
        cardSelectionPane = new Pane();
        cardSelectionPane.setPrefSize(600, 400);
        cardSelectionPane.setLayoutX(100);
        cardSelectionPane.setLayoutY(100);

        ImageView frame = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Sun/Sun_2.png")));
        frame.setFitWidth(600);
        frame.setFitHeight(400);
        cardSelectionPane.getChildren().add(frame);

        double[][] positions = {
                {50, 50}, {150, 50}, {250, 50}, {350, 50}, {450, 50},
                {50, 200}, {150, 200}, {250, 200}, {350, 200}, {450, 200}
        };

        for (int i = 0; i < 10; i++) {
            String cardName = "card" + (i + 1);
            Image img = new Image(getClass().getResourceAsStream("/Plants/Sun/Sun_" + i + ".png"));
            Cart card = new Cart(cardName, img);

            ImageView cardView = new ImageView(img);
            cardView.setFitWidth(80);
            cardView.setFitHeight(100);
            cardView.setLayoutX(positions[i][0]);
            cardView.setLayoutY(positions[i][1]);

            cardView.setOnMouseClicked(e -> {
                if (selectedCards.size() < 5 && !selectedCards.contains(card)) {
                    selectedCards.add(card);

                    ImageView selectedView = new ImageView(card.getCardImage());
                    selectedView.setFitWidth(80);
                    selectedView.setFitHeight(100);
                    selectedCardsBox.getChildren().add(selectedView);

                    if (selectedCards.size() == 5) {
                        gamePane.getChildren().remove(cardSelectionPane);
                        gamePane.getChildren().remove(selectedCardsBox);
                        if (onCardsSelected != null) onCardsSelected.run();
                    }
                }
            });

            cardSelectionPane.getChildren().add(cardView);
        }

        selectedCardsBox = new VBox(10);
        selectedCardsBox.setPrefWidth(150);
        selectedCardsBox.setLayoutX(20);
        selectedCardsBox.setLayoutY(100);
        selectedCardsBox.setStyle("-fx-background-color: rgba(200,200,200,0.7); -fx-padding: 10; -fx-border-color: black;");

        gamePane.getChildren().addAll(cardSelectionPane, selectedCardsBox);
    }



    public GameManager(Pane gamePane) {
        this.gamePane = gamePane;
        this.sunPoint = 0;
        gridPane = new GridPane();
        map_row = 9;
        map_col = 5;
        cells = new Cell[map_row][map_col];
        gridPane.setTranslateX(Sizes.START_X_GRID);
        gridPane.setTranslateY(Sizes.START_Y_GRID);
        gridPane.setGridLinesVisible(true);
        buildMap();
        initializePlantMenu();

    }


    private void removeCardSelectionUI() {
        gamePane.getChildren().removeAll(cardSelectionGrid, selectedCardsBox);
    }


    private void initializePlantMenu() {
        plantMenuVBox = new VBox(8);
        plantMenuVBox.setLayoutX(0);
        plantMenuVBox.setLayoutY(90);
       // plantMenuVBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-padding: 10;");

        plantMenuVBox.setStyle(
                "-fx-background-color: #8B4513;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 10;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4);"
        );

        String[] plantImages = {
                "/Screen/sun.png",
                "/Screen/shooter.png",
                "/Screen/sun.png",
                "/Screen/shooter.png",
                "/Screen/sun.png",
                "/Screen/shooter.png",
        };

        for (String imagePath : plantImages) {
            try {
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(180);
                imageView.setPreserveRatio(true);

                Rectangle clip = new Rectangle(180, image.getHeight() * (180 / image.getWidth()));
                clip.setArcWidth(30);
                clip.setArcHeight(30);
                imageView.setClip(clip);

                plantMenuVBox.getChildren().add(imageView);
            } catch (Exception e) {
                System.err.println("error" + imagePath);
            }
        }

        gamePane.getChildren().add(plantMenuVBox);
    }

    public static void setSunPointLabel(Label label) {
        sunPointLabel = label;
        updateSunPointLabel();
    }

    public static void updateSunPointLabel() {
        if (sunPointLabel != null) {
            sunPointLabel.setText("" + sunPoint);
        }
    }   

    public void buildMap(){
        for (int i = 0; i < map_row; i++) {
            for (int j = 0; j < map_col; j++) {
                cells[i][j] = new Cell(i , j);
                gridPane.add(cells[i][j] , i , j);
            }
        }
        gamePane.getChildren().add(gridPane);
    }

    public void addZombie(Zombie z) {
        zombies.add(z);
        gamePane.getChildren().add(z.getZombieView());
        z.run();
    }

    public void addPlant(Plant p) {
        plants.add(p);
        cells[p.getRow()][p.getCol()].setCellView(p.getPlantView());
    }

    public void addSun(Sun sun , int row , int col){
        suns.add(sun);
        ImageView view = sun.getPlantView();
        gamePane.getChildren().add(view);
    }

    public void updateGame() {

    }

    public void spawnZombie(){
        Timeline spawnZombies = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            int row = (int)(Math.random() * 100) % 5;
            int type = (int)(Math.random() * 100) % 2;
            Zombie z = type == 0 ? new Zombie(row) : new ConeheadZombie(row);
            addZombie(z);
        }));
        spawnZombies.setCycleCount(Timeline.INDEFINITE);
        spawnZombies.play();
    }

    public void spawnSun(){
        Timeline tlSun = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            int row = (int)(Math.random() * 100) % 5;
            int col = (int)(Math.random() * 100) % 12;
            Sun s = new Sun(row , col);
            addSun(s , row , col);
        }));
        tlSun.setCycleCount(Timeline.INDEFINITE);
        tlSun.play();
    }

    public static void addImageView(ImageView imageView){
        gamePane.getChildren().add(imageView);
    }

    public Cell[][] getCells() {
        return cells;
    }

    public static Pane getGamePane() {
        return gamePane;
    }

    public static List<Zombie> getZombies() {
        return zombies;
    }

    public static List<Plant> getPlants() {
        return plants;
    }
}
