package Plants;

import Map.GameManager;
import Zombies.Zombie;
import javafx.scene.image.Image;

public class PeaIce extends Pea{

    public PeaIce(Peashooter peashooter) {
        super(peashooter);
        peaImage = new Image(getClass().getResourceAsStream("/Bullets/PeaIce/PeaIce_0.png"));
        peaView.setImage(peaImage);
    }

    @Override
    protected void if_touch_Zombie(){
        if(isAlive)
            for (Zombie z : GameManager.getZombies()){
                if(z.getCol() == peashooter.col){
                    if(Math.abs(peaView.getLayoutX() - z.getZombieView().getLayoutX()) < 5) {
                        System.out.println(peaView.getLayoutX());
                        z.setHP(z.getHP() - 1); //zombie health
                        z.setSpeed(z.getSpeed()/2);
                        dead();
                        break;
                    }
                }
            }
    }
}
