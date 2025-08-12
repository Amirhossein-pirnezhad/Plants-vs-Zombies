package Plants.NightPlant;

import Map.GameManager;
import Map.Meh;
import Plants.Plant;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Planter extends Plant {
    private final List<int[]> hiddenMehPositions = new ArrayList<>();

    public Planter(int row, int col) {
        super(row, col);
        HP = 5;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Plantern/Plantern.gif")));

        Meh[][] cellsMeh = GameManager.getMehcell();
        hideFogAround(cellsMeh);

    }

    public void hideFogAround(Meh[][] cellsMeh) {
        if (cellsMeh == null) return;

        int[][] deltas = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                { 0, 0},{-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };
        for (int[] d : deltas) {
            int r = (row - 5) + d[0];
            int c = col + d[1];

        if (isValidCell(cellsMeh, r, c)) {
                Meh m = cellsMeh[r][c];
                if (m != null) {
                    hiddenMehPositions.add(new int[]{r, c});
                    m.fadeOutFog();
                }
            }
        }
    }

    public void restoreFogAround() {
        Meh[][] cellsMeh = GameManager.getMehcell();
        if (cellsMeh == null) return;

        for (int[] s : hiddenMehPositions) {
            int r = s[0];
            int c = s[1];
            if (isValidCell(cellsMeh, r, c)) {
                Meh m = cellsMeh[r][c];
                if (m != null) {
                    m.fadeInFog();
                }
            }
        }
        hiddenMehPositions.clear();
    }

    private boolean isValidCell(Meh[][] grid, int r, int c) {
        return grid != null
                && r >= 0 && r < grid.length
                && c >= 0 && c < grid[0].length;
    }

    @Override
    public void dead() {
        if (!isAlive) return;
        isAlive = false;
        restoreFogAround();
        for (Plant p : GameManager.getPlants()){
            if (p instanceof Planter && p != this){
                ((Planter) p).hideFogAround(GameManager.getMehcell());
            }
        }
        GameManager.removePlant(this);
        plantView.setOnMouseClicked(null);
    }

    @Override
    public void pause() {
        Platform.runLater(() -> {
            Image frozenImage = plantView.snapshot(null, null);
            plantView.setImage(frozenImage);
        });
    }

    @Override
    public void resume() {
        if (plantView == null)
            plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Plantern/Plantern.gif")));
        else plantView.setImage(new Image(getClass().getResourceAsStream("/Plants/Plantern/Plantern.gif")));

        Meh[][] cellsMeh = GameManager.getMehcell();
        hideFogAround(cellsMeh);

        GameManager.getCells()[row][col].removePlant();
        GameManager.getCells()[row][col].setPlant(this);
    }

    @Override
    public void update() {
        if(HP <= 0){
            dead();
        }
    }
}
