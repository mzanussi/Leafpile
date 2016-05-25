package com.michaelzanussi.leafpile.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;
import com.michaelzanussi.leafpile.ui.property.GuiPropertyManager;

/**
 * This class defines the action for the Open menu item.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class OpenAction extends AbstractAction {

	private LeafpileGUI mf;
	private GuiPropertyManager pm;
	
	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = -4193494587657082734L;

	/**
	 * No-arg constructor.
	 */
	public OpenAction(LeafpileGUI mf) {
		this.mf = mf;
		pm = mf.getPropertyManager();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event) {
		JFileChooser file = new JFileChooser();
		File dir = pm.getDir();
		file.setCurrentDirectory(dir);
		file.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int choice = file.showDialog(mf, "Select Story");
		if (choice != JFileChooser.CANCEL_OPTION) {
			//mf.setScreenModel();
			mf.start(file.getSelectedFile());
		}
	}
}
