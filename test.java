import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class test {
    public static final int scale = 4;
    //Buffered Reader - read line by line
    public static final int width = 100 * scale;
    
    public static void main(String [] args)
    {
	ArrayList<Box> boxes = new ArrayList<>();
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

        getData(filePath, boxes);

        initialSolution(boxes);
        boxes = LargeSearch(boxes);

        result(boxes);
        int totalHeight = getHighest(boxes) / scale;
        
        System.out.println("TotalHeight: " + totalHeight);
        System.out.println("Number of Boxes: " + boxes.size());
        JFrame f = new JFrame("Boxes");
        BoxDrawer display = new BoxDrawer();
        display.setArray(boxes);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(display);
        f.setSize(width, getHighest(boxes) + 50);

        f.setVisible(true);
    }

    public static void initialSolution(ArrayList<Box> boxes)
    {
        int totalWidth = 0;
        int totalHeight = 0;
        int nextWidth = 0;
        int nextHighest = 0;
        int nextHeight = 0;
        ArrayList<Point> spaceArray = new ArrayList<Point>();
        //loop through the list of boxes
        for(int i = 0; i<boxes.size(); i++)
        {
            //get the nextbox in the list
            Box current = boxes.get(i);
            Boolean spaceFound = false;
            //get its height and width
            nextHeight = current.getHeight();
            nextWidth = current.getWidth();
            for(Point space:spaceArray)
            {
                if(current.getWidth() <= space.x)
                {
                    current.setX(width - space.x);
                    current.setY(space.y);
                    if(!(current.checkCollision(boxes)))
                    {
                        spaceFound = true;
                        space.x = width - (current.getX() + current.getWidth());
                        break;
                    }
                }
                current.rotate();
                if(current.getWidth() <= space.x)
                {
                    current.setX(width - space.x);
                    current.setY(space.y);
                    if(!(current.checkCollision(boxes)))
                    {
                        spaceFound = true;
                        space.x = width - (current.getX() + current.getWidth());
                        break;
                    }
                }
                current.rotate();
            }
            if(spaceFound == false)
            {
                //if it can fit in the current line
                if((totalWidth + nextWidth) <= width)
                {
                    //add it at this height
                    current.setX(totalWidth);
                    current.setY(totalHeight);
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
                    current.rotate();
                    if((totalWidth + nextWidth) <= width)
                    {
                        //add it at this height
                        current.setX(totalWidth);
                        current.setY(totalHeight);
                        //increase the total width for this height
                        totalWidth += nextWidth;
                        //test if this is the tallest block on the row
                        if(nextHeight > nextHighest)
                        {
                            //if it is, store the height
                            nextHighest = nextHeight;
                        }
                    }
                    else
                    {
                        Point space = new Point(width - totalWidth, totalHeight);
                        spaceArray.add(space);
                        current.rotate();
                        //reset the width, and go to height of the tallest box
                        totalWidth = 0;
                        totalHeight+=nextHighest;
                        current.setX(totalWidth);
                        current.setY(totalHeight);
                        totalWidth += nextWidth;
                        nextHighest = nextHeight;
                    }
                }
            }

        }
        shuffleDown(boxes);
    }

    public static void shuffleDown(ArrayList<Box> boxes)
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
            }
        }

    }

    public static ArrayList<Box> LargeSearch(ArrayList<Box> boxes)
    {
        ArrayList<Box> currentSolution = new ArrayList<Box>();
        ArrayList<Box> globalOptimum = boxes;
        {
            for(int i = 0; i< 10000; i++)
            {
                System.out.print(".");
                currentSolution = shift(boxes);
                if(accepted(currentSolution, boxes))
                {
                    boxes = currentSolution;
                }
                if(getHighest(currentSolution) < getHighest(globalOptimum))
                {
                    globalOptimum = currentSolution;
                }

            }
            return globalOptimum;
        }
    }

    public static ArrayList<Box> shift(ArrayList<Box> boxes)
    {
        ArrayList<Box> newList = boxes;
        Random random1 = new Random();
        for(int i=0; i<3;i++)
        {
		    int rand1 = random1.nextInt(boxes.size() - 1);
            Box moveBox = newList.get(rand1);
            newList.remove(moveBox);
            newList.add(moveBox);
        }
        initialSolution(newList);
        return newList;
    }

    public static boolean accepted(ArrayList<Box> currentSolution, ArrayList<Box> boxes)
    {
        if(getHighest(currentSolution) < getHighest(boxes))
        {
            return true;
        }
        return false;
    }

    public static BufferedReader BR;
    //The line
    public static String line;
    //Valid file extension
    public static String csv = "csv";

    // GETDATA ---------------------------------------------------------------------------------------------------------
    // Reads the data in from the provided csv file
    public static void getData(String filePath, ArrayList<Box> boxes)
    {
        try{
            BR = new BufferedReader(new FileReader(filePath));
            //Until the end of the file is reached
            while ((line = BR.readLine()) != null){
                //Split the line into the list of items
                String[] stringDim = line.split(",");

                Box b = new Box((Integer.parseInt(stringDim[0])) * scale, (Integer.parseInt(stringDim[1])) * scale);
                b.minimizeHeight();
                boxes.add(b);
            }
        }catch (IOException e){
            System.out.println("IO ERROR in GETDATA" + e.getMessage());
        }catch (Exception e){
            System.out.println("ERROR in GETDATA" + e.getMessage());
        }
        return;
    }

    public static class BoxDrawer extends JPanel
    {
        ArrayList<Box> boxes;
        public void setArray(ArrayList<Box> boxesCopy)
        {
            boxes = boxesCopy;
        }
    
         public void paintComponent(Graphics g){
        super.paintComponent(g);
            this.setBackground(Color.LIGHT_GRAY);
        
            for(int i =0; i< boxes.size(); i++)
            {
                g.setColor(Color.WHITE);
                g.fillRect(boxes.get(i).getX(), boxes.get(i).getY(), boxes.get(i).getWidth(), boxes.get(i).getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(boxes.get(i).getX(), boxes.get(i).getY(), boxes.get(i).getWidth(), boxes.get(i).getHeight());
            }
        }
    }

    public static void result(ArrayList<Box> boxes)
    {
        int c = 1;
        for (Box b:boxes) {
            //writes each box to the console on separate lines
            System.out.println("Box " + c + " is at position: " + (b.getX()/scale) + ", " + (b.getY()/scale));
            c++;
        }

    }

    public static int getHighest(ArrayList<Box> boxes)
	{
		int currentHighest = 0;
		for(Box b:boxes)
		{
			if(b.getY() + b.getHeight() > currentHighest)
			{
				currentHighest = b.getY() + b.getHeight();
			}
		}
		return currentHighest;
	}
}