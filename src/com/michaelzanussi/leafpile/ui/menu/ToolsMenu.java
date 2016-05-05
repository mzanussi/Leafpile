package com.michaelzanussi.leafpile.ui.menu;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;
import com.michaelzanussi.leafpile.ui.property.GuiPropertyManager;

/**
 * This class defines the Tools menu.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class ToolsMenu extends JMenu {

	private OptionsSubmenu optionsMenu;

	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = 8133213534019849504L;

	/**
	 * No-arg constructor.
	 */
	public ToolsMenu(LeafpileGUI mf) {
		
		GuiPropertyManager pm = mf.getPropertyManager();
		
		setText(pm.getToolsMenuText());
		setMnemonic(KeyEvent.VK_T);

		// Create menu items.
		optionsMenu = new OptionsSubmenu(mf); 

		// Add the menu items.
		add(optionsMenu);

	}
	
	/**
	 * Returns the Options submenu.
	 * 
	 * @return the Options submenu 
	 */
	public OptionsSubmenu getOptionsMenu() {
		return optionsMenu;
	}
	
}
