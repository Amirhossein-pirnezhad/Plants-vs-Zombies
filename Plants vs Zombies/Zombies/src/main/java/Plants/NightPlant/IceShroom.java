package Plants.NightPlant;

import Map.GameManager;
import Plants.CherryBomb;
import Zombies.Zombie;

public class IceShroom extends CherryBomb {
    public IceShroom(int row, int col) {
        super(row, col);
    }
    @Override
    protected boolean isKilled(Zombie z){
        return false;
    }
    @Override
    protected void killZombie(){
        for (Zombie z : GameManager.getZombies()){
            z.ice();
        }
    }
}
