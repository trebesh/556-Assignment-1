/*
  Main Class for calling functions to Implement Variable Neighbourhood Search
  
  ConnorFergusson_1299038_HannahTrebes_1306378
*/

 
public class VNS{
	public static void main(String [] args)
	{
    	public ArrayList[][] boxes = new ArrayList[][2];
    public ArrayList[][] pos = new ArrayList[][2];
public static final int width = 15;
    
    getData();
    rotateBoxes();
    initialSolution();
    reuslt();
    
  }
	
	//declares a variable for the width of the sheet
   

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
        result();
        System.out.println(Integer.toString(totalHeight));
    }
	
	//a method that writes the position of of each box to the console
    public static void result()
    {
        //stores the size of the position array
        int size = InitialSolution.pos.size();
        //loops through the position array
        for(int i = 0; i<size; i++)
        {
            //writes each box to the console on separate lines
            System.out.println("Box " + Integer.toString(i + 1) + " is at position: " + InitialSolution.pos.get(i).get(0) + ", " + InitialSolution.pos.get(i).get(1));
        }
    }
}
