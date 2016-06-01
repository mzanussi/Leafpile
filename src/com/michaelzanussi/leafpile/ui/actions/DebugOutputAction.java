package com.michaelzanussi.leafpile.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;

/**
 * This class defines the action for the Debug output option.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class DebugOutputAction extends AbstractAction {

	private LeafpileGUI mf;
	
	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = -4761956363870177909L;

	/**
	 * No-arg constructor.
	 */
	public DebugOutputAction(LeafpileGUI mf) {
		this.mf = mf;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		JCheckBoxMenuItem debout = (JCheckBoxMenuItem)event.getSource();
		mf.setDebug(debout.isSelected());
	}
	
}
