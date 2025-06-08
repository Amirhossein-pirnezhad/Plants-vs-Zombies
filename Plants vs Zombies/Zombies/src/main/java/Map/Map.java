package Map;

import javafx.scene.layout.GridPane;

public class Map {
    private GridPane gridPane;
    private int map_row , map_col;
    private Cell[][] cells;
    private int startX = 404 ,startY = 107;

    public Map(){
        gridPane = new GridPane();
        map_row = 12;
        map_col = 5;
        cells = new Cell[map_row][map_col];
        gridPane.setTranslateX(startX);
        gridPane.setTranslateY(startY);
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
    public void update(){
        for (int i = 0; i < map_row; i++) {
            for (int j = 0; j < map_col; j++) {
                cells[i][j].setOnMouseClicked(event -> {

                });
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
