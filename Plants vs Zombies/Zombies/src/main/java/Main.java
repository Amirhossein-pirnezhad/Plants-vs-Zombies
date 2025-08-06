
import Plants.NightPlant.HypenoShroom;
import Plants.NightPlant.PuffShroom;
import Plants.NightPlant.ScaredyShroom;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
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
    private boolean isNight;
    private boolean online;

    //initadda
    @Override
    public void start(Stage primaryStage) {
        startGame(primaryStage);
    }

    private void startGame(Stage stage){
        stage.setTitle("Plants vs Zombies");
        stage.setFullScreen(true);
        ImageView imageView= new ImageView(new Image(getClass().getResourceAsStream("/Screen/main-menu.png")));
        ImageView Adventure_0= new ImageView(new Image(getClass().getResourceAsStream("/Screen/daymode.png")));
        ImageView Adventure_2= new ImageView(new Image(getClass().getResourceAsStream("/Screen/aks2.png")));

        imageView.setFitWidth(Sizes.SCREEN_WIDTH);//set background
        imageView.setFitHeight(Sizes.SCREEN_HEIGHT);
        System.out.println(Sizes.SCREEN_WIDTH);
        System.out.println(Sizes.SCREEN_HEIGHT);


        Adventure_0.setLayoutX(Sizes.SCREEN_WIDTH / 2.05);
        Adventure_0.setLayoutY(Sizes.SCREEN_HEIGHT/7.58);
        Adventure_0.setFitWidth(484);
        Adventure_0.setFitHeight(322);

        Adventure_2.setLayoutX(Sizes.SCREEN_WIDTH / 2.05);
        Adventure_2.setLayoutY(280);
        Adventure_2.setFitWidth(380);
        Adventure_2.setFitHeight(380);
        Adventure_2.setRotate(15);

        DropShadow neonEffect = new DropShadow();
        neonEffect.setColor(Color.LIME);
        neonEffect.setRadius(30);
        neonEffect.setSpread(0.5);
        neonEffect.setOffsetX(0);
        neonEffect.setOffsetY(0);
        neonEffect.setInput(null);

        Adventure_0.setOnMouseEntered(e -> Adventure_0.setEffect(neonEffect));
        Adventure_0.setOnMouseExited(e -> Adventure_0.setEffect(null));

        Adventure_2.setOnMouseEntered(e -> Adventure_2.setEffect(neonEffect));
        Adventure_2.setOnMouseExited(e -> Adventure_2.setEffect(null));


        Pane pane = new Pane(imageView  , Adventure_0,Adventure_2);
//        pane.setOnMouseClicked(mouseEvent -> {
//            System.out.println(mouseEvent);
//        });
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        Adventure_0.setOnMouseClicked(event -> {
            online = false;

            isNight = false;
            initializeCardSelection();
            stage.close();
        });
        Adventure_2.setOnMouseClicked(event -> {
            online = false;
            isNight = true;
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
        String imagePath = isNight
                ? "/Items/Background/Background_1.jpg"
                : "/Items/Background/Background_5.jpg";
        ImageView imageView= new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        imageView.setFitWidth(Sizes.SCREEN_WIDTH);//set background
        imageView.setFitHeight(Sizes.SCREEN_HEIGHT);
        cardSelectionPane.getChildren().add(imageView);

        selectedCardsBox = new VBox(10);



        selectedCardsBox.setLayoutX(20);
        selectedCardsBox.setLayoutY(Sizes.SCREEN_HEIGHT / 6);
        selectedCardsBox.setStyle(
                "-fx-background-color: #C3B091 ;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 10;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4);"
        );


        cardSelectionPane.getChildren().addAll(selectedCardsBox);

        ImageView frame = new ImageView(new Image(getClass().getResourceAsStream("/Screen/PanelBackground.png")));
        // frame.setScaleX(1.5);
        // frame.setScaleY(1.5);


        frame.setFitWidth(460);
        frame.setFitHeight(700);
        frame.setLayoutX( (Sizes.SCREEN_WIDTH / 3) );
        frame.setLayoutY(Sizes.SCREEN_HEIGHT / 3 );

        double originalWidth = frame.getImage().getWidth();
        double originalHeight = frame.getImage().getHeight();
        double scaledWidth = originalWidth * frame.getScaleX();
        double scaledHeight = originalHeight * frame.getScaleY();
        button.setPrefWidth(190);
        button.setPrefHeight(30);
        loading.setPrefWidth(190);
        loading.setPrefHeight(30);
        Font adventureFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Wonderland_3.ttf"), 30);
        button.setStyle(
                "-fx-background-color: #8B4513;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 10;" +
                        "-fx-text-fill: white;"+
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4);"
        );
        loading.setStyle(
                "-fx-background-color: #8B4513;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 10;" +
                        "-fx-text-fill: white;"+
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4);"
        );
        button.setFont(adventureFont);
        loading.setFont(adventureFont);
        frame.setLayoutX((Sizes.SCREEN_WIDTH / 2) - (scaledWidth / 2));
        frame.setLayoutY((Sizes.SCREEN_HEIGHT / 2) - (scaledHeight / 1.7));
        button.setLayoutX(frame.getLayoutX() + (frame.getBoundsInParent().getWidth() / 2) - (button.getPrefWidth()));
        button.setLayoutY(frame.getLayoutY() + frame.getBoundsInParent().getHeight() - 75);
        loading.setLayoutX(frame.getLayoutX() + (frame.getBoundsInParent().getWidth() / 2) + 3);
        loading.setLayoutY(frame.getLayoutY() + frame.getBoundsInParent().getHeight() - 75);

        cardSelectionPane.getChildren().addAll(frame , button ,loading);

        double tempx = (Sizes.SCREEN_WIDTH / 2) - (scaledWidth / 2) + 15;
        double tempy = (Sizes.SCREEN_HEIGHT / 2) - (scaledHeight / 1.7) + 40;

        double[][] positions = {
                {tempx, tempy      }, {tempx + 150,  tempy      }, {tempx + 300, tempy      },
                {tempx, tempy + 100}, {tempx + 150 , tempy + 100}, {tempx + 300, tempy + 100},
                {tempx, tempy + 200}, {tempx + 150 , tempy + 200}, {tempx + 300, tempy + 200},
                {tempx, tempy + 300}, {tempx + 150 , tempy + 300}, {tempx + 300, tempy + 300},
                {tempx, tempy + 400}, {tempx + 150 , tempy + 400}, {tempx + 300, tempy + 400},
                {tempx, tempy + 500}, {tempx + 150 , tempy + 500}

        };
        int k = -1;
        for (CardsType c : CardsType.values()) {
            Cart card = new Cart(c);

            ImageView cardView = card.getCardImageView();
            cardView.setFitWidth(128);
            cardView.setFitHeight(85.3);
            cardView.setLayoutX(positions[++k][0]);
            cardView.setLayoutY(positions[k][1]);


            // cardView.setOnMouseClicked(e -> {
            //     ImageView selectedView = card.getCardImageView();
            //     if (selectedCards.size() < 6  && !card.isAdded()) {
            //         selectedCards.add(card);
            //         card.setAdded(true);
            //         System.out.println("price cart " + card.getPrice());



            //         selectedView.setFitWidth(142);
            //         selectedView.setFitHeight(95);
            //         selectedCardsBox.getChildren().add(selectedView);
            //     }else if (card.isAdded()) {
            //         selectedCards.remove(card);
            //         card.setAdded(false);
            //         selectedCardsBox.getChildren().remove(selectedView);
            //         System.out.println("removed card: ");
            //     }
            // });

            cardView.setOnMouseClicked(e -> {
                if (selectedCards.size() < 6 && !card.isAdded()) {
                    selectedCards.add(card);
                    card.setAdded(true);
                    System.out.println("price cart " + card.getPrice());

                    ImageView selectedView = new ImageView(card.getCardImageView().getImage());
                    selectedView.setFitWidth(142);
                    selectedView.setFitHeight(95);

                    selectedView.setOnMouseClicked(ev -> {
                        selectedCards.remove(card);
                        card.setAdded(false);
                        selectedCardsBox.getChildren().remove(selectedView);
                        System.out.println("removed card: ");
                    });

                    selectedCardsBox.getChildren().add(selectedView);
                } else if (card.isAdded()) {
                    selectedCards.remove(card);
                    card.setAdded(false);

                    selectedCardsBox.getChildren().removeIf(node ->
                            node instanceof ImageView && ((ImageView) node).getImage().equals(card.getCardImageView().getImage())
                    );

                    System.out.println("removed card: ");
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
        String imagePath = isNight
                ? "/Items/Background/Background_6.jpg"
                : "/Items/Background/Background_0.jpg";

        ImageView background = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        background.setFitHeight(Sizes.SCREEN_HEIGHT);
        background.setFitWidth(Sizes.SCREEN_WIDTH);
        ImageView sunCounter = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Sun/sunCounter.png")));
        sunCounter.setFitHeight(90);
        sunCounter.setFitWidth(250);


        Pane pane = new Pane( background);
        pane.getChildren().add(sunCounter);
        GameManager g = new GameManager(pane , saveLoad , isNight , online);

        Label sunLabel = new Label("SunPoints: 0");
        sunLabel.setFont(new Font("Arial", 60));
        sunLabel.setTextFill(Color.BLACK);
        sunLabel.setLayoutX(110);
        sunLabel.setLayoutY(7);
        pane.getChildren().add(sunLabel);
        GameManager.addPlant(new PuffShroom(0 , 0));
        GameManager.addPlant(new ScaredyShroom(1 ,1));
        GameManager.addPlant(new HypenoShroom(3 , 2));

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