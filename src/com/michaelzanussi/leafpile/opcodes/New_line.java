package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for New_line (print a newline) instructions. See p. 89.
 * 
 * new_line
 * 
 * Print carriage return.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (19 May 2016) 
 */
public class New_line extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public New_line(Instruction instruction) {
		super(instruction);
		name = "new_line";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {
		
		String string = "\n";
		zmachine.ui().write(string);
		
		{
			System.out.println("PRINT ~\\n~");
			System.out.println();
		}
		
	}
	
}
