package Plants.NightPlant;

import Map.Sizes;
import Plants.CherryBomb;
import Zombies.Zombie;

public class DoomShroom extends CherryBomb {
    public DoomShroom(int row, int col) {
        super(row, col);
    }
    @Override
    protected boolean isKilled(Zombie z){
        return true;
    }
}
