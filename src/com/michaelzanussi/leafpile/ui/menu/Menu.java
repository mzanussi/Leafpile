package com.michaelzanussi.leafpile.ui.menu;

import javax.swing.JMenuBar;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;

/**
 * This class defines the menu bar.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public class Menu extends JMenuBar {

	private FileMenu file;
	private ToolsMenu tools;
	private HelpMenu help;

	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = 3365326081441032027L;

	/**
	 * No-arg constructor.
	 */
	public Menu(LeafpileGUI mf) {
		
		// Create the menus.
		file = new FileMenu(mf);
		tools = new ToolsMenu(mf);
		help = new HelpMenu(mf);
		
		// Add menus to the menu bar.
		add(file);
		add(tools);
		add(help);
		
	}
	
	/**
	 * Returns the File menu.
	 * 
	 * @return the File menu
	 */
	public FileMenu getFileMenu() {
		return file;
	}
	
	/**
	 * Returns the Tools menu.
	 * 
	 * @return the Tools menu
	 */
	public ToolsMenu getToolsMenu() {
		return tools;
	}
	
	/**
	 * Returns the Help menu.
	 * 
	 * @return the Help menu
	 */
	public HelpMenu getHelpMenu() {
		return help;
	}
}
