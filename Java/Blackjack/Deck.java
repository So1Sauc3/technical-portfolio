/**
 *	Deck.java
 *
 *	Deck of cards!
 *
 *  *Note: I totally could have just gone with the inheritance route for implementing this,
 *   since this class is basically just a very specific extention of the ArrayList class,
 *   but I don't really like / use inheritance, because it is much more restrictive than 
 *   composition when it comes to a lot of stuff. Feel free to try rewriting this class as an
 *   "extends ArrayList" for fun tho :D (certainly will make some of the smaller methods unnecessary)
 */
 
import java.util.ArrayList;

public class Deck
{
	// card private vars
	private ArrayList<Card> cards;
	
	// building
	public Deck () {
		cards = new ArrayList<Card>();		
	}
	
	// generates a BJ Deck
	public void buildBJDeck () {
		
		/* symbol substitutes b/c console log doesn't display unicode suites
		# char 35, substitute to ?
		$ char 36, substitute to ?
		% char 37, substitute to ?
		& char 38, substitute to ?
		*/
		
		// building aces
		for (int i=35; i<39; i++) cards.add(new Card("A"+(char)i, -2));
		// building number cards
		for (int i=2; i<11; i++) for (int j=35; j<39; j++) cards.add(new Card(""+i+(char)j, i));
		// building face cards
		for (int i=35; i<39; i++) cards.add(new Card("J"+(char)i, 10));
		for (int i=35; i<39; i++) cards.add(new Card("Q"+(char)i, 10));
		for (int i=35; i<39; i++) cards.add(new Card("K"+(char)i, 10));
	}
	
	// adds a card
	public void addCard(Card card) {
		cards.add(card);
	}
	
	// gets a card
	public Card getCard(int i) {
		return cards.get(i);
	}
	
	// draws a card to return
	public Card drawCard() {
		return cards.remove(0);
	}
	
	// returns arraylist of cards
	public ArrayList<Card> getCards() {
		return cards;
	}

	// sets deck to new cards
	public void setCards(ArrayList<Card> newCards) {
		cards = newCards;
	}
	
	// returns string of card names
	public String getNames() {
		String names = "";
		names+="+===============[Cards]================+\n";
		for (int i=0; i<cards.size(); i+=13) {
			for (int j=0; j<13; j++) {
				if (i+j<cards.size()) names+=cards.get(i+j).getName()+" ";
			}
			names+="\n";
		}
		names+="+======================================+\n";
		return names;
	}
	
	// returns string array img of cards
	public String[] genCardImg () {
		String[] img = {"","","",""};
		for (Card fred : cards) {
			img[0]+="+---+ ";
			// *Note: the divide by 1.5 and casting stuff just makes it dynamically update number based on name size
			img[1]+=String.format("|%3s| ", fred.getName().substring(0,(int)(fred.getName().length()/1.5)));
			img[2]+=String.format("|%3s| ", fred.getName().charAt(fred.getName().length()-1));
			img[3]+="+---+ ";
		}
		
		return img;
	}
	
	// returns sum of deck according to blackjack rules
	public int bjSum() {
		int sum = 0;
		int aces = 0;
		for (Card fred : cards) { // filtering out aces
			if (fred.getVal()>0) sum+=fred.getVal();
			else aces++;
		}
		for (int i=0; i<aces; i++) { // dealing with aces
			if (sum+11<22) sum+=11;
			else sum++;
		}
		return sum;
	}
	
	// unfair "shuffle", splits deck and zipper combines, can predict card order
	public void shuffleFaro() {
		System.out.println("unfair \"shuffle\", splits deck and zipper combines, can predict card order");
		ArrayList<Card> left = new ArrayList<Card>();
		ArrayList<Card> right = new ArrayList<Card>();
		for (int i=0; i<cards.size()/2; i++) {
			left.add(cards.get(i+cards.size()/2));
			right.add(cards.get(i));
		}
		for (int i=0; i<cards.size()-1; i+=2) {
			cards.set(i, left.get((int)(i/2.0)));
			cards.set(i+1, right.get((int)(i/2.0)));
		}
	}
	
	// swap shuffle, runs through deck and swaps 2 cards until end of deck (can swap with self)
	public void shuffleSwap() {
		System.out.println("swap shuffle, runs through deck and swaps 2 cards until end of deck (can swap with self)");
		Card temp;
		int swap;
		for (int i=0; i<cards.size(); i++) {
			temp = cards.get(i);
			swap = (int)(Math.random()*cards.size());
			cards.set(i, cards.get(swap));
			cards.set(swap, temp);
		}
	}
	
	// fischer yates shuffle, iirc the algorithm that the Java Collections shuffle method uses
	// runs through deck, picks cards at random to put in new deck, deck copies new deck
	public void shuffleYate() {
		System.out.println("fischer yates shuffle, iirc the algorithm that the Java Collections shuffle method uses");
		System.out.println("runs through deck, picks cards at random to put in new deck, deck copies new deck");
		ArrayList<Card> newCards = new ArrayList<Card>();
		while (cards.size()>0) newCards.add(cards.remove((int)(Math.random()*cards.size())));
		for (int i=newCards.size()-1; i>=0; i--) cards.add(newCards.remove(i));
	}
	
	// merge sorts cards by value
	// USE s=0, e=size()-1 FOR STARTING PARAMS!!!
	// *Note: may implement a sort by suite later b/c the suite chars' id's are 35-38 
	public ArrayList<Card> sortValue(ArrayList<Card> arr, int s, int e) {
		// single elem arr, end of recursive splitting
		// *Note: the {{...;}}; is just shorthand for declaring + instantiating an ArrayList in 1 line b/c it looks nice :D
		if(s==e) return new ArrayList<Card>(){{add(arr.get(s));}}; 
		
		// split and recurse
		ArrayList<Card> arr1 = sortValue(arr,s,(s+e)/2);
		ArrayList<Card> arr2 = sortValue(arr,(s+e)/2+1,e);
		
		// set up merging
		ArrayList<Card> arr3 = new ArrayList<Card>();
		
		// merge sorted lists
		while (arr1.size()>0 && arr2.size()>0) if (arr1.get(0).getVal()<arr2.get(0).getVal()) arr3.add(arr1.remove(0));
		else arr3.add(arr2.remove(0));
		
		// copy any remaining elements from lists
		while (arr1.size()>0) arr3.add(arr1.remove(0));
		while (arr2.size()>0) arr3.add(arr2.remove(0));
		
		return arr3;
	}

	/* THIS IS CURRENTLY NOT IN USE ANYWHERE, IMPLEMENTATION MAY COME LATER
	// recursive binary search
	public static int find(int[] numbers, int key, int l, int h) {
		if (l>h) return -1;
		if (numbers[(l+h)/2]==key) return (l+h)/2;
		if (key<numbers[(l+h)/2]) return find(numbers, key, l, (l+h)/2-1);
		else return find(numbers, key, (l+h)/2+1, h);
	}*/
}