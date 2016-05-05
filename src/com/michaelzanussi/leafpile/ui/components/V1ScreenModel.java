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

	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = -612865576249729802L;

	public V1ScreenModel(Object/*ZMachine*/ zm, int width, int height, Font font) {
		super(/*zm, */width, height, font);
	}
	
	public void init() {
	}
	
}
