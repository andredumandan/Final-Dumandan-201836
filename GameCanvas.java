/**
	
    This is the GameCanvas which extends the JComponent Class. It overrides the paintComponent method in order to render the required objects like the players and the buck.
	@author Andre Matthew G. Dumandan (201836)
	@version May 16, 2021
**/

/*
	I have not discussed the Java language code in my program 
	with anyone other than my instructor or the teaching assistants 
	assigned to this course.

	I have not used Java language code obtained from another student, 
	or any other unauthorized source, either modified or unmodified.

	If any Java language code or documentation used in my program 
	was obtained from another source, such as a textbook or website, 
	that has been clearly noted with a proper citation in the comments 
	of my program.
*/
import javax.swing.*;
import java.awt.*;

public class GameCanvas extends JComponent{

    private Color p1 = new Color(24,126,230);
    private Color p1InnerColor = new Color(15,81,148);
    private Color p2 = new Color(210,24,222);
    private Color p2InnerColor = new Color(134,17,142);
    private Color innerPuckColor;
    private Color outerPuckColor;
    private Background bg = new Background();
    private Player one;
    private Player two;
    private Puck p;


    public GameCanvas(){
        p1 = new Color(24,126,230);
        p1InnerColor = new Color(15,81,148);
        p2 = new Color(210,24,222);
        p2InnerColor = new Color(134,17,142);
        innerPuckColor = new Color(53,51,51);
        outerPuckColor = Color.BLACK;
    }

    
    /** 
     * @param g
     */
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
        p = new Puck(382,182,outerPuckColor,innerPuckColor);
    }

    
    /** 
     * @param playerID
     * @return Player
     */
    public Player getPlayer(int playerID){
        if (playerID == 1) {
            return one;
        }
        else {
            return two;
        }
    }

    
    /** 
     * @param playerID
     * @return Player
     */
    public Player getEnemy(int playerID){
        if (playerID == 1) {
            return two;
        }
        else {
            return one;
        }
    }

    
    /** 
     * @return Puck
     */
    public Puck getPuck(){
        return p;
    }
}

    

