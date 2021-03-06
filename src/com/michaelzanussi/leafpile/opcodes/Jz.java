package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Jz (jump if zero) instructions. See p. 87.
 * 
 * jz a ?(label)
 * 
 * Jump if a = 0.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (12 May 2016) 
 */
public class Jz extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Jz(Instruction instruction) {
		super(instruction);
		isBranch = true;
		name = "jz";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int a = memory.signed(operands.get(0));

		// Perform the comparison.
		boolean result = (a == 0);

		{
			System.out.print("JZ a:" + a + " result:" + result + " ");
		}

		// Execute branch.
		executeBranch(result);
		
		{
			System.out.println();
		}
		
	}
	
}
