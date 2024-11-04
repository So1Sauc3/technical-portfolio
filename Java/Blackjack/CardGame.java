/*
 *  CardGame.java
 * 
 *  cs project for fun lol :D
 */

import java.util.ArrayList;
import java.util.Scanner;

public class CardGame 
{
	public static void main(String[] args) 
	{		
		Scanner scan = new Scanner(System.in);
		int bal = 10000;
		System.out.println("build 1.1.0\n");
		System.out.println("DISCLAIMER:");
		System.out.println("This game is best played in a windows powershell or command prompt window.");
		System.out.println("The method used to clear the window of text is named cls() in CardGame.java (line 368),");
		System.out.println("and to my knowledge does NOT function properly in JCreator's captured build output.");
		System.out.println("The game will still run fine, but may look slightly visually messy!");
		System.out.println("To get JCreator output in a cmd prompt, click on [Configure] tab and then [options]");
		System.out.println("Enjoy!                                                              -R.\n");
		System.out.println("[Press ANY to continue]");
		String acknowledge = scan.nextLine();
		
		boolean done = false;
		while (!done)
		{
			// display menu of choices
			cls();
			System.out.println("<<<[Card Games Menu]>>>");
			System.out.println("+---------------------+");
			System.out.println("| Blackjack [NEW]  (b)|");
			System.out.println("| Poker     [WIP]  (p)|");
			System.out.println("| (idk)     [WIP]  (i)|");
			System.out.println("| <EXIT>           (x)|");
			System.out.println("+---------------------+");
			System.out.printf("Current Balance: %d\n", bal);
			
			// game choosing
			System.out.print("[Menu choice]: ");
			String choice;
			try { // kinda scuffed when exiting blackjack game, low priority, will figure out exception handling later lol
				choice = scan.nextLine();
			} catch (Exception e) { choice = ""; }
			System.out.println();
			switch(choice) {
				case "b":
					cls();
					blackJack(bal);
					break;
				case "p":
					cls();
					System.out.println("glhf (poker code goes here)");
					System.out.println("[Press ANY to continue]");
					acknowledge = scan.nextLine();
					break;
				case "i":
					cls();
					System.out.println("idk");
					System.out.println("[Press ANY to continue]");
					acknowledge = scan.nextLine();
					break;
				case "x":
					cls();
					done = true;
					break;
				default:
					cls();
					System.out.println("[Invalid option selected]\n");
					break;
			}
		}
		System.out.println("[Thank you for playing!]");
		scan.close();
	}
	
