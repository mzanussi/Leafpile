package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Mul (signed 16-bit multiplication) instructions. See p. 89.
 * 
 * mul a b -> (result)
 * 
 * Signed 16-bit multiplication.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (4 August 2016) 
 */
public class Mul extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Mul(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "mul";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int a = memory.signed(operands.get(0));
		int b = memory.signed(operands.get(1));
		
		// Perform the signed multiplication and store unsigned.
		int result = a * b;
		current.setVariableValue(current.getStoreVariable(), memory.unsigned(result));
		
		{
			System.out.println("MUL a:" + a + " b:" + b + " result:" + result + " store:" + current.getStoreVariable());
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
