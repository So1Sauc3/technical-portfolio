import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GuiManager {
	// world tile dimensions and specifications
	int wRows = 32;
	int wCols = 48;
	int wGroundLevel = 14;
	// top left screen space coords, representative of player movement
	int sX = wCols/2;
	int sY = wGroundLevel-2;
	// screen tile dimensions
	int sRows = 9;
	int sCols = 13;
	// inv tile dimensions
	int iRows = 4;
	int iCols = sCols;
	// frame and tile pixel dimensions
	int tileRes = 64;
	int fWidth = tileRes*(sRows+iRows);
	int fHeight = tileRes*(sCols);

	// gui stuff
	JFrame a;
	JLayeredPane gLayers;
	JPanel gScreen;
	JPanel iScreen;
	drawPanel gOverlay;
	// putting GuiManager outside static main method
	public static void main(String[] args) {
		new GuiManager();
	}

	// actual code start
	public GuiManager() {
		
		// BASE WINDOW SETUP
		a = new JFrame();
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		a.setLocation(0,0);
		a.setSize(fWidth,fHeight);
		a.setTitle("Minecraft 2D");
		a.setIconImage(new ImageIcon("textures\\BlockItem\\grass_block.png").getImage());
		GridBagLayout grMain = new GridBagLayout();
		GridBagConstraints grC = new GridBagConstraints();
		a.setLayout(grMain);



		// overworld WorldManager initialization
		// may do multidimensional stuff eventually and add more dimension initializations here
		WorldManager overworld = new WorldManager(wRows,wCols,wGroundLevel);

		// InvManager initialization
		InvManager inv = new InvManager(iRows, iCols);

		// BLOCK PLACEMENT TESTING
		for (int i=0; i<16; i++) inv.addItemToHotbar(new OakPlanks());
		for (int i=0; i<16; i++) inv.addItemToHotbar(new OakLog(1));
		inv.addItemToHotbar(new DiamondPickaxe());
		for (int r=0; r<iRows; r++) for (int c=0; c<iCols; c++) {
			Item itm = inv.getSlot(r, c);
			if (itm!=null) System.out.printf("[%d][%d]: %s\n", r, c, inv.getSlot(r, c).getID());
			else System.out.printf("[%d][%d]: null\n", r, c);
		}



		// GAME SCREEN SETUP
		gLayers = new JLayeredPane();
		gLayers.setSize(a.getWidth(),a.getHeight()/tileRes*sRows);
		gLayers.setLayout(new GridBagLayout());
		grC.gridx = 0;
		grC.gridy = 0;
		grC.weightx = 1.0;
		grC.weighty = .66;
		grC.anchor = GridBagConstraints.PAGE_START;
		a.add(gLayers, grC);

		// game layer
		gScreen = new JPanel();
		gScreen.setSize(a.getWidth(),a.getHeight()/tileRes*sRows);
		GridLayout gGrid = new GridLayout(sRows,sCols);
		gScreen.setLayout(gGrid);
		grC.weightx = 1.0;
		grC.weighty = 1.0;
		gLayers.add(gScreen, grC);
		gLayers.setLayer(gScreen, JLayeredPane.DEFAULT_LAYER);
		for (int r=0; r<sRows; r++) for (int c=0; c<sCols; c++) {
			JButton jb = new JButton();
			jb.setName(r+" "+c);
			jb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
				String[] coords = jb.getName().split(" ");
				int r = Integer.parseInt(coords[0])+sY;
				int c = Integer.parseInt(coords[1])+sX;
				// THE MAGIC METHOD CALL
				// remind to block item class with a getDroppedItem method to call when mined
				Item drop = overworld.getTile(r, c).getDrop();
				Item result = overworld.updateTile(inv.getSelectedItem(),r,c);
				// updating the ImageIcon and inventory after updating
				if (result!=null) {
					jb.setIcon(new ImageIcon(result.getItemIcon().getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
					// updating inv slot with mined item, broken rn b/c the stack count update checks don't work
					if (drop!=null) inv.addItemToHotbar(drop);
					if (inv.getSelectedItem()!=null) {
						if (inv.getSelectedItem() instanceof ToolItem) ((ToolItem)inv.getSelectedItem()).changeDurability(-1);
						// change the if statement below to be more inclusive, fix the item voiding when breaking blocks problem
						else if (result instanceof Air) inv.getSelectedItem().changeItemStack(-1);
						if (inv.getSelectedItem().getItemStack()<1) inv.setSlot(null, iRows-1, inv.getSelectedSlot());
					}
				}
				gScreen.requestFocusInWindow();
			}});
			gScreen.add(jb);
		}
		ImageIcon[][] screenDisplay = overworld.getScreen(sX, sY, sRows, sCols);
		refresh(gScreen, screenDisplay);

		// steve overlay layer
		gOverlay = new drawPanel();
		gLayers.add(gOverlay, grC);
		gLayers.setLayer(gOverlay, JLayeredPane.PALETTE_LAYER);


		// INVENTORY SCREEN SETUP
		iScreen = new JPanel();
		iScreen.setSize(a.getWidth(),a.getHeight()/tileRes*iRows);
		GridLayout iGrid = new GridLayout(iRows,iCols);
		iScreen.setLayout(iGrid);
		grC.gridx = 0;
		grC.gridy = 1;
		grC.weightx = 1.0;
		grC.weighty = .34;
		grC.fill = GridBagConstraints.BOTH;
		grC.anchor = GridBagConstraints.PAGE_END;
		a.add(iScreen,grC);

		// inventory display setup
		for (int r=0; r<iRows; r++) for (int c=0; c<iCols; c++) {
			JButton jb = new JButton();
			jb.setName(r+" "+c);
			jb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
				String[] coords = jb.getName().split(" ");
				int r = Integer.parseInt(coords[0]);
				int c = Integer.parseInt(coords[1]);
				// IMPLEMENT INVENTORY MANAGEMENT FUNCTIONALITY HERE
				System.out.printf("Inv Slot Pressed: [%d, %d]\n", r, c);

				gScreen.requestFocusInWindow();
			}});
			iScreen.add(jb);
		}
		ImageIcon[][] invDisplay = inv.genInvIcons(iRows, iCols);
		refresh(iScreen, invDisplay);
		


		// BEHOLD, GRAVITYYYYYY, currently janky because it runs on a fixed delay instead of dynamically with movement but IT WORKS
		final Runnable gravity = new Runnable() { public void run() {
			if (overworld.getTile(sY+5,sX+6).getLayer()<1) {
				sY++;
				refresh(gScreen, overworld.getScreen(sX, sY, sRows, sCols));
			}
		}};
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleWithFixedDelay(gravity, 300, 300, TimeUnit.MILLISECONDS);



		// KEYBOARD EVENT SETUP

		// movement
		Action moveScreen = new AbstractAction() { public void actionPerformed(ActionEvent e) {
			try {
				switch (e.getActionCommand()) {
					// add logic for movement check here through the if statements
					// add check for out of bounds of world
					case ("w"): if (overworld.getTile(sY+2,sX+6).getLayer()<1 && overworld.getTile(sY+5,sX+6).getLayer()>0) sY--; refresh(gScreen, overworld.getScreen(sX, sY, sRows, sCols)); break;
					case ("a"): if (overworld.getTile(sY+3,sX+5).getLayer()<1 && overworld.getTile(sY+4,sX+5).getLayer()<1) sX--; refresh(gScreen, overworld.getScreen(sX, sY, sRows, sCols)); break;
					case ("s"): if (overworld.getTile(sY+5,sX+6).getLayer()<1) sY++; refresh(gScreen, overworld.getScreen(sX, sY, sRows, sCols)); break;
					case ("d"): if (overworld.getTile(sY+3,sX+7).getLayer()<1 && overworld.getTile(sY+4,sX+7).getLayer()<1) sX++; refresh(gScreen, overworld.getScreen(sX, sY, sRows, sCols)); break;
					case (" "): if (overworld.getTile(sY+2,sX+6).getLayer()<1 && overworld.getTile(sY+5,sX+6).getLayer()>0) sY--; refresh(gScreen, overworld.getScreen(sX, sY, sRows, sCols)); break;				
				}
				// crafting check, IMPLEMENT FUNCTIONALITY LATER
				if (overworld.checkCraftingTable(sX+5, sY+2)) {
					Component[] comps = iScreen.getComponents();
					for (int r=0; r<comps.length/11-1; r++) for (int c=comps.length/4-3; c<comps.length/4; c++) {
						((JButton)(comps[r*comps.length/4+c])).setIcon(new ImageIcon(new ImageIcon("textures\\Gui\\slot_crafting.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
					}
				}
				else refresh(iScreen, invDisplay);
			} catch (Exception ex) { System.out.println("world border reached"); }
		}};

		// hotbar selection
		Action moveSlot = new AbstractAction() { public void actionPerformed(ActionEvent e) {
			inv.setSelectedSlot(Integer.parseInt(e.getActionCommand()));
			String s1 = "nothing";
			int d2 = 0;
			if (inv.getSelectedItem()!=null) {
				s1 = inv.getSelectedItem().getID();
				d2 = inv.getSelectedItem().getItemStack();
			}
			System.out.printf("Selected Slot[%d]: %s, %d\n", Integer.parseInt(e.getActionCommand()), s1, d2);
		}};

		gScreen.getInputMap().put(KeyStroke.getKeyStroke('w'), "move");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke('a'), "move");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke('s'), "move");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke('d'), "move");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke(' '), "move");
		gScreen.getActionMap().put("move", moveScreen);

		gScreen.getInputMap().put(KeyStroke.getKeyStroke('1'), "hotbar");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke('2'), "hotbar");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke('3'), "hotbar");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke('4'), "hotbar");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke('5'), "hotbar");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke('6'), "hotbar");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke('7'), "hotbar");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke('8'), "hotbar");
		gScreen.getInputMap().put(KeyStroke.getKeyStroke('9'), "hotbar");
		gScreen.getActionMap().put("hotbar", moveSlot);
		


		// display
		a.setVisible(true);
	} 
	
	// updates image icons of jbutton components in a jpanel
	// ASSUMES ALL COMPONENTS INSIDE JPANEL ARE JBUTTONS!!!
	public void refresh(JPanel p, ImageIcon[][] icons) {
		Component[] comps = p.getComponents();
		int rs=icons.length;
		int cs=icons[0].length;
		for (int r=0; r<rs; r++) for (int c=0; c<cs; c++) {
			// figure out how to get rid of tileRes magic number here later
			ImageIcon icon = new ImageIcon(icons[r][c].getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
			((JButton)(comps[r*cs+c])).setIcon(icon);
		}
	}

	// steve layer
	public class drawPanel extends JPanel {
		public drawPanel() {
			setSize(a.getWidth(),a.getHeight()/tileRes*sRows);
			setBackground(new Color(0, 0, 0, 0));
			JLabel steve = new JLabel();
			steve.setIcon(new ImageIcon(new ImageIcon("textures\\entity\\steve_front.png").getImage().getScaledInstance(64, 300, java.awt.Image.SCALE_SMOOTH)));
			add(steve);
		}
		// FIX THIS AND REMOVE THE JLABEL IMPLEMENTATION
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			try {
				BufferedImage img = ImageIO.read(new File("textures\\entity\\steve_front.png"));
				g2.drawImage(img, 100, 100, null);
			}
			catch (IOException e) {e.printStackTrace();}
		}
	}
}