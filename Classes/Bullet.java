/**
 * Each instance of this class represents a single bullet
 * 
 * @author Zachary Palumbo
 * @version 1.0
 */

import java.lang.Math;
import java.awt.geom.*;

public class Bullet
{
    /*
     * x = the bullet's x position
     * y = the bullet's y position
     * dir = the bullet's direction
     * sp = the bullet's speed
     */
    double x, y, dir, sp;
    
    public Bullet (double myX, double myY, double myDir, double mySp)
    {
        x = myX;
        y = myY;
        dir = myDir;
        sp = mySp;
    }
    
    public void move () // Update position based on speed and direction
    {
        x += sp * Math.cos (dir);
        y += sp * Math.sin (dir);
    }
    
    public Rectangle2D.Double getCollisionRect () // Return a rectangle to be used as a reference for collisions
    {
        Rectangle2D.Double rect = new Rectangle2D.Double ((int) (x - 3), (int) (y - 3), 6, 6);
        return rect;
    }
    
    public Line2D.Double getCollisionPath () // Return a line representing the path this bullet will travel
    {                                        // between this step and the next (to prevent it "skipping" over enemies)
        Line2D.Double path = new Line2D.Double (x, y, x + (sp * Math.cos (dir)), y + (sp * Math.sin (dir)));
        return path;
    }
}
