package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Print_num (print signed number) instructions. See p. 92.
 * 
 * print_num value
 * 
 * Print (signed) number in decimal.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (23 May 2016) 
 */
public class Print_num extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Print_num(Instruction instruction) {
		super(instruction);
		name = "print_num";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int value = memory.signed(operands.get(0));
		
		// Convert int to String.
		String string = Integer.toString(value);
		
		zmachine.ui().write(string);
		
		{
			System.out.println("PRINT_NUM ~" + string + "~");
			System.out.println();
		}
		
	}
	
}
