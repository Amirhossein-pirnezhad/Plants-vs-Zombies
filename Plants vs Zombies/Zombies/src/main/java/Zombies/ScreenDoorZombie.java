package Zombies;

import static Map.Cell.cell_size;

public class ScreenDoorZombie extends Zombie{
    public ScreenDoorZombie(int col) {
        super(col);
        HP = 10;
        speed = cell_size / 4.4;
        zombieImages = setZombieImages("/Zombies/BucketheadZombie/BucketheadZombie/BucketheadZombie_" , 14);
        zombieAttack = setZombieImages("/Zombies/BucketheadZombie/BucketheadZombieAttack/BucketheadZombieAttack_" , 10);
    }
}
