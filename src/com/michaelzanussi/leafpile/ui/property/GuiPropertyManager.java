package com.michaelzanussi.leafpile.ui.property;

import java.io.File;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * The GUI property manager.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (19 February 2008) 
 */
public final class GuiPropertyManager {

	private boolean debug;
	private File dir;
	
	private String aboutMenuItemIcon;
	private String exitMenuItemIcon;
	private String openMenuItemIcon;
	private String optionsMenuIcon;
	private String appIcon;
	private String aboutDialogIcon;
	
	private String aboutMenuItemText;
	private String exitMenuItemText;
	private String fileMenuText;
	private String helpMenuText;
	private String openMenuItemText;
	private String optionsMenuText;
	private String toolsMenuText;
	
	private String debugOutputOptionText;
	
	private String openToolTip;
	
	/**
	 * No-arg constructor.
	 */
	public GuiPropertyManager() {
		
		PropertyResourceBundle props = (PropertyResourceBundle)ResourceBundle.getBundle("resources.gui");
		
		try {
			debug = Boolean.parseBoolean(props.getString("Debug"));
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: Debug");
			debug = false;
		}

		try {
			dir = new File(props.getString("Dir"));
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: Dir");
			dir = new File("/");
		}
		
		try {
			aboutMenuItemIcon = props.getString("AboutMenuItemIcon");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: AboutMenuItemIcon");
			aboutMenuItemIcon = "resources/images/drink.png";
		}
		
		try {
			exitMenuItemIcon = props.getString("ExitMenuItemIcon");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: ExitMenuItemIcon");
			exitMenuItemIcon = "resources/images/door_open.png";
		}
		
		try {
			openMenuItemIcon = props.getString("OpenMenuItemIcon");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: OpenMenuItemIcon");
			openMenuItemIcon = "resources/images/folder_explore.png";
		}
		
		try {
			optionsMenuIcon = props.getString("OptionsMenuIcon");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: OptionsMenuIcon");
			optionsMenuIcon = "resources/images/wrench.png";
		}
		
		try {
			appIcon = props.getString("AppIcon");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: AppIcon");
			appIcon = "resources/images/application_osx.png";
		}
		
		try {
			aboutMenuItemText = props.getString("AboutMenuItemText");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: AboutMenuItemText");
			aboutMenuItemText = "About";
		}
		
		try {
			exitMenuItemText = props.getString("ExitMenuItemText");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: ExitMenuItemText");
			exitMenuItemText = "Exit";
		}
		
		try {
			fileMenuText = props.getString("FileMenuText");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: FileMenuText");
			fileMenuText = "File";
		}
		
		try {
			helpMenuText = props.getString("HelpMenuText");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: HelpMenuText");
			helpMenuText = "Help";
		}
		
		try {
			openMenuItemText = props.getString("OpenMenuItemText");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: OpenMenuItemText");
			openMenuItemText = "Open Log Folder...";
		}
		
		try {
			optionsMenuText = props.getString("OptionsMenuText");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: OptionsMenuText");
			optionsMenuText = "Options";
		}
		
		try {
			toolsMenuText = props.getString("ToolsMenuText");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: ToolsMenuText");
			toolsMenuText = "Tools";
		}
		
		try {
			debugOutputOptionText = props.getString("DebugOutputOptionText");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: DebugOutputOptionText");
			debugOutputOptionText = "Generate debug output";
		}
		
		try {
			aboutDialogIcon = props.getString("AboutDialogIcon");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: AboutDialogIcon");
			aboutDialogIcon = "resources/images/Box.png";
		}
		
		try {
			openToolTip = props.getString("OpenToolTip");
		}
		catch (MissingResourceException mre) {
			System.out.println("Missing GUI resource: OpenToolTip");
			openToolTip = "Select the folder containing the logs to process.";
		}
		
	}
	
	/**
	 * Returns the debug stutus.
	 * 
	 * @return <code>true</code> if debug mode; otherwise
	 * <code>false</code>.
	 */
	public boolean isDebug() {
		return debug;
	}
	
	/**
	 * Returns the story file directory.
	 * 
	 * @return the story file directory.
	 */
	public File getDir() {
		return dir;
	}
	
	/**
	 * Returns the About menu item icon.
	 * 
	 * @return the About menu item icon.
	 */
	public String getAboutMenuItemIcon() {
		return aboutMenuItemIcon;
	}
	
	/**
	 * Returns the Exit menu item icon.
	 * 
	 * @return the Exit menu item icon.
	 */
	public String getExitMenuItemIcon() {
		return exitMenuItemIcon;
	}
	
	/**
	 * Returns the Open menu item icon.
	 * 
	 * @return the Open menu item icon.
	 */
	public String getOpenMenuItemIcon() {
		return openMenuItemIcon;
	}
	
	/**
	 * Returns the Options menu icon.
	 * 
	 * @return the Options menu icon.
	 */
	public String getOptionsMenuIcon() {
		return optionsMenuIcon;
	}

	/**
	 * Returns the application icon.
	 * 
	 * @return the application icon.
	 */
	public String getAppIcon() {
		return appIcon;
	}

	/**
	 * Returns the About menu item text.
	 * 
	 * @return the About menu item text.
	 */
	public String getAboutMenuItemText() {
		return aboutMenuItemText;
	}

	/**
	 * Returns the Exit menu item text.
	 * 
	 * @return the Exit menu item text.
	 */
	public String getExitMenuItemText() {
		return exitMenuItemText;
	}

	/**
	 * Returns the File menu text.
	 * 
	 * @return the File menu text.
	 */
	public String getFileMenuText() {
		return fileMenuText;
	}

	/**
	 * Returns the Help menu text.
	 * 
	 * @return the Help menu text.
	 */
	public String getHelpMenuText() {
		return helpMenuText;
	}

	/**
	 * Returns the Open menu item text.
	 * 
	 * @return the Open menu item text.
	 */
	public String getOpenMenuItemText() {
		return openMenuItemText;
	}

	/**
	 * Returns the Options menu text.
	 * 
	 * @return the Options menu text.
	 */
	public String getOptionsMenuText() {
		return optionsMenuText;
	}

	/**
	 * Returns the Tools menu text.
	 * 
	 * @return the Tools menu text.
	 */
	public String getToolsMenuText() {
		return toolsMenuText;
	}

	/**
	 * Returns the Debug Output option text.
	 * 
	 * @return the Debug Output option text.
	 */
	public String getDebugOutputOptionText() {
		return debugOutputOptionText;
	}

	/**
	 * Returns the About dialog icon.
	 * 
	 * @return the About dialog icon.
	 */
	public String getAboutDialogIcon() {
		return aboutDialogIcon;
	}
	
	/**
	 * Returns the Open tooltip.
	 * 
	 * @return the Open tooltip.
	 */
	public String getOpenToolTip() {
		return openToolTip;
	}
	
}
