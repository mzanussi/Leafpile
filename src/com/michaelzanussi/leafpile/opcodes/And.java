package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for And (bitwise AND) instructions. See p. 79.
 * 
 * and a b -> (result)
 * 
 * Bitwise AND.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (20 May 2016) 
 */
public class And extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public And(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "and";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int a = memory.signed(operands.get(0));
		int b = memory.signed(operands.get(1));
		
		// Perform the operation and store unsigned.
		int result = a & b;
		current.setVariableValue(current.getStoreVariable(), memory.unsigned(result));
		
		{
			System.out.println("AND a:" + a + " b:" + b + " result:" + result + " store:" + current.getStoreVariable());
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
