package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Buffer_mode (buffer mode) instructions. See p. 80.
 * 
 * buffer_mode flag
 * 
 * If set to 1, text output on the lower window in stream 1 is buffered up
 * so that it can be word-wrapped properly. If set to 0, it isn't.
 * 
 * In Version 6, this opcode is redundant (the "buffering" window attribute
 * can be set instead).
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (1 June 2016) 
 */
public class Buffer_mode extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Buffer_mode(Instruction instruction) {
		super(instruction);
		name = "buffer_mode";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int flag = operands.get(0);
		
		{
			System.out.println("BUFFER_MODE flag:" + flag);
			System.out.println();
		}
		
		// Currently, all output is buffered, and unless it becomes
		// absolutely necessary, this will not change. So do nothing.
		
	}
	
}
