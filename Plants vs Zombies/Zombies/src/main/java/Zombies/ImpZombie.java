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
        distance = 10;
        zombieImages = setZombieImages("/Zombies/FlagZombie/FlagZombie/FlagZombie_" , 12);
        zombieAttack = setZombieImages("/Zombies/FlagZombie/FlagZombieAttack/FlagZombieAttack_" , 11);
    }
}