	// BLACKJACK GAME
	public static void blackJack(int balance) {
		// input
		Scanner s = new Scanner(System.in);
		int bal = balance;
		// building deck arraylists
		Deck mainDeck = new Deck();
		mainDeck.buildBJDeck();
		Deck dDeck = new Deck();
		Deck pDeck = new Deck();
		Deck pDeck2 = new Deck();
		
		// start menu logic (add shuffle option)
		boolean start = false;
		while (!start) {
			// setup menu
			System.out.println("+===WELCOME TO BLACKJACK!===+");
			System.out.println("A default main deck of 52 cards (no jokers) has been generated.");
			System.out.println("It is generated sorted by default, please shuffle cards before playing.");
			System.out.println("Each shuffle also gives a little tidbit of information on it!");
			System.out.println("Once I figure out what the heck is wrong with my merge sort I'll add it here :D");
			System.out.println("(90% sure it's just some ArrayList shenanigans that break b/c I originally wrote the sort with arrays in mind)");
			System.out.println("+=========[Options]=========+");
			System.out.println("|Show cards              (c)|");
			System.out.println("|Deck info              (iD)|");
			System.out.println("|Game info              (iG)|");
			System.out.println("|Sort by value          (sV)|");
			System.out.println("|Faro shuffle           (fF)|");
			System.out.println("|Swap shuffle           (fS)|");
			System.out.println("|Fischer Yates shuffle  (fY)|");
			System.out.println("|start                   (s)|");
			System.out.println("+===========================+");
			System.out.println("[Choice]:");
			
			String choice = s.nextLine();
			System.out.println();
			switch(choice) {
				case "c":
					cls();
					System.out.println(mainDeck.getNames());
					System.out.println("Before the game starts, the dealer usually shows the SORTED deck ONCE to prove there isn't any card tampering.\n");
					break;
				case "iD":
					cls();
					System.out.println("The generated blackjack deck has 52 cards in total, with no jokers.");
					System.out.println("Aces have internal value of -2, which is recognized and converted to either 11 or 1 during sum function.");
					System.out.println("Suites are represented by #, $, %, and &, which have char values of 35-38 respectively.");
					System.out.println("This makes generating card names and suites automatically MUCH easier! (see Deck.java bjSum() method)\n");
					break;
				case "iG":
					cls();
					System.out.println("Basic Blackjack Rules & Stuff:");
					System.out.println("round progression - bet money, deal, hit/stand/split/double, dealer turn, round ends");
					System.out.println("Player - get cards' sum as close to sum of 21 without going over");
					System.out.println("Dealer - draws cards until sum >= 16, stands on all 17's");
					System.out.println("BLACKJACK - exact sum of 21, pays 3 to 2 (1.5x bet); other wins pay 2 to 1 (2x bet)");
					System.out.println("Hit - draws a card");
					System.out.println("Double - draws a card, doubles bet, ends player turn");
					System.out.println("Stand - ends player turn");
					System.out.println("Split - Only able to use when dealt 2 duplicate cards; splits deck into 2, plays each deck separately, allowing double payout!\n");
					break;
				case "sV":
					cls();
					mainDeck.setCards(mainDeck.sortValue(mainDeck.getCards(), 0, mainDeck.getCards().size()-1));
					System.out.println("Cards successfully sorted by blackjack value!");
					System.out.println(mainDeck.getNames()+"\n[In actual game this card list display will be hidden, this is mostly for debug]\n");
					break;
				case "fF":
					cls();
					mainDeck.shuffleFaro();
					System.out.println(mainDeck.getNames()+"\n[In actual game this card list display will be hidden, this is mostly for debug]\n");
					break;
				case "fS":
					cls();
					mainDeck.shuffleSwap();
					System.out.println(mainDeck.getNames()+"\n[In actual game this card list display will be hidden, this is mostly for debug]\n");
					break;
				case "fY":
					cls();
					mainDeck.shuffleYate();
					System.out.println(mainDeck.getNames()+"\n[In actual game this card list display will be hidden, this is mostly for debug]\n");
					break;
				case "s":
					cls();
					System.out.println("NEW BLACKJACK GAME STARTED");
					start=true;
					break;
				default:
					cls();
					System.out.println("[Invalid option selected]\n");
					break;
			}
		}
		// game code starts here
		updateBJTable(0, bal, dDeck, pDeck, pDeck2, true);
		
		boolean gameEnd = false;
		while (!gameEnd) {
			int bet = 0;
			boolean dealt = false;
			boolean split = false;
			boolean swap = false;
			boolean pTurn = true;
			
			// if mainDeck is running low
			if (mainDeck.getCards().size()<5) {
				System.out.println("WARNING! MAIN DECK SIZE LOW (<5 cards)");
				System.out.println("[Add new shuffled deck? (y/n)]");
				String choice = s.nextLine();
				switch(choice) {
					case "y": {
						Deck mainDeck2 = new Deck();
						mainDeck2.buildBJDeck();
						mainDeck2.shuffleFaro();
						mainDeck2.shuffleSwap();
						mainDeck2.shuffleYate();
						while (mainDeck2.getCards().size()>0) mainDeck.addCard(mainDeck2.drawCard());
						System.out.println("[Successfully added new deck to shooter!]");
						break;
					}
					default: {
						System.out.println("[Proceed at own risk! (will do exception handling later)]");
						break;
					}
				}
			}
			
			// player turn
			while (pTurn) {
				String choice = s.nextLine();
				switch(choice) {
					// bet money
					case "b": {
						if (!dealt) {
							System.out.println("[Bet Amount]:");
							try {
								int reqBet = s.nextInt();
								if (reqBet<=bal) {
									// transfer bet from balance
									bet+=reqBet;
									bal-=reqBet;
									updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
								} else {
									// not enough money
									updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
									System.out.println("[Insufficient Funds]");
								}
							} catch (Exception e) { System.out.println("[Invalid]"); }
						} else {
							updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
							System.out.println("[Round already started]");
						}
						break;
					}
					// deal cards
					case "deal": {
						if (bet>0) {
							// draw cards
							dDeck.addCard(mainDeck.drawCard());
							dDeck.addCard(mainDeck.drawCard());
							pDeck.addCard(mainDeck.drawCard());
							pDeck.addCard(mainDeck.drawCard());
							updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
							System.out.println("[Round started]");
							dealt = true;
						} else {
							updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
							System.out.println("[Bet not entered]");
						}
						break;
					}
					// hit
					case "h": {
						if (dealt) {
							if (!swap) pDeck.addCard(mainDeck.drawCard());
							else pDeck2.addCard(mainDeck.drawCard());
							updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
							System.out.println("[Hit!]");
							int pSum;
							if (!swap) pSum = pDeck.bjSum();
							else pSum = pDeck2.bjSum();
							if (pSum==21) {
								bal+=1.5*bet;
								updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
								System.out.println("[BLACKJACK!]");
								if (!swap && split) swap=true;
								else pTurn = false;
							} else if (pSum>21) {
								updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
								System.out.println("[BURST!]");
								if (!swap && split) swap=true;
								else pTurn = false;
							}
						} else {
							updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
							System.out.println("[Can't use this right now]");
						}
						break;
					}
					// stand
					case "s": {
						if (dealt) {
							updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
							System.out.println("[Stand!]");
							if (!swap && split) swap=true;
							else pTurn = false;
						} else {
							updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
							System.out.println("[Can't use this right now]");
						}
						break;
					}
					// double
					case "d": {
						if (dealt) {
							bal-=bet;
							bet*=2;
							if (!swap) pDeck.addCard(mainDeck.drawCard());
							else pDeck2.addCard(mainDeck.drawCard());
							updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
							System.out.println("[Double down!]");
							if (!swap && split) swap=true;
							else pTurn = false;
						} else {
							updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
							System.out.println("[Can't use this right now]");
						}
						break;
					}
					// split
					case "p": {
						if (dealt && !split && pDeck.getCards().size()==2 && pDeck.getCard(0).getName().charAt(0)==(pDeck.getCard(1).getName().charAt(0))) {
							pDeck2.addCard(pDeck.drawCard());
							updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
							System.out.println("[Split!]");
							split = true;
						} else {
							updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
							System.out.println("[Can't use this right now]");
						}
						break;
					}
					// leave blackjack game, currently throws NoSuchElement Exception when returning to main menu, will fix later
					case "EXIT": {
						updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
						System.out.println("[Game has been set to END after this round!]");
						gameEnd=true;
						break;
					}
					// peak at cards
					case "CHEAT": {
						updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
						System.out.println("[Debug (or CHEAT)!]");
						System.out.println(mainDeck.getNames());
						break;
					}
					// input error
					default: {
						updateBJTable(bet, bal, dDeck, pDeck, pDeck2, true);
						break;
					}
				} // end of switch case
			} // end of player turn
			
			// dealer turn
			updateBJTable(bet, bal, dDeck, pDeck, pDeck2, false);
			System.out.println("[Dealer's turn]");
			System.out.println("[Press ANY to continue]");
			String choice = s.nextLine();
			while (!pTurn && dDeck.bjSum()<17 && dDeck.bjSum()<pDeck.bjSum() && pDeck.bjSum()<21) {
				dDeck.addCard(mainDeck.drawCard());
				updateBJTable(bet, bal, dDeck, pDeck, pDeck2, false);
				if (dDeck.bjSum()==21) System.out.println("[DEALER BLACKJACK]");
				else if (dDeck.bjSum()>21) System.out.println("[DEALER BURST]");
				else System.out.println("[DEALER DREW]");
				System.out.println("[Press ANY to continue]");
				choice = s.nextLine();
			}
			
			// end round

			// default
			System.out.println("[Round finished!]");
			if (dDeck.bjSum()>pDeck.bjSum() && dDeck.bjSum()<22 || pDeck.bjSum()>21) {
				System.out.println("[Dealer wins!]");
			} else if (pDeck.bjSum()<22 && pDeck.bjSum()!=dDeck.bjSum()) {
				System.out.println("[Player wins!]");
				if (pDeck.bjSum()!=21) bal+=2*bet;
			} else {
				System.out.println("[Push!(tied)]");
				bal+=bet;
			}

			// if split scenario
			if (swap) {
				System.out.println("[Round finished!]");
				if (dDeck.bjSum()>pDeck2.bjSum() && dDeck.bjSum()<22 || pDeck2.bjSum()>21) {
					System.out.println("[Dealer wins! (hand 2)]");
				} else if (pDeck2.bjSum()<22 && pDeck2.bjSum()!=dDeck.bjSum()) {
					System.out.println("[Player wins! (hand 2)]");
					if (pDeck.bjSum()!=21) bal+=2*bet;
				} else {
					System.out.println("[Push!(tied hand 2)]");
					bal+=bet;
				}
			}
			
			// reset some stuff for final display before new round
			dDeck = new Deck();
			pDeck = new Deck();
			pDeck2 = new Deck();
			System.out.println("[Press ANY to continue]");
			choice = s.nextLine();
			updateBJTable(0, bal, dDeck, pDeck, pDeck2, true);
		}
		// end of game
		s.close();
	}
	
