package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Set_window (set window) instructions. See p. 101.
 * 
 * set_window window
 * 
 * Selects the given window for text output. 0=lower, 1=upper.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (1 June 2016) 
 */
public class Set_window extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Set_window(Instruction instruction) {
		super(instruction);
		name = "set_window";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int window = memory.signed(operands.get(0));
		
		{
			System.out.println("SET_WINDOW window:" + window);
			System.out.println();
		}
		
		zmachine.ui().set_window(window);
		
	}
	
}
