package com.michaelzanussi.leafpile.ui.menu;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;
import com.michaelzanussi.leafpile.ui.actions.AboutAction;
import com.michaelzanussi.leafpile.ui.property.GuiPropertyManager;

/**
 * This class defines the About menu item.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class AboutMenuItem extends JMenuItem {

	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = -3525407262648287902L;

	/**
	 * No-arg constructor.
	 */
	public AboutMenuItem(LeafpileGUI mf) {
		super(new AboutAction(mf));
		GuiPropertyManager pm = mf.getPropertyManager();
		ClassLoader cldr = this.getClass().getClassLoader();
		setIcon(new ImageIcon(cldr.getResource(pm.getAboutMenuItemIcon())));
		setMnemonic(KeyEvent.VK_A);
		setText(pm.getAboutMenuItemText());
	}

}
