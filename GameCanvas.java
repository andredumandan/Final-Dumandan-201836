import javax.swing.*;
import java.awt.*;

public class GameCanvas extends JComponent{

    private Color p1 = new Color(24,126,230);
    private Color p1InnerColor = new Color(15,81,148);
    private Color p2 = new Color(210,24,222);
    private Color p2InnerColor = new Color(134,17,142);
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
        one = new Player(169,179,p1,p1InnerColor);
        two = new Player(589,179,p2,p2InnerColor);
        p = new Puck(382,182);
    }

    public void changeColor(boolean isMoving, int playerID){
        if(playerID == 1){
            if (isMoving == true){
                p1 = new Color (24,126,230);
            }
            else{
                p1 = new Color(105,164,224);
            }
        }
       else{
        if (isMoving == true){
            p2 = new Color (210,24,222);
        }
        else{
            p2 = new Color (219,132,224);
        }
       }
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

    

