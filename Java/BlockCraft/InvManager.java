import javax.swing.ImageIcon;

@SuppressWarnings("unused")
public class InvManager {
    private Item[][] inv;
	private int selectedSlot; 

    public InvManager(int r, int c) {
        inv = new Item[r][c];
		selectedSlot = 1;
    }

    // returns inventory
    public Item[][] getItems() {
        return inv;
    }

    // returns item in slot
    public Item getSlot(int r, int c) {
        return inv[r][c];
    }

	public void setSlot(Item itm, int r, int c) {
		inv[r][c] = itm;
	} 

	// returns selected hotbar item
	public Item getSelectedItem() {
		return inv[inv.length-1][selectedSlot];
	}

	public int getSelectedSlot() {
		return selectedSlot;
	}
	
	public void setSelectedSlot(int slot) {
		selectedSlot = slot;
	}
	
	// swaps inventory slots
	public void swapSlots(int r1, int c1, int r2, int c2) {
		Item item1 = inv[r1][c1];
		inv[r1][c1] = inv[r2][c2];
		inv[r2][c2] = item1;
	}

	public void addItem(Item itm) {
		for (int r=0; r<inv.length-1; r++) for (int c=1; c<inv[0].length-3; c++) {
			if (inv[r][c]==null) {
				inv[r][c] = itm;
				return;
			}
			else if (inv[r][c].getID().equals(itm.getID()) && inv[r][c].getItemStack()<64) {
				inv[r][c].changeItemStack(1);
			}
		}
	}

	public void addItemToHotbar(Item itm) {
		for (int c=1; c<inv[0].length-3; c++) {
			if (inv[inv.length-1][c]==null) {
				inv[inv.length-1][c] = itm;
				return;
			}
			else if (inv[inv.length-1][c].getID().equals(itm.getID()) && inv[inv.length-1][c].getItemStack()<64) {
				inv[inv.length-1][c].changeItemStack(1);
				return;
			}
		}
	}

    // generates inventory textures
    public ImageIcon[][] genInvIcons(int rows, int cols) {
		ImageIcon[][] display = new ImageIcon[rows][cols];
		display[0][0] = new ImageIcon("textures\\Gui\\slot_helmet.png");
		display[1][0] = new ImageIcon("textures\\Gui\\slot_chestplate.png");
		display[2][0] = new ImageIcon("textures\\Gui\\slot_leggings.png");
		display[3][0] = new ImageIcon("textures\\Gui\\slot_boots.png");
		for (int r=0; r<rows-1; r++) for (int c=1; c<cols; c++) {
			if (c>cols-4) {
				if (c==cols-1 || r==rows-2) display[r][c] = new ImageIcon("textures\\Gui\\slot_crafting_null.png");
				else display[r][c] = new ImageIcon("textures\\Gui\\slot_crafting.png");
			} else display[r][c] = new ImageIcon("textures\\Gui\\slot.png");
		}
		for (int c=1; c<cols; c++) display[rows-1][c] = new ImageIcon("textures\\Gui\\slot_hotbar.png");
		display[rows-1][cols-3] = new ImageIcon("textures\\Gui\\slot_blank.png");
		display[rows-1][cols-1] = new ImageIcon("textures\\Gui\\slot_blank.png");

		return display;
	}
}
