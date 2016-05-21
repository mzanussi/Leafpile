package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Loadw (load word) instructions. See p. 88.
 * 
 * loadw array word-index -> (result)
 * 
 * Stores array -> word-index, i.e. the word at address array + 2 * word-index, 
 * (which must lie in static or dynamic memory).
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (12 May 2016) 
 */
public class Loadw extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Loadw(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "loadw";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {
		
		// Retrieve the operands.
		int array = operands.get(0);
		int word_index = operands.get(1);
		
		int address = array + (2 * word_index);
		int variable = current.getStoreVariable();
		int value = memory.getWord(address);
		
		current.setVariableValue(variable, value);
		
		{
			System.out.println("LOADW array:" + array + " word-index:" + word_index + " (addr=" + address + ") var:" + variable + " value:" + value);
			System.out.print("local vars now = ");
			int[] locals = current.getLocals();
			for (int i = 0; i < locals.length; i++) {
				System.out.print(locals[i] + " ");
			}
			System.out.print("\n");
			System.out.println("stack now = " + current.getStack());
		}

	}
	
}
