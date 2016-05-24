package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Rtrue (return true) instructions. See p. 98.
 * 
 * rtrue
 * 
 * Return true (i.e., 1) from the current routine.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (23 May 2016) 
 */
public class Rtrue extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Rtrue(Instruction instruction) {
		super(instruction);
		name = "rtrue";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {
		
		{
			System.out.println("RTRUE");
			System.out.println();
		}
		
		retuurn(1);
		
	}
	
}
