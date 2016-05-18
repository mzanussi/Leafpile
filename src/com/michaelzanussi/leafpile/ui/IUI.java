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
	
}