	// clears console screen
	public final static void cls() {
		try {
       		if (System.getProperty("os.name").contains("Windows")) new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        	else Runtime.getRuntime().exec("clear");
    	} catch (Exception e) {}
	}
	
	// displays BJ table
	public static void updateBJTable(int bet, int bal, Deck deal, Deck play, Deck play2, boolean hide) {
		// generate card "images" from decks
		Deck dealFinal = new Deck();
		if (hide && deal.getCards().size()!=0) dealFinal.addCard(deal.getCard(0));
		else for (int i=0; i<deal.getCards().size(); i++) dealFinal.addCard(deal.getCard(i));
		
		String[] dCards = dealFinal.genCardImg();
		if (hide && deal.getCards().size()!=0) {
			dCards[0]+="+---+ ";
			dCards[1]+="|XXX| ";
			dCards[2]+="|XXX| ";
			dCards[3]+="+---+ ";
		}
		String[] pCards = play.genCardImg();
		String[] pCards2 = play2.genCardImg();
		
		// implement dynamic options menu later (reveal options only if applicable)
		String opt0 = "Hit     (h)";
		String opt1 = "Stand   (s)";
		String opt2 = "Double  (d)";
		String opt3 = "Split   (p)";
		String opt4 = ""/*"Insure  (i)"*/; // I might add insure later, but it's low priority b/c I barely ever see it get played
		String[] options = {opt0, opt1, opt2, opt3, opt4};
		
		// displaying or hiding dealer's deck sum
		String dealSum = "";
		if (hide) dealSum = "??";
		else dealSum = ""+deal.bjSum();

		// displaying or hiding split deck 2's sum
		String splitSum = "";
		if (play2.getCards().size()!=0) splitSum = "Player's 2nd Hand (Sum: "+play2.bjSum()+")";
		
		// displaying or hiding blackjack
		String bj = "";
		if (play.bjSum()==21 || play2.bjSum()==21) bj = "!PLAYER BLACKJACK!";
		if (deal.bjSum()==21 && !hide) bj = "!DEALER BLACKJACK!";
		
		// chip rack disaplay, implement dynamic updates later
		String c0 = "@@@@";
		String c1 = "@@@";
		String c2 = "@@";
		String c3 = "@";
		String[] chips = {c0, c1, c2, c3};

		// building the table
		cls();
		System.out.printf("+=====+========================================================================+=====+\n");
		System.out.printf("|X----| [][]    []        []      [][]  []  []  [][][]    []      [][]  []  [] |----X|\n");
		System.out.printf("|-X---| []  []  []      []  []  []      []  []    []    []  []  []      []  [] |---X-|\n");
		System.out.printf("|--X--| [][]    []      [][][]  []      [][]      []    [][][]  []      [][]   |--X--|\n");
		System.out.printf("|---X-| []  []  []      []  []  []      []  []    []    []  []  []      []  [] |-X---|\n");
		System.out.printf("|----X| [][]    [][][]  []  []    [][]  []  []  []      []  []    [][]  []  [] |X----|\n");
		System.out.printf("|X----+==[Money]==+==[Type (deal) to DEAL]=====================================+----X|\n");
		System.out.printf("|-X---|%11s|%60s|---X-|\n", "Bet(b):    ", "Dealer's Hand (Sum: "+dealSum+")");
		System.out.printf("|--X--|%11s|%60s|--X--|\n", "$"+bet, dCards[0]); // bet and bal | dealer card image
		System.out.printf("|---X-|%11s|%60s|-X---|\n", "", dCards[1]);
		System.out.printf("|----X|%11s|%60s|X----|\n", "Balance:   ", dCards[2]);
		System.out.printf("|X----|%11s|%60s|----X|\n", "$"+bal, dCards[3]);
		System.out.printf("|-X---|%11s|%60s|---X-|\n", "", "");
		System.out.printf("|--X--+%11s+%60s|--X--|\n", "==[Chips]==", "Player's Hand (Sum: "+play.bjSum()+")");
		System.out.printf("|---X-|%11s|%60s|-X---|\n", chips[0], pCards[0]); // options menu | player card image
		System.out.printf("|----X|%11s|%60s|X----|\n", chips[1], pCards[1]);
		System.out.printf("|X----|%11s|%60s|----X|\n", chips[2], pCards[2]);
		System.out.printf("|-X---|%11s|%60s|---X-|\n", chips[3], pCards[3]);
		System.out.printf("|--X--+%11s+%60s|--X--|\n", "=[Options]=", splitSum);
		System.out.printf("|---X-|%11s|%60s|-X---|\n", options[0], pCards2[0]); // blank | split scenario player card image
		System.out.printf("|----X|%11s|%60s|X----|\n", options[1], pCards2[1]);
		System.out.printf("|X----|%11s|%60s|----X|\n", options[2], pCards2[2]);
		System.out.printf("|-X---|%11s|%60s|---X-|\n", options[3], pCards2[3]);
		System.out.printf("|--X--|%11s|%60s|--X--|\n", "", "");
		System.out.printf("+---X-+===========+============================================================+-X---+\n");
		System.out.printf("|----X|%11s+%60s|X----|\n","",bj);
		System.out.printf("+=====+===========+============================================================+=====+\n");
		System.out.printf("[Choice]:\n");
	}
}