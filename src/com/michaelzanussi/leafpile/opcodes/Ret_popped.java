package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Ret_popped (pop stack and return) instructions. See p. 98.
 * 
 * ret_popped
 * 
 * Pops top of stack and returns that. (This is equivalent to ret sp, but
 * is one byte cheaper.)
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (5 August 2016)
 */
public class Ret_popped extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Ret_popped(Instruction instruction) {
		super(instruction);
		name = "ret_popped";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Pop the stack.
		int value = current.getVariableValue(0);
		
		{
			System.out.println("RET_POPPED value:" + value);
			System.out.println();
		}
		
		ret(value);

	}
	
}
