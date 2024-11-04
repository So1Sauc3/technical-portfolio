'''
  []  []  []  [][][][][]  [][][][][]      [][][]    [][][][][]
  []  []  []  []              []        []      []      []
  []  []  []  [][][][]        []        []      []      []
  []  []  []  []              []        []    []        []
    []  []    [][][][][]  [][][][][]      [][]  []  [][][][][]

CHANGELOG (also accessible in game main menu):
Veerion 1.5.0:
 - changed board UI from 3x3 to 19x19 and added 4 sides of row/col numbers
 - added handicap starting conditions, yet to implement specific allowed handicap starting patterns
Version 1.4.0:
 - changed grid numbering to start at 1, and moved column #s to the top
 - added autoplay features, currently random moves only
 - default board back color is now BLACK for high contrast and better visibility
 - tweaked a bit of menu logic
Version 1.3.0
 - added capture message, prints out list of captured pieces and piece that captured them's coords
 - removed some commented out debug print statements
 - added color customization to certain prompt texts and board background
 - updated game guide with proper information
 - changed "press" to "input" for better readability
 - renamed some variables to make more sense with P1/P2 instead of Black/White
Version 1.2.0:
 - Renamed black and white to p1 and p2 for better coherence with custom color settings
 - spelling and grammer corrections
 - minor code optimization and cleanup
 - fixed a few exceptions
'''

import os
import time, random
from colorama import Fore, Back # do 'pip install colorama' if you don't already have it

# default settings
boardSize = 19
menuSize = 15
cText = Fore.RESET
cTrim = Fore.RESET
sym1 = chr(9632)
sym2 = chr(9632)
symH = chr(9675)
cP1 = Fore.BLACK
cP2 = Fore.RESET
bBoard = Back.BLACK
auto = False
autoDelay = 0.25
autoMax = 100
handicap = 0

cD = Fore.RESET
bD = Back.RESET

# board stones symbols
sX = f"{cText}{'.'}{cD}"
sH = f"{cText}{symH}{cD}"
s1 = f"{cP1}{sym1}{cD}" # 9675 for hollow, 9679 for solid
s2 = f"{cP2}{sym2}{cD}"

# for converting color names to color codes
foreDict = {
    "BLACK": Fore.BLACK,
    "WHITE": Fore.WHITE,
    "RED": Fore.RED,
    "YELLOW": Fore.YELLOW,
    "GREEN": Fore.GREEN,
    "CYAN": Fore.CYAN,
    "BLUE": Fore.BLUE,
    "MAGENTA": Fore.MAGENTA,
    "NONE": Fore.RESET
}
backDict = {
    "BLACK": Back.BLACK,
    "WHITE": Back.WHITE,
    "RED": Back.RED,
    "YELLOW": Back.YELLOW,
    "GREEN": Back.GREEN,
    "CYAN": Back.CYAN,
    "BLUE": Back.BLUE,
    "MAGENTA": Back.MAGENTA,
    "NONE": Back.RESET
}

# tracking game wins
win1 = 0
win2 = 0

# DEF clear terminal screen, helper function
def cls(): os.system('cls' if os.name=='nt' else 'clear')

# DEF clamp number, helper function
def clamp(n, small, large): return max(small, min(n, large))

# DEF get key from value in the dictionary, helper function
def get_key(val, dict):
    for key, value in dict.items():
        if val == value: return key
    return "key doesn't exist"

# DEF display game guide text
def displayGuide():
    print(f"{cText}WELCOME TO GO! Now with PIECE CAPTURING USING AN AMAZING TOTALLY NOT ALGORITHMICALLY INEFFICIENT RECURSIVE FLOOD FILL")
    print("Starting conditions and ending conditions and robust winner determiner NOT included :/")
    print(f"{cTrim}|{cText} Start a new game in main menu by inputting '{cTrim}n{cText}' for new game.")
    print(f"{cTrim}|{cText} When in game, P1 (black by default) starts first, and enters a comma-separated pair of integers for their move.")
    print(f"{cTrim}|{cText} Make sure to format it exactly as '{cTrim}x,y{cText}' where x represents the row and y represents the column.")
    print(f"{cTrim}|{cText} Don't worry if you mistype though! The program will handle that for you... (hopefully)")
    print(f"{cTrim}|{cText} Input '{cTrim}EXIT{cText}' in all caps to exit the game without changing the winner counter.")
    print(f"{cTrim}|{cText} Input '{cTrim}FINISH{cText}' in all caps to end the game during the turn and determine the winner with the current board.")
    print(f"{cTrim}|{cText} Currently, the winner is determined ONLY by {cTrim}amount{cText} of stones on the board, not including any 'claimed' empty territories.")
    print(f"{cTrim}|{cText} Maybe I will flood fill my way to a more robust winner determiner in the future...")
    print(f"{cTrim}|{cText} As a final note, please consider messing around with the {cTrim}settings{cText}! They have cool formatting options with more to come!")
    print(f"ENJOY!!!!")
    print(f"Input any {cTrim}key{cText} to continue...{cD}")

