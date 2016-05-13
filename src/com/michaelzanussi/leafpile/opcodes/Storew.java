package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Storew (store word) instructions. See p. 102.
 * 
 * storew array word-index value
 * 
 * array -> word-index = value, i.e. stores the given value in the word at
 * address array + 2 * word-index (which must lie in dynamic memory).
 * See Loadw.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (12 May 2016) 
 */
public class Storew extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Storew(Instruction instruction) {
		super(instruction);
		name = "storew";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {
		
		// Retrieve the operands.
		int array = operands.get(0);
		int word_index = operands.get(1);
		int value = operands.get(2);
		
		int address = array + (2 * word_index);
		
		memory.setWord(address, value);
		
		{
			System.out.println("STOREW array:" + array + " word-index:" + word_index + " (addr=" + address + ") value:" + value);
		}

	}
	
}
