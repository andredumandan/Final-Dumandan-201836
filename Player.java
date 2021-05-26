import java.awt.geom.*;
import java.awt.*;

public class Player implements DrawingObject{
    
    private double x, y, size, mass;
    private Color color;
    
    public Player(double x, double y, Color color){
        this.x = x;
        this.y = y;
        this.color = color;
        mass = 10;
    }

    public void draw(Graphics2D g2d) {
        Ellipse2D.Double player = new Ellipse2D.Double(x,y,42,42);
        g2d.setColor(color);
        g2d.fill(player);
    }

    public void moveH(double i){
        x += i;
    }

    public void moveV(double i){
        y += i;
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
