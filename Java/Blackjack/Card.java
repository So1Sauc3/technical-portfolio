/**
 *	Card.java
 *
 *	a card!
 *  *Note: may implement a "face value" private variable that updates based on cardName string later, since it's kinda cluttery to redo it constantly in main code
 */

public class Card
{
	// card private vars
	private String cardName;
	private int value;
	
	// building
	public Card (String name, int val) {
		cardName = name;
		value = val;
	}
	
	// value getting
	public String getName() {return cardName;}
	public int getVal() {return value;}
}