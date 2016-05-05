package com.michaelzanussi.leafpile.ui.menu;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;
import com.michaelzanussi.leafpile.ui.property.GuiPropertyManager;

/**
 * This class defines the Help menu.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class HelpMenu extends JMenu {
	
	private AboutMenuItem aboutMenuItem;
	
	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = -3323974129173596095L;

	/**
	 * No-arg constructor
	 */
	public HelpMenu(LeafpileGUI mf) {

		GuiPropertyManager pm = mf.getPropertyManager();

		setText(pm.getHelpMenuText());
		setMnemonic(KeyEvent.VK_H);

		// Create menu items.
		aboutMenuItem = new AboutMenuItem(mf);
		
		// Add the menu items.
		add(aboutMenuItem);
	}
	
	/**
	 * Returns the About menu item.
	 * 
	 * @return the About menu item.
	 */
	public AboutMenuItem getAboutMenuItem() {
		return aboutMenuItem;
	}
}
