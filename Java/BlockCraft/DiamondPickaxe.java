public class DiamondPickaxe extends ToolItem {
	private static final String ID = "diamond_pickaxe";
	private static final int DEFAULT_STACK_LIMIT = 1;
	private static final int DEFAULT_STACK = 1;
	private static final String DEFAULT_TEXTURE = "textures\\Item\\diamond_pickaxe.png";
	private static final String DEFAULT_USE = "mine";
	private static final int DEFAULT_DURABILITY = 1562;
	private static final int DEFAULT_MINE_STRENGTH = 3;
	private static final int DEFAULT_DMG = 6;
    
    public DiamondPickaxe() {
        super(ID, DEFAULT_STACK_LIMIT, DEFAULT_STACK, DEFAULT_TEXTURE, DEFAULT_USE, DEFAULT_DURABILITY, DEFAULT_MINE_STRENGTH, DEFAULT_DMG);
    }
}
