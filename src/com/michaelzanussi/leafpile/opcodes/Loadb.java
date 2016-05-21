package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Loadb (load byte) instructions. See p. 88.
 * 
 * loadb array byte-index -> (result)
 * 
 * Stores array -> byte-index, i.e. the byte at address array + byte-index, 
 * (which must lie in static or dynamic memory).
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (19 May 2016) 
 */
public class Loadb extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Loadb(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "loadb";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {
		
		// Retrieve the operands.
		int array = operands.get(0);
		int byte_index = operands.get(1);
		
		int address = array + byte_index;
		int variable = current.getStoreVariable();
		int value = memory.getByte(address);
		
		current.setVariableValue(variable, value);
		
		{
			System.out.println("LOADB array:" + array + " byte-index:" + byte_index + " (addr=" + address + ") var:" + variable + " value:" + value);
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
