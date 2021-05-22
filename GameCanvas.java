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


    public GameCanvas(){
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bgColor);
        Rectangle2D.Double bg = new Rectangle2D.Double(0, 0, 800, 400);
        g2d.fill(bg);
        one.drawSprite(g2d);
        two.drawSprite(g2d);
    }

    public void createSprites(){
        one = new Player(169,179,42,42,p1);
        two = new Player(589,179,42,42,p2);
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

}
    

