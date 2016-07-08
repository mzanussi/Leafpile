package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Jg (jump if greater tan) instructions. See p. 87.
 * 
 * jg a b ?(label)
 * 
 * Jump if a > b (using a signed 16-bit comparision).
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (7 July 2016) 
 */
public class Jg extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Jg(Instruction instruction) {
		super(instruction);
		isBranch = true;
		name = "jg";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int a = memory.signed(operands.get(0));
		int b = memory.signed(operands.get(1));
		
		// Perform the comparison.
		boolean result = (a > b);
		
		{
			System.out.print("JG a:" + a + " b:" + b + " result:" + result + " ");
		}
		
		// Execute branch.
		executeBranch(result);
		
		{
			System.out.println();
		}
		
	}
	
}