# DEF display changelog text
def displayChangelog():
    print(f"{cText}CHANGES:{cD}")
    print(f"{cTrim}1.5.0{cD}")
    print(f"{cTrim}-{cText} changed board UI from 3x3 to 19x19 and added 4 sides of row/col numbers")
    print(f"{cTrim}-{cText} added handicap starting conditions, yet to implement specific allowed handicap starting patterns")
    print(f"{cTrim}1.4.0{cD}")
    print(f"{cTrim}-{cText} changed grid numbering to start at 1, and moved column #s to the top")
    print(f"{cTrim}-{cText} added autoplay features, currently random moves only")
    print(f"{cTrim}-{cText} default board back color is now BLACK for high contrast and better visibility")
    print(f"{cTrim}-{cText} tweaked a bit of menu logic")
    print(f"{cTrim}1.3.0{cD}")
    print(f"{cTrim}-{cText} added capture message, prints out list of captured pieces and piece that captured them's coords")
    print(f"{cTrim}-{cText} removed some commented out debug print statements")
    print(f"{cTrim}-{cText} added color customization to certain prompt texts")
    print(f"{cTrim}-{cText} updated game guide with proper information")
    print(f"{cTrim}-{cText} changed 'press' to 'input' for better readability")
    print(f"{cTrim}-{cText} renamed some variables to make more sense with P1/P2 instead of Black/White")
    print(f"{cTrim}1.2.0{cD}")
    print(f"{cTrim}-{cText} Renamed black and white to p1 and p2 for better coherence with custom color settings")
    print(f"{cTrim}-{cText} spelling and grammer corrections")
    print(f"{cTrim}-{cText} minor code optimization and cleanup")
    print(f"{cTrim}-{cText} fixed a few exceptions")
    print(f"Input any {cTrim}key{cText} to continue...{cD}")

# DEF display settings menu
def displaySettings():
    print(f"NOTE: {Fore.RED}C{Fore.YELLOW}O{Fore.GREEN}L{Fore.CYAN}O{Fore.BLUE}R{Fore.MAGENTA}A{Fore.BLACK}M{cD}A IS REQUIRED FOR TERMINAL COLOR DISPLAY. INSTALL WITH 'pip install colorama'")
    print(f"SETTINGS:")
    print(f"[0] Menu Size: [{menuSize}]\t\t(default=15. NOTE: size<13 breaks text fitting in menu as some text are too long. Game runs fine.)")
    print(f"[1] Board Size: [{boardSize}x{boardSize}]\t\t(default=19. NOTE: size<3 breaks a tiny bit of board formatting. Game runs fine.)")
    print(f"[2] Text Color: [{cText+get_key(cText, foreDict)+cD}]")
    print(f"[3] Trim Color: [{cTrim+get_key(cTrim, foreDict)+cD}]")
    print(f"[4] Player 1 Color: [{cP1+get_key(cP1, foreDict)+cD}]")
    print(f"[5] Player 2 Color: [{cP2+get_key(cP2, foreDict)+cD}]")
    print(f"[6] Board Color: [{bBoard+get_key(bBoard, backDict)+bD}]\t(default=BLACK. NOTE: many background colors will diminish visibility of players' colors.)")
    print(f"[7] Auto Play: [{auto}]\t\t(default=FALSE (0=FALSE, 1=TRUE). NOTE: currently this will only make random inputs, no calculated plays included!)")
    print(f"[8] Auto Play Delay: [{autoDelay}]\t(default=0.25. NOTE: setting it to 0 will the game to lag a little when autoplaying. Use at own risk!)")
    print(f"[9] Auto Play Max Moves: [{autoMax}]\t(default=50. NOTE: setting it to 0 will cause autoplay to terminate immediately!)")
    print(f"[10] Handicap Moves: [{handicap}]\t(default=0, between [0,9]. NOTE: amount of extra moves P1 has, must be played on the handicap spots '{symH}')")
    print(f"Color Options: {Fore.BLACK}BLACK, {cD}WHITE, {Fore.RED}RED, {Fore.YELLOW}YELLOW, {Fore.GREEN}GREEN, {Fore.BLUE}BLUE, {Fore.MAGENTA}MAGENTA, {Fore.CYAN}CYAN{cD}")
    print(f"To edit board/menu size, input '1:N' where N is the new board/menu side length/width")
    print(f"To edit colors, input 'X:COLOR' where X is the setting number and COLOR is the new color")
    print("To exit settings, input 'EXIT'")

