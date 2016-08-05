package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Je (jump if equal) instructions. See p. 86.
 * 
 * test bitmap flags ?(label)
 * 
 * Jump if all the flags in bitmap are set (i.e. if bitmap & flags == flags).
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (4 August 2016) 
 */
public class Test extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Test(Instruction instruction) {
		super(instruction);
		isBranch = true;
		name = "test";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int bitmap = operands.get(0);
		int flags = operands.get(1);

		// Perform the comparison.
		boolean result = ((bitmap & flags) == flags);
		
		{
			System.out.print("TEST bitmap:" + bitmap + " flags:" + flags + " result:" + result + " ");
		}
		
		// Execute branch.
		executeBranch(result);
		
		{
			System.out.println();
		}
		
	}
	
}
