/**
 * This class runs the program. It handles graphics and thread
 * management (updating positions and states continuously).
 * 
 * @author Zachary Palumbo
 * @version 1.0
 */

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;
import java.util.ArrayList;
import java.awt.geom.*;

public class Master extends Applet implements KeyListener, Runnable
{
    Player p = new Player (250, 250, 5); // the player object
    Mouse m = new Mouse (); // the mouse object
    
    // holds data for all Button objects
    ArrayList <Button> buttons = new ArrayList <Button> ();
    // holds all enemies that currently exist
    ArrayList <Enemy> enemies = new ArrayList <Enemy> ();
    // holds all bullets that currently exist
    ArrayList <Bullet> bullets = new ArrayList <Bullet> ();
    
    /* enemysp = the maximum speed of enemies
     * spawnrate = the probability that an enemy will be generated each step
     * timer = the amount of time the player has survived (i.e. the score) */
    double enemysp, spawnrate, timer;
    
    // keep track of the keys currently pressed down
    boolean leftDown = false, rightDown = false, upDown = false, downDown = false;
    
    Font small = new Font ("TimesRoman", Font.PLAIN, 12);
    Font medium = new Font ("TimesRoman", Font.PLAIN, 24);
    Font big = new Font ("TimesRoman", Font.BOLD, 36);
    
    // (to be used for centering text)
    FontMetrics fm1 = getFontMetrics (big);
    FontMetrics fm2 = getFontMetrics (medium);
    
    // the image to be drawn on the screen
    Image backbuffer;
    Graphics backg;
    
    
    String screen; // the current "screen" of the game 
    // (either difficulty selection or the gameplay itself)
    
    Thread t; // the thread constantly updating the game's state
    boolean isRunning = true; // whether the game is currently running
    
    /**
     * Applet Methods
     */
   
    public void init () // Called when the applet starts or is restarted
    {   
        this.setFocusable (true);
        addKeyListener (this); // Allow the applet to track key presses
        addMouseListener (m); // Allow the mouse object to track button presses
        addMouseMotionListener (m); // Allow the mouse object to track the position of the mouse
        timer = 0; // Set the score to 0
        enemysp = 5; // Set the maximum enemy speed to 5
        
        // Place the player in the center of the screen
        p.x = 250;
        p.y = 250;
        
        // Stop any movement
        p.xsp = 0;
        p.ysp = 0;
        
        p.hp = p.maxhp; // Give the player full health
        p.isAlive = true; // Ensure the player is recognized as alive
        
        // Delete any objects that may exist
        buttons.clear ();
        bullets.clear ();
        enemies.clear ();
        
        // Create the buttons handling difficulty selection
        buttons.add (new Button (220, 200, 60, 40, "Easy"));
        buttons.add (new Button (200, 260, 100, 40, "Medium"));
        buttons.add (new Button (220, 320, 60, 40, "Hard"));
        
        // Instantiate the backbuffer as a blank canvas of dimensions 500x500
        backbuffer = createImage (500, 500);
        // Place the graphics stored in backbuffer into this Graphics object
        backg = backbuffer.getGraphics ();
        
        screen = "Difficulty"; // Go to the difficulty selection screen
        t = new Thread (this); // Instantiate the thread
        t.start (); // Start executing the thread
    }
    
    public void start () // Called when the applet is started
    {
        isRunning = true;
    }
    
    public void stop () // Called when the applet is stopped
    {
        isRunning = false;
    }
    
    public void paint (Graphics g) // Executed whenever repaint () is called
    {
        update (g);
    }
    
    /** 
     * Graphics Methods
     */
    
