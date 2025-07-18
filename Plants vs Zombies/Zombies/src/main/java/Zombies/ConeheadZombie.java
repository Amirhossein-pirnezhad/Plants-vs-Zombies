package Zombies;

import Map.Sizes;
import javafx.scene.image.ImageView;

import static Map.Cell.cell_size;

public class ConeheadZombie extends Zombie{

    public ConeheadZombie(int col) {
        super(col);
        HP = 7;
        imgPath = "/Zombies/ConeheadZombie/ConeheadZombie/ConeheadZombie_";
        imgPathAttack = "/Zombies/ConeheadZombie/ConeheadZombieAttack/ConeheadZombieAttack_";
        imgLen = 21;
        imgAttackLen = 10;
        zombieImages = setZombieImages(imgPath , imgLen);
        zombieAttack = setZombieImages(imgPathAttack , imgAttackLen);
    }
    @Override
    public void speedIsHalf(){
        isSpeedHalf = true;
        speed = speed/2;
        zombieImages = setZombieImages("/Zombies/ConeheadZombie/ConeheadZombieIce/ConeheadZombie_" , 21);
    }

//    @Override
//    public void resume(){
//        zombieView = new ImageView();
//        if(!isSpeedHalf)
//            zombieImages = setZombieImages("/Zombies/ConeheadZombie/ConeheadZombie/ConeheadZombie_" , 21);
//        else zombieImages = setZombieImages("/Zombies/ConeheadZombie/ConeheadZombieIce/ConeheadZombie_" , 21);
//        zombieAttack = setZombieImages("/Zombies/ConeheadZombie/ConeheadZombieAttack/ConeheadZombieAttack_" , 10);
//        zombieView.setLayoutX(x + Sizes.CELL_SIZE);
//        zombieView.setLayoutY(col * cell_size + 30);
//        run();
//    }

}
