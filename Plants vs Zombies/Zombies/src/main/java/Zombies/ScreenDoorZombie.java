package Zombies;

import Map.Sizes;
import javafx.scene.image.ImageView;

import static Map.Cell.cell_size;

public class ScreenDoorZombie extends Zombie{
    public ScreenDoorZombie(int col) {
        super(col);
        HP = 10;
        speed = cell_size / 4.4;
        imgPath = "/Zombies/BucketheadZombie/BucketheadZombie/BucketheadZombie_";
        imgPathAttack = "/Zombies/BucketheadZombie/BucketheadZombieAttack/BucketheadZombieAttack_";
        imgLen = 14;
        imgAttackLen = 10;
        zombieImages = setZombieImages( imgPath, imgLen);
        zombieAttack = setZombieImages(imgPathAttack , imgAttackLen);
        imgFreezedZombie = "/Zombies/FreezedZombie/freezBucketheadZombie.png";

    }

//    @Override
//    public void resume(){
//        zombieView = new ImageView();
//        if(!isSpeedHalf)
//            zombieImages = setZombieImages("/Zombies/BucketheadZombie/BucketheadZombie/BucketheadZombie_" , 14);
//        else zombieImages = setZombieImages("/Zombies/ConeheadZombie/ConeheadZombieIce/ConeheadZombie_" , 21);
//        zombieAttack = setZombieImages("/Zombies/BucketheadZombie/BucketheadZombieAttack/BucketheadZombieAttack_" , 10);
//        zombieView.setLayoutX(x + Sizes.CELL_SIZE);
//        zombieView.setLayoutY(col * cell_size + 30);
//        run();
//    }
}