# DEF display main menu text
def displayMenu(win1, win2):
    # menu title and settings, may pull settings into its own page later
    print(f"{cTrim}+"+"-"*menuSize+f"+{cD}")
    print(f"{cTrim}|{cD}"+f"{cText}GO BOARD GAME{cD}".center(menuSize+10)+f"{cTrim}|{cD}")
    print(f"{cTrim}+"+"-"*menuSize+f"+{cD}")
    
    
    # wins display currently mostly unreachable unless you take time to fill the entire board
    print(f"{cTrim}|{cD}"+f"{cP1}P1{cText} Wins:    {win1}{cD}".center(menuSize+15)+f"{cTrim}|{cD}")
    print(f"{cTrim}|{cD}"+f"{cP2}P2{cText} Wins:    {win2}{cD}".center(menuSize+15)+f"{cTrim}|{cD}")
    print(f"{cTrim}+"+"-"*menuSize+f"+{cD}")
    
    # menu options
    print(f"{cTrim}|{cD}"+f"{cText}SETTINGS:   S{cD}".center(menuSize+10)+f"{cTrim}|{cD}")
    print(f"{cTrim}|{cD}"+f"{cText}GAME GUIDE: G{cD}".center(menuSize+10)+f"{cTrim}|{cText} <- PLEASE READ{cD}")
    print(f"{cTrim}|{cD}"+f"{cText}NEW GAME:   N{cD}".center(menuSize+10)+f"{cTrim}|{cD}")
    print(f"{cTrim}|{cD}"+f"{cText}CHANGELOG:  C{cD}".center(menuSize+10)+f"{cTrim}|{cD}")
    print(f"{cTrim}|{cD}"+f"{cText}EXIT GAME:  E{cD}".center(menuSize+10)+f"{cTrim}|{cD}")
    print(f"{cTrim}+"+"-"*menuSize+f"+{cD}")

# DEF display board being played
def displayBoard(board, turn):
    # title
    print(f"{cTrim}+"+(boardSize*2+7)*"-"+f"+{cD}")
    print(f"{cTrim}|{cD}"+f"{cText}GO BOARD {boardSize}X{boardSize}{cD}".center(boardSize*2+17)+f"{cTrim}|{cD}")
    print(f"{cTrim}+--+"+(boardSize*2+1)*"-"+f"+--+{cD}")
    
    # top col numbers
    print(f"{cTrim}|{cD}{cText}XX{cD}{cTrim}|{cD}",end=" ")
    for i in range(boardSize): print(f"{cText if i%2==0 else cTrim}{(str(i+1)+" ")[:2]}{cD}", end="")
    print(f"{cTrim}|XX|{cD}")
    print(f"{cTrim}+--+"+(boardSize*2+1)*"-"+f"{cTrim}+--+{cD}")
    
    # actual board
    for i in range(boardSize):
        print(f"{cTrim}|{cD}{cText}{(" "+str(i+1))[-2:]}{cTrim}|{cD}{bBoard}", end=" ")
        for j in range(boardSize): print(f"{(board[i][j])}", end=" ")
        print(f"{bD}{cTrim}|{cText}{(str(i+1)+" ")[:2]}{cTrim}|{cD}")
    print(f"{cTrim}+--+"+(boardSize*2+1)*"-"+f"{cTrim}+--+{cD}")
    
    # top bot numbers
    print(f"{cTrim}|{cD}{cText}XX{cD}{cTrim}|{cD}",end=" ")
    for i in range(boardSize): print(f"{cText if i%2==0 else cTrim}{(str(i+1)+" ")[:2]}{cD}", end="")
    print(f"{cTrim}|XX|{cD}")
    print(f"{cTrim}+--+"+(boardSize*2+1)*"-"+f"{cTrim}+--+{cD}")
    
    # turn and player
    player = f"{cP1}P1{cD}"
    if turn%2==0: player = f"{cP2}P2{cD}"
    print(f"{cTrim}|{cD}"+f"{cText}Turn: {turn} ({player}{cText}){cD}".center(boardSize*2+32)+f"{cTrim}|{cD}")
    print(f"{cTrim}+"+(boardSize*2+7)*"-"+f"+{cD}")


