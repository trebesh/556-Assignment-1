

//a class that contains a single method to loop through the position array and output the position of each box
public class Result {

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