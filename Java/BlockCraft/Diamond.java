public class Diamond extends Item {
	private static final String ID = "diamond";
	private static final int DEFAULT_STACK_LIMIT = 64;
	private static final int DEFAULT_STACK = 1;
	private static final String DEFAULT_TEXTURE = "textures\\Item\\diamond.png";
	private static final String DEFAULT_USE = "";
    
    public Diamond() {
		super(ID, DEFAULT_STACK_LIMIT, DEFAULT_STACK, DEFAULT_TEXTURE, DEFAULT_USE);
	}
}
