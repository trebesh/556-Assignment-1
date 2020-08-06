/*
  Main Class to Implement Variable Neighbourhood Search
  
  ConnorFergusson_1299038_HannahTrebes_1306378
  neighbourhood function idea:
    take tallest box on second/next row, move down as best improvement
    swap tallest box with shorter box/es of same width
*/

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class VNS{

    //List of the items
    public static ArrayList<Box> boxes = new ArrayList<>();
    public static final int scale = 5;
    //Buffered Reader - read line by line
    public static final int width = 40 * scale;
    
    public static void main(String [] args)
    {
        //Check that a file path is valid
        if (args.length == 0){
            System.out.println("Error: File Path needed as argument");
            return;
        }

        else if(!csv.equals(args[0].substring(args[0].length() - 3))){
            System.out.println("ERROR: File not in csv format");
            return;
        }

        String filePath = args[0];

        getData(filePath);

        initialSolution();
        boxes = LargeSearch(boxes);
        shuffleDown();
        rightDownLeft();

        result();
        int totalHeight = getHighest(boxes) / scale;
        
        System.out.println("TotalHeight: " + totalHeight);
        System.out.println("Number of Boxes: " + boxes.size());
        JFrame f = new JFrame("Boxes");
        BoxDrawer display = new BoxDrawer();
//        JScrollPane scrollPane = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        JScrollBar bar = scrollPane.getVerticalScrollBar();
//        f.add(scrollPane, BorderLayout.RIGHT);
//        f.add(bar);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(display);
        f.setSize(width, getHighest(boxes) + 50);
        //bar.setPreferredSize(new Dimension(10, f.getHeight()));

        f.setVisible(true);
    }


    // VARIABLES -----------------------if--------------------------------------------------------------------------------
    //An item

    public static BufferedReader BR;
    //The line
    public static String line;
    //Valid file extension
    public static String csv = "csv";

    // GETDATA ---------------------------------------------------------------------------------------------------------
    // Reads the data in from the provided csv file
    public static void getData(String filePath){
        try{
            BR = new BufferedReader(new FileReader(filePath));
            //Until the end of the file is reached
            while ((line = BR.readLine()) != null){
                //Split the line into the list of items
                String[] stringDim = line.split(",");

                Box b = new Box((Integer.parseInt(stringDim[0])) * scale, (Integer.parseInt(stringDim[1])) * scale);
                b.minimizeHeight();
                boxes.add(b);        int totalHeight = getHighest(boxes) / scale;

            }
//            //Testing message
//            for(int i = 0; i < boxes.size(); i++){
//                System.out.println(boxes.get(i).toString());
//            }
        }catch (IOException e){
            System.out.println("IO ERROR in GETDATA" + e.getMessage());
        }catch (Exception e){
            System.out.println("ERROR in GETDATA" + e.getMessage());
        }
        return;
    }

    public static void initialSolution()
    {

        int totalWidth = 0;
        int totalHeight = 0;
        int nextWidth = 0;
        int nextHighest = 0;
        int nextHeight = 0;

        //loop through the list of boxes
        for(int i = 0; i<boxes.size(); i++)
        {
            //get the nextbox in the list
            Box current = boxes.get(i);
            //get its height and width
            nextHeight = current.height;
            nextWidth = current.width;

            //if it can fit in the current line
            if((totalWidth + nextWidth) <= width)
            {
                //add it at this height
                current.x = totalWidth;
                current.y = totalHeight;
                //increase the total width for this height
                totalWidth += nextWidth;
                //test if this is the tallest block on the row
                if(nextHeight > nextHighest)
                {
                    //if it is, store the height
                    nextHighest = nextHeight;
                }
            }
            //if it cant fit
            else
            {
                //reset the width, and go to height of the tallest box
                totalWidth = 0;
                totalHeight+=nextHighest;
                current.x = totalWidth;
                current.y = totalHeight;
                totalWidth += nextWidth;
                nextHighest = nextHeight;
            }

        }
        shuffleDown();
        //display the positions of each box and the total height
    }

    //a method that writes the position of of each box to the console
    public static void result()
    {
        int c = 1;
        for (Box b:boxes) {
            //writes each box to the console on separate lines
            System.out.println("Box " + c + " is at position: " + (b.x/scale) + ", " + (b.y/scale));
            c++;
        }

    }
    //shuffles all the boxes down into free space
    public static void shuffleDown()
    {
        int numChanges = 1;
        //while there was still changes
        while(numChanges > 0)
        {
            numChanges = 0;
            //go through the list of boxes
            for(Box b: boxes)
            {
                Boolean collision = false;
                //while there isnt a collision
                while(collision == false)
                {
                    //store the old y coordinate
                    int tempY = b.getY();
                    if(tempY-1 >= 0)
                    {
                        //reduce the y by one, then test if there are collisions
                        b.setY(tempY - 1);
                        //if there  are collisions, change the y back
                        if(b.checkCollision(boxes))
                        {
                            b.setY(tempY);
                            collision = true;
                        }
                        else
                        {
                            numChanges++;
                        }
                    }
                    else
                    {
                        collision = true;
                    }
                    
                }
                shuffleLeft(b);
                
            }
        }

    }
    
    public static void shuffleLeft(Box b)
    {
    	int numChanges = 1;
        //while there was still changes
        while(numChanges > 0)
        {
            numChanges = 0;
                Boolean collision = false;
                //while there isnt a collision
                while(collision == false)
                {
                    //store the old x coordinate
                    int tempX = b.x;
                    if(tempX-1 >= 0)
                    {
                        //reduce the y by one, then test if there are collisions
                        b.x = tempX - 1;
                        //if there  are collisions, change the y back
                        if(b.checkCollision(boxes))
                        {
                            b.x = tempX;
                            collision = true;
                        }
                        else
                        {
                            numChanges++;
                        }
                    }
                    else
                    {
                        collision = true;
                    }
                }
        }
    }

    public static void shuffleRight(Box b)
    {
    	int numChanges = 1;
        //while there was still changes
        while(numChanges > 0)
        {
            numChanges = 0;
                Boolean collision = false;
                //while there isnt a collision
                while(collision == false)
                {
                    //store the old x coordinate
                    int tempX = b.x;
                    if(tempX + b.width + 1 <= width)
                    {
                        //increase the y by one, then test if there are collisions
                        b.x = tempX + 1;
                        //if there  are collisions, change the y back
                        if(b.checkCollision(boxes))
                        {
                            b.x = tempX;
                            collision = true;
                        }
                        else
                        {
                            numChanges++;
                        }
                    }
                    else
                    {
                        collision = true;
                    }
                }
        }
    }

    //a method that gets a random assortment of boxes and drops them from the top of the stack
    public static ArrayList<Box> tetrisShift()
	{
	    ArrayList<Box> boxesCopy = boxes;
		ArrayList<Box> movedBoxes = new ArrayList<>();
		Random random1 = new Random();
		int currentHighest = getHighest(boxesCopy);
		int x = 0;
		int rand1 = random1.nextInt(boxesCopy.size());
		Box nextBox = boxesCopy.get(rand1);
		//while we can fit for boxes on the row
		while((x + nextBox.width) < width)
		{
		    //if(rand1 % 2 == 0) nextBox.rotate();

            //set the new y to the highest previous point
			nextBox.y = currentHighest;
			//set the new x as far left as possible
			nextBox.x = x;
			//get the x value for the next box
			x += nextBox.width;
			//remove the box from the original list so it isnt chosen again
			boxesCopy.remove(nextBox);
			//add it to a new list so it can be re added later
			movedBoxes.add(nextBox);
			//get another random box
			rand1 = random1.nextInt(boxes.size());
			nextBox = boxes.get(rand1);


        }
		//add the moved boxes back to the original list
		for(Box b: movedBoxes)
		{
			boxesCopy.add(b);
		}

		rightDownLeft();
		return boxesCopy;
	}

    public static ArrayList<Box> LargeSearch(ArrayList<Box> boxesCopy)
    {
        ArrayList<Box> currentSolution = new ArrayList<Box>();
        ArrayList<Box> globalOptimum = boxesCopy;
        {
            for(int i = 0; i< 10000; i++)
            {
                currentSolution = tetrisShift();
                if(accepted(currentSolution, boxesCopy))
                {
                    boxesCopy = currentSolution;
                }
                if(getHighest(currentSolution) < getHighest(globalOptimum))
                {
                    globalOptimum = currentSolution;
                }

            }
            return globalOptimum;
        }
    }

    public static boolean accepted(ArrayList<Box> currentSolution, ArrayList<Box> boxesCopy)
    {
        if(getHighest(currentSolution) < getHighest(boxesCopy))
        {
            return true;
        }
        return false;
    }

	// Shuffles the top box to the right, down as far as possible and then left as far as possible
	public static void rightDownLeft(){
        int preHeight = getHighest(boxes) / scale;
        //System.out.println("RDL Pre height: " + preHeight);
        System.out.print(".");

        //get the top box
        Box top = getHighestBox();
        int oldX = top.x;
        int oldY = top.y;

        //move it to the right of the object
        shuffleRight(top);

        //move it as far down and then left as possible
        top.x = width - top.width;

        Box collidingBox = top.getCollision(boxes);

        //move it up until there are no collisions
        while(collidingBox != null){
            top.y += collidingBox.height;
            collidingBox = top.getCollision(boxes);
        }

        //move down and left
        shuffleDown();

        //System.out.println("RDL Post height: " + (getHighest(boxes) / scale));

        if (preHeight > (getHighest(boxes) / scale)){ rightDownLeft();}
        else return;
	}

	
	public static int getHighest(ArrayList<Box> boxesCopy)
	{
		int currentHighest = 0;
		for(Box b:boxes)
		{
			if(b.y + b.height > currentHighest)
			{
				currentHighest = b.y + b.height;
			}
		}
		return currentHighest;
	}

    public static Box getHighestBox()
    {
        int currentHighest = 0;
        Box highestBox = boxes.get(0);
        for(Box b:boxes)
        {
            if(b.y + b.height > currentHighest)
            {
                currentHighest = b.y + b.height;
                highestBox = b;
            }
        }
        return highestBox;
    }

    public static class BoxDrawer extends JPanel{


//        @Override
//        public Dimension getPrefferedSize(){
//            return new Dimension(width - 10, getHighest() + 50);
//        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            this.setBackground(Color.LIGHT_GRAY);

            for(int i =0; i< boxes.size(); i++)
            {
                g.setColor(Color.WHITE);
                g.fillRect(boxes.get(i).x, boxes.get(i).y, boxes.get(i).width, boxes.get(i).height);
                g.setColor(Color.BLACK);
                g.drawRect(boxes.get(i).x, boxes.get(i).y, boxes.get(i).width, boxes.get(i).height);
            }
        }
    }
}
