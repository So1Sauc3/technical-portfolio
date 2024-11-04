public class Cobblestone extends BlockItem {
	private static final String ID = "cobblestone";
	private static final int DEFAULT_STACK_LIMIT = 64;
	private static final int DEFAULT_STACK = 1;
	private static final String DEFAULT_TEXTURE = "textures\\BlockItem\\cobblestone.png";
	private static final int DEFAULT_LAYER = 1;
	private static final String DEFAULT_USE = "";
	
	public Cobblestone() {
		super(ID, DEFAULT_STACK_LIMIT, DEFAULT_STACK, DEFAULT_TEXTURE, DEFAULT_TEXTURE, DEFAULT_LAYER, DEFAULT_USE);
	}
	
	@Override
	public Item update(Item itm) {
		return null;
	}

	@Override
	public Item getDrop() {
		return new Cobblestone();
	}
}