package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Inc (increment variable) instructions. See p. 86.
 * 
 * inc (variable)
 * 
 * Increment variable by 1. (This is signed, so -1 increments to 0.)
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (25 May 2016) 
 */
public class Inc extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Inc(Instruction instruction) {
		super(instruction);
		name = "inc";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int variable = operands.get(0);
		
		// Retrieve the value at variable, increment it, and put back.
		int nv = memory.signed(current.getVariableValue(variable));
		nv++;
		current.setVariableValue(variable, memory.unsigned(nv));
		
		{
			System.out.println("INC var:" + variable + " (inc:" + nv + ")");
			System.out.println();
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
