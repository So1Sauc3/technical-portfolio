import javax.swing.ImageIcon;

public class WorldManager {
	private static final int IRON_LEVEL = 10;
	private BlockItem[][] world;

	public WorldManager(int w, int h, int groundLevel) {
		world = generateWorld(w, h, groundLevel);
	}
	
	// generates world, returns 2D BlockItem array
	private BlockItem[][] generateWorld(int w, int h, int groundLevel) {
		BlockItem[][] tileMap = new BlockItem[w][h];
		
		// NOISE FOR SURFACE GENERATION
		double[] surfaceNoise = genNoise(tileMap[0].length, groundLevel);
		
		// BASE TERRAIN GEN
		for (int c=0; c<tileMap[0].length; c++) for (int r=0; r<tileMap.length-2; r++) {
			double level = r-tileMap.length+surfaceNoise[c];
			if (level<0) tileMap[r][c] = new Air();
			else if (level<1) tileMap[r][c] = new GrassBlock();
			else if (level<2) tileMap[r][c] = new Dirt();
			else tileMap[r][c] = new Stone();
		}

		// bedrock randomness
		for (int c=0; c<tileMap[0].length; c++) {
			if (Math.random()>.5) tileMap[tileMap.length-2][c] = new Bedrock();
			else tileMap[tileMap.length-2][c] = new Stone();
		}
		
		// bedrock base layer
		for (int c=0; c<tileMap[0].length; c++) tileMap[tileMap.length-1][c] = new Bedrock();

		// POPULATE ORE
		for (int c=0; c<tileMap[0].length; c++) for (int r=groundLevel+1; r<tileMap.length; r++) if (Math.random()>.8 && tileMap[r][c] instanceof Stone) {
			if (r>tileMap.length-4) tileMap[r][c] = new DiamondOre();
			else if (r>groundLevel+IRON_LEVEL) tileMap[r][c] = new IronOre();
			else tileMap[r][c] = new CoalOre();
		}

		// POPULATE TREE (add later)
		for (int c=2; c<tileMap[0].length-2; c++) for (int r=groundLevel; r<groundLevel+6; r++) if (Math.random()>.8 && tileMap[r][c] instanceof Air && tileMap[r+1][c] instanceof GrassBlock) {
			tileMap[r][c] = new OakLog();
			tileMap[r-1][c] = new OakLog();
			for (int i=-3; i<-1; i++) for (int j=-2; j<3; j++) if (tileMap[r+i][c+j] instanceof Air) tileMap[r+i][c+j] = new OakLeaves();
			for (int i=-5; i<-3; i++) for (int j=-1; j<2; j++) if (tileMap[r+i][c+j] instanceof Air) tileMap[r+i][c+j] = new OakLeaves();
		}

		// returns BlockItem 2D array
		return tileMap;
	}
	
	// returns 1D noise array, change to perlin noise later, currently just a choppy sinusoidal function
	private double[] genNoise(int size, int baseLevel) {
		double[] noise = new double[size];
		// sin(rt(2) * x * random) + sin(pi * x * random) + h;
		for (int i=0; i<noise.length; i++) noise[i] = Math.sin(Math.sqrt(2)*i*Math.random())+Math.sin(Math.PI*i*Math.random())+baseLevel;
		return noise;
	}
	
	// returns array of ImageIcons to display on game screen
	public ImageIcon[][] getScreen(int x, int y, int rows, int cols) {
		ImageIcon[][] s = new ImageIcon[rows][cols];
		for (int r=0; r<rows; r++) for (int c=0; c<cols; c++) {
			if (c+x<0 || r+y<0 || r+y>world.length-1 || c+x>world[0].length-1) s[r][c] = new ImageIcon("textures\\Background\\sky.png"); // approaching end of generated world
			else s[r][c] = world[r+y][c+x].getItemIcon();
		}
		return s;
	}

	// returns the tile object at a location
	public BlockItem getTile(int r, int c) {
		if (r>=0 && r<world.length && c>=0 && c<=world[0].length) return world[r][c];
		else return null;
	}
	
	// updates a single tile, tied to player interaction -> returns potential item output from said interaction
	public Item updateTile(Item item, int r, int c) {
		System.out.println("trying to update tile "+world[r][c].getID());
		// updates the tile
		Item result = world[r][c].update(item);
		// updates the world with the new tile
		if (result instanceof BlockItem) world[r][c] = (BlockItem)result;
		// returns updated tile
		return result;
	}

	public boolean checkCraftingTable(int x, int y) {
		for (int r=y; r<y+4; r++) for (int c=x; c<x+3; c++) if (world[r][c] instanceof CraftingTable) return true;
		return false;
	}
	
	// updates the screen, for automatic updates that the player doesn't influence
	public void updateScreen(int x, int y, int rows, int cols) {
		for (int r=0; r<rows; r++) for (int c=0; c<cols; c++) world[r][c].update(null);
	}
}