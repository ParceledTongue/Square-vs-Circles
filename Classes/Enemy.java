/**
 * Each instance of this class represents a single enemy.
 * 
 * @author Zachary Palumbo 
 * @version 1.0
 */

import java.lang.Math;
import java.awt.geom.*;

public class Enemy
{
    /*
     * x = the enemy's x position
     * y = the enemy's y position
     * xsp = the enemy's horizontal speed vector
     * ysp = the enemy's vertical speed vector
     * maxsp = the maximum speed the enemy can move at
     * hp = the enemy's current health
     * maxhp = the enemy's maximum health
     */
    double x, y, xsp = 0, ysp = 0, maxsp;
    int hp, maxhp;
    
    public Enemy (double myX, double myY, int myMaxHp, double myMaxSp)
    {
        x = myX;
        y = myY;
        maxhp = myMaxHp;
        hp = maxhp;
        maxsp = myMaxSp;
    }
    
    public double getSpeed () // Return the current speed of the enemy
    {
        // Use Pythagoras to calculate the total speed from the horizontal and vertical vectors
        double speed = Math.sqrt (Math.pow (xsp, 2) + Math.pow (ysp, 2));
        return speed;
    }
    
    public double getDirection () // Return the current direction in which the enemy is moving
    {
        double direction = Math.atan2 (ysp, xsp);
        return direction;
    }
    
    public void addMotion (double dir, double sp) // Add a vector of a specified direction and size
    {                                             // to the current motion vector
        xsp += sp * Math.cos (dir); // Add the x component
        ysp += sp * Math.sin (dir); // Add the y component
        limitSpeed (); // Ensure that this addition does not cause the enemy to exceed its max speed
    }
    
    public void limitSpeed () // Make sure the enemy does not exceed its max speed
    {
        double sp = getSpeed ();
        double dir = getDirection ();
        
        if (sp > maxsp) // If the enemy is exceeding its max speed...
        {
            double subsp = sp - maxsp; // the amount of speed which must be subtracted
            xsp -= subsp * Math.cos (dir); // Subtract the x component
            ysp -= subsp * Math.sin (dir); // Subtract the y component
        }
    }
    
    public double getScale () // Return the "scale" of the enemy (between 0 and 1) based on health
    {
        return (double) hp / maxhp;
    }
    
    public Rectangle2D.Double getCollisionRect () // Return a rectangle to be used as a reference for collisions
    {
        double scale = getScale ();
        
        // This is the rectangular area of the screen which this enemy currently occupies
        Rectangle2D.Double rect = new Rectangle2D.Double ((int) (x - (15 * scale)), (int) (y - (15 * scale)), (int) (30 * scale), (int) (30 * scale));
        return rect;
    }
    
    public boolean checkCollision (Player p) // Return whether this enemy is currently colliding with the player
    {
        Rectangle2D.Double myrect = getCollisionRect ();
        Rectangle2D.Double playerrect = p.getCollisionRect ();
        return (myrect.intersects (playerrect));
    }
    
    public boolean checkCollision (Bullet b) // Return whether this enemy is currently colliding with bullet b
    {
        Rectangle2D.Double myrect = getCollisionRect ();
        Rectangle2D.Double bulletrect = b.getCollisionRect ();
        Line2D.Double bulletpath = b.getCollisionPath ();
        return (myrect.intersects (bulletrect) || myrect.intersectsLine (bulletpath));
    }
}
