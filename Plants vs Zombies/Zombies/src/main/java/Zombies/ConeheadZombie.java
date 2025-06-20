package Zombies;

public class ConeheadZombie extends Zombie{

    public ConeheadZombie(int col) {
        super(col);
        zombieImages = setZombieImages("/Zombies/ConeheadZombie/ConeheadZombie/ConeheadZombie_" , 21);
        zombieAttack = setZombieImages("/Zombies/ConeheadZombie/ConeheadZombieAttack/ConeheadZombieAttack_" , 10);
        HP = 7;
    }
    @Override
    public void speedIsHalf(){
        isSpeedHalf = true;
        speed = speed/2;
        zombieImages = setZombieImages("/Zombies/ConeheadZombie/ConeheadZombieIce/ConeheadZombie_" , 21);
    }
}
