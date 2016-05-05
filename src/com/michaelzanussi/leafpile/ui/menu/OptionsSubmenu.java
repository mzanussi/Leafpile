package com.michaelzanussi.leafpile.ui.menu;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;
import com.michaelzanussi.leafpile.ui.actions.DebugOutputAction;
import com.michaelzanussi.leafpile.ui.property.GuiPropertyManager;

/**
 * This class defines the Options submenu.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class OptionsSubmenu extends JMenu {

	private static JCheckBoxMenuItem debout;
	
	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = -569018064464627506L;

	/**
	 * No-arg constructor.
	 */
	public OptionsSubmenu(LeafpileGUI mf) {
		
		GuiPropertyManager pm = mf.getPropertyManager();
		ClassLoader cldr = this.getClass().getClassLoader();
		setIcon(new ImageIcon(cldr.getResource(pm.getOptionsMenuIcon())));
		setMnemonic(KeyEvent.VK_O);
		setText(pm.getOptionsMenuText());

		debout = new JCheckBoxMenuItem(new DebugOutputAction(mf));
		debout.setText(pm.getDebugOutputOptionText());
		debout.setSelected(mf.isDebug());
		
		add(debout);
	}
	
	/**
	 * Returns the debug output option.
	 * 
	 * @return the debug output option
	 */
	public JCheckBoxMenuItem getDebugOutput() {
		return debout;
	}
	
}
