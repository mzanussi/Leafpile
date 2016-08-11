package com.michaelzanussi.leafpile.ui.console;

import java.awt.Font;
import java.awt.Point;

import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * This class provides a concrete implementation of the <code>Console</code> 
 * interface for versions 4 and 5 screen models.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (23 May 2016) 
 */
public class V4ConsoleModel extends Console {

	/**
	 * The constructor.
	 * 
	 * @param width the width of the screen, in units.
	 * @param height the height of the screen, in units.
	 * @param font the initial font.
	 */
	public V4ConsoleModel(Zmachine zmachine, int width, int height, Font font) {
		super(zmachine, width, height, font);
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
		// Upper window definition. Initial height of 0. (8.7.3.3)
		setUpperWindow(new Window(0, 0, getScreenWidth(), 0));
		// Lower window definition, where game play occurs.
		setLowerWindow(new Window(0, 0, getScreenWidth(), getScreenHeight()));
		// TODO: handle version 5 cursor
		getLowerWindow().setCursor(0, getScreenHeight() - 1);
		// Set default window to the lower window. (8.7.3.3)
		setCurrentWindow(getLowerWindow());
		// With windows set, call the superclass init() method.
		super.init();
		// Clear the windows.
		wipe(getUpperWindow(), getUpperBgColor());	// (8.7.3.3)
		wipe(getLowerWindow(), getLowerBgColor());	// (8.7.3.3)
	}
	
    /* (non-Javadoc)
     * @see com.michaelzanussi.leafpile.ui.components.Console#erase_window(int)
     */
	@Override
    public void erase_window(int window) {

		if (window == -1) {
			// Erasing window -1 clears the whole screen to the background colour of the
			// lower screen, collapses the upper window to height 0, moves the cursor of
			// the lower screen to the bottom left (in Version 4) or top left (in Version 5
			// and later) and selects the lower screen. The same operation should happen at
			// the start of the game. (8.7.3.3)
			wipe(getUpperWindow(), getUpperBgColor());
			wipe(getLowerWindow(), getLowerBgColor());
			setUpperWindow(new Window(0, 0, getScreenWidth(), 0));
			setLowerWindow(new Window(0, 0, getScreenWidth(), getScreenHeight()));
			// TODO: handle version 5 cursor
			getLowerWindow().setCursor(0, getScreenHeight() - 1);
			setCurrentWindow(getLowerWindow());
		} else if (window == -2) {	// clear the screen
			Window uw = getUpperWindow();
			uw.setCursor(0, 0);
			wipe(uw, getUpperBgColor());
			Window lw = getLowerWindow();
			lw.setCursor(0, 0);
			wipe(lw, getLowerBgColor());
		} else {					// erase window to background color
			assert(false):"implement details erase_window, reset cursor";
		}
		
    }
    
    /* (non-Javadoc)
     * @see com.michaelzanussi.leafpile.ui.components.Console#split_window(int)
     */
	@Override
    public void split_window(int lines) {
		
		// Get current cursor positions before splitting the window.
		Point uwc = getUpperWindow().getCursor();	// current upper window's cursor
		Point lwc = getLowerWindow().getCursor();	// current lower window's cursor

		// If current window is upper window, splitting is not allowed.
		// Leave the cursor where it is if the cursor is still inside the 
		// new upper window, and otherwise moving the cursor back to the
		// top left. (8.7.1.1.1) 
		if (getCurrentWindow() == getUpperWindow()) {
			if (uwc.y >= lines) {
				getUpperWindow().setCursor(0, 0);
			}
			return;
		}
		
		// Split the window.
		setUpperWindow(new Window(0, 0, getScreenWidth(), lines));
		setLowerWindow(new Window(0, lines, getScreenWidth(), getScreenHeight() - lines));
		
		// If cursor is no longer inside the upper window, reset to top left. (8.7.2.1.1)
		if (uwc.y >= lines) {
			getUpperWindow().setCursor(0, 0);
		}
		
		// For Version 4, the lower window's cursor is always on the bottom 
		// screen line. (8.7.2.2) Maintain x position, but reset y to the 
		// bottom line.
		// TODO: reposition cursor for Version 5
		getLowerWindow().setCursor(lwc.x, getScreenHeight() - lines - 1);

		// Reset the current window.
		setCurrentWindow(getLowerWindow());
    }

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.components.Console#set_window(int)
	 */
	@Override
	public void set_window(int window) {
		if (window == 0) {
			setCurrentWindow(getLowerWindow());
		} else if (window == 1) {
			getUpperWindow().setCursor(0, 0);	// (8.7.2)
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
		cw.setCursor(cw.getWindow().x + column - 1, cw.getWindow().y + line - 1);
	}

}
