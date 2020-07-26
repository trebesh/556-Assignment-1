package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GetData {
    // VARIABLES -------------------------------------------------------------------------------------------------------
    //An item
    public static ArrayList<Integer> box;
    //List of the items
    public static ArrayList<ArrayList<Integer>> boxes = new ArrayList<ArrayList<Integer>>();
    //Buffered Reader - read line by line
    public static BufferedReader BR;
    //The line
    public static String line;
    //Valid file extension
    public static String csv = "csv";

    // MAIN ------------------------------------------------------------------------------------------------------------
    public static void main(String[] args)
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
}
