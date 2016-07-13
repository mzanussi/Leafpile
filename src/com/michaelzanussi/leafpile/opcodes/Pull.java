package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Pull (pull value) instructions. See p. 93.
 * 
 * pull (variable)
 * pull stack -> (result)
 * 
 * Pulls value off a stack. (If the stack underflows, the interpreter should
 * halt with a suitable error message.) In version 6, the stack in question
 * may be specified as a user one: otherwise it is the game stack.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (23 May 2016) 
 */
public class Pull extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Pull(Instruction instruction) {
		super(instruction);
		name = "pull";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int variable = operands.get(0);
		
		if (memory.getVersion() < 6) {
			// Pop the stack and store locally via var.
			int value = current.getVariableValue(0);
			current.setVariableValue(variable, value);
		}
		else {
			assert(false) : "handle v6 pulls";
		}
		
		{
			System.out.println("PULL variable:" + variable);
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
