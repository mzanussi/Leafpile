package com.michaelzanussi.leafpile.ui.components;

import java.awt.Font;

/**
 * This class provides a concrete implementation of the <code>Console</code> 
 * interface for versions 1 and 2 screen models.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (12 February 2008)
 */
public class V1ScreenModel extends Console {

	public V1ScreenModel(int width, int height, Font font) {
		super(width, height, font);
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
