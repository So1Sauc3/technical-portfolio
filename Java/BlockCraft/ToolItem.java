public abstract class ToolItem extends Item {
	public static final String DEFAULT_STATE = "default";
	private int durability;
    private int mineStrength; // String format is miningLevelNumber+miningType
    private double dmg;
	
	public ToolItem(String itemID, int stackSize, int stack, String itemFilePath, String use, int durability, int mineStrength, double dmg) {
		super(itemID, stackSize, stack, itemFilePath, use);
        this.durability = durability;
        this.mineStrength = mineStrength;
        this.dmg = dmg;
	}

    public int getDurability() {
        return durability;
    }

    public void changeDurability(int change) {
        durability = Math.max(durability+=change, 0);
    }

    public int getMineStrength() {
        return mineStrength;
    }

    public double getDmg() {
        return dmg;
    }
}