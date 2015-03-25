/**
 * Each instance of this class represents a single button.
 * 
 * @author Zachary Palumbo
 * @version 1.0
 */

public class Button
{
    /*
     * x = the button's x position
     * y = the button's y position
     * width = the button's width (in pixels)
     * height = the button's height (also in pixels)
     * text = the text to be displayed on this button
     */
    int x, y, width, height;
    String text;
    
    public Button (int myX, int myY, int myWidth, int myHeight, String myText)
    {
        x = myX;
        y = myY;
        width = myWidth;
        height = myHeight;
        text = myText;
    }
    
    public boolean isSelected (Mouse m) // Return whether this button has been clicked
    {
        if (m.isDown && m.x >= x && m.y >= y && m.x <= (x + width) && m.y <= (y + height))
            return true;
        else return false;
    }
}
