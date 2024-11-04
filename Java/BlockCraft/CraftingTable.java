public class CraftingTable extends BlockItem {
	private static final String ID = "crafting_table";
	private static final int DEFAULT_STACK_LIMIT = 64;
	private static final int DEFAULT_STACK = 1;
	private static final String DEFAULT_TEXTURE = "textures\\BlockItem\\crafting_table.png";
	private static final int DEFAULT_LAYER = 1;
	private static final String DEFAULT_USE = "";
	
	public CraftingTable() {
		super(ID, DEFAULT_STACK_LIMIT, DEFAULT_STACK, DEFAULT_TEXTURE, DEFAULT_TEXTURE, DEFAULT_LAYER, DEFAULT_USE);
	}
	
	@Override
	public Item update(Item itm) {
		return new Air();
	}

	@Override
	public Item getDrop() {
		return new CraftingTable();
	}
}