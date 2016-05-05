package com.michaelzanussi.leafpile;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (27 April 2016) 
 */
public class Debug {
	
	private boolean debug;
	
	public Debug() {
		debug = false;
	}
	
	public Debug(boolean debug) {
		this.debug = debug;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public void print(String string) {
		if (debug) { 
			System.out.print(string);
		}
	}
	
	public void println(String string) {
		if (debug) {
			System.out.println(string);
		}
	}

}
