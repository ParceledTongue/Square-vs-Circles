/**
 * This class tracks the state of the mouse.
 * 
 * @author Zachary Palumbo
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;

public class Mouse implements MouseListener, MouseMotionListener
{
    /*
     * x = the mouse's x position
     * y = the mouse's y position
     * isDown = whether the mouse button is currently pressed down
     */
    int x, y;
    boolean isDown = false;
    
    public void mouseEntered (MouseEvent e) // Called when the mouse enters the window
    {
        // Update the mouse's current position
        x = e.getX ();
        y = e.getY ();
        e.consume (); // Do not process this event any further.
    }
    
    public void mouseExited (MouseEvent e) {} // Called when the mouse exits the window
    
    public void mouseMoved (MouseEvent e) // Called whenever the mouse moves 
    {                                     // and the mouse button is not down
        x = e.getX ();
        y = e.getY ();
        e.consume ();
    }
    
    public void mouseDragged (MouseEvent e) // Called whenever the mouse moves 
    {                                       // and the mouse button is down
        x = e.getX ();
        y = e.getY ();
        e.consume ();
    }
    
    public void mousePressed (MouseEvent e) // Called when the mouse button is pressed
    {
        isDown = true;
        e.consume ();
    }
    
    public void mouseClicked (MouseEvent e) {} // Called when the mouse button is pressed
                                               // and released with no mouse movement
    
    public void mouseReleased (MouseEvent e) // Called when the mouse button is released
    {
        isDown = false;
        e.consume ();
    }
}
