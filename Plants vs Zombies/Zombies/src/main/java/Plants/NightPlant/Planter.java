package Plants.NightPlant;

import Map.GameManager;
import Map.Meh;
import Plants.Plant;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Planter extends Plant {
    protected transient Timeline time;
    private final List<int[]> hiddenMehPositions = new ArrayList<>();
    private boolean fogHidden = false;

    public Planter(int row, int col) {
        super(row, col);
        HP = 5;
        plantView = new ImageView(new Image(getClass().getResourceAsStream("/Plants/Plantern/Plantern.gif")));

        Meh[][] cellsMeh = GameManager.getMehcell();
        hideFogAround(cellsMeh);

        animPlanter();
    }

    private void hideFogAround(Meh[][] cellsMeh) {
        if (fogHidden || cellsMeh == null) return;

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
        fogHidden = true;
    }

    private void restoreFogAround() {
        Meh[][] cellsMeh = GameManager.getMehcell();
        if (!fogHidden || cellsMeh == null) return;

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
        fogHidden = false;
    }

    private boolean isValidCell(Meh[][] grid, int r, int c) {
        return grid != null
                && r >= 0 && r < grid.length
                && c >= 0 && c < grid[0].length;
    }
    protected void animPlanter(){
        time = new Timeline(new KeyFrame(Duration.millis(200) , event -> {
            if(HP <= 0){
                dead();
            }

        }));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    @Override
    public void dead() {
        if(time.getStatus() == Animation.Status.RUNNING){
            time.stop();
        }
        if (!isAlive) return;
        isAlive = false;
        restoreFogAround();
        GameManager.removePlant(this);
        plantView.setOnMouseClicked(null);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