    public void buffer () // Create the image that will be drawn to the screen when update is called.
    {
        // Draw a black background
        backg.setColor (Color.black);
        backg.fillRect (0, 0, 500, 500);
        
        if (screen == "Difficulty")
        {
            backg.setColor (Color.white); // Set the drawing color to white.
            
            backg.setFont (big);
            printCentered ("Difficulty?", fm1, 250, 100);
            
            backg.setFont (medium);
            
            // Cycle through the array of Button objects.
            for (Button b : buttons)
            {
                backg.drawRect (b.x, b.y, b.width, b.height); // Draw the button.
                printCentered (b.text, fm2, 250, b.y + 27); // Draw the button's text.
            }
        }
        
        if (screen == "Main")
        {
            // Set the drawing color to white.
            backg.setColor (Color.white);
            if (p.isAlive) // If the player is alive...
            {
                // The scale of the player (from 0 to 1).
                double scale = (double) p.hp / p.maxhp;
                // Draw a square representing the player with side length (30 * scale).
                backg.drawRect ((int) (p.x - (15 * scale)), (int) (p.y - (15 * scale)), (int) (30 * scale), (int) (30 * scale));
                
                backg.setFont (small);
                // Draw the score in the bottom left corner.
                backg.drawString ("" + (timer / 1000), 10, 480);
            }
            else // If the player has died...
            {
                backg.setFont (big);
                printCentered ("GAME OVER", fm1, 250, 200);
            
                backg.setFont (medium);
                printCentered ("You survived " + (timer / 1000) + " seconds.", fm2, 250, 300);
            }
            
            backg.setColor (Color.gray); // Set the drawing color to gray.
            
            // Draw each Bullet as a square with side length 6.
            for (Bullet b : bullets)
                backg.drawRect ((int) b.x - 3, (int) b.y - 3, 6, 6);
            
            backg.setColor (Color.red); // Set the drawing color to red.
            
            // Draw each Enemy object as a circle with radius (e.scale * 15);
            for (Enemy e : enemies)
            {
                double scale = e.getScale ();
                backg.drawOval ((int) (e.x - (15 * scale)), (int) (e.y - (15 * scale)), (int) (30 * scale), (int) (30 * scale));
            }
        }
    }
    
    // Draw the image stored in backbuffer (created in the buffer method) onto the screen
    public void update (Graphics g) 
    {
        g.drawImage (backbuffer, 0, 0, this);
    }
    
    // Print String printing to the screen, with the drawn text centered at (x, y)
    public void printCentered (String printing, FontMetrics fm, int x, int y)
    {
        backg.drawString (printing, x - ((fm.stringWidth (printing)) / 2), y);
    }
    
    /**
     * KeyListener Methods
     */
    
    public void keyTyped (KeyEvent e) {} // Only here to override the method in the interface
    
    public void keyPressed (KeyEvent e) // Check if any keys have been pressed, and
    {                                   // update variables accordingly
        if (e.getKeyCode () == e.VK_A)
            leftDown = true;
        if (e.getKeyCode () == e.VK_D)
            rightDown = true;
        if (e.getKeyCode () == e.VK_W)
            upDown = true;
        if (e.getKeyCode () == e.VK_S)
            downDown = true;
        
        e.consume (); // Do not process this event any further.
    }
    
    public void keyReleased (KeyEvent e) // Check if any keys have been released, and
    {                                    // update variables accordingly
        if (e.getKeyCode () == e.VK_A)
            leftDown = false;
        if (e.getKeyCode () == e.VK_D)
            rightDown = false;
        if (e.getKeyCode () == e.VK_W)
            upDown = false;
        if (e.getKeyCode () == e.VK_S)
            downDown = false;
        
        e.consume ();
    }
    
    /**
     * Steps
     * (Executed every step of the thread)
     */
    
    public void playerStep ()
    {
        // Accelerate the player in the appropriate direction based on which keys are pressed
        if (leftDown) p.xsp -= p.accel;
        else if (rightDown) p.xsp += p.accel;
        else p.xsp /= 1.05;
        if (upDown) p.ysp -= p.accel;
        else if (downDown) p.ysp += p.accel;
        else p.ysp /= 1.05;
            
        p.limitSpeed ();
                
        // When the player moves off the edge, wrap around the screen.
        if (p.x > 520)
            p.x = -20;
        if (p.x < -20)
            p.x = 520;
        if (p.y > 520)
            p.y = -20;
        if (p.y < -20)
            p.y = 520;
                    
        if (m.isDown) // If the left mouse button is down...
        {
            // Calculate the direction from the player to the mouse
            double mousedir = Math.atan2 (m.y - p.y, m.x - p.x);
            // Create a bullet at the player's coordinates moving toward the mouse
            bullets.add (new Bullet (p.x, p.y, mousedir, p.bulletsp));
        }
        
        // Change the player's position based on its x and y velocities
        p.x += p.xsp;
        p.y += p.ysp;
        
        if (p.hp <= 0) // If the player has 0 health (or less), he is no longer alive
            p.isAlive = false;
    }
    
