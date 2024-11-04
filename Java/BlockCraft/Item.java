import javax.swing.*;

@SuppressWarnings("unused")
public abstract class Item {
	private String itemID;
	private int stackSize;
	private int itemStack;
	private ImageIcon itemIcon;
	private String use;
	public Item(String itemID, int stackSize, int itemStack, String itemFilePath, String use) {
		this.itemID = itemID;
		this.stackSize = stackSize;
		this.itemIcon = new ImageIcon(itemFilePath);
		this.itemStack = itemStack;
		this.use = use;
	}
	
	public String use() {
		return use;
	}
	
	public String getID() {
		return itemID;
	}
	public int getStackSize() {
		return stackSize;
	}
	public int getItemStack() {
		return itemStack;
	}
	public ImageIcon getItemIcon() {
		return itemIcon;
	}

	public void changeItemStack(int num) {
		itemStack = Math.min(itemStack+=num, stackSize);
	}
}