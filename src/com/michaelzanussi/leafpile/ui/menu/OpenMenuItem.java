package com.michaelzanussi.leafpile.ui.menu;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;
import com.michaelzanussi.leafpile.ui.actions.OpenAction;
import com.michaelzanussi.leafpile.ui.property.GuiPropertyManager;

/**
 * This class defines the Open menu item.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class OpenMenuItem extends JMenuItem {

	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = -8403055782903123874L;

	/**
	 * No-arg constructor.
	 */
	public OpenMenuItem(LeafpileGUI mf) {
		super(new OpenAction(mf));
		GuiPropertyManager pm = mf.getPropertyManager();
		ClassLoader cldr = this.getClass().getClassLoader();
		setIcon(new ImageIcon(cldr.getResource(pm.getOpenMenuItemIcon())));
		setMnemonic(KeyEvent.VK_O);
		setText(pm.getOpenMenuItemText());
	}

}
