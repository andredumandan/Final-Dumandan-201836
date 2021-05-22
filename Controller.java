import java.util.*;
import java.awt.*;

public class Controller {
   
    ArrayList<Projectile> pArray = new ArrayList<Projectile>();
    public Controller(){

    }

    public void drawProjectiles(Graphics2D g2d){
        for(Projectile p : pArray) {
            p.draw(g2d);
        }
    }

    public void moveProjectiles(){
        for(int i=0; i < pArray.size(); i++){
            if(pArray.get(i).isOutOfBounds()){
                if(pArray.get(i).getX() >= 800 || pArray.get(i).getX() <= 0){
                    pArray.get(i).invertHSpeed();
                }
                else if(pArray.get(i).getY() >= 400 || pArray.get(i).getY() < 0){
                    pArray.get(i).invertVSpeed();
                }
            }
            if (i%2 == 0){
                pArray.get(i).moveH(1);
                pArray.get(i).moveV(-1);
            }
            else{
                pArray.get(i).moveH(-1);
                pArray.get(i).moveV(-1);
            }
           
        }
    }

    public void clearProjectiles(){
        for (Projectile p : pArray){
            pArray.remove(p);
        }
    }

    public void addProjectile(){
        pArray.add(new Projectile(2,5));
    }


    public ArrayList getProjectiles(){
        return pArray;
    }


}
