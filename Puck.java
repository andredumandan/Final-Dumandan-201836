
import java.awt.geom.*;
import java.awt.*;

public class Puck {
    
    double x,y,size,mass;
    public Puck(double x, double y){
        this.x=x;
        this.y=y;
        size=36;
        mass=5;
    }
    
    public void draw(Graphics2D g2d){
        Ellipse2D.Double p = new Ellipse2D.Double(x,y,size,size);
        g2d.setColor(Color.BLACK);
        g2d.fill(p);
    }

    public void moveH(int velocity){
        x+=velocity;
    }

    public void moveV(int velocity){
        y+=velocity;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getMass(){
        return mass;
    }
}
