public class Air extends BlockItem {
	private static final String ID = "air";
	private static final int DEFAULT_STACK_LIMIT = 0;
	private static final int DEFAULT_STACK = 0;
	private static final String DEFAULT_TEXTURE = "textures\\Background\\sky.png";
	private static final int DEFAULT_LAYER = 0;
	private static final String DEFAULT_USE = "";
	
	public Air() {
		super(ID, DEFAULT_STACK_LIMIT, DEFAULT_STACK, DEFAULT_TEXTURE, DEFAULT_TEXTURE, DEFAULT_LAYER, DEFAULT_USE);
	}
	
	@Override
	public Item update(Item itm) {
		if (itm instanceof BlockItem) return itm;
		else return null;
	}

	@Override
	public Item getDrop() {
		return null;
	}
}