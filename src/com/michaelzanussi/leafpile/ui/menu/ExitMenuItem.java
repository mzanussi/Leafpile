package com.michaelzanussi.leafpile.ui.menu;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;
import com.michaelzanussi.leafpile.ui.actions.ExitAction;
import com.michaelzanussi.leafpile.ui.property.GuiPropertyManager;

/**
 * This class defines the Exit menu item.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class ExitMenuItem extends JMenuItem {

	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = 5059952682257545753L;

	/**
	 * No-arg constructor.
	 */
	public ExitMenuItem(LeafpileGUI mf) {
		super(new ExitAction());
		GuiPropertyManager pm = mf.getPropertyManager();
		ClassLoader cldr = this.getClass().getClassLoader();
		setIcon(new ImageIcon(cldr.getResource(pm.getExitMenuItemIcon())));
		setMnemonic(KeyEvent.VK_X);
		setText(pm.getExitMenuItemText());
	}

}
