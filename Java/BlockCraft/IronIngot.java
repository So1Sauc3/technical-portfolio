public class IronIngot extends Item {
	private static final String ID = "iron_ingot";
	private static final int DEFAULT_STACK_LIMIT = 64;
	private static final int DEFAULT_STACK = 1;
	private static final String DEFAULT_TEXTURE = "textures\\Item\\iron_ingot.png";
	private static final String DEFAULT_USE = "";
    
    public IronIngot() {
		super(ID, DEFAULT_STACK_LIMIT, DEFAULT_STACK, DEFAULT_TEXTURE, DEFAULT_USE);
	}
}
