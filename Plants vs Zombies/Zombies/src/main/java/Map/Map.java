package Map;

import javafx.scene.layout.GridPane;

public class Map {
    private GridPane gridPane;
    private int map_row , map_col;
    private Cell[][] cells;
    public static int startXPane = 404 ,startYPane = 107;

    public Map(){
        gridPane = new GridPane();
        map_row = 12;
        map_col = 5;
        cells = new Cell[map_row][map_col];
        gridPane.setTranslateX(startXPane);
        gridPane.setTranslateY(startYPane);
        gridPane.setGridLinesVisible(true);
    }
    public void buildMap(){
        for (int i = 0; i < map_row; i++) {
            for (int j = 0; j < map_col; j++) {
                cells[i][j] = new Cell(i , j);
                gridPane.add(cells[i][j] , i , j);
            }
        }
    }
//    public void update(){
//        for (int i = 0; i < map_row; i++) {
//            for (int j = 0; j < map_col; j++) {
//                cells[i][j].setOnMouseClicked(event -> {
//
//                });
//            }
//        }
//    }

    public Cell[][] getCells() {
        return cells;
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
