public class RawIron extends Item {
	private static final String ID = "raw_iron";
	private static final int DEFAULT_STACK_LIMIT = 64;
	private static final int DEFAULT_STACK = 1;
	private static final String DEFAULT_TEXTURE = "textures\\Item\\raw_iron.png";
	private static final String DEFAULT_USE = "";
    
    public RawIron() {
		super(ID, DEFAULT_STACK_LIMIT, DEFAULT_STACK, DEFAULT_TEXTURE, DEFAULT_USE);
	}
}
