package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Push (push value) instructions. See p. 93.
 * 
 * push value
 * 
 * Pushes value onto the game stack.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (23 May 2016) 
 */
public class Push extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Push(Instruction instruction) {
		super(instruction);
		name = "push";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int value = memory.signed(operands.get(0));
		
		// Push the value onto the game stack.
		current.setVariableValue(0, value);
		
		{
			System.out.println("PUSH value:" + value);
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
