public class OakSapling extends BlockItem {
	private static final String ID = "oak_sapling";
	private static final int DEFAULT_STACK_LIMIT = 64;
	private static final int DEFAULT_STACK = 1;
	private static final String DEFAULT_TEXTURE = "textures\\BlockItem\\oak_sapling.png";
	private static final int DEFAULT_LAYER = 0;
	private static final String DEFAULT_USE = "";
	
	public OakSapling() {
		super(ID, DEFAULT_STACK_LIMIT, DEFAULT_STACK, DEFAULT_TEXTURE, DEFAULT_TEXTURE, DEFAULT_LAYER, DEFAULT_USE);
	}
	
	@Override
	public Item update(Item itm) {
		return new Air();
	}

	@Override
	public Item getDrop() {
		return new OakSapling();
	}
}