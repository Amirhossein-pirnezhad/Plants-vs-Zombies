package Zombies;

import Map.GameManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import static Map.Cell.cell_size;

public class ImpZombie extends Zombie{
    public ImpZombie(int col) {
        super(col);
        HP = 3;
        speed = cell_size / 2;
        distance = 10;
        imgPath = "/Zombies/Imp/ZombieWalk/";
        imgPathAttack = "/Zombies/Imp/ZombieAttack/";
        imgLen = 33;
        imgAttackLen = 26;
        zombieImages = setZombieImages(imgPath , imgLen);
        zombieAttack = setZombieImages(imgPathAttack , imgAttackLen);
        imgFreezedZombie = "/Zombies/FreezedZombie/freezConeheadZombie.png"; //should be change this image

    }

    @Override
    protected void deadZombie(){
        isAlive = false;
        if(runZombie != null && (runZombie.getStatus() == Animation.Status.RUNNING)){
            runZombie.stop();
        }
        if(getDamage != null && (getDamage.getStatus() == Animation.Status.RUNNING)){
            getDamage.stop();
        }
        zombieDei = setZombieImages("/Zombies/Imp/ZombieDie/" , 22);

        final int[] frame = new int[]{0};

        deadZombie = new Timeline(new KeyFrame(Duration.millis(150) , event -> {
            zombieView.setImage(zombieDei[frame[0]]);
            frame[0] = (frame[0] + 1) % zombieDei.length;
        }));

        deadZombie.setCycleCount(zombieDei.length);
        deadZombie.play();

        Timeline dead = new Timeline(new KeyFrame(Duration.millis(200 * zombieDei.length) , event -> {
            GameManager.getZombies().remove(this);
            GameManager.getPanePlantVsZombie().getChildren().removeAll(zombieView);
        }));
        dead.setCycleCount(1);
        dead.play();
    }
}
