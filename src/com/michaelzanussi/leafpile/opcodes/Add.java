package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Add (signed 16-bit addition) instructions. See p. 79.
 * 
 * add a b -> (result)
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (10 May 2016) 
 */
public class Add extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Add(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "add";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {
		
		// Retrieve the operands.
		int a = memory.signed(operands.get(0));
		int b = memory.signed(operands.get(1));
		
		// Perform the signed addition and store unsigned.
		int result = a + b;
		current.setVariableValue(current.getStoreVariable(), memory.unsigned(result));
		
		{
			System.out.println("ADD a:" + a + " b:" + b + " result:" + result + " store:" + current.getStoreVariable());
			System.out.print("local vars now = ");
			int[] locals = current.getLocals();
			for (int i = 0; i < locals.length; i++) {
				System.out.print(locals[i] + " ");
			}
			System.out.println("\n");
		}
		
	}
		
}
