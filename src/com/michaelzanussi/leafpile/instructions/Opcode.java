package com.michaelzanussi.leafpile.instructions;

/**
 * This interface defines an opcode.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (6 May 2016) 
 */
public interface Opcode {
	
	/**
	 * Execute the opcode portion of the instruction.
	 */
	public void exec();

}
