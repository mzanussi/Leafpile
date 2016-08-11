package com.michaelzanussi.leafpile.ui.console;

import java.awt.Font;

import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * This class provides a concrete implementation of the <code>Console</code> 
 * interface for versions 1 and 2 screen models.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (12 February 2008)
 */
public class V1ConsoleModel extends Console {

	public V1ConsoleModel(Zmachine zmachine, int width, int height, Font font) {
		super(zmachine, width, height, font);
		noStatusLine = false;
	}
	
	@Override
	public void init() {
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
		throw new UnsupportedOperationException();
    }

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.components.Console#set_window(int)
	 */
	@Override
	public void set_window(int window) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.components.Console#set_cursor(int, int, int)
	 */
	@Override
	public void set_cursor(int line, int column, int window) {
		throw new UnsupportedOperationException();
	}

}
