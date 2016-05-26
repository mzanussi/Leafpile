package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Jl (jump if less than) instructions. See p. 87.
 * 
 * jl a b ?(label)
 * 
 * Jump if a < b (using a signed 16-bit comparison).
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (25 May 2016) 
 */
public class Jl extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Jl(Instruction instruction) {
		super(instruction);
		isBranch = true;
		name = "jl";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {
		
		// Retrieve the operands.
		int a = memory.signed(operands.get(0));
		int b = memory.signed(operands.get(1));
		
		// Perform the comparison.
		boolean result = (a < b);
		
		{
			System.out.print("JL a:" + a + " b:" + b + " result:" + result + " ");
		}
		
		// Execute branch.
		executeBranch(result);
		
		{
			System.out.println();
		}
		
	}

}
