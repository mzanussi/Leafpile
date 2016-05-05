package com.michaelzanussi.leafpile.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * This class defines the action for the Exit menu item.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class ExitAction extends AbstractAction {

	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = -1140028649474412190L;

	/**
	 * No-arg constructor. 
	 */
	public ExitAction() {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event) {
		System.exit(0);
	}
}
