/**
	
    This is a Player object that creates the design of the player. A player contains methods to move and set it's horizontal ng verticl positions.

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
import java.awt.geom.*;
import java.awt.*;

public class Player implements DrawingObject{
    
    private double x, y, size;
    private Color color,innerColor;
    
    /**
     *  Initializes the values for the Player Object.
     * @param x - a double which determines the horizontal position of the Player.
     * @param y - a double which determines the vertical position of the Player.
     * @param color - a Color which is used as the primary color of the PLayer.
     * @param innerColor - a Color which depicts the depth for the mallet.
     */
    public Player(double x, double y, Color color,Color innerColor){
        this.x = x;
        this.y = y;
        this.color = color;
        this.innerColor = innerColor;
        size=42;
    }

    /**
     * @param g2d - accepts a Graphics2D objects which will be used to draw shapes for the Player.
     */
    @Override
    public void draw(Graphics2D g2d) {
        Ellipse2D.Double player = new Ellipse2D.Double(x,y,size,size);
        g2d.setColor(color);
        g2d.fill(player);
        Ellipse2D.Double playerInner = new Ellipse2D.Double(x-size*-0.07,y-size*-0.07,size*0.86,size*0.86);
        g2d.setColor(innerColor);
        g2d.fill(playerInner);
        Ellipse2D.Double playerHandle = new Ellipse2D.Double(x-size*-0.26,y-size*-0.26,size*0.48,size*0.48);
        g2d.setColor(color);
        g2d.fill(playerHandle);
        

    }

    /**
     * @param i - accepts a double which will determine the amount of horizontal movement.
     */
    public void moveH(double i){
        x += i;
    }

    /**
     * @param i - accepts a double which will determine the amount of vertical movement.
     */
    public void moveV(double i){
        y += i;
    }

    /**
     * @param i - accepts a double which the x variable will be set to.
     */
    public void setX(double i){
        x = i;
    }

    /**
     * @param i - accepts a double which the y variable will be set to.
     */
    public void setY(double i){
        y = i;
    }

    /**
     * @return - returns the horizontal position of the PLayer.
     */
    public double getX(){
        return x;
    }

    /**
     * @return - returns the vertical position of the PLayer.
     */
    public double getY(){
        return y;
    }

}
