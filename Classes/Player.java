/**
 * This class represents the player.
 * 
 * @author Zachary Palumbo
 * @version 1.0
 */

import java.awt.geom.*;

public class Player
{
    /*
     * x = the player's x position
     * y = the player's y position
     * xsp = the player's horizontal speed vector
     * ysp = the player's vertical speed vector
     * dir = the direction in which the player is moving
     * accel = the amount by which to increment or decrement the speed vectors each step
     * maxsp = the maximum speed the player may move at
     * bulletsp = the speed at which the player's bullets travel
     * hp = the player's current health
     * maxhp = the player's max health
     * isAlive = whether the player is currently alive
     */
    double x, y, xsp = 0, ysp = 0, dir = 0, accel, maxsp, bulletsp;
    int hp, maxhp;
    boolean isAlive = true;
    
    public Player (double myX, double myY, int myHp)
    {
        x = myX;
        y = myY;
        accel = 1;
        maxsp = 8;
        bulletsp = 20;
        maxhp = myHp;
        hp = maxhp;
    }
    
    public double getSpeed () // Return the current speed of the enemy
    {
        // Use Pythagoras to calculate the total speed from the horizontal and vertical vectors
        double speed = Math.sqrt (Math.pow (xsp, 2) + Math.pow (ysp, 2));
        return speed;
    }
    
    public double getDirection () // Return the current direction in which the player is moving
    {
        double direction = Math.atan2 (ysp, xsp);
        return direction;
    }
    
    public void limitSpeed () // Make sure the player does not exceed his max speed
    {
        double sp = getSpeed ();
        double dir = getDirection ();
        
        if (sp > maxsp) // If the player is exceeding his max speed...
        {
            double subsp = sp - maxsp; // the amount of speed which must be subtracted
            xsp -= subsp * Math.cos (dir); // Subtract the x component
            ysp -= subsp * Math.sin (dir); // Subtract the y component
        }
    }
    
    public double getScale () // Return the "scale" of the player (between 0 and 1) based on health
    {
        return (double) hp / maxhp;
    }
    
    public Rectangle2D.Double getCollisionRect () // Return a rectangle to be used as a reference for collisions
    {
        double scale = getScale ();
        
        // This is the rectangular area of the screen which the player currently occupies
        Rectangle2D.Double rect = new Rectangle2D.Double ((int) (x - (15 * scale)), (int) (y - (15 * scale)), (int) (30 * scale), (int) (30 * scale));
        return rect;
    }
}
