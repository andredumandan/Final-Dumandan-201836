import java.awt.geom.*;
import java.awt.*;

public class Player implements DrawingObject{
    
    private double x, y, size, mass;
    private Color color,innerColor;
    
    public Player(double x, double y, Color color,Color innerColor){
        this.x = x;
        this.y = y;
        this.color = color;
        this.innerColor = innerColor;
        size=42;
        mass = 10;
    }

    public void draw(Graphics2D g2d) {
        Ellipse2D.Double player = new Ellipse2D.Double(x,y,42,42);
        g2d.setColor(color);
        g2d.fill(player);
        Ellipse2D.Double playerInner = new Ellipse2D.Double(x-size*-0.07,y-size*-0.07,size*0.86,size*0.86);
        g2d.setColor(innerColor);
        g2d.fill(playerInner);
        Ellipse2D.Double playerHandle = new Ellipse2D.Double(x-size*-0.26,y-size*-0.26,size*0.48,size*0.48);
        g2d.setColor(color);
        g2d.fill(playerHandle);
        

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
