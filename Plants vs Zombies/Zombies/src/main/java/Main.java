
import Plants.NightPlant.PuffShroom;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import Map.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
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
        Pane pane = new Pane(imageView , button);
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
        Button loading = new Button("load");

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
        cardSelectionPane.getChildren().addAll(frame , button ,loading);

        double[][] positions = {
                {50, 50}, {150, 50}, {250, 50}, {350, 50}, {450, 50},
                {50, 200}, {150, 200}, {250, 200}, {350, 200}, {450, 200}
        };
        int k = -1;
        for (CardsType c : CardsType.values()) {
            Cart card = new Cart(c);

            ImageView cardView = card.getCardImageView();
            cardView.setFitWidth(80);
            cardView.setFitHeight(100);
            cardView.setLayoutX(positions[++k][0]);
            cardView.setLayoutY(positions[k][1]);


            cardView.setOnMouseClicked(e -> {
                if (selectedCards.size() < 6 && !selectedCards.contains(card) && !card.isAdded()) {
                    selectedCards.add(card);
                    card.setAdded(true);
                    System.out.println("price cart " + card.getPrice());

                    ImageView selectedView = card.getCardImageView();
                    selectedView.setFitWidth(80);
                    selectedView.setFitHeight(100);
                    selectedCardsBox.getChildren().add(selectedView);
                }
            });

            cardSelectionPane.getChildren().add(cardView);
        }
        button.setOnAction(event -> {
            if(selectedCards.size() == 6) {
                Game(selectedCards , new SaveLoad(selectedCards));
                stage.close();
            }

        });
        loading.setOnAction(event -> {
            createLoadMenu();
        });
    }

    private void Game(List<Cart> selectedCards , SaveLoad saveLoad){
        Stage primaryStage = new Stage();

        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/Items/Background/Background_0.jpg")));
        background.setFitHeight(Sizes.SCREEN_HEIGHT);
        background.setFitWidth(Sizes.SCREEN_WIDTH);
        ImageView sunCounter = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Sun/sunCounter.png")));
        sunCounter.setFitHeight(90);
        sunCounter.setFitWidth(250);

        Pane pane = new Pane( background );
        pane.getChildren().add(sunCounter);
        GameManager g = new GameManager(pane , saveLoad);

        Label sunLabel = new Label("SunPoints: 0");
        sunLabel.setFont(new Font("Arial", 60));
        sunLabel.setTextFill(Color.BLACK);
        sunLabel.setLayoutX(110);
        sunLabel.setLayoutY(7);
        pane.getChildren().add(sunLabel);
        GameManager.addPlant(new PuffShroom(2 , 3));


        pane.getChildren().addAll(
            GameManager.getPanePeas() ,
                GameManager.getPanePlantVsZombie()
        );
        pane.setOnMouseClicked(event -> {
            System.out.println("Pane clicked at X = " + event.getX() + ", Y = " + event.getY());
        });

        Scene scene = new Scene(pane);

        GameManager.setSunPointLabel(sunLabel);
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

    private List<String> getSaveFiles() {
        List<String> saveFiles = new ArrayList<>();
        File dir = new File("saves");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    saveFiles.add(file.getName().replace(".txt", ""));
                }
            }
        }
        return saveFiles;
    }

    private void createLoadMenu() {
        Stage loadStage = new Stage();
        loadStage.setTitle("Load Game");
        loadStage.setWidth(400);
        loadStage.setHeight(500);

        VBox loadMenu = new VBox(10);
        loadMenu.setPadding(new Insets(20));
        loadMenu.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Select a save file to load:");
        title.setFont(Font.font(20));

        ListView<String> saveList = new ListView<>();
        saveList.getItems().addAll(getSaveFiles());

        Button loadButton = new Button("Load");
        loadButton.setDisable(true);

        saveList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadButton.setDisable(newVal == null);
        });

        loadButton.setOnAction(e -> {
            String selectedSave = saveList.getSelectionModel().getSelectedItem();
            if (selectedSave != null) {
                loadGame(selectedSave);
                loadStage.close();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> loadStage.close());

        HBox buttonBox = new HBox(10, loadButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        loadMenu.getChildren().addAll(title, saveList, buttonBox);

        Scene scene = new Scene(loadMenu);
        loadStage.setScene(scene);
        loadStage.show();
    }

    private void loadGame(String saveName) {
        SaveLoad saveLoad;
        File file = new File("saves", saveName + ".txt");

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            saveLoad = (SaveLoad) objectIn.readObject();
            System.out.println("Loading game: " + saveName);
            Game(saveLoad.getSelectedCards(), saveLoad);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load game");
            alert.setContentText("Could not load the selected save file.");
            alert.showAndWait();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}