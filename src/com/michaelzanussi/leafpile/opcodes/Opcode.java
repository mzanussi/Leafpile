package com.michaelzanussi.leafpile.opcodes;

/**
 * This interface defines an opcode.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (6 May 2016) 
 */
public interface Opcode {
	
	/**
	 * Does this instruction store its result?
	 * 
	 * @return <code>true</code> if the instruction stores its
	 * result; otherwise, <code>false</code>.
	 */
	public boolean isStore();
	
	/**
	 * Does this instruction branch?
	 * 
	 * @return <code>true</code> if the instruction branches;
	 * otherwise, <code>false</code>.
	 */
	public boolean isBranch();
	
	/**
	 * Execute the opcode portion of the instruction.
	 */
	public void exec();

}
