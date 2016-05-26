package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Erase_window (erase window) instructions. See p. 84.
 * 
 * erase_window window
 * 
 * Erases window with given number (to background colour); or if -1 it
 * unsplits the screen and clears the lot; or if -2 it clears the screen
 * without unsplitting it. In cases -1 and -2, the cursor may move (see
 * section 8 for precise details).
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (25 May 2016) 
 */
public class Erase_window extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Erase_window(Instruction instruction) {
		super(instruction);
		name = "erase_window";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {
		
		// Retrieve the operand.
		int window = memory.signed(operands.get(0));
		
		{
			System.out.println("ERASE_WINDOW window:" + window);
			System.out.println();
		}
		
		if (window == -1) {			// unsplit then clear
			System.err.println("implement details erase_window -1");
		} else if (window == -2) {	// clear the screen
			System.err.println("implement details erase_window -2");
		} else {					// erase window to background color
			System.err.println("implement details erase_window " + window);
		}
		
	}
		
}
