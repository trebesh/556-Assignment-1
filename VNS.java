package com.company;
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
import java.lang.reflect.Array;
import java.util.*;

public class VNS{

    //List of the items
    public static final int scale = 5;
    //Buffered Reader - read line by line
    public static final int width = 40 * scale;
    public static final int kmax = 3;
    public static final int tmax = 10;
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

        newSolution(boxes);
        boxes = BVNS(boxes);

        result(boxes);
        int totalHeight = getHighest(boxes) / scale;

        System.out.println("TotalHeight: " + totalHeight);
        System.out.println("Number of Boxes: " + boxes.size());

        //drawBoxes(boxes);

    }


    // VARIABLES -----------------------if--------------------------------------------------------------------------------
    //An item

    public static BufferedReader BR;
    //The line
    public static String line;
    //Valid file extension
    public static String csv = "csv";

    public static void  drawBoxes(ArrayList<Box> boxes, String title){
        JFrame f = new JFrame(title);
        BoxDrawer display = new BoxDrawer();
        f.setVisible(true);

        display.setArray(boxes);
//        JScrollPane scrollPane = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        JScrollBar bar = scrollPane.getVerticalScrollBar();
//        f.add(scrollPane, BorderLayout.RIGHT);
//        f.add(bar);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(display);
        f.setSize(width, getHighest(boxes) + 50);
        //bar.setPreferredSize(new Dimension(10, f.getHeight()));

        return;
    }

    // GETDATA ---------------------------------------------------------------------------------------------------------
    // Reads the data in from the provided csv file
    public static void getData(String filePath, ArrayList<Box> boxes){
        try{
            BR = new BufferedReader(new FileReader(filePath));
            int counter = 0;
            //Until the end of the file is reached
            while ((line = BR.readLine()) != null){
                //Split the line into the list of items
                String[] stringDim = line.split(",");

                Box b = new Box((Integer.parseInt(stringDim[0])) * scale, (Integer.parseInt(stringDim[1])) * scale);
                b.minimizeHeight();

                // Order the boxes by area
                int area = b.getArea();
                if (counter == 0)
                {
                    boxes.add(b);
                }
                else
                {
                    for (int i = 0; i<boxes.size(); i++)
                    {
                        if (area >= boxes.get(i).getArea())
                        {
                            boxes.add(i, b);
                            break;
                        }
                        else if(i+1 == boxes.size())
                        {
                            boxes.add(b);
                            break;
                        }
                    }
                }
                counter = 1;


                //boxes.add(b);
                int totalHeight = getHighest(boxes) / scale;

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

    public static void newSolution(ArrayList<Box> boxes)
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
            nextHeight = current.getHeight();
            nextWidth = current.getWidth();

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
                //reset the width, and go to height of the tallest box
                totalWidth = 0;
                totalHeight+=nextHighest;
                current.setX(totalWidth);
                current.setY(totalHeight);
                totalWidth += nextWidth;
                nextHighest = nextHeight;
            }

        }
        //shuffleDown(boxes);
        //display the positions of each box and the total height

        ArrayList<Box> boxesCopy = new ArrayList<Box>();
        for (Box b: boxes) {
            boxesCopy.add(new Box(b.width, b.height));
            boxesCopy.get(boxesCopy.size() - 1).x = b.x;
            boxesCopy.get(boxesCopy.size() - 1).y = b.y;
        }
        findGaps(boxes);
    }

    //a method that writes the position of of each box to the console
    public static void result(ArrayList<Box> boxes)
    {
        int c = 1;
        for (Box b:boxes) {
            //writes each box to the console on separate lines
            System.out.println("Box " + c + " is at position: " + (b.getX()/scale) + ", " + (b.getY()/scale));
            c++;
        }

        drawBoxes(boxes, "Result");

    }
    //shuffles all the boxes down into free space
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
                shuffleLeft(b, boxes);
            }
        }

        //drawBoxes(boxes, "S D");

    }

    public static void shuffleLeft(Box b, ArrayList<Box> boxes)
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
                int tempX = b.getX();
                if(tempX-1 >= 0)
                {
                    //reduce the y by one, then test if there are collisions
                    b.setX(tempX - 1);
                    //if there  are collisions, change the y back
                    if(b.checkCollision(boxes))
                    {
                        b.setX(tempX);
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

    public static void shuffleRight(Box b, ArrayList<Box> boxes)
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
                int tempX = b.getX();
                if(tempX + b.getWidth() + 1 <= width)
                {
                    //increase the y by one, then test if there are collisions
                    b.setX(tempX + 1);
                    //if there  are collisions, change the y back
                    if(b.checkCollision(boxes))
                    {
                        b.setX(tempX);
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

    public static void shuffleBottomUp(ArrayList<Box> boxes){
        for (Box b : boxes) {
            int oldY = b.y;
            b.y = 0;
            while (b.y < oldY){
                if (b.checkCollision(boxes) == true){b.y += 1;}
                else break;
            }
        }
    }

    public static ArrayList<Box> BVNS(ArrayList<Box> boxes)
    {
        int timer = 1;
        while(timer < tmax) {
            System.out.print(".");
            ArrayList<Box> shookBoxes = new ArrayList<Box>();
            ArrayList<Box> bestSolution = new ArrayList<Box>();
            int k = 1;
            for (int i = k; i <= kmax; i++) {
                shookBoxes = shake(boxes, i);
                shookBoxes = localSearch(shookBoxes);
                if (bestSolution.size() <= 0 || getHighest(bestSolution) > getHighest(shookBoxes)) {
                    bestSolution = shookBoxes;
                }
            }

            if (getHighest(boxes) > getHighest(bestSolution)) {
                boxes = new ArrayList<Box>();
                for (Box b : bestSolution) {
                    Box newbox = new Box(b.getWidth(), b.getHeight());
                    newbox.setX(b.getX());
                    newbox.setY(b.getY());
                    boxes.add(newbox);
                }
                System.out.println("boxes height end = " + getHighest(boxes) / scale);
            }
            timer++;
        }
        return boxes;
    }

    public static ArrayList<Box> shake(ArrayList<Box> boxes, int k)
    {
        ArrayList<Box> shakeBoxes = new ArrayList<Box>();
        for(Box b:boxes)
        {
            Box newbox = new Box(b.getWidth(), b.getHeight());
            newbox.setX(b.getX());
            newbox.setY(b.getY());
            shakeBoxes.add(newbox);
        }
        Random random1 = new Random();
        if(k == 1)
        {
            int rand1 = random1.nextInt(shakeBoxes.size() - 1);
            Box moveBox = shakeBoxes.get(rand1);
            shakeBoxes.remove(moveBox);
            shakeBoxes.add(moveBox);
        }
        else if(k==2)
        {
            int rand1 = random1.nextInt(shakeBoxes.size() - 1);
            Box moveBox = shakeBoxes.get(rand1);
            moveBox.rotate();
            shakeBoxes.remove(moveBox);
            shakeBoxes.add(moveBox);
        }
        else {
            int rand1 = random1.nextInt(shakeBoxes.size() - 1);
            Box rotateBox = shakeBoxes.get(rand1);
            rotateBox.rotate();
        }

        newSolution(shakeBoxes);
        return shakeBoxes;
    }


    public static ArrayList<Box> localSearch(ArrayList<Box> boxes)
    {
        ArrayList<Box> currentBest = new ArrayList<Box>();
        ArrayList<Box> localSearch = new ArrayList<Box>();
        for(Box b:boxes)
        {
            Box newbox = new Box(b.getWidth(), b.getHeight());
            newbox.setX(b.getX());
            newbox.setY(b.getY());
            currentBest.add(newbox);
        }
        for(int i = 0; i<100; i++)
        {
            localSearch = new ArrayList<Box>();
            for(Box b:boxes)
            {
                Box newbox = new Box(b.getWidth(), b.getHeight());
                newbox.setX(b.getX());
                newbox.setY(b.getY());
                localSearch.add(newbox);
            }
            Random random1 = new Random();
            int k = random1.nextInt(3);
            if (k == 1) {
                int rand1 = random1.nextInt(localSearch.size() - 1);
                Box moveBox = localSearch.get(rand1);
                localSearch.remove(moveBox);
                localSearch.add(moveBox);
            } else if (k == 2) {
                Random random2 = new Random();
                int rand1 = random1.nextInt(localSearch.size() - 1);
                Box moveBox = localSearch.get(rand1);
                moveBox.rotate();
                localSearch.remove(moveBox);
                localSearch.add(moveBox);
            } else {
                int rand1 = random1.nextInt(localSearch.size() - 1);
                Box rotateBox = localSearch.get(rand1);
                rotateBox.rotate();

            }
            newSolution(localSearch);
            if(getHighest(localSearch) < getHighest(currentBest))
            {
                currentBest = new ArrayList<Box>();
                for(Box b:localSearch)
                {
                    Box newbox = new Box(b.getWidth(), b.getHeight());
                    newbox.setX(b.getX());
                    newbox.setY(b.getY());
                    currentBest.add(newbox);
                }
            }
        }
        return currentBest;
    }

    // Tries to find gaps in the object and fit items into them
    public static ArrayList<Box> findGaps(ArrayList<Box> boxes){
        //Find gaps
        // Gaps stored as an item
        ArrayList<Box> gaps = new ArrayList<Box>();
        Box gap = new Box(scale, scale);
        gap.x = 0;
        gap.y = 0;

        for (Box b : boxes) {
            gap = new Box(1 * scale, 1 * scale);
            gap.x = b.x + b.width + 1;
            gap.y = b.y;

            if (gap.x + gap.width < width){
                //Travel up the side of a box looking for gaps
                while(gap.checkCollision(boxes) && gap.y < b.y + b.height -1 && gap.y < getHighest(boxes)){
                    gap.y++;
                }

                //find the max width
                while (gap.checkCollision(boxes) == false) {
                    gap.width = gap.width  + 1 * scale;
                    if(gap.x + gap.width >= width) break;
                }
                gap.width = gap.width - scale;

                //find the max height
                while (gap.checkCollision(boxes) == false){
                    gap.height = gap.height + 1 * scale;
                    if(gap.y + gap.height >= getHighest(boxes)) break;
                }
                gap.height = gap.height - scale;

                //Add to the list of gaps and reset
                if(gap.width != 0 && gap.height != 0 && gap.getArea() >= boxes.get(boxes.size() - 1).area){
                    gaps.add(gap);
                }
            }
        }

        //drawBoxes(gaps, "Gaps");

        boxes = fillGaps(boxes, gaps);

        return boxes;
    }

    public static ArrayList<Box> fillGaps(ArrayList<Box> boxes, ArrayList<Box> gaps){
        Random rand = new Random();
        int previousHeight = getHighest(boxes);

        //Copy boxes to cope with potential rotation
        ArrayList<Box> boxesCopy = new ArrayList<Box>();
        for (Box b: boxes) {
            boxesCopy.add(new Box(b.width, b.height));
            boxesCopy.get(boxesCopy.size() - 1).x = b.x;
            boxesCopy.get(boxesCopy.size() - 1).y = b.y;
        }

        //System.out.println("BoxesCopy:" + boxesCopy);


        //Go through each gap and try to find a box that will fit in it
        for (Box g : gaps) {
            ArrayList<Integer> fillers = new ArrayList<Integer> ();

            for (Box b : boxesCopy) {
                if (b.fitsIn(g)) {
                    fillers.add(boxesCopy.indexOf(b));
                }
            }

            int upperRand = fillers.size();
            //System.out.println("Filler Size " + fillers.size());
            int rnum = 0;
            if(upperRand != 0){
                int c = 0;
                while (c <= upperRand * 2) {
                    c++;
                    rnum = rand.nextInt(fillers.size());
                    //Move the box
                    Box oldBox = new Box(boxes.get(rnum).width, boxes.get(rnum).height);
                    oldBox.x = boxes.get(rnum).x;
                    oldBox.y = boxes.get(rnum).y;
                    boxes.remove(rnum);
                    boxes.add(rnum, boxesCopy.get(rnum));
                    boxes.get(rnum).x = g.x;
                    boxes.get(rnum).y = g.y;

                    //If this causes an invalidation, move it back
                    if (boxes.get(rnum).checkCollision(boxes) || boxes.get(rnum).x + boxes.get(rnum).width > width /scale || previousHeight < getHighest(boxes)) {
                        boxes.remove(rnum);
                        boxes.add(rnum, oldBox);
                    }
                }
            }

        }

        shuffleBottomUp(boxes);
        shuffleDown(boxes);

        rightDownLeft(boxes);

        return boxes;
    }


//    public static ArrayList<Box> LargeSearch(ArrayList<Box> boxes)
//    {
//        ArrayList<Box> currentSolution = new ArrayList<Box>();
//        ArrayList<Box> globalOptimum = boxes;
//        {
//            for(int i = 0; i< 1000; i++)
//            {
////                currentSolution = shift(boxes);
////                if(accepted(currentSolution, boxes))
////                {
////                    boxes = currentSolution;
////                }
////                if(getHighest(currentSolution) < getHighest(globalOptimum))
////                {
////                    globalOptimum = currentSolution;
////                }
//
//                currentSolution = findGaps(boxes);
//                if(accepted(currentSolution, boxes))
//                {
//                    boxes = currentSolution;
//                }
//                if(getHighest(currentSolution) < getHighest(globalOptimum))
//                {
//                    globalOptimum = currentSolution;
//                }
//
////                currentSolution = rightDownLeft(boxes);
////                if(accepted(currentSolution, boxes))
////                {
////                    boxes = currentSolution;
////                }
////                if(getHighest(currentSolution) < getHighest(globalOptimum))
////                {
////                    globalOptimum = currentSolution;
////                }
//
//            }
//            return globalOptimum;
//        }
//    }

//    public static boolean accepted(ArrayList<Box> currentSolution, ArrayList<Box> boxes)
//    {
//        if(getHighest(currentSolution) < getHighest(boxes))
//        {
//            return true;
//        }
//        return false;
//    }

    // Shuffles the top box to the right, down as far as possible and then left as far as possible
    public static ArrayList<Box> rightDownLeft(ArrayList<Box> boxes){
        int preHeight = getHighest(boxes) / scale;
        //System.out.println("RDL Pre height: " + preHeight);

        //get the top box
        Box top = getHighestBox(boxes);
        //System.out.println("Initial highest Box: " + boxes.indexOf(top));

        //move it to the right of the object
        shuffleRight(top, boxes);

        //move it to the bottom right corner
        top.setX(width - top.getWidth());

        //Check if it's colliding with another box (expected)
        Box collidingBox = top.getCollision(boxes);

        //Move it up until there are no collisions
        while(collidingBox != null){
            top.setY(top.getY() + collidingBox.getHeight());
            collidingBox = top.getCollision(boxes);
        }

        //move down and left
        shuffleBottomUp(boxes);
        shuffleDown(boxes);

        //System.out.println("RDL Post height: " + (getHighest(boxes) / scale));
        //System.out.println("New highest Box: " + boxes.indexOf(getHighestBox(boxes)));

        //if (preHeight > (getHighest(boxes) / scale)){ rightDownLeft(boxes);}
        if (top != getHighestBox(boxes)){rightDownLeft(boxes);}

        //drawBoxes(boxes, "RDL");

        return boxes;
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

    public static Box getHighestBox(ArrayList<Box> boxes)
    {
        int currentHighest = 0;
        Box highestBox = boxes.get(0);
        for(Box b:boxes)
        {
            if(b.getY() + b.getHeight() > currentHighest)
            {
                currentHighest = b.getY() + b.getHeight();
                highestBox = b;
            }
        }
        return highestBox;
    }

    public static class BoxDrawer extends JPanel{

        ArrayList<Box> boxes;
        public void setArray(ArrayList<Box> boxesCopy)
        {
            boxes = boxesCopy;
        }

//        @Override
//        public Dimension getPrefferedSize(){
//            return new Dimension(width - 10, getHighest() + 50);
//        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            this.setBackground(Color.LIGHT_GRAY);

            g.setColor(Color.RED);
            g.fillRect(width, 0, 1, getHighest(boxes));
            g.fillRect(0, getHighest(boxes), width, 1);

            for(int i =0; i< boxes.size(); i++)
            {
                g.setColor(Color.WHITE);
                g.fillRect(boxes.get(i).getX(), boxes.get(i).getY(), boxes.get(i).getWidth(), boxes.get(i).getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(boxes.get(i).getX(), boxes.get(i).getY(), boxes.get(i).getWidth(), boxes.get(i).getHeight());

            }
        }

        public void draw(ArrayList<Box> boxes){

        }
    }
}
