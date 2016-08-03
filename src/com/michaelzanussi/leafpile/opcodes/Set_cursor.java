package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Set_cursor (set cursor) instructions. See p. 100.
 * 
 * set_cursor line column window
 * 
 * Move cursor in the current window to the position (x, y) (in units)
 * relative to (1, 1) in the top left. (In Version 6 the window is
 * supplied and need not be the current one. Also, if the cursor would
 * lie outside the current margin settings, it is moved to the left
 * margin of the current line.)
 * 
 * In Version 6, set_cursor -1 turns the cursor off, and either
 * set_cursor -2 or set_cursor -2 0 turn it back on. It is not known
 * what, if anything, this second argument means: in all known cases
 * it is 0.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (1 June 2016) 
 */
public class Set_cursor extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Set_cursor(Instruction instruction) {
		super(instruction);
		name = "set_cursor";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int line = operands.get(0);
		int column = operands.get(1);
		// TODO: V6 get window from operand 2
		int window = 0;
		
		{
			System.out.println("SET_CURSOR line:" + line + " column:" + column);
			System.out.println();
		}
		
		zmachine.ui().flush_buf();
		zmachine.ui().set_cursor(line, column, window);
		
	}
		
}
