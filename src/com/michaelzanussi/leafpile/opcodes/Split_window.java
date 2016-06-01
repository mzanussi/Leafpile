package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Split_window (split window) instructions. See p. 101.
 * 
 * split_window lines
 * 
 * Splits the screen so that the upper window has the given number of lines:
 * or, if this is zero, unsplits the screen again. In Version 3 (only) the
 * upper window should be cleared after the split.
 * 
 * In Version 6, this is supposed to roughly emulate the earlier Version 5
 * behaviour (see section 8), though the line count is in units rather than
 * lines. (Existing Version 6 games seem to use this opcode only for
 * bounding cursor movement. 'Journey' creates a status region which is the
 * whole screen and then overlays it with two other windows.) The width and
 * x-coordinates of windows 0 and 1 are not altered. A cursor remains in
 * the same absolute screen position (which means that its y-coordinate will
 * be different relative to the window origin, since this origin will have
 * moved) unless this position is no longer in the window at all, in which
 * case it is moved to the window origin (at the top left of the window).
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (27 May 2016) 
 */
public class Split_window extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Split_window(Instruction instruction) {
		super(instruction);
		name = "split_window";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int lines = operands.get(0);
		
		{
			System.out.println("SPLIT_WINDOW lines:" + lines);
			System.out.println();
		}
		
		zmachine.ui().split_window(lines);
		
	}
	
}
