import java.awt.*;

/**
 * This java file is an interface that requires objects to implement the draw method. These DrawingObjects are grouped to declutter the GameCanvas.
 */
public interface DrawingObject {
    void draw(Graphics2D g2d);
}
