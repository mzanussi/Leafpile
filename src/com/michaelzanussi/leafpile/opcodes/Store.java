package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Store (store at variable) instructions. See p. 102.
 * 
 * store (variable) value
 * 
 * Set the variable referenced by the operand to value.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (16 May 2016) 
 */
public class Store extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Store(Instruction instruction) {
		super(instruction);
		name = "store";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int variable = operands.get(0);
		int value = operands.get(1);
		
		current.setVariableValue(variable, value);
		
		{
			System.out.println("STORE variable:" + variable + " value:" + value);
			System.out.print("local vars now = ");
			int[] locals = current.getLocals();
			for (int i = 0; i < locals.length; i++) {
				System.out.print(locals[i] + " ");
			}
			System.out.print("\n");
			System.out.println("stack now = " + current.getStack());
			System.out.println();
		}
		
	}
	
}
