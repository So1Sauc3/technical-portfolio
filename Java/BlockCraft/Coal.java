public class Coal extends Item {
	private static final String ID = "coal";
	private static final int DEFAULT_STACK_LIMIT = 64;
	private static final int DEFAULT_STACK = 1;
	private static final String DEFAULT_TEXTURE = "textures\\Item\\coal.png";
	private static final String DEFAULT_USE = "fuel";
    
    public Coal() {
		super(ID, DEFAULT_STACK_LIMIT, DEFAULT_STACK, DEFAULT_TEXTURE, DEFAULT_USE);
	}
}
