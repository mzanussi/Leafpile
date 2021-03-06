package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Je (jump if equal) instructions. See p. 86.
 * 
 * je a b ?(label)
 * 
 * Jump if a is equal to any of the subsequent operands. (Thus JE a
 * never jumps and JE a b jumps if a = b.)
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (11 May 2016) 
 */
public class Je extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Je(Instruction instruction) {
		super(instruction);
		isBranch = true;
		name = "je";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int a = memory.signed(operands.get(0));

		boolean result = false;
		
		// Perform the comparison(s).
		for (int i = 1; i < operands.size(); i++) {
			// Branch on True occurs if first operand 
			// equals any of the subsequent operands.
			int b = memory.signed(operands.get(i));
			if (a == b) {
				result = true;
				break;
			}
		}
		
		{
			System.out.print("JE a:" + a + " ");
			for (int j = 1; j < operands.size(); j++) {
				System.out.print("b" + j + ":" + memory.signed(operands.get(j)) + " ");
			}
			System.out.print("result:" + result + " ");
		}
		
		// Execute branch.
		executeBranch(result);
		
		{
			System.out.println();
		}
		
	}
	
}
