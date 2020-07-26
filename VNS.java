/*
  Main Class for calling functions to Implement Variable Neighbourhood Search
  
  ConnorFergusson_1299038_HannahTrebes_1306378
*/

 
public class VNS{
	public static void main(String [] args)
	{
    	public static ArrayList<Integer> box;
    //List of the items
    public static ArrayList<ArrayList<Integer>> boxes = new ArrayList<ArrayList<Integer>>();
    //Buffered Reader - read line by line
public static final int width = 15;
    
    getData();
    initialSolution();
    reuslt();
    
  }
	
	// VARIABLES -------------------------------------------------------------------------------------------------------
    //An item
    
    public static BufferedReader BR;
    //The line
    public static String line;
    //Valid file extension
    public static String csv = "csv";

    // MAIN ------------------------------------------------------------------------------------------------------------
    public static void getData()
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
    }

    // GETDATA ---------------------------------------------------------------------------------------------------------
    // Reads the data in from the provided csv file
    public static void getData(String filePath){
        try{
            BR = new BufferedReader(new FileReader(filePath));
            //Until the end of the file is reached
            while ((line = BR.readLine()) != null){
                box = new ArrayList<Integer>();
                //Split the line into the list of items
                String[] stringDim = line.split(",");

                //Rotate the box 90 degrees if it's taller than it is wide
                if(Integer.parseInt(stringDim[0]) < Integer.parseInt(stringDim[1])){
                    box.add(Integer.parseInt(stringDim[1]));
                    box.add(Integer.parseInt(stringDim[0]));
                }
                else{
                    box.add(Integer.parseInt(stringDim[0]));
                    box.add(Integer.parseInt(stringDim[1]));
                }

                // Add the box to the list of all boxes
                boxes.add(box);
            }
            //Testing message
            for(int i = 0; i < boxes.size(); i++){
                System.out.println(boxes.get(i).get(0)+ " " + boxes.get(i).get(1));
            }
        }catch (IOException e){
            System.out.println("IO ERROR in GETDATA" + e.getMessage());
        }catch (Exception e){
            System.out.println("ERROR in GETDATA" + e.getMessage());
        }
        return;
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
        for(int i = 0; i<box.size(); i++)
        {
            //get the nextbox in the list
            ArrayList<Integer> current = box.get(i);
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
        int size = pos.size();
        //loops through the position array
        for(int i = 0; i<size; i++)
        {
            //writes each box to the console on separate lines
            System.out.println("Box " + Integer.toString(i + 1) + " is at position: " + pos.get(i).get(0) + ", " + pos.get(i).get(1));
        }
    }
}
