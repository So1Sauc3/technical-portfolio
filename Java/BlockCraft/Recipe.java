public class Recipe {
	private Item[] ings;
	private Item result;
	@SuppressWarnings("unused")
	private boolean is2x2;
	
	public Recipe(Item[] ings, Item result) {
		this.ings=ings;
		this.result=result;
		is2x2=true; // IMPLEMENT ACTUAL CHECK LATER
	}
	
	public Item[] getIngs() {
		return ings;
	}
	
	public Item getResult() {
		return result;
	}
}