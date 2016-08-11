package com.michaelzanussi.leafpile.ui.console;

import java.awt.Font;

import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * This class provides a concrete implementation of the <code>Console</code> 
 * interface for version 3 screen model.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (12 February 2008)
 */
public class V3ConsoleModel extends Console {

	/**
	 * The constructor.
	 * 
	 * @param width the width of the screen, in units.
	 * @param height the height of the screen, in units.
	 * @param font the initial font.
	 */
	public V3ConsoleModel(Zmachine zmachine, int width, int height, Font font) {
		super(zmachine, width, height, font);
		splitScreen = true;
		noStatusLine = false;
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
		wipe(getLowerWindow(), getLowerBgColor());
	}
	
    /* (non-Javadoc)
     * @see com.michaelzanussi.leafpile.ui.components.Console#erase_window(int)
     */
	@Override
    public void erase_window(int window) {
		throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.michaelzanussi.leafpile.ui.components.Console#split_window(int)
     */
	@Override
    public void split_window(int lines) {
		setUpperWindow(new Window(0, 1, getScreenWidth(), lines));
		setLowerWindow(new Window(0, lines + 1, getScreenWidth(), getScreenHeight() - lines - 1));
		wipe(getUpperWindow(), getUpperBgColor());
		wipe(getLowerWindow(), getLowerBgColor());
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
		throw new UnsupportedOperationException();
	}

}
