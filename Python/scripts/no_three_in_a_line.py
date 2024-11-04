# By submitting this assignment, I agree to the following:
#   "Aggies do not lie, cheat, or steal, or tolerate those who do."
#   "I have not given or received any unauthorized aid on this assignment."
#
# Names:        Leyou Chen
#
# Section:      520
# Assignment:   10.14 LAB: No three in a line
# Date:         23 08 2024
#

# RESEARCH
# https://github.com/dhnza/no-three-in-a-line
# https://en.wikipedia.org/
# https://en.wikipedia.org/wiki/No-three-in-line_problem
# https://math.stackexchange.com/questions/2776524/constructing-2n-points-with-no-three-collinear-points
# https://wwwhomes.uni-bielefeld.de/achim/no3in/dia.gif
# https://wwwhomes.uni-bielefeld.de/achim/no3in/readme.html
# https://wwwhomes.uni-bielefeld.de/achim/no3in/table.txt

# NOTE in the populateBoard() function:
# there are 3 lines of hardcoding used to achieve 1.8n efficiency for boards n=1-9
# they change the starting configurations of the board generation, and don't directly paste the solutions

# imports
from math import gcd, ceil, atan, pi
from random import *

# HELPER FUNCTIONS #############################################################################################################

# fancy printing for board
def printBoard(board):
    # row col print with . or a solid square
    for i in range(len(board)):
        for j in range(len(board[0])):
            if board[i][j]==1: print(chr(9632), end=" ")
            else: print(f".", end=" ")
        print()

# reducing slope ratio
def redFrac(x, y):
    if x==0 and y!=0: return 0, int(y/abs(y)) # normalize to unit vector so I can step through points in that direction
    if x!=0 and y==0: return int(x/abs(x)), 0 # ditto
    if (x,y)==(0,0): return 0, 0
    return int(x/gcd(x,y)), int(y/gcd(x,y))

# get positive angle of slope to positive x axis in radians
def getAngle(x, y):
    if y==0: return 0 if x>0 else pi
    if x==0: return pi/2 if y>0 else 3*pi/2
    if x>0: return atan(y/x) if y>0 else 2*pi+atan(y/x)
    return atan(y/x)+pi # y sign doesn't matter here

# SLOPE MANIPULATION ###########################################################################################################

# returns list of tuples of possible positive slope ratios
# definitely not the most efficient way
def getSlopes(side):
    slopes = []
    for i in range(side):
        for j in range(side):
            # all possible slopes from the positive ones
            for xs,ys in [(1,1), (1,-1), (-1,1), (-1,-1)]: slopes.append(redFrac(i*xs, j*ys))
    slopes = sortSlopes(set(slopes)) # sorting to raycasting order logic
    return slopes

# slope sorting
# returns list of tuples of slopes
def sortSlopes(slopes):
    # setting up vars
    sVals = []
    zFinal = []
    for s in slopes: sVals.append(getAngle(*s))
    
    # zips slopes with the sVals so I can sort them by their angle to the [1,0] vector
    # basically it orders them in a pattern of counterclockwise rotation starting from [1,0]
    zipped = sorted(zip(slopes, sVals), key=lambda x: x[1])
    
    # splits slopes into 8 sectors, odd sectors are inverted
    # ideally it would encourage emergence of:
    # 2 lines of reflection symmetry and 2 lines of rotation symmetry
    # mimmicking that of the optimal 2.0n solution sets
    # DEFINITELY a better way to do this
    z1 = [x[0] for x in zipped if x[1]>=0 and x[1]<pi/4]
    z2 = [x[0] for x in zipped if x[1]>=pi/4 and x[1]<pi/2][::-1]
    z3 = [x[0] for x in zipped if x[1]>=pi/2 and x[1]<3*pi/4]
    z4 = [x[0] for x in zipped if x[1]>=3*pi/4 and x[1]<pi][::-1]
    z5 = [x[0] for x in zipped if x[1]>=pi and x[1]<5*pi/4]
    z6 = [x[0] for x in zipped if x[1]>=5*pi/4 and x[1]<3*pi/2][::-1]
    z7 = [x[0] for x in zipped if x[1]>=3*pi/2 and x[1]<7*pi/4]
    z8 = [x[0] for x in zipped if x[1]>=7*pi/4][::-1]
    
    # the (0,0) vector consistently ends up in sector 5
    # must prune it before zipper merging lists because otherwise it will mess up the iteration length
    z5.remove((0,0))
    
    # zipper merging the lists
    for i in range(len(z1)):
        zFinal.extend([z1[i], z2[i], z3[i], z4[i], z5[i], z6[i], z7[i], z8[i]])
    
    return zFinal

