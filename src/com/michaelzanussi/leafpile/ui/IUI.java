package com.michaelzanussi.leafpile.ui;

/**
 * Simple API for game user interfaces. 
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (16 May 2016) 
 */
public interface IUI {

	/**
	 * Write data to console. Currently only supports buffered output.
	 * 
	 * @param data the data to output to console.
	 */
	public void write(String data);
	
	/**
	 * Flush the text output buffer.
	 */
	public void flush_buf();
	
	/**
	 * Get input from device.
	 * 
	 * @param in a StringBuilder containing input
	 * @param count the count of chars to read
	 */
	public void read(StringBuilder in, int count);
	
	/**
	 * Get input from device.
	 * 
	 * @param in a StringBuilder containing input
	 */
	public void read_char(StringBuilder in);
	
	/**
	 * Gets a filename from the user, for saving and
	 * retrieving game state.
	 * 
	 * @param true if this is a save operation;
	 * otherwise, false.
	 * @return the filename.
	 */
	public String filename(boolean isSave);
	
	/**
	 * Write data to debug console, if any.
	 * @param string the string to output to debug console
	 */
	public void debug(String string);
	
	/**
	 * Returns whether the game console supports split screen.
	 * 
	 * @return whether the game console supports split screen
	 */
	public boolean hasSplitScreen();
	
	/**
	 * Returns whether the game console has no status line.
	 * 
	 * @return whether the game console has no status line.
	 */
	public boolean hasNoStatusLine();
	
	/**
	 * Returns whether a variable pitch font is default.
	 * 
	 * @return whether a variable pitch font is default.
	 */
	public boolean isVariablePitchDefault();
	
	/**
	 * Returns if bold is available.
	 * 
	 * @return if bold is available.
	 */
	public boolean isBoldAvailable();
	
	/**
	 * Returns if italic is available.
	 * 
	 * @return if italic is available.
	 */
	public boolean isItalicAvailable();
	
	/**
	 * Returns whether a fixed-space font is available.
	 * 
	 * @return whether a fixed-space font is available.
	 */
	public boolean isFixedSpaceFontAvailable();
	
	/**
	 * Returns whether timed keyboard input is available.
	 * 
	 * @return whether timed keyboard input is available.
	 */
	public boolean hasTimedInput();
	
	/**
	 * Erases window with given number.
	 * 
	 * @param window the window to erase.
	 */
	public void erase_window(int window);
	
	/**
	 * Split window the designated number of lines.
	 * 
	 * @param lines the number of lines in upper window.
	 */
	public void split_window(int lines);
	
	/**
	 * Selects the given window.
	 * 
	 * @param window the window to select
	 */
	public void set_window(int window);
	
	/**
	 * Move the cursor.
	 * 
	 * @param line the line to move cursor to
	 * @param column the column to move cursor to
	 * @param window the window to move cursor in
	 */
	public void set_cursor(int line, int column, int window);
	
}
