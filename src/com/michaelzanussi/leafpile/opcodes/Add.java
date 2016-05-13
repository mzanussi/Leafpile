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
		int op1 = memory.signed(operands.get(0));
		int op2 = memory.signed(operands.get(1));
		
		// Perform the signed addition and store unsigned.
		int result = op1 + op2;
		current.setVariableValue(current.getStoreVariable(), memory.unsigned(result));
		
		{
			System.out.println("ADD op1:" + op1 + " op2:" + op2 + " result:" + result + " store:" + current.getStoreVariable() + " (" + current.getVariableValue(current.getStoreVariable()) + ")");
			System.out.print("local vars now = ");
			int[] foo = current.getLocals();
			for (int i = 0; i < foo.length; i++) {
				System.out.print(foo[i] + " ");
			}
			System.out.println("\n");
		}
		
	}
		
}