# THE GRAND ALMIGHTY WEI QI EATING ALGORITHM
# right now this can result in a player self-eating their pieces, eg. blundering
# might add check later to prevent this but honestly I might leave it in because it's funny - R.C.
def updateBoard(board, turn, coords, hMoves):
    # catching invalid input
    try: x, y = [eval(i) for i in coords.split(",")]
    except: return False
    
    # converting display coords to 2D list coords
    x-=1
    y-=1
    if x<0 or x>=boardSize or y<0 or y>=boardSize: return False
    
    if hMoves and board[x][y]!=sH: return ""
    
    # has open space
    if board[x][y] in [sX,sH]:
        # boolean logic for self-eating
        isEating = False
        isEaten = True
        
        # capture messages
        output = "\n"
        
        # determining player and opponent piece colors
        if turn%2==1: sPlayer = s1
        else: sPlayer = s2
        sOpp = s2 if sPlayer==s1 else s1
        
        # placing piece
        board[x][y] = sPlayer
        
        # checking if placed piece causes any opponent pieces to be eaten
        for dx, dy in [(-1,0), (1,0), (0,-1), (0,1)]:
            if x+dx<boardSize and x+dx>=0 and y+dy<boardSize and y+dy>=0:
                if board[x+dx][y+dy] != sOpp: isEaten = False # updating danger of being eaten. DO NOT CLAMP because it will push checker onto the piece trying to be updated
                sList = []
                floodCheck(board, x+dx, y+dy, board[x+dx][y+dy], sList) # recursing adjacent spaces
                sList = list(filter(None, sList))
                # if no air spaces found -> part is DEAD
                if -1 not in sList:
                    isEating = True
                    if sList != []:
                        output += f"{cText}{board[sList[0][0]][sList[0][1]]} piece(s) at {cTrim}{[[n+1 for n in row] for row in sList]}{cText} eaten by {sPlayer} {cTrim}[{x+1},{y+1}]{cD}\n(+{len(sList)} point(s) to {sPlayer})\n"
                    for s in sList:
                        try: board[s[0]][s[1]] = sX
                        except: pass
                else: pass # -> part is ALIVE
        # self-eating case, currently only checks if you are self-eating the piece you placed
        if not isEating and isEaten:
            board[x][y] = sX
            return ""
        else: return output
    else: return ""

# flood fill algorithm, modified to return list of 'flooded' spaces as potential pieces for eating, -1 for air
def floodCheck(board, x, y, sPlayer, stones):
    # check if part is air
    if board[x][y] == sX: return -1
    # same part, recurse
    if board[x][y] == sPlayer and [x,y] not in stones:
        stones.append([x,y])
        for dx, dy in [(-1,0), (1,0), (0,-1), (0,1)]:
            stones.append(floodCheck(board, clamp(x+dx,0,boardSize-1), clamp(y+dy,0,boardSize-1), sPlayer, stones))

