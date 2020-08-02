package com.company;

import java.util.ArrayList;

public class Box {
    // Variables ---------------------------------------------------------------------
    public int width;
    public int height;
    public int x;
    public int y;

    // Constructor -------------------------------------------------------------------
    public Box(int w, int h){
        width = w;
        height = h;
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

    // CheckCollision -----------------
    // Arguments: arraylist of boxes
    // Returns: TRUE if a collision occurs, otherwise FALSE
    // Checks this box against a list of boxes to see if their x/y/width/height are colliding
    public boolean checkCollision(ArrayList<Box> boxes){
        for (Box b: boxes) {
            if(b.x < this.x
                    && this.x < (b.x + b.width)
                    && b.y < this.y
                    && this.y < (b.y + b.height)){
                return true;
            }
        }
        return false;
    }
}
