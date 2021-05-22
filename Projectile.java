import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;

public class Projectile {
    
    double x, y, width, height, hSpeed, vSpeed;
    public Projectile(double hSpeed, double vSpeed){
        x = 397;
        y = 393;
        width = 6;
        height = 6;
        this.hSpeed = hSpeed;
        this.vSpeed = vSpeed;
    }

    public void draw(Graphics2D g2d){
        Rectangle2D.Double d = new Rectangle2D.Double(x,y,width,height);
        g2d.setColor(Color.BLACK);
        g2d.fill(d);
    }

    public void moveH(double i){
        x += hSpeed * i;
    }

    public void moveV(double i){
        y += vSpeed * i;
    }

    public void invertHSpeed(){
        hSpeed = hSpeed *-1;
    }

    public void invertVSpeed(){
        vSpeed = vSpeed *-1;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public boolean isOutOfBounds(){
        if(x+width > 800 || y+height > 400 || x < 0 || y < 0){
            return true;
        }
        else{
            return false;
        }
    }

}
