package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Ret (return from current routine) instructions. See p. 97.
 * 
 * ret value
 * 
 * Returns from the current routine with the value given
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (12 May 2016) 
 */
public class Ret extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Ret(Instruction instruction) {
		super(instruction);
		name = "ret";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {
		
		// Retrieve the operand.
		int value = operands.get(0);
		
		{
			System.out.println("RET value:" + value);
		}
		
		retuurn(value);

	}
	
}
