import javax.swing.*;
import java.awt.*;

public class GameCanvas extends JComponent{

    private Color p1 = new Color(24,126,230);
    private Color p2 = new Color(210,24,222);
    Background bg = new Background();
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
        bg.draw(g2d);
        p.draw(g2d);
        one.draw(g2d);
        two.draw(g2d);
        
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

    

