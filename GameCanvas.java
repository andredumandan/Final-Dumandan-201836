import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


public class GameCanvas extends JComponent{
    
    private Color bgColor = new Color(255,244,111);
    private Color p1 = new Color(0,168,219);
    private Color p2 = new Color(255,157,168);
    Player one;
    Player two;
    Puck p;


    public GameCanvas(){
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        g2d.setColor(bgColor);
        Rectangle2D.Double bg = new Rectangle2D.Double(0, 0, 800, 400);
        g2d.fill(bg);
        g2d.setColor(Color.WHITE);
        Rectangle2D.Double line = new Rectangle2D.Double(396, 0, 8, 400);
        g2d.fill(line);
        Ellipse2D.Double center = new Ellipse2D.Double(340, 140, 121, 121);
        g2d.fill(center);
        g2d.setColor(bgColor);
        Ellipse2D.Double centerCover = new Ellipse2D.Double(350, 150, 100, 100);
        g2d.fill(centerCover);
        p.draw(g2d);
        one.drawSprite(g2d);
        two.drawSprite(g2d);
        
    }

    public void createSprites(){
        one = new Player(169,179,p1);
        two = new Player(589,179,p2);
        p = new Puck(382,182);
    }

    public Player getPlayer(int playerID){
        if (playerID == 1) {
            return one;
        }
        else {
            return two;
        }
    }

    public Player getEnemy(int playerID){
        if (playerID == 1) {
            return two;
        }
        else {
            return one;
        }
    }

    public Puck getPuck(){
        return p;
    }
}

    

