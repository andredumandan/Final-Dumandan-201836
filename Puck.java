import java.awt.geom.*;
import java.awt.*;

public class Puck implements DrawingObject{
    
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
        Ellipse2D.Double puckInside = new Ellipse2D.Double(x-size*-0.11,y-size*-0.11,size*0.78,size*0.78);
        g2d.setColor(new Color(53,51,51));
        g2d.fill(puckInside);
    }

    public void moveH(double velocity){
        x+=velocity;
    }

    public void moveV(double velocity){
        y+=velocity;
    }
    
    public void setX(double i){
        x = i;
    }

    public void setY(double i){
        y = i;
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