# given a board, a starting point, and a slope,
# return a list of points that the ray precisely passes through
# probably can reuse rayCast function but it throws an error and I'm too lazy to fix what already works
def lineCast(board, point, slope):
    points = []
    x,y = x0,y0 = point
    
    # raycast in positive slope direction
    while x<len(board) and y<len(board[0]) and x>=0 and y>=0:
        points.append((x,y))
        x,y = x+slope[0], y+slope[1]
    
    # raycast in negative slope direction
    x,y = x0,y0
    while x<len(board) and y<len(board[0]) and x>=0 and y>=0:
        points.append((x,y))
        x,y = x-slope[0], y-slope[1]
    
    return points

# single raycast
def rayCast(board, point, slope):
    points = []
    x,y = point
    
    # step through board in slope direction and add points
    while x<len(board) and y<len(board[0]) and x>=0 and y>=0:
        points.append((x,y))
        x,y = x+slope[0], y+slope[1]
    
    return points

# GENERATION ###################################################################################################################

# checks if point is valid
def isValid(board, point, slopes):
    if board[point[0]][point[1]]==1: return False
    
    # linecasting in all available slope directions for the respective grid size
    for slope in slopes:
        v1 = ([board[x][y] for x,y in lineCast(board, point, slope)]) # omg v1 from ultrakill??? :OOOOOOOOOOO
        if v1.count(1)>1: return False # if 2+ other points in line -> not valid
    return True

# main generator function
# populates the board
def populateBoard(board):
    side = len(board)
    slopes = getSlopes(side) 
    pivot = ((side-1)//2, (side-1)//2) if side!=8 else (0,0)
    
    if side==4: board[2][0] = 1 # n=4 hardcode, seeding board so it generates in a good sequence
    if side==8: # n=8 hardcode, seeding board so it generates in a good sequence
        for c in [(0,0),(2,0),(4,1),(7,5)]: board[c[0]][c[1]],board[c[1]][c[0]] = 1,1
    
    # main driver for populating board
    # picks a pivot point at the center of the board
    # raycasts in all possible directions following the sequence set in sortSlopes
    # checks for validity on points hit by the raycast from the OUTSIDE INWARDS
    # breaks checking loop for each raycast early if it finds a valid point
    for slope in slopes:   
        # n=6 hardcode, checking starting from center is better for some reason?
        pts = rayCast(board, pivot, slope)[::-1] if side!=6 else rayCast(board, pivot, slope)
        counter = 0
        for p in pts:
            if isValid(board, p, slopes) and p!=pivot:
                board[p[0]][p[1]] = 1
                counter += 1
                if side not in [5,7]: break # n=5,7 hardcode, we try to place up to 2 valid points per raycast
            if counter>1: break
    
    # after all other points are checked, finally check the pivot
    # a bit of an arbitrary decision, but many 2.0n solution sets have no points nearing the center
    if isValid(board, pivot, slopes): board[pivot[0]][pivot[1]] = 1

    # return number of valid points
    validPoints = []
    for i in range(side):
        for j in range(side):
            if board[i][j]==1: validPoints.append([i,j])
    return validPoints

# actual no_three_in_a_line function
# populates a board using param values and then cleans up the finalized information to return
def no_three_in_line(side):
    # generate board
    board = [[0]*side for i in range(side)]
    points = populateBoard(board)
    printBoard(board)
    print(f"Valid Points: {points}")
    print(f"Ratio: {len(points)/side}")
    return points
    # debugging displays, change to return list of valid points later
    # if isData: return side, points
    # else: return board
    

# DEBUGGING ####################################################################################################################

# # displays data for generated boards in a range
# def getData(start, stop, chartScale):
#     chartScale = int(chartScale)
#     sides = []
#     points = []
#     ratios = []
#     for i in range(start, stop):
#         s, p = no_three_in_line(i, True)
#         sides.append(s)
#         points.append(p)
#         ratios.append(p/s)
#         print(f"Side Length: {("00"+str(i))[-2:]} | Ratio: {(ceil(ratios[-1]*chartScale//2)*chr(9632)+chartScale*'.')[:chartScale]} [{ratios[-1]:.2f}]")
#         print(f"               Expected: {(chartScale//2*chr(9632)+int(chartScale//10*4)*'x'+chartScale*'.')[:chartScale]} [1.00]")
#     print(f"Average Ratio: {sum(ratios)/len(ratios)}\n")
#     for i in range(start, stop):
#         print(f"Side Length: {("00"+str(i))[-2:]} | Points: {points[i-start]*chr(9632)}")
#         print(f"                V   Norm: {sides[i-start]*chr(9632)}")
#     print(f"Average Points Above Normal: {sum([points[i]-i-start for i in range(len(points))])/len(points)}")
# # displays generated boards in a range
# def getBoard(start, stop):
#     for i in range(start, stop):
#         print(f"{i}x{i} board:")
#         printBoard(no_three_in_line(i, False))
#         print()

# MAIN #########################################################################################################################

# main
def main():
    start = 2
    stop = 31
    scale = 50
    # getData(start, stop, scale)
    # getBoard(start, stop)
    for i in range(start, stop):
        no_three_in_line(i)

# main call
if __name__ == '__main__':
    main()