    public void enemyStep (Enemy e)
    {
        // The direction in which this enemy should try to move
        double targetdir;
        
        if (p.isAlive) // If the player is alive, move toward him
            targetdir = Math.atan2 (p.y - e.y, p.x - e.x);
        else // If not, move off of the screen
            targetdir = Math.atan2 (e.y - 250, e.x - 250);
            
        e.addMotion (targetdir, enemysp / 10); // Add motion in the target direction
        
        // If this enemy comes into contact with the player...
        if (e.checkCollision (p))
        {
            p.hp--; // Damage the player
            enemies.remove (e); // Destroy this enemy
        }
        
        // Cycle through the array of bullets
        for (int i = 0; i < bullets.size (); i++)
        {
            Bullet b = bullets.get (i); // the bullet currently being analyzed
            
            if (e.checkCollision (b)) // If this enemy is touching the bullet...
            {
                e.addMotion (b.dir, b.sp); // "Knock" the enemy back
                e.hp -= 1; // Damage the enemy
                bullets.remove (b); // Destroy the bullet
            }
        }
        
        // Change this enemy's position based on its x and y velocities
        e.x += e.xsp;
        e.y += e.ysp;
        
        // If the enemy has 0 health or less, destroy it
        if (e.hp <= 0)
            enemies.remove (e);
    }
    
    public void bulletStep (Bullet b)
    {
        b.move (); // Move the bullet based on its direction and speed
        
        // If the bullet has left the screen, delete it
        if (b.x > 510 || b.y > 510 || b.x < -10 || b.y < -10)
            bullets.remove (b);
    }
    
    /**
     * Runnable Method
     */
    
    public void run () // Called every time the thread executes
    {
        try
        {
            while (isRunning)
            {   
                if (screen == "Difficulty") // On the difficulty selection screen...
                {
                    for (Button b : buttons) // Cycle through the array of buttons
                    {
                        if (b.isSelected (m)) // If the button is clicked...
                        {
                            // Set the spawn rate based on of the buttons was pressed
                            
                            if (b.text == "Easy")
                                spawnrate = .01;
                        
                            if (b.text == "Medium")
                                spawnrate = .02;
                        
                            if (b.text == "Hard")
                                spawnrate = .04;
                            
                            // Go to the main screen
                            screen = "Main";
                        }
                    }
                }
                
                if (screen == "Main") // On the main (gameplay) screen...
                {
                    if (p.isAlive) // If the player is alive...
                    {
                        playerStep (); // Execute the player's step
                        
                        // If a randomly generated number between 0 and 1 
                        // is less than the spawn rate, generate an enemy
                        if (Math.random () < spawnrate)
                            generateEnemy ();
                            
                        timer += (100/3); // Increase the timer (score)
                    }
                    
                    // Cycle through the array of enemies, executing
                    // the step method for each of them
                    for (int i = 0; i < enemies.size (); i++)
                        enemyStep (enemies.get (i));
                
                    // Cycle through the array of bullets, executing
                    // the step method for each of them
                    for (int i = 0; i < bullets.size (); i++)
                        bulletStep (bullets.get (i));
                    
                    // Increment the enemy speed
                    // (This causes enemies to become faster as the game progresses)
                    enemysp += .001;
                }
                
                buffer (); // Draw the current screen to the backbuffer
                repaint (); // Paint the backbuffer to the screen
                t.sleep (30); // Rest for 30 milliseconds until the next step
            }
        }
        catch (InterruptedException e) {}
    }
    
    /**
     * Misc.
     */
    
    public void generateEnemy ()
    {
        // Generate a random integer between 0 and 3, inclusive
        int diceRoll = (int) (Math.random () * 4);
        
        // Based on the dice roll, generate an enemy from a certain side of the screen
        // (0 for left, 1 for right, 2 for top, 3 for bottom)
        if (diceRoll == 0)
            enemies.add (new Enemy (-20, Math.random () * 500, 5, enemysp));
        if (diceRoll == 1)
            enemies.add (new Enemy (520, Math.random () * 500, 5, enemysp));
        if (diceRoll == 2)
            enemies.add (new Enemy (Math.random () * 500, -20, 5, enemysp));
        if (diceRoll == 3)
            enemies.add (new Enemy (Math.random () * 500, 520, 5, enemysp));
    }
}
