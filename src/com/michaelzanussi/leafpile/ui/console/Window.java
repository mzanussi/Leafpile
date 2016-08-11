package com.michaelzanussi.leafpile.ui.console;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * This class represents a region on the screen. The region
 * is represented by a rectangle, defined by its top-left point 
 * (x, y) in the coordinate space, its width, and its height;
 * and the current point location of the window's cursor. 
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (15 February 2008)
 */
public class Window {

	private Rectangle wnd;
	private Point cursor;
	
	public Window(int x, int y, int width, int height) {
		wnd = new Rectangle(x, y, width, height);
		cursor = new Point(0, 0);
	}
	
	public Rectangle getWindow() {
		return wnd;
	}
	
	public Point getCursor() {
		return cursor;
	}
	
	public void setCursor(int x, int y) {
		cursor.x = x;
		cursor.y = y;
	}
	
}
