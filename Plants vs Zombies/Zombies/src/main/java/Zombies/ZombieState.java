package Zombies;

import java.io.Serializable;
import static Zombies.Type.*;

public class ZombieState implements Serializable {
    private int HP;
    private boolean isAlive;
    private int col;
    private double speed;
    private boolean isSpeedHalf;
    private double x;
    private ZombieType zombieType;
    private Type type;
    private String imgPath , imgPathAttack  , imgPathDead  , imgPathBoomDie;
    private int imgLen , imgAttackLen, imgDieLen;
    private boolean targetPlant = false;

    public ZombieState(Zombie z){
        HP = z.getHP();
        isAlive = z.isAlive();
        col = z.getCol();
        speed = z.getSpeed();
        isSpeedHalf = z.isSpeedHalf();
        zombieType = z.getMode();
        imgPath = z.getImgPath();
        imgPathAttack = z.getImgPathAttack();
        imgPathDead = z.getImgPathDead();
        imgPathBoomDie = z.getImgPathBoomDie();
        imgLen = z.getImgLen();
        imgAttackLen = z.getImgAttackLen();
        imgDieLen = z.getImgDieLen();

        x = z.getX();
        if(z.getTargetPlant() != null) targetPlant = true;
        if(z.getClass() == Zombie.class) type = NORMAL;
        else if (z.getClass() == ConeheadZombie.class) type = CONEHEAD;
        else if (z.getClass() == ScreenDoorZombie.class) type = SCREENDOOR;
        else type = IMP;
    }

    public int getHP() {
        return HP;
    }

    public int getCol() {
        return col;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isSpeedHalf() {
        return isSpeedHalf;
    }

    public ZombieType getZombieType() {
        return zombieType;
    }

    public Type getType() {
        return type;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getImgPathAttack() {
        return imgPathAttack;
    }

    public String getImgPathDead() {
        return imgPathDead;
    }

    public String getImgPathBoomDie() {
        return imgPathBoomDie;
    }

    public boolean isTargetPlant() {
        return targetPlant;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public double getX() {
        return x;
    }

    public int getImgLen() {
        return imgLen;
    }

    public int getImgAttackLen() {
        return imgAttackLen;
    }

    public int getImgDieLen() {
        return imgDieLen;
    }
}
