import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class test {

    //List of the items
    public static final int scale = 5;
    //Buffered Reader - read line by line
    public static final int width = 40 * scale;
    public static final int kmax = 3;
    public static final int tmax = 1000;
    public static BufferedReader BR;
    //The line
    public static String line;
    //Valid file extension
    public static String csv = "csv";

    public static void main(String[] args) {
        ArrayList<Box> boxes = new ArrayList<Box>();
        //Check that a file path is valid
        if (args.length == 0) {
            System.out.println("Error: File Path needed as argument");
            return;
        } else if (!csv.equals(args[0].substring(args[0].length() - 3))) {
            System.out.println("ERROR: File not in csv format");
            return;
        }

        String filePath = args[0];

        getData(filePath, boxes);

        initialSolution(boxes);
        boxes = BVNS(boxes);

        result(boxes);
        int totalHeight = getHighest(boxes) / scale;

        System.out.println("TotalHeight: " + totalHeight);
        System.out.println("Number of Boxes: " + boxes.size());

        JFrame f = new JFrame("Boxes");
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

    }

    public static void initialSolution(ArrayList<Box> boxes)
    {
        int currentX = 0;
        int currentY = 0;
        int nextX = 0;
        int nextY = 0;
        int tallestBox = 0;

        for(int i=0; i<boxes.size(); i++)
        {
            Box current = boxes.get(i);
            nextX = currentX + current.getWidth();
            nextY = currentY + current.getHeight();

            if(nextX <= width)
            {
                current.setX(currentX);
                current.setY(currentY);
                currentX = nextX;
                if(tallestBox < nextY)
                {
                    tallestBox = nextY;
                }
            }
            else
            {
                currentX = 0;
                currentY = tallestBox;
                i--;
            }
        }
        shuffleDown(boxes);
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
            initialSolution(localSearch);
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
            Random random2 = new Random();
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
        initialSolution(shakeBoxes);
        return shakeBoxes;
    }

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
                int totalHeight = getHighest(boxes) / scale;

            }

        }catch (IOException e){
            System.out.println("IO ERROR in GETDATA" + e.getMessage());
        }catch (Exception e){
            System.out.println("ERROR in GETDATA" + e.getMessage());
        }
        return;
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
                shuffleLeft(b, boxes);
            }
        }

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

    //a method that writes the position of of each box to the console
    public static void result(ArrayList<Box> boxes)
    {
        int c = 1;
        for (Box b:boxes) {
            //writes each box to the console on separate lines
            System.out.println("Box " + c + " is at position: " + (b.getX()/scale) + ", " + (b.getY()/scale));
            c++;
        }

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

    public static class BoxDrawer extends JPanel
    {

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

            for(int i =0; i< boxes.size(); i++)
            {
                g.setColor(Color.WHITE);
                g.fillRect(boxes.get(i).getX(), boxes.get(i).getY(), boxes.get(i).getWidth(), boxes.get(i).getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(boxes.get(i).getX(), boxes.get(i).getY(), boxes.get(i).getWidth(), boxes.get(i).getHeight());

            }
        }
    }

    public static boolean accepted(ArrayList<Box> currentSolution, ArrayList<Box> boxes)
    {
        if(getHighest(currentSolution) < getHighest(boxes))
        {
            return true;
        }
        return false;
    }
}
