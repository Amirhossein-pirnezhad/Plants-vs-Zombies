package Zombies;

import Map.GameManager;
import Map.Sizes;
import Plants.Plant;

import static Map.Cell.cell_size;

public class ImpZombie extends Zombie{
    public ImpZombie(int col) {
        super(col);
        HP = 3;
        speed = cell_size / 2;
        zombieImages = setZombieImages("/Zombies/FlagZombie/FlagZombie/FlagZombie_" , 12);
        zombieAttack = setZombieImages("/Zombies/FlagZombie/FlagZombieAttack/FlagZombieAttack_" , 11);
    }
    @Override
    protected Plant if_touch_plant(){
        for (Plant p : GameManager.getPlants()){
            if(this.col == p.getCol()){
                if(Math.abs((p.getRow() * cell_size + Sizes.START_X_GRID) - this.zombieView.getLayoutX()) < 10){
                    return p;
                }
            }
        }
        return null;
    }
}
