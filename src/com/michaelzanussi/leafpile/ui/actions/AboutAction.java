package com.michaelzanussi.leafpile.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;
import com.michaelzanussi.leafpile.ui.property.GuiPropertyManager;

/**
 * This class defines the action for the About menu item.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class AboutAction extends AbstractAction {

	private LeafpileGUI mf;
	private GuiPropertyManager pm;
	
	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = -1426019265184145704L;

	/**
	 * No-arg constructor. 
	 */
	public AboutAction(LeafpileGUI mf) {
		this.mf = mf;
		pm = mf.getPropertyManager();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		String app = "Leafpile Test Driver v1.0";
		app += "\n\nDeveloped by Michael Zanussi\nhttp://www.michaelzanussi.com/";
		app += "\n\nSkull box courtesy of Infinise Design\nhttp://www.infinisedesign.com/";
		app += "\n\nSilk Icons courtesy of Mark James\nhttp://www.famfamfam.com/";
		ClassLoader cldr = this.getClass().getClassLoader();
		JOptionPane.showMessageDialog(mf, app, "About", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(cldr.getResource(/*GuiPropertyManager.getInstance()*/pm.getAboutDialogIcon())));
	}
	
}
