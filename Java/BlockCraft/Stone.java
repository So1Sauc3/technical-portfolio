public class Stone extends BlockItem {
	private static final String ID = "stone";
	private static final int DEFAULT_STACK_LIMIT = 64;
	private static final int DEFAULT_STACK = 1;
	private static final String DEFAULT_TEXTURE = "textures\\BlockItem\\stone.png";
	private static final int DEFAULT_LAYER = 1;
	private static final String DEFAULT_USE = "";
	
	public Stone() {
		super(ID, DEFAULT_STACK_LIMIT, DEFAULT_STACK, DEFAULT_TEXTURE, DEFAULT_TEXTURE, DEFAULT_LAYER, DEFAULT_USE);
	}
	
	@Override
	public Item update(Item itm) {
		if (itm instanceof ToolItem && ((ToolItem)itm).getMineStrength()>2) return new Air();
		else return null;
	}

	@Override
	public Item getDrop() {
		return new Cobblestone();
	}
}