import java.awt.geom.*;
import java.awt.*;

public class Player {
    
    private double x, y, height, width;
    private Color color;
    
    public Player(double x, double y, double width, double height, Color color){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void drawSprite(Graphics2D g2d) {
        Rectangle2D.Double player = new Rectangle2D.Double(x,y,width,height);
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


}