# <> MAIN PROGRAM LOOP ----------------------------------------------------------------------------------------------------------------------------------------------------------</>
inErr = False
while True:
    cls()
    menuPrompt = f"{cText}CHOICE: {cD}"
    # if inErr: menuPrompt = f"{cText}INVALID INPUT. TRY AGAIN: {cD}" # currently always defaults to invalid input when switching back to main menu
    
    # MAIN MENU ##################################################################################################
    # display menu
    displayMenu(win1, win2)
    choice = input(menuPrompt).upper()
    
    # SETTINGS ###################################################################################################
    if choice=="S":
        inErr = False
        userIn = ""
        while userIn.upper()!="EXIT":
            cls()
            # print settings text
            displaySettings()
            userIn = input().upper()
            # updating settings variables
            try:
                inputPair = userIn.split(":")
                match inputPair[0]:
                    case "0": menuSize = int(inputPair[1])
                    case "1": boardSize = int(inputPair[1])
                    case "2": cText = foreDict[inputPair[1]]
                    case "3": cTrim = foreDict[inputPair[1]]
                    case "4":
                        cP1 = foreDict[inputPair[1]]
                        s1 = f"{cP1}{sym1}{cD}"
                    case "5":
                        cP2 = foreDict[inputPair[1]]
                        s2 = f"{cP2}{sym2}{cD}"
                    case "6": bBoard = backDict[inputPair[1]]
                    case "7": auto = bool(int(inputPair[1]))
                    case "8": autoDelay = float(inputPair[1])
                    case "9": autoMax = int(inputPair[1])
                    case "10": handicap = clamp(int(inputPair[1]),0,9)
            except: pass
    
    # GAME GUIDE #################################################################################################
    if choice=="G":
        inErr = False
        cls()
        # print user guide text
        displayGuide()
        userIn = input()
    
    # CHANGELOG ##################################################################################################
    if choice=="C":
        inErr = False
        cls()
        # print changelog text
        displayChangelog()
        userIn = input()
    
    # START NEW GAME #############################################################################################
    elif choice=="N":
        # keeping track of input error for prompt display
        inErr = False
        
        # initializing board
        board = [[sX for i in range(boardSize)] for j in range(boardSize)]
        for i in range(1,4):
            for j in range(1,4):
                board[boardSize*i//4][boardSize*j//4] = sH
        
        # game variables
        turn = 1
        exitGame = ""
        moveErr = False
        succeed = ""
        hMoves = handicap
        
        # main gameplay loop
        while not exitGame:
            # display board
            cls()
            displayBoard(board, turn)
            
            # reporting any captures/dead pieces from last iteration
            if succeed and succeed!="\n": print(succeed)
            succeed = ""
            
            # persisting move error message from last iteration
            prompt = f"{cText}ENTER MOVE {cTrim}[ROW,COL]{cText}: {cD}"
            if moveErr: prompt = f"{cText}INVALID MOVE. TRY AGAIN: {cD}"
            
            # attempting move
            if auto and turn<=autoMax:
                if hMoves: userIn = str(boardSize*random.randint(1,3)//4+1)+","+str(boardSize*random.randint(1,3)//4+1)
                else: userIn = str(random.randint(1,boardSize))+","+str(random.randint(1,boardSize))
                time.sleep(autoDelay)
            else: userIn = input(prompt)
            if userIn.upper() in ["EXIT", "FINISH"]: exitGame = succeed = "exiting game..." # treated as true in binary logic
            else: succeed = updateBoard(board, turn, userIn, hMoves)
            
            # increment turn count
            if succeed:
                if hMoves: hMoves -= 1
                else: turn += 1
                moveErr = False
            else: moveErr = True
            
            # finish game and determine winner if asked or if board full
            if userIn.upper()=="FINISH" or sum(row.count(sX) for row in board)<=1:
                exitGame = True
                # doesn't account for "claimed" territories, only stones currently on the board
                # IDEAS FOR COUNTING TERRITORIES:
                # for each empty spot on the board, flood fill and count the number of pieces of each player bordering the spot
                # if p1 pieces > p2 pieces for that border, p1 claims the territory, vice versa p2 claims territory, else stay empty
                # add resolved empty territory spots to a list, don't count them again in the for loop
                sum1 = sum(row.count(s1) for row in board)
                sum2 = sum(row.count(s2) for row in board)
                if sum1 > sum2:
                    win1 += 1
                    print(f"{cP1}P1{cText} WINS! {cP1}{sum1}{cText} pt(s) to {cP2}{sum2}{cText} pt(s){cD}")
                elif sum1 < sum2:
                    win2 += 1
                    print(f"{cP2}P2{cText} WINS! {cP1}{sum1}{cText} pt(s) to {cP2}{sum2}{cText} pt(s){cD}")
                else: print(f"{cText}DRAW!{cD}")
                finalInput = input(f"{cText}Input any key to continue...{cD}")
    
    # EXIT program ###############################################################################################
    elif choice=="E":
        inErr = False
        cls()
        
        print(f"{cText}Thanks for playing! -R.{cD}")
        break
    
    # place for catch-all in main menu ###########################################################################
    else: inErr = True