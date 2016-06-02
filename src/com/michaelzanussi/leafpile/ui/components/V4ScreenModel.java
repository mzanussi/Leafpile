package com.michaelzanussi.leafpile.ui.components;

import java.awt.Font;
import java.awt.Point;

/**
 * This class provides a concrete implementation of the <code>Console</code> 
 * interface for versions 4 and 5 screen models.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (23 May 2016) 
 */
public class V4ScreenModel extends Console {

	/**
	 * The constructor.
	 * 
	 * @param width the width of the screen, in units.
	 * @param height the height of the screen, in units.
	 * @param font the initial font.
	 */
	public V4ScreenModel(int width, int height, Font font) {
		super(width, height, font);
		bold = true;
		italic = true;
		fixedSpaceAvail = true;
		timedInput = true;
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.components.Console#init()
	 */
	@Override
	public void init() {
		// Byte 0 of text-buffer initially contains the max number of
		// letters which can be typed, minus 1. Adjust
		// buffer accordingly. See p. 95.
		setAdjustBufferSize(true);
		// Status bar is 1 row high and sits at top of panel.
		setStatusBar(new Window(0, 0, getScreenWidth(), 1));
		// Upper window definition. Initially 0 rows high. 
		setUpperWindow(new Window(0, 1, getScreenWidth(), 0));
		// Lower window definition, where game play occurs.
		setLowerWindow(new Window(0, 1, getScreenWidth(), getScreenHeight() - 1));
		// Set default window to the lower window.
		setCurrentWindow(getLowerWindow());
		// With windows set, call the superclass init() method.
		super.init();
		// Clear the windows.
		wipe(getStatusBar(), getFgColor());
		wipe(getUpperWindow(), getBgColor());
		wipe(getLowerWindow(), getBgColor());
	}
	
    /* (non-Javadoc)
     * @see com.michaelzanussi.leafpile.ui.components.Console#erase_window(int)
     */
	@Override
    public void erase_window(int window) {
    	
		if (window == -1) {			// unsplit then clear
			setUpperWindow(new Window(0, 1, getScreenWidth(), 0));
			setLowerWindow(new Window(0, 1, getScreenWidth(), getScreenHeight() - 1));
			wipe(getUpperWindow(), getBgColor());
			wipe(getLowerWindow(), getBgColor());
		} else if (window == -2) {	// clear the screen
			Window uw = getUpperWindow();
			uw.setCursor(0, 0);
			wipe(uw, getBgColor());
			Window lw = getLowerWindow();
			lw.setCursor(0, 0);
			wipe(lw, getBgColor());
		} else {					// erase window to background color
			assert(false):"implement details erase_window, reset cursor";
		}
		
    }
    
    /* (non-Javadoc)
     * @see com.michaelzanussi.leafpile.ui.components.Console#split_window(int)
     */
	@Override
    public void split_window(int lines) {
		setUpperWindow(new Window(0, 1, getScreenWidth(), lines));
		setLowerWindow(new Window(0, lines + 1, getScreenWidth(), getScreenHeight() - lines - 1));
    }

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.components.Console#set_window(int)
	 */
	@Override
	public void set_window(int window) {
		if (window == 0) {
			setCurrentWindow(getLowerWindow());
		} else if (window == 1) {
			setCurrentWindow(getUpperWindow());
		} else {
			assert(false) : "unsupported window " + window;
		}
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.components.Console#set_cursor(int, int, int)
	 */
	@Override
	public void set_cursor(int line, int column, int window) {
		Window cw = getCurrentWindow();
		Point origin = cw.getOrigin();
		cw.setCursor(origin.x + line - 1, origin.y + column - 1);
	}

}
