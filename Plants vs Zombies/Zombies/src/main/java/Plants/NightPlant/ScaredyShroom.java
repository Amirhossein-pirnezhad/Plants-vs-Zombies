package Plants.NightPlant;

import Map.GameManager;
import Map.Sizes;
import Zombies.Zombie;

public class ScaredyShroom extends PuffShroom{

    public ScaredyShroom(int row, int col) {
        super(row, col);
        sleepPath = "/Plants/ScaredyShroom/ScaredyShroomCry/ScaredyShroomCry_";
        imgLenSleep = 10;
        awakePath = "/Plants/ScaredyShroom/ScaredyShroom/ScaredyShroom_";
        imgLenAwake = 16;
        setSleepImage();
    }
    @Override
    protected boolean if_Zombie_exist() {
        for (Zombie z : GameManager.getZombies()) {
            if (z.getCol() == col && ((z.getZombieView().getLayoutX() - (row * Sizes.CELL_SIZE + Sizes.START_X_GRID)) < 2 * Sizes.CELL_SIZE)) {
                return false;
            }
        }
        for (Zombie z : GameManager.getZombies()) {
            if (z.getCol() == col && ((z.getZombieView().getLayoutX() - (row * Sizes.CELL_SIZE + Sizes.START_X_GRID)) >= 2 * Sizes.CELL_SIZE)) {
                return true;
            }
        }
        return false;
    }
}
