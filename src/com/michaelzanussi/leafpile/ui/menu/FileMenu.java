package com.michaelzanussi.leafpile.ui.menu;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;
import com.michaelzanussi.leafpile.ui.property.GuiPropertyManager;

/**
 * This class defines the File menu.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class FileMenu extends JMenu {
	
	private OpenMenuItem openMenuItem;
	private ExitMenuItem exitMenuItem;
	
	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = -7799423745693086737L;

	/**
	 * No-arg constructor.
	 */
	public FileMenu(LeafpileGUI mf) {

		GuiPropertyManager pm = mf.getPropertyManager();
		
		setText(pm.getFileMenuText());
		setMnemonic(KeyEvent.VK_F);

		// Create menu items.
		openMenuItem = new OpenMenuItem(mf);
		exitMenuItem = new ExitMenuItem(mf);
		
		// Add the menu items.
		add(openMenuItem);
		addSeparator();
		add(exitMenuItem);
	}
	
	/**
	 * Returns the Open menu item.
	 * 
	 * @return the Open menu item.
	 */
	public OpenMenuItem getOpenMenuItem() {
		return openMenuItem;
	}
	
	/**
	 * Returns the Exit menu item.
	 * 
	 * @return the Exit menu item.
	 */
	public ExitMenuItem getExitMenuItem() {
		return exitMenuItem;
	}
}
