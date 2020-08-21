/*
  Box class

  ConnorFergusson_1299038_HannahTrebes_1306378
*/

import java.util.ArrayList;

public class Box {
    // Variables ---------------------------------------------------------------------
    int width;
    int height;
    int x;
    int y;
    int area;

    // Constructor -------------------------------------------------------------------
    public Box(int w, int h){
        width = w;
        height = h;
        area = width * height;
    }

    // Methods -----------------------------------------------------------------------

    // Rotate -------------------------
    // Switches the width and height
    public void rotate(){
        int temp = width;
        width = height;
        height = temp;

        return;
    }

    //FitsIn
    //Check if one box will fit within the given box
    public boolean fitsIn(Box g){
        //System.out.println("GAP: " + g.width + " " + g.height);
        //System.out.println("BOX: "+ this.width + " " + this.height);

        if(this.width < g.width && this.height < g.height){
            return true;
        }
        this.rotate();
        if(this.width < g.width && this.height < g.height){
            return true;
        }
        return false;
    }

    // CheckCollision -----------------
    // Arguments: arraylist of boxes
    // Returns: TRUE if a collision occurs, otherwise FALSE
    // Checks this box against a list of boxes to see if their x/y/width/height are colliding
    public boolean checkCollision(ArrayList<Box> boxes){
        for (Box b: boxes)
        {
            if(b != this)
            {
                if(this.x < (b.x + b.width))
                {
                    if((this.x + this.width) > b.x)
                    {
                        if(this.y < (b.y + b.height))
                        {
                            if((this.y + this.height) > b.y)
                            {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // GetCollision -----------------
    // Arguments: arraylist of boxes
    // Returns: The box with which a collision is occuring, null if there is no collision
    public Box getCollision(ArrayList<Box> boxes){
        if (checkCollision(boxes) == false) return null;
        else{
            for (Box b: boxes)
            {
                if(b != this)
                {
                    if(this.x < (b.x + b.width))
                    {
                        if((this.x + this.width) > b.x)
                        {
                            if(this.y < (b.y + b.height))
                            {
                                if((this.y + this.height) > b.y)
                                {
                                    return b;
                                }
                            }
                        }
                    }
                }
            }
        }
        // This return should never be reached
        return null;
    }
//a method to minimise the height of the box
    public void minimizeHeight(){
        if (width >= height){ return; }
        rotate();
        return;
    }
//a method that returns the area of the box
    public int getArea(){
        area = width * height;
        return area;
    }
//a public method to return the width and height in string format
    public String toString(){
        return width + " " + height;
    }
//a method that gets the y value
    public int getY()
    {
        return y;
    }
//a method that sets the y value
    public void setY(int tempY)
    {
        y = tempY;
    }
//a method that gets the x value
    public int getX()
    {
        return x;
    }
//a method that sets the x value
    public void setX(int tempX)
    {
        x = tempX;
    }
//a method that gets the width
    public int getWidth()
    {
        return width;
    }
//a method that sets the width
    public void setWidth(int tempWidth)
    {
        width = tempWidth;
    }
//a method that gets the height
    public int getHeight()
    {
        return height;
    }
//a method that sets the height
    public void setHeight(int tempHeight)
    {
        height = tempHeight;
    }
}
