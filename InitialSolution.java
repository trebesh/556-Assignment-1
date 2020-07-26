import java.util.*;

//a class that generates an initial solution but placing each box next the previous one starting bottom left, if it would reach the edge of the sheet, 
//moves back to start position but at a height equal to the tallest box
public class InitialSolution
{
    //declares a variable for the width of the sheet
    public static final int width = 15;
    //arrays for storing the list of boxes and the positions of those boxes
    public static ArrayList<ArrayList<Integer>> boxes = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<ArrayList<Integer>> pos = new ArrayList<ArrayList<Integer>>();
    public static void main(String[] args)
    {
        
        int totalWidth = 0;
        int totalHeight = 0;
        int nextWidth = 0;
        int nextHighest = 0;
        int nextHeight = 0;

        //declares a set of boxes for testing
        boxes.add(new ArrayList<Integer>(Arrays.asList(2,5)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(7,3)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(1,8)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(8,2)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(5,5)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(2,5)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(7,3)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(1,8)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(8,2)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(5,5)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(2,5)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(7,3)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(1,8)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(8,2)));
        boxes.add(new ArrayList<Integer>(Arrays.asList(5,5)));

        //loop through the list of boxes
        for(int i = 0; i<boxes.size(); i++)
        {
            //get the nextbox in the list
            ArrayList<Integer> current = boxes.get(i);
            //get its height and width
            nextHeight = current.get(1);
            nextWidth = current.get(0);
            
            //if it can fit in the current line
            if((totalWidth + nextWidth) <= width)
            {
                //add it at this height
                pos.add(new ArrayList<Integer>(Arrays.asList(totalWidth, totalHeight)));
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
                pos.add(new ArrayList<Integer>(Arrays.asList(totalWidth, totalHeight)));
                totalWidth += nextWidth;
                nextHighest = nextHeight;
            }
            
        }
        //display the positions of each box and the total height
        totalHeight+=nextHighest;
        Result.result();
        System.out.println(Integer.toString(totalHeight));
    }
}