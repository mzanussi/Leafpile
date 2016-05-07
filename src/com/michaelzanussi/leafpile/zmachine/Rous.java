package com.michaelzanussi.leafpile.zmachine;

/**
 * A routine state, having nothing to do with rodents.
 * 
 * Each routine call, including the initial game routine,
 * will have its own routine state, which will be place
 * on the Z-machine stack. A description of this can be
 * found in "Overview of Z-machine Architecture" in the
 * standard.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (5 May 2016) 
 */
public class Rous {
	
	private int pc;
	
	public Rous(int pc) {
		this.pc = pc;
	}

	public int getPC() {
		return pc;
	}

	public void setPC(int pc) {
		this.pc = pc;
	}
	

}
