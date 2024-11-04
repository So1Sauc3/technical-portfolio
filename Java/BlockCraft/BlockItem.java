import javax.swing.*;

@SuppressWarnings("unused")
public abstract class BlockItem extends Item {
	public static final String DEFAULT_STATE = "default";
	private String blockstate;
	private ImageIcon tileIcon;
	private int layer;
	
	public BlockItem(String itemID, int stackSize, int itemStack, String itemFilePath, String tileFilePath, int layer, String use) {
		super(itemID, stackSize, itemStack, itemFilePath, use);
		blockstate = DEFAULT_STATE;
		tileIcon = new ImageIcon(tileFilePath);
		this.layer=layer;
	}
	
	public abstract Item update(Item itm);
	
	public abstract Item getDrop();

	public ImageIcon getTileIcon() {
		return tileIcon;
	}
	
	public int getLayer() {
		return layer;
	}
}