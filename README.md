# 556-Assignment-1

Language: JAVA
Metaheuristic Algorithm of Choice: Variable Neighboorhood Search

General:
  Maybe allow some invalid
  Has to be touching another box (except first box)

Ideas for aspects:
  Number of Neighbourhoods
  
Problem Representation:
  Box size: read into array width and height
  Box Location: 2D Array X, Y, one big arraylist
  X,Y coordinates initially, move to visualising boxes graphically somehow if time
  
Tetris Fill - Move flat rows to bottom and Leave alone.
Naieve - place next box on right of previous one.
Shift Down and Left - As far down and Left as possible.
Random - Place anywhere below height and if it doesnt fit just place as near as possible.
Left Floor, Right Ceiling - Biggest fit from left on floor then biggest fit from right on ceiling.
fill width - keep adding things to a total number, subtract width, take the one with the smallest difference

Global Variables.
  
Methods:
  GetData - H
    Reads in the box dimensions
  MinimiseBoxHeight - H
    Rotate boxes so that height is minimal
  Initial - C
    Generate initial solution
  NeighbourhoodChange
    slide 9 VNS
    Able to rotate boxes to fit gap
  Shake
    slide 9 VNS
  BestImprovement
    slide 9 VNS
  OutputResult - C
  
Termination:
  CPU/Wall Clock Time if present neighborhood contain no improvement
