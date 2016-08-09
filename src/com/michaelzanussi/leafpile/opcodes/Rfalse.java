package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Rfalse (return false) instructions. See p. 98.
 * 
 * rfalse
 * 
 * Return false (i.e., 0) from the current routine.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (8 August 2016) 
 */
public class Rfalse extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Rfalse(Instruction instruction) {
		super(instruction);
		name = "rfalse";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		{
			System.out.println("RFALSE");
			System.out.println();
		}
		
		ret(0);
		
	}
	
}
