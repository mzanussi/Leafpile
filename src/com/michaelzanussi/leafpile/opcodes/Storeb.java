package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Storeb (store byte) instructions. See p. 102.
 * 
 * storeb array byte-index value
 * 
 * array -> byte-index = value, i.e. stores the given value in the byte at
 * address array + byte-index (which must lie in dynamic memory).
 * See Loadw.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (25 May 2016) 
 */
public class Storeb extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Storeb(Instruction instruction) {
		super(instruction);
		name = "storeb";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {
		
		// Retrieve the operands.
		int array = operands.get(0);
		int byte_index = operands.get(1);
		int value = operands.get(2);
		
		int address = array + byte_index;
		
		memory.setByte(address, value);
		
		{
			System.out.println("STOREB array:" + array + " byte-index:" + byte_index + " (addr=" + address + ") value:" + value);
			System.out.println();
		}

	}
	
}
